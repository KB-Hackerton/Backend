package kb_hack.backend.domain.chat.service;

import static kb_hack.backend.global.common.exception.enums.BadStatusCode.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kb_hack.backend.domain.chat.dto.response.ChatMemberListResponse;
import kb_hack.backend.domain.chat.dto.response.ChatMessageHistoryDto;
import kb_hack.backend.domain.chat.dto.ChatMessageDto;

import kb_hack.backend.domain.chat.dto.response.ChatRoomDetailResponse;
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
	 * @return
	 */
	public Long createPrivateChatRoom(MemberVO memberVO, Long sosId) {
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
		// row 없으면 생성
		chatRoomStateMapper.insertIfNotExists(roomId, memberId);

		Long lastMessageId = chatRoomMapper.findLastMessageId(roomId);
		if (lastMessageId == null) {
			return; // 메시지가 없으면 종료
		}

		chatRoomStateMapper.updateLastReadMessage(roomId, memberId, lastMessageId);
	}

	public List<MyChatListResponse> getMyChatRooms(MemberVO memberVO) {
		// 1. 현재 로그인한 사용자 조회
		Member member = memberMapper.getMemberByMemberId(memberVO.getMemberId());
		if (member == null) {
			throw new CustomException(USER_NOT_FOUND_EXCEPTION);
		}
		// 2. 자신이 속한 채팅방 목록 조회


		// 1. 단 한 번의 쿼리로 모든 필요한 정보를 가져옵니다.
		List<MyChatListResponse> queryResults = chatRoomStateMapper.findMyChatList(member.getMemberId());

		// 2. Mapper 결과를 최종 DTO로 변환합니다. (DB 접근 없음)
		return queryResults.stream()
			.map(result -> MyChatListResponse.builder()
				.roomId(result.getRoomId())
				.roomName(result.getRoomName())
				.unReadCount(result.getUnReadCount())
				.lastMessage(result.getLastMessage())
				.lastMessageTime(result.getLastMessageTime())
				.bussinessName(result.getBussinessName())
				.memberProfileImage(result.getMemberProfileImage())
				.build())
			.collect(Collectors.toList());

		// List<ChatRoom> chatRooms = chatRoomStateMapper.findChatRoomsByMemberId(member.getMemberId());
		// List<MyChatListResponse> myChatListResponses = new ArrayList<>();
		// for (ChatRoom c : chatRooms) {
		// 	Long count = readStatusMapper.countUnreadMessages(c.getChatRoomId(), member.getMemberId());
		//
		// 	ChatMessage lastReadMessage = chatRoomStateMapper.findLastReadMessage(c.getChatRoomId());
		// 	List<Member> membersByRoomId = chatRoomStateMapper.findMembersByRoomId(c.getChatRoomId());
		// 	String bussinessName = "";
		// 	for (Member member1 : membersByRoomId) {
		// 		log.info("membersByRoomId member1: {}", member1);
		// 		if(member1.getMemberId().equals(member.getMemberId())) {
		// 			continue;
		// 		}
		// 		else {
		// 			bussinessName = member1.getMemberName();
		// 			break;
		// 		}
		// 	}
		// 	MyChatListResponse dto = MyChatListResponse.builder()
		// 		.roomId(c.getChatRoomId())
		// 		.roomName(c.getRoomName())
		// 		.unReadCount(count)
		// 		.lastMessage(lastReadMessage.getContent())
		// 		.lastMessageTime(lastReadMessage.getCreatedAt())
		// 		.bussinessName(bussinessName)
		// 		.build();
		// 	myChatListResponses.add(dto);
		// }
	}




	public ChatRoomDetailResponse getChatRoomDetail(Long roomId, MemberVO memberVO) {
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

		// 4. 참여자 목록 조회
		Member owner = memberMapper.getMemberByMemberId(chatRoom.getOwnerId());

		// 5. SOS 정보 조회
		Long sosId = chatRoom.getSosId();
		Sos sos = sosMapper.findById(sosId);
		// 긴급도 설정


		// 6. 채팅방 상세 정보 DTO 생성 및 반환
		return ChatRoomDetailResponse.builder()
			.roomName(chatRoom.getRoomName())
			.sosType(chatRoom.getRoomType())
			.isComplete(chatRoom.getIsComplete() == 1)
			.memberBadge(owner.getBadge())
			.isOwner(chatRoom.getOwnerId().equals(member.getMemberId()))
			.createdAt(sos.getCreatedAt())
			// .unReadCount()
			// .partnerImage(owner.getProfileImage())
			.build();
	}

	public List<ChatMemberListResponse> leaveSelectMember(Long roomId, MemberVO memberVO) {
		// 1. 현재 로그인한 사용자 정보 (메소드 파라미터로 받음)
		Long currentMemberId = memberVO.getMemberId();

		// 2. roomId로 현재 채팅방 정보 조회
		ChatRoom currentChatRoom = chatRoomMapper.findByRoomId(roomId);
		if (currentChatRoom == null) {
			throw new CustomException(CHATROOM_NOT_FOUND_EXCEPTION);
		}

		// 3. 채팅방에 연결된 SOS 정보 조회
		Long sosId = currentChatRoom.getSosId();
		Sos sos = sosMapper.findById(sosId);
		if (sos == null) {
			throw new CustomException(SOS_NOT_FOUND_EXCEPTION);
		}

		// 4. 권한 확인: SOS 작성자와 현재 로그인한 사용자가 동일한지 확인
		if (!sos.getMemberId().equals(currentMemberId)) {
			throw new CustomException(FORBIDDEN_EXCEPTION); // 권한 없음
		}
		// 5. 이 SOS와 관련된 모든 채팅방 목록 조회
		List<ChatRoom> allRelatedChatRooms = chatRoomMapper.findAllBySosId(sosId);
		// 6. 모든 채팅방의 모든 참여자 목록을 중복 없이 가져오기

		// Set을 사용하여 중복 자동 제거
		for (ChatRoom allRelatedChatRoom : allRelatedChatRooms) {
			log.info(allRelatedChatRoom.getRoomName());
		}

		Set<Member> allParticipants = new HashSet<>();
		for (ChatRoom chatRoom : allRelatedChatRooms) {
			List<Member> participantsInRoom = chatRoomStateMapper.findMembersByRoomId(chatRoom.getChatRoomId());
			allParticipants.addAll(participantsInRoom);
		}


		// 7. 참여자 목록에서 본인(SOS 작성자)을 제외하고, DTO로 변환
		return allParticipants.stream()
			.filter(member -> !member.getMemberId().equals(currentMemberId))
			.map(member -> ChatMemberListResponse.builder()
				.memberId(member.getMemberId())
				.memberName(member.getMemberName())
				.build())
			.collect(Collectors.toList());
	}

	@Transactional // 이 메소드 내의 모든 DB 작업은 하나의 트랜잭션으로 처리됩니다.
	public void completeSos(Long sosId, List<Long> helperMemberIds, MemberVO memberVO) {
		// 1. SOS 정보 조회 및 권한 확인
		Sos sos = sosMapper.findById(sosId);
		if (sos == null) {
			throw new CustomException(SOS_NOT_FOUND_EXCEPTION);
		}
		// SOS를 생성한 사용자인지 확인
		if (!sos.getMemberId().equals(memberVO.getMemberId())) {
			throw new CustomException(FORBIDDEN_EXCEPTION); // 권한 없음
		}
		// 이미 완료된 요청인지 확인 (중복 처리 방지)
		if (sos.getIsComplete() == true) {
			throw new CustomException(ALREADY_COMPLETED_EXCEPTION);
		}

		// 2. SOS와 관련된 모든 채팅방 조회
		List<ChatRoom> relatedChatRooms = chatRoomMapper.findAllBySosId(sosId);
		if (relatedChatRooms.isEmpty()) {
			// 채팅방이 없는 경우에도 SOS는 종료 처리해야 할 수 있습니다.
			// 이 부분은 정책에 따라 결정합니다. 여기서는 일단 계속 진행합니다.
		}

		// 3. 각 채팅방의 상태를 '완료'로 변경하고, 참여자 정보(state) 삭제
		for (ChatRoom room : relatedChatRooms) {
			// 3-1. chat_room의 is_complete = 1 로 업데이트
			chatRoomMapper.updateIsComplete(room.getChatRoomId());

			// 3-2. chat_room_state 에서 해당 채팅방의 모든 참여자 정보 삭제
			chatRoomStateMapper.deleteByRoomId(room.getChatRoomId());
		}

		// 4. 원본 SOS의 상태를 '완료'로 변경
		sosMapper.updateIsComplete(sosId);

		// 5. (선택사항) 도움을 준 사용자(helper)에게 보상 제공
		if (helperMemberIds != null && !helperMemberIds.isEmpty()) {
			for (Long helperId : helperMemberIds) {
				memberMapper.incrementHelpCount(helperId); // 예: help_count + 1
			}
		}

	}
}
