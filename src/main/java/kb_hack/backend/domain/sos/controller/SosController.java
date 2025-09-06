package kb_hack.backend.domain.sos.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import kb_hack.backend.global.common.response.success.SuccessResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kb_hack.backend.domain.sos.dto.SosCreateRequest;
import kb_hack.backend.domain.sos.dto.SosCreateResponse;
import kb_hack.backend.domain.sos.dto.SosDetailResponse;
import kb_hack.backend.domain.sos.dto.SosListResponse;
import kb_hack.backend.domain.sos.entity.SosType;
import kb_hack.backend.domain.sos.service.SosService;
import kb_hack.backend.global.common.exception.enums.SuccessStatusCode;
import kb_hack.backend.global.security.dto.SecurityCustomUser;
import kb_hack.backend.global.security.entity.MemberVO;
import lombok.RequiredArgsConstructor;

@RestController
@Tag(name = "Sos", description = "SOS API ")
@RequestMapping("/sos")
@RequiredArgsConstructor
@Validated
public class SosController {

	private final SosService sosService;

	@Operation(summary = "SOS 생성", description = "새로운 SOS 요청을 생성합니다.",security = @SecurityRequirement(name = "bearerAuth"))
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "SOS 생성 성공",
			content = @Content(schema = @Schema(implementation = SosCreateResponse.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
		@ApiResponse(responseCode = "500", description = "서버 에러")

	})
	@PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public SuccessResponse<SosCreateResponse> createSos(
		@Parameter(description = "SOS 제목") @RequestParam(required = false) String sosTitle,
		@Parameter(description = "SOS 유형") @RequestParam SosType sosType,
		@Parameter(description = "SOS 내용") @RequestParam String sosContent,
		@Parameter(description = "만료일 (yyyy-MM-dd HH:mm:ss)") @RequestParam String expiresAt,
		@Parameter(description = "이미지 파일들") @RequestPart(name = "images", required = false) List<MultipartFile> images
	) {
		Long memberId = getLoginMemberId();

		SosCreateRequest req = SosCreateRequest.builder()
			.memberId(memberId)
			.sosTitle(sosTitle)
			.sosType(sosType)
			.sosContent(sosContent)
			.expiresAt(expiresAt)
			.images(images)
			.build();

		SosCreateResponse response = sosService.create(req);
		return SuccessResponse.makeResponse(SuccessStatusCode.SOS_CREATE_SUCCESS,response);
	}

	@Operation(summary = "SOS 수정", description = "기존 SOS 요청을 수정합니다.",security = @SecurityRequirement(name = "bearerAuth"))
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "SOS 수정 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 SOS")
	})
	@PutMapping("/{sosId}")
	public SuccessResponse<Void> updateSos(
		@PathVariable Long sosId,
		@RequestParam(required = false) String sosTitle,
		@RequestParam SosType sosType,
		@RequestParam String sosContent,
		@RequestParam String expiresAt
	) {
		Long memberId = getLoginMemberId();
		SosCreateRequest req = SosCreateRequest.builder()
			.memberId(memberId)
			.sosTitle(sosTitle)
			.sosType(sosType)
			.sosContent(sosContent)
			.expiresAt(expiresAt)
			.build();
		sosService.update(sosId, req);
		return SuccessResponse.makeResponse(SuccessStatusCode.SOS_UPDATE_SUCCESS);
	}

	@Operation(summary = "SOS 삭제", description = "SOS 요청을 완전히 삭제합니다 (하드 딜리트).",security = @SecurityRequirement(name = "bearerAuth"))
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "SOS 삭제 성공"),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 SOS")
	})
	@DeleteMapping("/{sosId}")
	public SuccessResponse<Void> hardDeleteSos(@PathVariable Long sosId) {
		sosService.hardDelete(sosId);
		return SuccessResponse.makeResponse(SuccessStatusCode.SOS_DELETE_SUCCESS);
	}


	private Long getLoginMemberId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		SecurityCustomUser securityUser = (SecurityCustomUser) authentication.getPrincipal();
		MemberVO vo = securityUser.getMemberVO();
		return vo.getMemberId();
	}

	@Operation(summary = "SOS 목록 조회", description = "등록된 SOS 목록을 조회합니다.",security = @SecurityRequirement(name = "bearerAuth"))
	@GetMapping
	public ResponseEntity<List<SosListResponse>> getSosList() {
		return ResponseEntity.ok(sosService.getSosList());
	}

	@Operation(summary = "SOS 상세 조회", description = "특정 SOS 요청의 상세 정보를 조회합니다.",security = @SecurityRequirement(name = "bearerAuth"))
	@GetMapping("/{sosId}")
	public SuccessResponse<SosDetailResponse> getSosDetail(@PathVariable Long sosId) {
		return SuccessResponse.makeResponse(SuccessStatusCode.SOS_GET_SUCCESS,sosService.getSosDetail(sosId));
	}

}

