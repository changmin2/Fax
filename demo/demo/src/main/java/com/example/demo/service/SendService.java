package com.example.demo.service;

import classes.Multipart.HttpPostMultipart;
import com.example.demo.VO.SendReq;
import com.example.demo.VO.SendRes;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class SendService {
    public static JSONObject sendTest(SendReq req) throws IOException, ParseException {
        ////////////////////////////////////////////
        //[문자 - 발송 요청]
        ////////////////////////////////////////////
        // 헤더값 설정
        Map<String, String> headers = new HashMap<String, String>();
        HttpPostMultipart multipart = new HttpPostMultipart("https://balsong.com/Linkage/API/", "utf-8", headers);

        //데이터 (요청변수 대소문자 구분)
        multipart.addFormField("UserID", req.getUserID());
        multipart.addFormField("UserPW", req.getUserPW());
        multipart.addFormField("Service", req.getService());
        multipart.addFormField("Type", req.getType());
        multipart.addFormField("Send_Date", req.getSend_Date());

        //PDF1 -> 서버파일경로

        multipart.addFilePart("PDF1", new File(req.getPDF1()));
        // 데이터 - 수신처
        JSONArray DestArr = new JSONArray();

        List<SendReq.Destination> DestinationList = req.getDestinationList();
        for (SendReq.Destination dest:DestinationList) {
            JSONObject Dest1 = new JSONObject();
            Dest1.put("Company",dest.getCompany());
            Dest1.put("Name",dest.getName());
            Dest1.put("Fax",dest.getFax());
            DestArr.add(Dest1);
        }
        multipart.addFormField("Destination", DestArr.toString());

        // 응답 값
        String ResultJson = multipart.finish();
        // Json parse (json.simple 라이브러리)
        JSONParser jsonParse = new JSONParser();
        JSONObject ObjToJson = (JSONObject) jsonParse.parse(ResultJson);

        return ObjToJson;
    }
}
