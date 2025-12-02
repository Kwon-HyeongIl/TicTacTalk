package com.khi.ragservice.client;

import com.khi.ragservice.common.api.ApiResponse;
import com.khi.ragservice.dto.UserNicknameRequestDto;
import com.khi.ragservice.dto.UserProfileResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Feign client for security-service user profile operations
 */
@FeignClient(name = "security-service", url = "${security-service.url}")
public interface UserProfileClient {

    @PostMapping("/security/feign/user/nickname")
    ApiResponse<UserProfileResponseDto> getUserNickname(@RequestBody UserNicknameRequestDto requestDto);
}
