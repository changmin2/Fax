package com.example.demo.controller;

import com.example.demo.service.PayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

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

    //결재함 목록
    @PostMapping("/payRecieve")
    public List<HashMap<String, Object>> payRecieve(@RequestParam("userId")String userId){
        List<HashMap<String, Object>> hashMaps = payService.apprList(userId);
        return hashMaps;
    }

    //결재 상세정보
    @PostMapping("/payDetail")
    public List<HashMap<String, Object>> payDetail(@RequestParam("apprNo")String apprNo){
        List<HashMap<String, Object>> hashMaps = payService.apprDetail(apprNo);
        return hashMaps;
    }

    //결재 승인
    @PostMapping("/apprOk")
    public String apprOk(@RequestParam("apprNo")String apprNo) throws IOException{
        log.info("apprOk진입");
        return payService.apprOk(apprNo);
    }

    //결재 반려
    @PostMapping("/apprReturn")
    public String apprReturn(@RequestParam("apprNo")String apprNo,@RequestParam("apprRemark")String apprRemark){
        log.info("apprReturn진입");
        return payService.apprReturn(apprNo,apprRemark);
    }

    //보낸팩스함 목록
    @PostMapping("/sendRecieve")
    public List<HashMap<String, Object>> sendRecieve(@RequestParam("userId")String userId){
        log.info("sendRecieve진입");
        List<HashMap<String, Object>> hashMaps = payService.sendRecieve(userId);
        return hashMaps;
    }

    //결재 상세정보
    @PostMapping("/sendRecieveDetail")
    public List<HashMap<String, Object>> sendRecieveDetail(@RequestParam("userKey")String userKey){
        List<HashMap<String, Object>> hashMaps = payService.sendRecieveDetail(userKey);
        return hashMaps;
    }
}
