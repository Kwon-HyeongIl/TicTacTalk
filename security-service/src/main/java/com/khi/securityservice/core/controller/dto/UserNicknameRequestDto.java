package com.khi.securityservice.core.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for Feign requests to get user nickname
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserNicknameRequestDto {
    private String userId;
}
