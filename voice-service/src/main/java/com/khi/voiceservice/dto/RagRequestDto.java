package com.khi.voiceservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RagRequestDto {
    private String user1Id;
    private String user2Id;
    private List<ChatMessageDto> chatData;
}
