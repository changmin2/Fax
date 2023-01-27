package com.example.demo.service;

import classes.Multipart.HttpPostMultipart;
import com.example.demo.GlobalVariables;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@Service
@Slf4j
@RestController
@RequiredArgsConstructor
public class ReceiveService {
//
//    private final GlobalVariables globalVariables;
////
////    public String Receive(){
////        HttpPostMultipart multipart = new HttpPostMultipart("https://balsong.com/Linkage/API/", "utf-8", headers);
//
////        //데이터 (요청변수 대소문자 구분)
////        multipart.addFormField("UserID", globalVariables.getFaxId());
////        multipart.addFormField("UserPW", globalVariables.getFaxPw());
////        multipart.addFormField("Service", globalVariables.getService());
////        multipart.addFormField("Type", "Send");
////        multipart.addFormField("Send_Date", req.getSend_Date());
//        return null;
//    }
}
