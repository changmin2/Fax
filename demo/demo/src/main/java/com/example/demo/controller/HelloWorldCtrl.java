package com.example.demo.controller;

import com.example.demo.GlobalVariables;
import com.example.demo.service.DetectionServiceV2;
import com.example.demo.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class HelloWorldCtrl {

    private final DetectionServiceV2 detectionServiceV2;
    private final SmsService smsService;
    private final GlobalVariables globalVariables;

    @RequestMapping("/hello")
    public void hello() throws IOException {
        log.info(globalVariables.getNow());
    }

}