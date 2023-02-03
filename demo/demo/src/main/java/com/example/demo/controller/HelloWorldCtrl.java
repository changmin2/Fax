package com.example.demo.controller;

import com.example.demo.service.DetectionServiceV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class HelloWorldCtrl {

    private final DetectionServiceV2 detectionServiceV2;

    @RequestMapping("/hello")
    public void hello() throws IOException {
        detectionServiceV2.pdfTopng("TEST01120230202050356_1.pdf");
    }

}