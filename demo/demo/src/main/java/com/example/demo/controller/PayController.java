package com.example.demo.controller;

import com.example.demo.service.PayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;

    //결제 수신함
    @PostMapping("/payRecieve")
    public List<HashMap<String, Object>> payRecieve(@RequestParam("userId")String userId){
        List<HashMap<String, Object>> hashMaps = payService.apprList(userId);
        return hashMaps;
    }

    //결제 상세정보
    @PostMapping("/payDetail")
    public List<HashMap<String, Object>> payDetail(@RequestParam("apprNo")String apprNo){
        List<HashMap<String, Object>> hashMaps = payService.apprDetail(apprNo);
        return hashMaps;
    }

    //결제 승인
    @PostMapping("/apprOk")
    public void apprOk(@RequestParam("apprNo")String apprNo){
        log.info("apprOk진입");
        payService.apprOk(apprNo);
    }

    //반려 상태 업데이트
    @PostMapping("/apprReturn")
    public void apprReturn(@RequestParam("apprNo")String apprNo){
        log.info("apprOk진입");
        payService.apprReturn(apprNo);
    }

    //발신 수신함
    @PostMapping("/sendRecieve")
    public List<HashMap<String, Object>> sendRecieve(@RequestParam("userId")String userId){
        List<HashMap<String, Object>> hashMaps = payService.sendRecieve(userId);
        return hashMaps;
    }
}
