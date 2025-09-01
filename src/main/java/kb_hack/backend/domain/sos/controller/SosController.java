package kb_hack.backend.domain.sos.controller;

import kb_hack.backend.domain.sos.dto.SosCreateRequest;
import kb_hack.backend.domain.sos.dto.SosCreateResponse;
import kb_hack.backend.domain.sos.entity.SosType;
import kb_hack.backend.domain.sos.service.SosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/sos")
@RequiredArgsConstructor
@Validated
public class SosController {

	private final SosService sosService;

	// 프런트가 multipart로 보낼 경우 (텍스트 + 파일 동시)
	@PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public SosCreateResponse createSos(
		@RequestParam Long memberId,
		@RequestParam(required = false) String sosTitle,
		@RequestParam SosType sosType,
		@RequestParam String sosContent,
		@RequestParam String expiresAt,
		@RequestPart(name = "images", required = false) List<MultipartFile> images
	) {
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
}
