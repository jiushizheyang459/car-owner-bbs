package com.ori.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    String uploadImg(MultipartFile imgFile);
}
