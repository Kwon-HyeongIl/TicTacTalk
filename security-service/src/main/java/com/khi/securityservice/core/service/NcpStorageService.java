package com.khi.securityservice.core.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class NcpStorageService {

    private final S3Client s3Client;

    @Value("${ncp.bucket-name}")
    private String bucketName;

    public NcpStorageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    // 프로필 이미지 업로드
    public String uploadFile(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            // 업로드 실행
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            log.info("프로필 이미지 업로드 완료");

            return "https://kr.object.ncloudstorage.com/" + bucketName + "/" + encodedFileName;
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패", e);
        }
    }
    // 프로필 이미지 삭제
    public void deleteFile(String profileImageUrl) {

        String objectKey = extractObjectKey(profileImageUrl);

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }
    // URL에서 삭제할 이미지 key 추출
    public String extractObjectKey(String profileImageUrl) {

        int bucketIdx = profileImageUrl.indexOf(bucketName);
        if (bucketIdx == -1) {
            throw new IllegalArgumentException("Invalid file URL: bucket name not found");
        }

        return profileImageUrl.substring(bucketIdx + bucketName.length() + 1);
    }
}
