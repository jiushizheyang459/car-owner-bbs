package com.ori.controller;

import com.ori.domain.ResponseResult;
import com.ori.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping("/upload")
    public ResponseResult uploadImg(MultipartFile imgFile) {
        String vo = uploadService.uploadImg(imgFile);
        return ResponseResult.okResult(vo);
    }
}
