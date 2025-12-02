package com.khi.ragservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for fetching user nickname from security-service
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserNicknameRequestDto {
    private String userId;
}
