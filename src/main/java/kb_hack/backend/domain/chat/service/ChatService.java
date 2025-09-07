package kb_hack.backend.domain.chat.service;

import static kb_hack.backend.global.common.exception.enums.BadStatusCode.*;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kb_hack.backend.domain.chat.controller.ChatMessageResponse;
import kb_hack.backend.domain.chat.dto.ChatMessageDto;
import kb_hack.backend.domain.chat.entity.ChatMessage;
import kb_hack.backend.domain.chat.entity.ChatRoom;
import kb_hack.backend.domain.chat.entity.ChatRoomState;
import kb_hack.backend.domain.chat.entity.ReadStatus;
import kb_hack.backend.domain.chat.mapper.ChatRoomMapper;
import kb_hack.backend.domain.chat.mapper.ChatRoomStateMapper;
import kb_hack.backend.domain.chat.mapper.ReadStatusMapper;
import kb_hack.backend.domain.member.domain.Member;
import kb_hack.backend.domain.member.mapper.MemberMapper;
import kb_hack.backend.domain.member.service.MemberService;
import kb_hack.backend.domain.sos.entity.Sos;
import kb_hack.backend.domain.sos.mapper.SosMapper;
import kb_hack.backend.global.common.exception.type.CustomException;
import kb_hack.backend.global.security.entity.MemberVO;
import kb_hack.backend.global.security.mapper.SecurityMemberMapper;
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
	// private final ChatMessageMapper chatMessageMapper;

	private final ChatRoomStateMapper chatRoomStateMapper;

	// private final ReadStatusMapper readStatusMapper;

	// private final MemberDomainMapper memberDomainMapper;


	public void addParticipantToRoom(ChatRoom chatRoom, Member member) {
		ChatRoomState chatRoomState = ChatRoomState.builder()
			.chatRoomId(chatRoom.getChatRoomId())
			.memberId(member.getMemberId())
			.build();
		chatRoomStateMapper.save(chatRoomState);
	}

	/**
	 * 해당 roomId에 email을 가진 사용자가 참여자인지 체크
	 * @param email
	 * @param roomId
	 * @return
	 */
	public boolean isRoomParticipant(String email, Long roomId) {
		// 해당 roomId에 email을 가진 사용자가 참여자인지 체크
		// 1. 채팅방 조회
		ChatRoom chatRoom = chatRoomMapper.findByRoomId(roomId);
		// 2. 현재 로그인한 사용자 조회
		Member member = memberMapper.getMemberByEmail(email);

		// 3. 참여자 여부 체크
		List<ChatRoomState> chatRoomState = chatRoomStateMapper.findByChatRoom(chatRoom.getChatRoomId());
		for (ChatRoomState c : chatRoomState) {
			if (c.getMemberId().equals(member.getMemberId())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 메시지 저장하기
	 * @param roomId
	 * @param chatMessageDto
	 */
	@Transactional
	public void saveMessage(Long roomId, ChatMessageDto chatMessageDto) {
		// 1. 채팅방 조회
		ChatRoom chatRoom = chatRoomMapper.findByRoomId(roomId);
		if (chatRoom == null) {
			throw new CustomException(CHAT_ROOM_NOT_FOUND);
		}
		// 2. 보낸 사람 조회

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		log.info("authentication: {}", authentication);
		MemberVO memberVO = (MemberVO) authentication.getPrincipal();

		Member member = memberMapper.getMemberByEmail(memberVO.getMemberEmail());
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
	}

	/**
	 * 1:1 채팅방 생성
	 * @param sosId
	 * @param otherMemberId
	 * @return
	 */
	public void createPrivateChatRoom(MemberVO memberVO, Long sosId, Long otherMemberId) {
		// 1-1. 현재 로그인한 사용자 조회
		Member member = memberMapper.getMemberByMemberId(memberVO.getMemberId());
		// 1.2. 상대방 사용자 조회
		Member otherMember = memberMapper.getMemberByMemberId(otherMemberId);
		if (otherMember == null) {
			throw new CustomException(USER_NOT_FOUND_EXCEPTION);
		}
		// 1.3. sos 조회
		Sos sos = sosMapper.findById(sosId);
		if (sos == null) {
			throw new CustomException(CHAT_SOS_NOT_FOUND);
		}
		// 2. 채팅방 생성
		ChatRoom newChatRoom = ChatRoom
			.builder()
			.roomName(sos.getSosTitle())
			.sosId(sosId)
			.roomType(sos.getSosType())
			.isComplete(0)
			.ownerId(otherMemberId)
			.build();

		log.info("chatRoom: {}", newChatRoom);
		int affectedRows = chatRoomMapper.save(newChatRoom);
		log.info("chatRoom: {}", newChatRoom.getChatRoomId());
		// 3. 두사람 모두 참여자로 추가
		addParticipantToRoom(newChatRoom, member);
		addParticipantToRoom(newChatRoom, otherMember);

		if (affectedRows < 1) {
			throw new CustomException(CHAT_ROOM_CREATE_FAIL);
		}

	}

	public List<ChatMessageResponse> getChatHistory(Long roomId, Long memberId) {
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
			throw new IllegalArgumentException("본인이 속하지 않은 채팅방입니다." );
		}
		// 2. 특정 room에 대한 message 조회

		List<ChatMessage> chatMessages = chatRoomMapper.getChatMessagesByRoomId(roomId);
		List<ChatMessageResponse> chatMessageResponses = chatMessages.stream().map(c -> ChatMessageResponse.builder()
			.chatMessageId(c.getChatMessageId())
			.content(c.getContent())
			.senderId(c.getSenderId())
			.createdAt(c.getCreatedAt())
			.build()).toList();
		return chatMessageResponses;
	}

	// public void saveMessage(Long roomId, ChatMessageDto chatMessageDtoRequest) {
	// 	// 1. 채팅방 조회
	// 	ChatRoom chatRoom = chatRoomMapper.findById(roomId).orElseThrow(
	// 		() -> new EntityNotFoundException("room cannot found. id: ")
	// 	);
	// 	// 2. 보낸 사람 조회
	// 	Member sender = memberMapper.findByEmail(chatMessageDtoRequest.getSenderEmail()).orElseThrow(
	// 		() -> new EntityNotFoundException("sender cannot found. email: ")
	// 	);
	// 	// 3. 채팅 메시지 저장
	// 	ChatMessage chatMessage = ChatMessage.builder()
	// 		.chatRoom(chatRoom)
	// 		.member(sender)
	// 		.content(chatMessageDtoRequest.getMessage())
	// 		.build();
	// 	chatMessageMapper.save(chatMessage);
	// 	// 4. 사용자 별로 읽음 여부 저장
	// 	List<ChatParticipant> chatParticipants = chatParticipantMapper.findByChatRoom(chatRoom);
	// 	for (ChatParticipant c : chatParticipants) {
	// 		ReadStatus readStatus = ReadStatus.builder()
	// 			.chatRoom(c.getChatRoom())
	// 			.member(c.getMember())
	// 			.chatMessage(chatMessage)
	// 			.isRead(c.getMember().equals(sender))
	// 			.build();
	// 		readStatusMapper.save(readStatus);
	// 	}
	//
	// }

	// public void createGroupChatRoom(String roomName) {
	// 	Member member = memberMapper
	// 		.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
	// 		.orElseThrow(EntityNotFoundException::new);
	//
	// 	// 1. 채팅방 생성
	// 	ChatRoom chatRoom = ChatRoom.builder()
	// 		.name(roomName)
	// 		.isGroupChat("Y")
	// 		.build();
	// 	chatRoomMapper.save(chatRoom);
	// 	// 2. 채팅방 참여자 추가 (현재 로그인한 사용자)
	// 	ChatParticipant chatParticipant = ChatParticipant.builder()
	// 		.chatRoom(chatRoom)
	// 		.member(member)
	// 		.build();
	//
	// 	chatParticipantMapper.save(chatParticipant);
	//
	// }

	/**
	 * 전체 그룹채팅방 목록 조회
	 * @return List<ChatRoomListResponse>
	 */
	// public List<ChatRoomListResponse> getAllGroupChatRooms() {
	// 	List<ChatRoom> chatRooms = chatRoomMapper.findByIsGroupChat("Y");
	// 	List<ChatRoomListResponse> chatRoomDtos = new ArrayList<>();
	// 	for (ChatRoom c : chatRooms) {
	// 		ChatRoomListResponse dto = ChatRoomListResponse.builder()
	// 			.roomId(c.getChatRoomId())
	// 			.roomName(c.getName())
	// 			.build();
	// 		chatRoomDtos.add(dto);
	// 	}
	// 	return chatRoomDtos;
	// }

	/**
	 * 자신이 속한 그룹채팅방 목록 조회
	 * @return List<ChatRoomListResponse>
	 */
	// public List<MyChatListResponse> getMyChatRooms() {
	// 	// 1. 현재 로그인한 사용자 조회
	// 	Member member = memberMapper.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(EntityNotFoundException::new);
	// 	// 2. 참여중인 그룹채팅방 조회
	// 	List<ChatParticipant> chatParticipants = chatParticipantMapper.findAllByMember(member);
	//
	// 	// 3. DTO로 변환
	// 	List<MyChatListResponse> myChatListResponses = new ArrayList<>();
	// 	for (ChatParticipant c : chatParticipants) {
	// 		Long count = readStatusMapper.countByChatRoomAndMemberAndIsReadFalse(c.getChatRoom(), member);
	// 		MyChatListResponse dto = MyChatListResponse.builder()
	// 			.roomId(c.getChatRoom().getChatRoomId())
	// 			.roomName(c.getChatRoom().getName())
	// 			.isGroupChat(c.getChatRoom().getIsGroupChat())
	// 			.unReadCount(count)
	// 			.build();
	// 		myChatListResponses.add(dto);
	// 	}
	//
	// 	return myChatListResponses;
	//
	// }

	// public void addParticipantToGroupChat(Long roomId) {
	// 	// 1. 채팅방 조회
	// 	ChatRoom chatRoom = chatRoomMapper.findById(roomId).orElseThrow(
	// 		() -> new EntityNotFoundException("room cannot found. id: " + roomId)
	// 	);
	//
	// 	// 2. 현재 로그인한 사용자 조회
	// 	Member member = memberMapper
	// 		.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
	// 		.orElseThrow(EntityNotFoundException::new);
	//
	// 	// 3. 단체 채팅방 여부 체크
	// 	if(chatRoom.getIsGroupChat().equals("N")) {
	// 		throw new IllegalArgumentException("단체 채팅방이 아닙니다.");
	// 	}
	//
	// 	// 4. 중복 참여자 체크
	// 	Optional<ChatParticipant> participant = chatParticipantMapper.findByChatRoomAndMember(chatRoom, member);
	//
	// 	if (!participant.isPresent()) {
	// 		addParticipantToRoom(chatRoom, member);
	// 	}
	//
	//
	// }
	//
	// // ChatParticipant 엔티티 생성 및 저장
	//
	// public void addParticipantToRoom(ChatRoom chatRoom, Member member) {
	// 	ChatParticipant chatParticipant = ChatParticipant.builder()
	// 		.chatRoom(chatRoom)
	// 		.member(member)
	// 		.build();
	//
	// 	chatParticipantMapper.save(chatParticipant);
	// }

	// public List<ChatMessageResponse> getChatHistory(Long roomId) {
	// 	// 1. 해당 채팅방의 참여자가 아닐경우 예외처리
	// 	// 1-1. 채팅방 조회
	// 	ChatRoom chatRoom = chatRoomMapper.findById(roomId).orElseThrow(() -> new EntityNotFoundException("room cannot found. id: " + roomId));
	// 	// 1-2. 현재 로그인한 사용자 조회
	// 	Member member = memberMapper.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(EntityNotFoundException::new);
	// 	// 1-3. 참여자 여부 체크
	// 	List<ChatParticipant> chatParticipants = chatParticipantMapper.findByChatRoom(chatRoom);
	// 	boolean check = false;
	// 	for (ChatParticipant c : chatParticipants) {
	// 		if (c.getMember().equals(member)) {
	// 			check = true;
	// 		}
	// 	}
	// 	if (!check) {
	// 		throw new IllegalArgumentException("본인이 속하지 않은 채팅방입니다." );
	// 	}
	//
	// 	// 2. 특정 room에 대한 message 조회
	// 	List<ChatMessage> chatMessages = chatMessageMapper.findByChatRoomOrderByCreatedTimeAsc(chatRoom);
	// 	List<ChatMessageResponse> chatMessageResponses = new ArrayList<>();
	// 	for (ChatMessage c : chatMessages) {
	// 		ChatMessageResponse dto = ChatMessageResponse.builder()
	// 			.message(c.getContent())
	// 			.senderEmail(c.getMember().getEmail())
	// 			.build();
	//
	// 		chatMessageResponses.add(dto);
	// 	}
	//
	// 	return chatMessageResponses;
	//
	// }

	// public boolean isRoomParticipant(String email, Long roomId) {
	// 	// 해당 roomId에 email을 가진 사용자가 참여자인지 체크
	//
	// 	// 1. 채팅방 조회
	// 	ChatRoom chatRoom = chatRoomMapper.findById(roomId).orElseThrow(() -> new EntityNotFoundException("room cannot found. id: " + roomId));
	// 	// 2. 현재 로그인한 사용자 조회
	// 	Member member = memberMapper.findByEmail(email).orElseThrow(EntityNotFoundException::new);
	//
	// 	// 3. 참여자 여부 체크
	// 	List<ChatParticipant> chatParticipants = chatParticipantMapper.findByChatRoom(chatRoom);
	// 	for (ChatParticipant c : chatParticipants) {
	// 		if (c.getMember().equals(member)) {
	// 			return true;
	// 		}
	// 	}
	// 	return false;
	// }

	// public void messageRead(Long roomId) {
	// 	// 1. 해당 채팅방의 참여자가 아닐경우 예외처리
	// 	// 1.1. 채팅방 조회
	// 	ChatRoom chatRoom = chatRoomMapper.findById(roomId).orElseThrow(() -> new EntityNotFoundException("room cannot found. id: " + roomId));
	// 	// 1.2. 현재 로그인한 사용자 조회
	// 	Member member = memberMapper.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(EntityNotFoundException::new);
	// 	// 2.1. 메시지 읽음 여부 조회
	// 	List<ReadStatus> readStatuses = readStatusMapper.findByChatRoomAndMember(chatRoom, member);
	// 	// 2.2. 읽음 처리
	// 	for (ReadStatus r : readStatuses) {
	// 		r.updateReadStatus(true);
	// 	}
	//
	// }

	// del Y/N
	// 1. 참여자 객체 삭제
	// 2. 모두가 나갔을 경우 모든 엔티티 삭제
	// public void leaveChatRoom(Long roomId) {
	// 	// 1.1. 채팅방 조회
	// 	ChatRoom chatRoom = chatRoomMapper.findById(roomId).orElseThrow(() -> new EntityNotFoundException("room cannot found. id: " + roomId));
	// 	// 1.2. 현재 로그인한 사용자 조회
	// 	Member member = memberMapper.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(EntityNotFoundException::new);
	//
	// 	if(chatRoom.getIsGroupChat().equals("N")) {
	// 		throw new IllegalArgumentException("단체 채팅방이 아닙니다.");
	// 	}
	//
	// 	ChatParticipant c = chatParticipantMapper.findByChatRoomAndMember(chatRoom, member).orElseThrow(() -> new EntityNotFoundException("participant cannot found. roomId: " + roomId + ", memberId: " + member.getMemberId()));
	// 	chatParticipantMapper.delete(c);
	//
	// 	List<ChatParticipant> participants = chatParticipantMapper.findByChatRoom(chatRoom);
	// 	if (participants.isEmpty()) {
	// 		chatRoomMapper.delete(chatRoom);
	// 	}
	//
	// }

	// public Long getOrCreatePrivateChatRoom(String otherMemberId) {
	// 	// 1-1. 현재 로그인한 사용자 조회
	// 	Member member = memberMapper
	// 		.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
	// 		.orElseThrow(EntityNotFoundException::new);
	//
	// 	// 1.2. 상대방 사용자 조회
	// 	Member otherMember = memberMapper
	// 		.findById(Long.parseLong(otherMemberId)).orElseThrow(() -> new EntityNotFoundException("member cannot found. id: " + otherMemberId));
	//
	// 	// 2. 상대방이 1:1 채팅에 이미 참여하고 있다면 roomId return
	// 	Optional<ChatRoom> chatRoom = chatParticipantMapper.findChatRoomIdExistingPrivateRoom(member.getMemberId(), otherMember.getMemberId());
	// 	if (chatRoom.isPresent()) {
	// 		return chatRoom.get().getChatRoomId();
	// 	}
	// 	// 3. 만약 1:1 채팅방이 없다면 새로 생성
	// 	ChatRoom newChatRoom = ChatRoom.builder()
	// 		.isGroupChat("N")
	// 		.name(member.getName() + "-" + otherMember.getName())
	// 		.owner(member)
	// 		.build();
	// 	chatRoomMapper.save(newChatRoom);
	//
	// 	// 4. 두사람 모두 참여자로 추가
	// 	addParticipantToRoom(newChatRoom, member);
	// 	addParticipantToRoom(newChatRoom, otherMember);
	// 	return newChatRoom.getChatRoomId();
	// }
}
