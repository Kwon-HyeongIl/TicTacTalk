package com.khi.voiceservice.dto;

import com.khi.voiceservice.Entity.Transcript;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ClovaResultDto {
    private Transcript transcript;
    private String userId;
    private List<ChatMessageDto> chatData;
}
