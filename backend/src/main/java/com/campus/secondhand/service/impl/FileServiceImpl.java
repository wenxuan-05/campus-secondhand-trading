package com.campus.secondhand.service.impl;

import com.campus.secondhand.config.MinioConfig;
import com.campus.secondhand.service.FileService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final MinioClient minioClient;
    private final MinioConfig minioConfig;

    @Value("${app.upload.local-dir:uploads}")
    private String localUploadDir;

    private boolean minioAvailable = false;

    @PostConstruct
    public void init() {
        // Check if MinIO is reachable
        try {
            minioClient.bucketExists(
                    io.minio.BucketExistsArgs.builder().bucket(minioConfig.getBucket()).build());
            minioAvailable = true;
            log.info("MinIO is available — using MinIO for file storage");
        } catch (Exception e) {
            minioAvailable = false;
            log.warn("MinIO is NOT available — falling back to local file storage ({}): {}",
                    localUploadDir, e.getMessage());
        }

        // Ensure local upload directory exists
        try {
            Path goodsDir = Paths.get(localUploadDir, "goods");
            Files.createDirectories(goodsDir);
            Path avatarsDir = Paths.get(localUploadDir, "avatars");
            Files.createDirectories(avatarsDir);
            log.info("Local upload directories ready: goods, avatars");
        } catch (Exception e) {
            log.error("Failed to create local upload directories", e);
        }
    }

    @Override
    public String upload(MultipartFile file) {
        String originalName = file.getOriginalFilename();
        String ext = "";
        if (originalName != null && originalName.contains(".")) {
            ext = originalName.substring(originalName.lastIndexOf("."));
        }
        String uuid = UUID.randomUUID().toString();
        String objectName = "goods/" + uuid + ext;

        try {
            if (minioAvailable) {
                // Upload to MinIO
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(minioConfig.getBucket())
                        .object(objectName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build());
                log.info("File uploaded to MinIO: {}", objectName);
            } else {
                // Upload to local disk
                Path targetPath = Paths.get(localUploadDir, objectName);
                Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                log.info("File saved locally: {}", targetPath.toAbsolutePath());
            }
        } catch (Exception e) {
            log.error("File upload failed", e);
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        }

        // Return backend proxy URL (controller maps /api/file/goods/{uuid}.ext)
        return "/api/file/goods/" + uuid + ext;
    }

    @Override
    public String upload(MultipartFile file, String prefix) {
        String originalName = file.getOriginalFilename();
        String ext = "";
        if (originalName != null && originalName.contains(".")) {
            ext = originalName.substring(originalName.lastIndexOf("."));
        }
        String uuid = UUID.randomUUID().toString();
        String objectName = prefix + "/" + uuid + ext;

        try {
            if (minioAvailable) {
                // Upload to MinIO
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(minioConfig.getBucket())
                        .object(objectName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build());
                log.info("File uploaded to MinIO: {}", objectName);
            } else {
                // Upload to local disk
                Path targetPath = Paths.get(localUploadDir, objectName);
                Files.createDirectories(targetPath.getParent());
                Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                log.info("File saved locally: {}", targetPath.toAbsolutePath());
            }
        } catch (Exception e) {
            log.error("File upload failed", e);
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        }

        // Return backend proxy URL
        return "/api/file/" + prefix + "/" + uuid + ext;
    }

    @Override
    public Resource getImage(String objectName) {
        try {
            if (minioAvailable) {
                // Try MinIO first
                try {
                    InputStream stream = minioClient.getObject(GetObjectArgs.builder()
                            .bucket(minioConfig.getBucket())
                            .object(objectName)
                            .build());
                    return new InputStreamResource(stream);
                } catch (Exception e) {
                    log.warn("MinIO get failed, trying local: {}", objectName);
                    return loadFromLocal(objectName);
                }
            } else {
                return loadFromLocal(objectName);
            }
        } catch (Exception e) {
            log.error("Get image failed: {}", objectName, e);
            throw new RuntimeException("获取图片失败: " + e.getMessage());
        }
    }

    private Resource loadFromLocal(String objectName) {
        try {
            Path filePath = Paths.get(localUploadDir, objectName);
            InputStream stream = Files.newInputStream(filePath);
            return new InputStreamResource(stream);
        } catch (Exception e) {
            throw new RuntimeException("本地文件不存在: " + objectName, e);
        }
    }
}
