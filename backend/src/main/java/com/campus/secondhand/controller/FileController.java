package com.campus.secondhand.controller;

import com.campus.secondhand.common.Result;
import com.campus.secondhand.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        return Result.ok(fileService.upload(file));
    }

    /**
     * Proxy images from MinIO through the backend.
     * This avoids exposing MinIO URLs directly and works through the Vite dev proxy.
     */
    @GetMapping("/goods/{objectName}")
    public ResponseEntity<Resource> getImage(@PathVariable String objectName) {
        Resource resource = fileService.getImage("goods/" + objectName);
        MediaType mediaType = guessContentType(objectName);
        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(resource);
    }

    private MediaType guessContentType(String filename) {
        String lower = filename.toLowerCase();
        if (lower.endsWith(".png")) return MediaType.IMAGE_PNG;
        if (lower.endsWith(".gif")) return MediaType.IMAGE_GIF;
        if (lower.endsWith(".webp")) return MediaType.parseMediaType("image/webp");
        if (lower.endsWith(".svg")) return MediaType.parseMediaType("image/svg+xml");
        return MediaType.IMAGE_JPEG; // default for jpg, jpeg, and others
    }
}
