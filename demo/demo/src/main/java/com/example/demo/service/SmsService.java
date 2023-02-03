package com.example.demo.service;

import classes.Multipart.HttpPostMultipart;
import com.example.demo.GlobalVariables;
import com.example.demo.repository.ApprovalRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class SmsService {

    private final GlobalVariables globalVariables;
    private final ApprovalRepository approvalRepository;
    private final UserRepository userRepository;

    //결제 보낼때(결제를 받는사람), 승인 되어 받는사람에게 문자 전송
    public void smsSend(String userKey) throws IOException {
        Map<String, String> headers = new HashMap<>();
        HttpPostMultipart multipart = new HttpPostMultipart("https://balsong.com/Linkage/API/", "utf-8", headers);
        Map<String,Object> result = new HashMap<>();

        //데이터 (요청변수 대소문자 구분)
        multipart.addFormField("UserID", globalVariables.getFaxId());
        multipart.addFormField("UserPW", globalVariables.getFaxPw());
        multipart.addFormField("Service", "SMS");
        multipart.addFormField("Type", "Send");
        multipart.addFormField("Callback", "010-5451-0814");
        multipart.addFormField("List_EA", "10");
        multipart.addFormField("Subject", "BNK E-FAX 발송");

        String userName = userRepository.getUserName(userKey);
        multipart.addFormField("Main_Text", "BNK E-FAX\n"+userName+"님이 보낸 "+"결제 요청이 왔습니다.");

        String phoneN = approvalRepository.getPhoneNumber(userKey);
        JSONArray arr = new JSONArray();
        JSONObject d = new JSONObject();
        d.put("Phone",phoneN);
        arr.add(d);
        multipart.addFormField("Destination", arr.toString());


        // 응답 값
        String ResultJson = multipart.finish();
        log.info(ResultJson);
    }
}

