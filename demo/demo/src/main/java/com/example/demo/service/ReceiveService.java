package com.example.demo.service;

import classes.Multipart.HttpPostMultipart;
import com.example.demo.GlobalVariables;
import java.util.List;
import com.example.demo.domain.Form.RecieveForm;
import com.example.demo.domain.Recieve.Recieve;
import com.example.demo.repository.RecieveRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
@RestController
@RequiredArgsConstructor
public class ReceiveService {

    private final GlobalVariables globalVariables;
    private final RecieveRepository recieveRepository;

    //발송닷컴에서 수신 목록 모두 가져오기
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
        RecieveForm nn;
        String[] split = new String[7];
        List<String> want = new ArrayList<>(Arrays.asList("RFax_No","RFax_No_Seq","Recv_Date","Sender_No","Page_Cnt","Read"));

        while(tokenizer.hasMoreTokens()){

            String s1 = tokenizer.nextToken();;
            String[] strings = s1.split("=");
            if(strings.length==2 && want.contains(strings[0].replace(" ",""))){
                idx+=1;
                log.info(strings[0] +" "+idx);
                split[idx] = strings[1];
            }
            if(idx==5){
                idx=-1;
                nn = new RecieveForm();
                nn.setFAX_NO(split[0]);
                nn.setRECEIVE_No_SEQ(split[1]);
                nn.setRECEIVE_DATE(split[2]);
                nn.setSENDER_NO(split[3]);
                nn.setPAGE_CNT(split[4]);
                nn.setREAD_YN("N");
                rf.add(nn);
            }

        }

        return rf;
    }

    //TB_RECIEVE에 저장된 모든 수신 목록 가져오기
    public List<Recieve> allListRecieve(){
        return recieveRepository.findAll();
    }

    //DB에 저장되어 있지 않은 수신 목록 업데이트
    @Transactional
    public List<Recieve> DBListUpdate(List<RecieveForm> recieves){

        for (RecieveForm recieve : recieves) {
            if(!recieveRepository.existsById(recieve.getRECEIVE_No_SEQ())){
                Recieve re = new Recieve();
                re.setRECEIVE_No_SEQ(recieve.getRECEIVE_No_SEQ());
                re.setFAX_NO(recieve.getFAX_NO());
                re.setFILE_DATA("");
                re.setRECEIVE_DATE(recieve.getRECEIVE_DATE());
                re.setREAD_USER("");
                re.setPAGE_CNT(recieve.getPAGE_CNT());
                re.setTITLE(recieve.getTITLE());
                re.setREAD_YN("N");
                re.setSENDER_NO(recieve.getSENDER_NO());
                recieveRepository.save(re);
            }

        }
        return recieveRepository.findAll();
    }
}
