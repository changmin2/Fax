package com.example.demo.service;

import classes.Multipart.HttpPostMultipart;
import com.example.demo.GlobalVariables;
import com.example.demo.VO.SendRes;
import com.example.demo.domain.Form.RecieveForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
@RestController
@RequiredArgsConstructor
public class ReceiveService {

    private final GlobalVariables globalVariables;

    public List<RecieveForm> Receive() throws IOException, ParseException {
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
        Map<String,Object> map = null;
        map = new ObjectMapper().readValue(ResultJson,Map.class);
        Object list = map.get("List");

        String s = String.valueOf(list);
        s= s.replace("[","");
        s = s.replace("]","");
        s= s.replace("{","");
        s=s.replace("}","");
        StringTokenizer tokenizer = new StringTokenizer(s,",");
        log.info(tokenizer.toString());

        int idx = -1;
        List<RecieveForm> rf = new ArrayList<>();
        List<String> tempString = new ArrayList<>();
        RecieveForm nn = new RecieveForm();
        String[] split = new String[9];
        while(tokenizer.hasMoreTokens()){
            idx+=1;
            String s1 = tokenizer.nextToken();;
            String[] strings = s1.split("=");
            if(strings.length==2){
                split[idx] = strings[1];
            }else split[idx]="";

            if(idx==8){
                nn.setNo(split[0]);
                nn.setRFax_No(split[1]);
                nn.setRFax_No_Seq(split[2]);
                nn.setRecv_Date(split[3]);
                nn.setSender_No(split[4]);
                nn.setSender_NM(split[5]);
                nn.setPage_Cnt(split[6]);
                nn.setRead(split[7]);
                nn.setMemo(split[8]);
                idx=-1;
                rf.add(nn);
            }

        }
        log.info(rf.toString());


        return rf;
    }
}
