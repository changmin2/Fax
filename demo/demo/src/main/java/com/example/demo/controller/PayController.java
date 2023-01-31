package com.example.demo.controller;

import com.example.demo.service.PayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @ResponseBody
    public List<HashMap<String, Object>> payRecieve(@RequestBody Map<String,String> map){
        String userId= map.get("userId"); //유저아이디
        String status= map.get("status"); //상태 ( 미결재 : 0 , 결재&
        List<HashMap<String, Object>> hashMaps = payService.apprList(userId);
        return hashMaps;
    }

    //결재 상세정보
    @PostMapping("/payDetail")
    @ResponseBody
    public List<HashMap<String, Object>> payDetail(@RequestBody Map<String,String> map){
        String apprNo = map.get("apprNo");
        List<HashMap<String, Object>> hashMaps = payService.apprDetail(apprNo);
        return hashMaps;
    }

    //결재 승인
    @PostMapping("/apprOk")
    @ResponseBody
    public String apprOk(@RequestBody Map<String,String> map) throws IOException{
        String apprNo = map.get("apprNo");
        log.info("apprOk진입");
        return payService.apprOk(apprNo);
    }

    //결재 반려
    @PostMapping("/apprReturn")
    @ResponseBody
    public String apprReturn(@RequestBody Map<String,String> map){
        log.info("apprReturn진입");
        String apprNo = map.get("apprNo"); //결재번호
        String apprRemark = map.get("apprRemark"); //반려사유
        return payService.apprReturn(apprNo,apprRemark);
    }

    //보낸팩스함 목록
    @PostMapping("/sendRecieve")
    public List<HashMap<String, Object>> sendRecieve(@RequestBody Map<String,String> map){
        String userId = map.get("userId");
        log.info("sendRecieve진입");
        List<HashMap<String, Object>> hashMaps = payService.sendRecieve(userId);
        return hashMaps;
    }

    //결재 상세정보
    @PostMapping("/sendRecieveDetail")
    public List<HashMap<String, Object>> sendRecieveDetail(@RequestBody Map<String,String> map){
        String userKey = map.get("userKey");
        List<HashMap<String, Object>> hashMaps = payService.sendRecieveDetail(userKey);
        return hashMaps;
    }

    //회수
    @PostMapping("/withdraw")
    public String withdraw(@RequestBody Map<String,String> map){
        String userKey = map.get("userKey");
        payService.withdraw(userKey);
        return "회수완료";
    }

}
