package kb_hack.backend.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kb_hack.backend.domain.member.dto.request.SigunUpRequestDTO;
import kb_hack.backend.domain.member.service.MemberService;
import kb_hack.backend.global.common.exception.enums.SuccessStatusCode;
import kb_hack.backend.global.common.response.bad.BadResponse;
import kb_hack.backend.global.common.response.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "로그인/회원가입/정보수정")
public class AuthController {

    private final MemberService memberService;

    @Operation(
            summary = "회원가입 API",
            description = "회원 이메일, 비밀번호, 이름, 사업체 정보를 받아서 회원과 사업체를 함께 등록합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값",
                    content = @Content(schema = @Schema(implementation = BadResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = BadResponse.class)))
    })

    @PostMapping("/member-info")
    public SuccessResponse<Void> signUpMember(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "회원가입 요청 DTO",
                    required = true,
                    content = @Content(schema = @Schema(implementation = SigunUpRequestDTO.class))
            )
            @RequestBody SigunUpRequestDTO requestDTO
    ) {
        memberService.signup(requestDTO);
        return SuccessResponse.makeResponse(SuccessStatusCode.SIGNUP_SUCCESS);
    }

    @DeleteMapping("/member-info")
    @Operation(
            summary = "회원 탈퇴",
            description = "현재 로그인한 사용자의 계정을 삭제합니다. "
                    + "인증된 JWT 토큰이 필요하며, 성공 시 회원 정보가 영구적으로 삭제됩니다.",
            security = @SecurityRequirement(name = "bearerAuth")

    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (요청 정보 오류 등)"),
            @ApiResponse(responseCode = "401", description = "인증 실패 (JWT 토큰 없음/만료)"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public SuccessResponse<Void> signOutMember(){
        memberService.delete();
        return SuccessResponse.makeResponse(SuccessStatusCode.SIGNOUT_SUCCESS);
    }
}

