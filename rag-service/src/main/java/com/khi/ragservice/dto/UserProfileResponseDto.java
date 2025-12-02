package com.khi.ragservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for user profile response from security-service
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponseDto {
    private String userId;
    private String nickname;
}
