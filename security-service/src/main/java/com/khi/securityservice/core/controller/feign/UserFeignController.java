package com.khi.securityservice.core.controller.feign;

import com.khi.securityservice.common.api.ApiResponse;
import com.khi.securityservice.core.controller.dto.UserNicknameRequestDto;
import com.khi.securityservice.core.controller.dto.UserNicknameResponseDto;
import com.khi.securityservice.core.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Feign 전용 컨트롤러 (다른 서비스에서 호출)
 */
@Hidden
@Slf4j
@RestController
@RequestMapping("/security/feign")
@RequiredArgsConstructor
public class UserFeignController {

    private final UserService userService;

    @PostMapping("/user/nickname")
    public ResponseEntity<ApiResponse<UserNicknameResponseDto>> getUserNickname(
            @RequestBody UserNicknameRequestDto requestDto) {

        log.info("[SECURITY-SERVICE][FEIGN] (Rag-Service에서 요청) Received request for userId: {}", requestDto.getUserId());

        UserNicknameResponseDto response = userService.getUserNickname(requestDto.getUserId());

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
