package com.example.demo.controller;

import com.example.demo.service.PayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@CrossOrigin(
        // localhost:5500 과 127.0.0.1 구분
        origins = "http://localhost:5500", // allowCredentials = "true" 일 경우, orogins="*" 는 X
        allowCredentials = "true",
        allowedHeaders = "*",
        methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PUT,RequestMethod.HEAD,RequestMethod.OPTIONS}
)
@RequestMapping("/api")
@RestController
@Slf4j
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;
    @PostMapping("/payRecieve")
    public HashMap<String,String> payRecieve(@RequestParam("userId")String userId){
        payService.apprList(userId);
        return null;
    }

    @PostMapping("/payDetail")
    public HashMap<String,String> payDetail(@RequestParam("apprNo")String apprNo){
        payService.apprDetail(apprNo);
        return null;
    }

    @PostMapping("/apprOk")
    public void apprOk(@RequestParam("apprNo")String apprNo){
        log.info("apprOk진입");
        payService.apprOk(apprNo);
    }

    @PostMapping("/apprReturn")
    public void apprReturn(@RequestParam("apprNo")String apprNo){
        log.info("apprOk진입");
        payService.apprReturn(apprNo);
    }
}
