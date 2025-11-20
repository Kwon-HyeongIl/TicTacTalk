package com.khi.ragservice.controller;

import com.khi.ragservice.service.RagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/rag")
@RequiredArgsConstructor
public class RagController {

    private final RagService ragService;

    @PostMapping("/receive")
    public String rag(@RequestBody String body) {

        log.info("[RagContorller] 응답 수신");
        return ragService.getRagResponse(body);
    }
}
