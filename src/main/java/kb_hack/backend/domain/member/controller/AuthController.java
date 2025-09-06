package kb_hack.backend.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kb_hack.backend.domain.member.dto.request.LoginUpdateNewPassword;
import kb_hack.backend.domain.member.dto.request.MemberInfoRequestDTO;
import kb_hack.backend.domain.member.dto.request.SigunUpRequestDTO;
import kb_hack.backend.domain.member.service.MemberInfoService;
import kb_hack.backend.domain.member.service.MemberService;
import kb_hack.backend.global.common.exception.enums.SuccessStatusCode;
import kb_hack.backend.global.common.response.bad.BadResponse;
import kb_hack.backend.global.common.response.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "로그인/회원가입/정보수정")
public class AuthController {

    private final MemberService memberService;
    private final MemberInfoService memberInfoService;

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

    @Operation(
            summary = "비밀번호 변경(로그인 페이지)",
            description = """
            회원 이메일을 기반으로 새 비밀번호를 설정합니다.  
            - 입력받은 비밀번호는 서버에서 BCrypt로 암호화되어 저장됩니다.  
            """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 변경 성공",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (이메일 없음, 비밀번호 누락 등)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/password")
    public SuccessResponse<Void> changeNewPassword(@RequestBody LoginUpdateNewPassword loginUpdateNewPassword){
        memberService.updatePassword(loginUpdateNewPassword.getMemberEmail(),loginUpdateNewPassword.getPassword());
        return SuccessResponse.makeResponse(SuccessStatusCode.CHANGE_NEW_PASSWORD_SUCCESS);
    }

    @PatchMapping("/member-info")
    @Operation(
            summary = "회원/사업체 정보 수정",
            security = @SecurityRequirement(name = "bearerAuth"),
            description = """
            로그인된 회원의 사업체 정보와 회원 이름을 수정합니다.  
            - `minor_name` 값으로 업종 분류 ID를 조회하여 업데이트합니다.  
            - 사업체 정보와 회원 이름이 모두 정상적으로 업데이트되지 않으면 실패합니다.
            """
    )
    @ApiResponse(responseCode = "200", description = "회원 정보 수정 성공",
            content = @Content(schema = @Schema(implementation = SuccessResponse.class)))
    @ApiResponse(responseCode = "400", description = "잘못된 요청 (예: 잘못된 minor_name 입력, 파라미터 오류)")
    @ApiResponse(responseCode = "404", description = "존재하지 않는 회원")
    @ApiResponse(responseCode = "500", description = "서버 내부 오류 (DB 처리 실패 등)")
    public SuccessResponse<Void> updateUserInfo(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "수정할 회원/사업체 정보",
                    required = true,
                    content = @Content(schema = @Schema(implementation = MemberInfoRequestDTO.class))
            )
            @RequestBody MemberInfoRequestDTO memberInfoRequestDTO
    ) {
        memberInfoService.updateMemberInfo(memberInfoRequestDTO);
        return SuccessResponse.makeResponse(SuccessStatusCode.CHANGE_MEMBER_INFO_SUCCESS);
    }
}

