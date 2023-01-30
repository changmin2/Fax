package com.example.demo.controller;

import com.example.demo.domain.Form.RecieveForm;
import com.example.demo.domain.Recieve.Recieve;
import com.example.demo.service.ReceiveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.List;

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
}
