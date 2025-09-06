package kb_hack.backend.domain.sos.controller;

import kb_hack.backend.domain.sos.dto.SosCreateRequest;
import kb_hack.backend.domain.sos.dto.SosCreateResponse;
import kb_hack.backend.domain.sos.entity.SosType;
import kb_hack.backend.domain.sos.service.SosService;
import kb_hack.backend.global.security.dto.SecurityCustomUser;
import kb_hack.backend.global.security.entity.MemberVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/sos")
@RequiredArgsConstructor
@Validated
public class SosController {

	private final SosService sosService;
	@PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public SosCreateResponse createSos(
		@RequestParam(required = false) String sosTitle,
		@RequestParam SosType sosType,
		@RequestParam String sosContent,
		@RequestParam String expiresAt,
		@RequestPart(name = "images", required = false) List<MultipartFile> images
	) {
		Long memberId = getLoginMemberId(); // ✅ 로그인한 사용자 ID 가져오기

		SosCreateRequest req = SosCreateRequest.builder()
			.memberId(memberId)
			.sosTitle(sosTitle)
			.sosType(sosType)
			.sosContent(sosContent)
			.expiresAt(expiresAt)
			.images(images)
			.build();

		return sosService.create(req);
	}

	// ✅ 공통 메서드 (Service에 두거나 Util로 분리해도 됨)
	private Long getLoginMemberId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		SecurityCustomUser securityUser = (SecurityCustomUser) authentication.getPrincipal();
		MemberVO vo = securityUser.getMemberVO();
		return vo.getMemberId();
	}

}
