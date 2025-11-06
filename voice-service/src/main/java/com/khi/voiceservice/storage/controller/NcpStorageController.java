package com.khi.voiceservice.storage.controller;

import com.khi.voiceservice.storage.service.NcpStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/voice")
public class NcpStorageController {

    private final NcpStorageService ncpStorageService;

    public NcpStorageController(NcpStorageService ncpStorageService) {
        this.ncpStorageService = ncpStorageService;
    }

    // NCP Object Storage에 음성 파일 업로드
    @PostMapping("/upload")
    public ResponseEntity<Void> uploadFile(@RequestPart("file")MultipartFile voiceFile) {
        String fileUrl = ncpStorageService.uploadFile(voiceFile);
        return ResponseEntity.ok().build();
    }
}
