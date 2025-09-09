package kb_hack.backend.domain.alarm.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kb_hack.backend.domain.alarm.dto.AlarmPreferenceReq;
import kb_hack.backend.domain.alarm.dto.AlarmPreferenceRes;
import kb_hack.backend.domain.alarm.service.AlarmPreferenceService;
import kb_hack.backend.domain.email.dto.request.MailVerificationDTO;
import kb_hack.backend.global.common.exception.enums.SuccessStatusCode;
import kb_hack.backend.global.common.response.success.SuccessResponse;
import kb_hack.backend.global.security.dto.SecurityCustomUser;
import kb_hack.backend.global.security.entity.MemberVO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alarm")
@RequiredArgsConstructor
@Tag(name = "alarm ", description = "알림 API ")
public class AlarmController {
    private final AlarmPreferenceService alarmPreferenceService;

    @GetMapping("/preference")
    @Operation(
            summary = "선호 알림 가져오기 ",
            description = "사용자가 받고자 하는 알림을 확인합니다",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public SuccessResponse<AlarmPreferenceRes> get(@AuthenticationPrincipal SecurityCustomUser user){
        Long memberId = user.getMemberVO().getMemberId();
        return SuccessResponse.makeResponse(SuccessStatusCode.ALARM_SETTING_GET_SUCCESS,alarmPreferenceService.getOrCreate(memberId));
    }


    @PatchMapping("/preference")
    public void update(@AuthenticationPrincipal SecurityCustomUser user,
                       @Valid @RequestBody AlarmPreferenceReq req) {
        alarmPreferenceService.partialUpdate(user.getMemberVO().getMemberId(), req);
    }
}
