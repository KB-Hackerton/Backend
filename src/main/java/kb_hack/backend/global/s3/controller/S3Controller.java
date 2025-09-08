package kb_hack.backend.global.s3.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kb_hack.backend.global.common.exception.enums.SuccessStatusCode;
import kb_hack.backend.global.common.response.success.SuccessResponse;
import kb_hack.backend.global.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile-image")
@Tag(name = "S3 API", description = "프로필 이미지 관련 기능 제공")
public class S3Controller {

    private final S3Service s3Service;

    @Operation(
            summary = "S3 이미지 업로드",
            description = "이미지를 S3에 업로드하고 업로드된 이미지 URL을 반환합니다.",
            security = @SecurityRequirement(name = "bearerAuth")

    )
    @ApiResponse(
            responseCode = "200",
            description = "이미지 업로드 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SuccessResponse.class)
            )
    )
    @PostMapping("/upload")
    public SuccessResponse<String> upload(
            @org.springframework.web.bind.annotation.RequestPart("file")
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "업로드할 이미지 파일",
                    required = true,
                    content = @Content(mediaType = "multipart/form-data",
                            schema = @Schema(type = "string", format = "binary"))
            )
            MultipartFile file
    ) throws IOException {
        return SuccessResponse.makeResponse(SuccessStatusCode.IMAGE_UPLOAD_SUCCESS, s3Service.uploadFile(file));
    }


}
