package com.example.demo.service;

import classes.Multipart.HttpPostMultipart;
import com.example.demo.GlobalVariables;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RestController
@RequiredArgsConstructor
public class ReceiveService {

    private final GlobalVariables globalVariables;

    public String Receive() throws IOException, ParseException {
        Map<String, String> headers = new HashMap<>();
        HttpPostMultipart multipart = new HttpPostMultipart("https://balsong.com/Linkage/API/", "utf-8", headers);

        //데이터 (요청변수 대소문자 구분)
        multipart.addFormField("UserID", globalVariables.getFaxId());
        multipart.addFormField("UserPW", globalVariables.getFaxPw());
        multipart.addFormField("Service", "RFAX");
        multipart.addFormField("Type", "Report");
        multipart.addFormField("RFax_No", "050-4926-0237");
        multipart.addFormField("Date_Start", "2023-01-01");
        multipart.addFormField("Date_End", "2023-01-27");
        multipart.addFormField("List_EA", "10");
        multipart.addFormField("Page", "1");

        String ResultJson = multipart.finish();
        // Json parse (json.simple 라이브러리)
        JSONParser jsonParse = new JSONParser();
        JSONObject ObjToJson = (JSONObject) jsonParse.parse(ResultJson);
        log.info(ObjToJson.toString());
        return null;
    }
}
