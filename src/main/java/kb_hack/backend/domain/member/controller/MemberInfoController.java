package kb_hack.backend.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kb_hack.backend.domain.member.dto.request.MemberInfoUpdateNewPassword;
import kb_hack.backend.domain.member.service.MemberInfoService;
import kb_hack.backend.global.common.exception.enums.SuccessStatusCode;
import kb_hack.backend.global.common.response.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
@Tag(name = "회원 정보 API", description = "회원 비밀번호 변경 및 관련 기능을 제공합니다.")
public class MemberInfoController {
    private final MemberInfoService memberInfoService;

    @Operation(
            summary = "비밀번호 변경 API (마이페이지)",
            security = @SecurityRequirement(name = "bearerAuth"),
            description = """
        로그인한 사용자가 기존 비밀번호를 검증한 뒤,
        새로운 비밀번호로 변경합니다.
        
        - 기존 비밀번호(`originalPassword`)는 DB에 저장된 해시값과 비교됩니다.
        - 새 비밀번호(`newPassword`)는 BCrypt로 암호화되어 저장됩니다.
        - 성공 시 별도의 응답 데이터 없이 성공 코드만 반환됩니다.
        """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 변경 성공"),
            @ApiResponse(responseCode = "400", description = "기존 비밀번호 불일치 또는 잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "DB 처리 중 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/password")
    SuccessResponse<Void> changePassword(@RequestBody MemberInfoUpdateNewPassword memberInfoUpdateNewPassword){
        memberInfoService.updatePassword(memberInfoUpdateNewPassword.getOriginalPassword(),memberInfoUpdateNewPassword.getNewPassword());
        return SuccessResponse.makeResponse(SuccessStatusCode.CHANGE_NEW_PASSWORD_SUCCESS);
    }
}
