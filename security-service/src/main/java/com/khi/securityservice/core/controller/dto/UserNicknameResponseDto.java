package com.khi.securityservice.core.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for Feign requests to get user nickname
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserNicknameResponseDto {
    private String userId;
    private String nickname;
}
