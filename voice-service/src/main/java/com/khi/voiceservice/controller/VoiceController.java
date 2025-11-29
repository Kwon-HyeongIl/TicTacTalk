package com.khi.voiceservice.controller;

import com.khi.voiceservice.Entity.Transcript;
import com.khi.voiceservice.client.ClovaSpeechClient;
import com.khi.voiceservice.client.RagClient;
import com.khi.voiceservice.dto.*;
import com.khi.voiceservice.repository.TranscriptRepository;
import com.khi.voiceservice.service.TranscriptService;
import com.khi.voiceservice.service.NcpStorageService;
import com.khi.voiceservice.common.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/voice")
@RequiredArgsConstructor
public class VoiceController {

    private final NcpStorageService ncpStorageService;
    private final ClovaSpeechClient clovaSpeechClient;
    private final TranscriptService transcriptService;
    private final RagClient ragClient;

    @Value("${clova.speech.callback-url}")
    private String callbackUrl;

    @PostMapping("/transcribe")
    public ResponseEntity<VoiceResponseDto> transcribe(
            @RequestPart("userdata") UserPairRequest userPairRequest,
            @RequestPart("file")MultipartFile voiceFile
    ) {
        String fileUrl = ncpStorageService.uploadFile(voiceFile);

        Long transcriptId = transcriptService.getTranscriptId(userPairRequest);
        VoiceResponseDto voiceResponseDto = new VoiceResponseDto();
        voiceResponseDto.setTranscribeId(transcriptId);

        clovaSpeechClient.asyncRecognize(fileUrl, callbackUrl, transcriptId);

        return ResponseEntity.ok(voiceResponseDto);
    }

    // 전사 결과 전달 받는 콜백
    @PostMapping("/callback")
    public ResponseEntity<Void> clovaCallback(
            @RequestBody String resultJson
    ) {
        RagRequestDto requestDto;
        try {
            requestDto = transcriptService.processClovaResult(resultJson);
        } catch (IllegalArgumentException e) {
            log.warn("[Clova] 콜백 파싱 실패: {}", e.getMessage());

            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("[Clova] clova 콜백 처리 중 예외 발생", e);

            return ResponseEntity.internalServerError().build();
        }

        if (requestDto == null) {
            return ResponseEntity.ok().build();
        }
        // Rag 분석 요청, 결과는 rag-service에서 저장
        ReportSummaryDto reportSummaryDto = ragClient.getRagResult(requestDto);
        log.info("[Rag] 분석 완료");



        return ResponseEntity.ok().build();
    }
}
