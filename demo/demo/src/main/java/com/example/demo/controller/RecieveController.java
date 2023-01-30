package com.example.demo.controller;

import com.example.demo.SessionManager;
import com.example.demo.domain.Form.RecieveForm;
import com.example.demo.domain.Recieve.Recieve;
import com.example.demo.domain.User.User;
import com.example.demo.service.ReceiveService;
import jakarta.servlet.http.HttpServletRequest;
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
@RestController
@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
public class RecieveController {

    private final ReceiveService receiveService;
    private final SessionManager sessionManager;

    //수신 목록함 가져오기
    @RequestMapping("/recieveList")
    public List<Recieve> recieveList(@RequestBody  Map<String, String> map) throws IOException, ParseException {
        //api호출을 통해 얻은 전체 수신 목록
        List<RecieveForm> receive = receiveService.Receive(map);
        //DB에 저장되어 있지 않은 수신 목록 업데이트
        return receiveService.DBListUpdate(receive);
    }

    //수신 목록 상세 보기
    @RequestMapping("/recieveDetail")
    public HashMap<String,String> recieveDetail(@RequestBody Map<String, String> param, HttpServletRequest request) throws IOException, ParseException {
        String RFax_No_Seq = param.get("RFax_No_Seq");
        User user = (User) sessionManager.getSession(request);
        //테스트 에러 안나게
        String name = (user ==null)?"임시유저":user.getUSER_NAME();

        HashMap<String, String> result = receiveService.receiveDetail(RFax_No_Seq);
        HashMap<String,String> re = receiveService.targetRecieve(RFax_No_Seq,result,name);
        return re;
    }

}
