package com.example.demo.controller;

import com.example.demo.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class S3TestController {

        private final S3Uploader s3Uploader;

        @PostMapping("/s3")
        @ResponseBody
        public String updateUserImage(@RequestParam("images") MultipartFile multipartFile) {
            try {
                s3Uploader.uploadFiles(multipartFile, "static");
            } catch (Exception e) {
                return "실패";
            }
            return "성공";
        }
}

