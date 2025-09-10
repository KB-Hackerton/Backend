package kb_hack.backend.domain.chat.service;

import static kb_hack.backend.global.common.exception.enums.BadStatusCode.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kb_hack.backend.domain.chat.dto.response.ChatMessageHistoryDto;
import kb_hack.backend.domain.chat.dto.ChatMessageDto;
import kb_hack.backend.domain.chat.dto.response.ChatMessageResponse;
import kb_hack.backend.domain.chat.dto.response.MyChatListResponse;
import kb_hack.backend.domain.chat.entity.ChatMessage;
import kb_hack.backend.domain.chat.entity.ChatRoom;
import kb_hack.backend.domain.chat.entity.ChatRoomState;
import kb_hack.backend.domain.chat.entity.ReadStatus;
import kb_hack.backend.domain.chat.mapper.ChatRoomMapper;
import kb_hack.backend.domain.chat.mapper.ChatRoomStateMapper;
import kb_hack.backend.domain.chat.mapper.ReadStatusMapper;
import kb_hack.backend.domain.member.domain.Member;
import kb_hack.backend.domain.member.mapper.MemberMapper;
import kb_hack.backend.domain.sos.entity.Sos;
import kb_hack.backend.domain.sos.mapper.SosMapper;
import kb_hack.backend.global.common.exception.type.CustomException;
import kb_hack.backend.global.security.dto.SecurityCustomUser;
import kb_hack.backend.global.security.entity.MemberVO;
import kb_hack.backend.global.util.JwtProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

	private final ChatRoomMapper chatRoomMapper;
	private final MemberMapper memberMapper;
	private final SosMapper sosMapper;
	private final JwtProcessor jwtProcessor;
	private final ReadStatusMapper readStatusMapper;

	private final ChatRoomStateMapper chatRoomStateMapper;

	public void addParticipantToRoom(ChatRoom chatRoom, Member member) {
		ChatRoomState chatRoomState = ChatRoomState.builder()
			.chatRoomId(chatRoom.getChatRoomId())
			.memberId(member.getMemberId())
			.build();
		chatRoomStateMapper.save(chatRoomState);
	}

	/**
	 * 해당 roomId에 email을 가진 사용자가 참여자인지 체크
	 *
	 * @param email
	 * @param roomId
	 * @return
	 */
	public boolean isRoomParticipant(String email, Long roomId) {
		// 해당 roomId에 email을 가진 사용자가 참여자인지 체크
		// 1. 채팅방 조회
		ChatRoom chatRoom = chatRoomMapper.findByRoomId(roomId);
		if (chatRoom == null) {
			log.warn("참여자인지 확인하려 했으나, roomId '{}'에 해당하는 채팅방을 찾을 수 없습니다.", roomId);
			return false; // 채팅방이 없으면 참여자가 아님
		}

		// 2. 현재 로그인한 사용자 조회
		Member member = memberMapper.getMemberByEmail(email);
		if (member == null) {
			log.warn("참여자인지 확인하려 했으나, 이메일 '{}'에 해당하는 사용자를 찾을 수 없습니다.", email);
			return false; // 사용자가 없으면 당연히 참여자가 아님
		}

		// 3. 참여자 여부 체크
		List<ChatRoomState> chatRoomState = chatRoomStateMapper.findByChatRoom(chatRoom.getChatRoomId());
		for (ChatRoomState c : chatRoomState) {
			log.info("c.getMemberId(): {}, member.getMemberId(): {}", c.getMemberId(), member.getMemberId());
			if (c.getMemberId().equals(member.getMemberId())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 메시지 저장하기
	 *
	 * @param roomId
	 * @param chatMessageDto
	 */
	@Transactional
	public ChatMessage saveMessage(Long roomId, ChatMessageDto chatMessageDto, SecurityCustomUser customUser) {
		// 1. 채팅방 조회
		ChatRoom chatRoom = chatRoomMapper.findByRoomId(roomId);
		if (chatRoom == null) {
			throw new CustomException(CHAT_ROOM_NOT_FOUND);
		}
		// 2. 보낸 사람 조회
		Long memberId = customUser.getMemberVO().getMemberId(); // SecurityCustomUser에 MemberVO를 가져오는 getter가 있다고 가정

		Member member = memberMapper.getMemberByMemberId(memberId);
		if (member == null) {
			throw new CustomException(USER_NOT_FOUND_EXCEPTION);
		}
		// 3. 채팅 메시지 저장
		ChatMessage chatMessage = ChatMessage.builder()
			.chatRoomId(chatRoom.getChatRoomId())
			.senderId(member.getMemberId())
			.content(chatMessageDto.getMessage())
			.build();
		int affectedRows = chatRoomMapper.saveMessage(chatMessage);
		if (affectedRows < 1) {
			throw new CustomException(CHAT_MESSAGE_SAVE_FAIL);
		}
		// 4. 사용자 별로 읽음 여부 저장
		List<ChatRoomState> chatRoomStates = chatRoomStateMapper.findByChatRoom(chatRoom.getChatRoomId());
		for (ChatRoomState c : chatRoomStates) {
			ReadStatus readStatus = ReadStatus.builder()
				.chatRoomId(c.getChatRoomId())
				.memberId(c.getMemberId())
				.chatMessageId(chatMessage.getChatMessageId())
				.isRead(c.getMemberId().equals(member.getMemberId()))
				.build();
			int saved = readStatusMapper.save(readStatus);
			if (saved < 1) {
				throw new CustomException(CHAT_MESSAGE_SAVE_FAIL);
			}

		}

		return chatRoomMapper.findMessageById(chatMessage.getChatMessageId());
	}

	/**
	 * 1:1 채팅방 생성
	 * @param sosId
	 * @param otherMemberId
	 * @return
	 */
	public Long createPrivateChatRoom(MemberVO memberVO, Long sosId, Long otherMemberId) {
		// 1-1. 현재 로그인한 사용자 조회
		Member member = memberMapper.getMemberByMemberId(memberVO.getMemberId());
		// 1.2. sos 조회
		Sos sos = sosMapper.findById(sosId);
		if (sos == null) {
			throw new CustomException(CHAT_SOS_NOT_FOUND);
		}
		// 1.3. 상대방 사용자 조회
		Member otherMember = memberMapper.getMemberByMemberId(sos.getMemberId());
		if (otherMember == null) {
			throw new CustomException(USER_NOT_FOUND_EXCEPTION);
		}

		// 1-4. 이미 존재하는 채팅방인지 확인
		ChatRoom existingRooms = chatRoomStateMapper.findChatRoomsByMemberIdAndSosId(member.getMemberId(), sosId);
		if (existingRooms != null) {
			log.info("이미 존재하는 채팅방입니다. roomId: {}", existingRooms.getChatRoomId());
			return existingRooms.getChatRoomId();
		}
		// 2. 채팅방 생성
		ChatRoom newChatRoom = ChatRoom
			.builder()
			.roomName(sos.getSosTitle())
			.sosId(sosId)
			.roomType(sos.getSosType())
			.isComplete(0)
			.ownerId(sos.getMemberId())
			.build();

		int affectedRows = chatRoomMapper.save(newChatRoom);
		// 3. 두사람 모두 참여자로 추가
		addParticipantToRoom(newChatRoom, member);
		addParticipantToRoom(newChatRoom, otherMember);

		if (affectedRows < 1) {
			throw new CustomException(CHAT_ROOM_CREATE_FAIL);
		}
		return newChatRoom.getChatRoomId();

	}

	public List<ChatMessageHistoryDto> getChatHistory(Long roomId, Long memberId) {
		// 1. 해당 채팅방의 참여자가 아닐경우 예외처리
		// 1-1. 채팅방 조회
		ChatRoom chatRoom = chatRoomMapper.findByRoomId(roomId);
		if (chatRoom == null) {
			throw new CustomException(CHAT_ROOM_NOT_FOUND);
		}
		// 1-2. 현재 로그인한 사용자 조회
		Member member = memberMapper.getMemberByMemberId(memberId);

		// 1-3. 참여자 여부 체크
		List<ChatRoomState> chatRoomStates = chatRoomStateMapper.findByChatRoom(chatRoom.getChatRoomId());
		boolean check = false;
		for (ChatRoomState c : chatRoomStates) {
			if (c.getMemberId().equals(member.getMemberId())) {
				check = true;
			}
		}
		if (!check) {
			throw new CustomException(CHAT_ROOM_NOT_PARTICIPANT);
		}
		// 2. 특정 room에 대한 message 조회
		return chatRoomMapper.findChatHistoryWithSenderEmailByRoomId(memberId, roomId);
	}

	@Transactional
	public void markMessagesAsRead(Long roomId, Long memberId) {

		// 1. 해당 방의 모든 메시지를 가져온다.
		List<ChatMessageResponse> messages = chatRoomMapper.getChatMessagesByRoomId(roomId);

		if (messages.isEmpty()) {
			return; // 읽을 메시지가 없으면 종료

		}


		// 2. 마지막 메시지의 ID를 찾는다.
		Long lastMessageId = messages.get(messages.size() - 1).getChatMessageId();

		// 3. 해당 사용자의 읽음 상태를 마지막 메시지 ID로 업데이트한다.

		readStatusMapper.updateLastReadMessage(roomId, memberId, lastMessageId);


	}

	public List<MyChatListResponse> getMyChatRooms(MemberVO memberVO) {
		// 1. 현재 로그인한 사용자 조회
		Member member = memberMapper.getMemberByMemberId(memberVO.getMemberId());
		if (member == null) {
			throw new CustomException(USER_NOT_FOUND_EXCEPTION);
		}
		// 2. 자신이 속한 채팅방 목록 조회
		List<ChatRoom> chatRooms = chatRoomStateMapper.findChatRoomsByMemberId(member.getMemberId());
		List<MyChatListResponse> myChatListResponses = new ArrayList<>();
		for (ChatRoom c : chatRooms) {
			Long count = readStatusMapper.countUnreadMessages(c.getChatRoomId(), member.getMemberId());
			MyChatListResponse dto = MyChatListResponse.builder()
				.roomId(c.getChatRoomId())
				.roomName(c.getRoomName())
				.unReadCount(count)
				.build();
			myChatListResponses.add(dto);
		}
		return myChatListResponses;
	}

	public ChatRoom getChatRoomDetail(Long roomId, MemberVO memberVO) {
		// 1. 현재 로그인한 사용자 조회
		Member member = memberMapper.getMemberByMemberId(memberVO.getMemberId());
		if (member == null) {
			throw new CustomException(USER_NOT_FOUND_EXCEPTION);
		}
		// 2. 채팅방 조회
		ChatRoom chatRoom = chatRoomMapper.findByRoomId(roomId);
		if (chatRoom == null) {
			throw new CustomException(CHAT_ROOM_NOT_FOUND);
		}
		// 3. 참여자 여부 체크
		List<ChatRoomState> chatRoomStates = chatRoomStateMapper.findByChatRoom(chatRoom.getChatRoomId());
		boolean check = false;
		for (ChatRoomState c : chatRoomStates) {
			if (c.getMemberId().equals(member.getMemberId())) {
				check = true;
			}
		}
		if (!check) {
			throw new CustomException(CHAT_ROOM_NOT_PARTICIPANT);
		}
		return chatRoom;
	}

	public void leaveChatRoom(Long roomId, MemberVO memberVO) {
		// 1. 현재 로그인한 사용자 조회
		Member member = memberMapper.getMemberByMemberId(memberVO.getMemberId());
		if (member == null) {
			throw new CustomException(USER_NOT_FOUND_EXCEPTION);
		}
		// 2. 채팅방 조회
		ChatRoom chatRoom = chatRoomMapper.findByRoomId(roomId);
		if (chatRoom == null) {
			throw new CustomException(CHAT_ROOM_NOT_FOUND);
		}
		// 3. 참여자 여부 체크
		List<ChatRoomState> chatRoomStates = chatRoomStateMapper.findByChatRoom(chatRoom.getChatRoomId());
		boolean check = false;
		for (ChatRoomState c : chatRoomStates) {
			if (c.getMemberId().equals(member.getMemberId())) {
				check = true;
			}
		}
		if (!check) {
			throw new CustomException(CHAT_ROOM_NOT_PARTICIPANT);
		}
		// 4. 채팅방에서 나가기
		chatRoomMapper.leaveChatRoom(roomId, member.getMemberId());

	}
}
