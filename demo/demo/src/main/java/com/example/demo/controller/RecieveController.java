package com.example.demo.controller;

import com.example.demo.domain.Form.RecieveForm;
import com.example.demo.domain.Recieve.Recieve;
import com.example.demo.service.ReceiveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
public class RecieveController {

    private final ReceiveService receiveService;

    //수신 목록함 가져오기
    @RequestMapping("/recieveList")
    public List<Recieve> recieveList() throws IOException, ParseException {
        //api호출을 통해 얻은 전체 수신 목록
        List<RecieveForm> receive = receiveService.Receive();
        //DB에 저장되어 있지 않은 수신 목록 업데이트
        return receiveService.DBListUpdate(receive);
    }

    //수신 목록 상세 보기
    @RequestMapping("/recieveDetail")
    public HashMap<String,String> recieveDetail(@RequestBody Map<String, String> request) throws IOException, ParseException {
        String RFax_No_Seq = request.get("RFax_No_Seq");
        HashMap<String, String> result = receiveService.receiveDetail(RFax_No_Seq);
        HashMap<String,String> re = receiveService.targetRecieve(RFax_No_Seq,result);

        return re;
    }

}
