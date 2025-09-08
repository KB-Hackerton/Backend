package kb_hack.backend.domain.alarm.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kb_hack.backend.domain.alarm.Notification;
import kb_hack.backend.domain.alarm.dto.AlarmPreferenceReq;
import kb_hack.backend.domain.alarm.dto.AlarmPreferenceRes;
import kb_hack.backend.domain.alarm.dto.NotificationRes;
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

import java.util.List;

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
    @Operation(
            summary = "선호 알림 설정하기 ",
            description = "하나라도 알림설정이 바뀌면 바로 db 변경",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public void update(@AuthenticationPrincipal SecurityCustomUser user,
                       @Valid @RequestBody AlarmPreferenceReq req) {
        alarmPreferenceService.partialUpdate(user.getMemberVO().getMemberId(), req);
    }

    @GetMapping
    @Operation(
            summary = "알림 조회",
            description = "수신받은 알림 내용을 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public SuccessResponse<List<NotificationRes>> list(@AuthenticationPrincipal SecurityCustomUser user){
        Long memberId = user.getMemberVO().getMemberId();
        return SuccessResponse.makeResponse(SuccessStatusCode.ALARM_LIST_GET_SUCCESS,alarmPreferenceService.list(memberId));
    }

    @PostMapping
    @Operation(
            summary = "알림 생성",
            description = "알림을 생성합니다 (관리자용)",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public void create(@AuthenticationPrincipal SecurityCustomUser user,
                       @RequestBody Notification p) {
        p.setMemberId(user.getMemberVO().getMemberId());
        alarmPreferenceService.create(p);
    }

    @PatchMapping("/{notificationId}/read")
    @Operation(
            summary = "단일 알림 읽음 처리 ",
            description = "읽은 알림을 처리합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public void markRead(@AuthenticationPrincipal SecurityCustomUser user,
                         @PathVariable Long notificationId) {
        alarmPreferenceService.markRead(user.getMemberVO().getMemberId(), notificationId);
    }

    @PostMapping("/read-all")
    @Operation(
            summary = "전체 알림 읽음 처리 ",
            description = "전체 알림 처리로 수정합니다 ",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public void markAllRead(@AuthenticationPrincipal SecurityCustomUser user) {
        alarmPreferenceService.markAllRead(user.getMemberVO().getMemberId());
    }

    @DeleteMapping("/{notificationId}")
    @Operation(
            summary = "단일 알림 삭제 ",
            description = "해당 알림을 삭제합니다 ",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public void deleteOne(@AuthenticationPrincipal SecurityCustomUser user,
                          @PathVariable Long notificationId) {
        alarmPreferenceService.deleteOne(user.getMemberVO().getMemberId(), notificationId);
    }

    @DeleteMapping
    @Operation(
            summary = "전체 알림 삭제  ",
            description = "전체 알림을 삭제합니다 ",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public void deleteAll(@AuthenticationPrincipal SecurityCustomUser user) {
        alarmPreferenceService.deleteAll(user.getMemberVO().getMemberId());
    }
}
