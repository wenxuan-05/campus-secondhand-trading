package com.campus.secondhand.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String upload(MultipartFile file);
    String upload(MultipartFile file, String prefix);
    Resource getImage(String objectName);
}
