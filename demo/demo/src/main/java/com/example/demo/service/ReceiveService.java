package com.example.demo.service;

import classes.Multipart.HttpPostMultipart;
import com.example.demo.GlobalVariables;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import com.example.demo.S3Uploader;
import com.example.demo.domain.Form.RecieveForm;
import com.example.demo.domain.Recieve.Recieve;
import com.example.demo.repository.RecieveRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
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
    private final S3Uploader s3Uploader;

    //발송닷컴에서 수신 목록 모두 가져오기
    public List<RecieveForm> Receive(Map<String,String > param) throws IOException, ParseException {
        Map<String, String> headers = new HashMap<>();
        HttpPostMultipart multipart = new HttpPostMultipart("https://balsong.com/Linkage/API/", "utf-8", headers);

        //데이터 (요청변수 대소문자 구분)
        multipart.addFormField("UserID", globalVariables.getFaxId());
        multipart.addFormField("UserPW", globalVariables.getFaxPw());
        multipart.addFormField("Service", "RFAX");
        multipart.addFormField("Type", "Report");
        multipart.addFormField("RFax_No", param.get("RFax_No"));
        multipart.addFormField("Date_Start", param.get("Date_Start"));
        multipart.addFormField("Date_End", param.get("Date_End"));
        multipart.addFormField("List_EA", "10");
        multipart.addFormField("Page", "1");

        String ResultJson = multipart.finish();
        Map<String,Object> map = null;
        map = new ObjectMapper().readValue(ResultJson,Map.class);
        Object list = map.get("List");
        log.info("수신목록"+list.toString());
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

    //DB에 저장된 특정 수신 정보 가져오기
    @Transactional
    public HashMap<String, String> targetRecieve(String RFax_No_Seq, HashMap<String,String> result,String userId){
        Recieve recieve = recieveRepository.findById(RFax_No_Seq).get();
        log.info(recieve.toString());
        if(recieve.getREAD_USER().equals("")){
            recieve.setREAD_USER(userId);
            recieve.setREAD_YN("Y");
            recieve.setREAD_DATE(globalVariables.getNow());
        }
        result.put("receive_DATE",recieve.getRECEIVE_DATE());
        result.put("receive_No_SEQ",recieve.getRECEIVE_No_SEQ());
        result.put("page_CNT",recieve.getPAGE_CNT());
        result.put("fax_NO",recieve.getFAX_NO());
        result.put("title",recieve.getTITLE());
        result.put("read_DATE",recieve.getREAD_DATE());
        result.put("read_USER",recieve.getREAD_USER());
        result.put("read_YN",recieve.getREAD_YN());
        result.put("sender_NO",recieve.getSENDER_NO());
        return result;
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

    public HashMap<String, String> receiveDetail(String RFax_No_Seq) throws IOException, ParseException {
        log.info("Service 진입");
        HashMap<String,String> result = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        HttpPostMultipart multipart = new HttpPostMultipart("https://balsong.com/Linkage/API/", "utf-8", headers);
        log.info("Service 로직1");
        //데이터 (요청변수 대소문자 구분)
        multipart.addFormField("UserID", globalVariables.getFaxId());
        multipart.addFormField("UserPW", globalVariables.getFaxPw());
        multipart.addFormField("Service", "RFAX");
        multipart.addFormField("Type", "Down");
        multipart.addFormField("RFax_No", "050-4926-0237");
        multipart.addFormField("RFax_No_Seq", RFax_No_Seq);

        String ResultJson = multipart.finish();
        JSONParser jsonParse = new JSONParser();
        JSONObject ObjToJson = (JSONObject) jsonParse.parse(ResultJson);
        String Result = (String) ObjToJson.get("Result");
        Result = Result.replace("|","");
        if(Result.equals("OK")){
            String PDF = (String) ObjToJson.get("Data");
            PDF = PDF.substring(28);
            File f = new File(PDF);
            log.info("진입");
            byte[] binary = Base64.getDecoder().decode(PDF);
            // 그대로 파일로 생성한다.
            try (FileOutputStream stream = new FileOutputStream(System.getProperty("user.dir") + "/" +"temp.pdf")) {
                stream.write(binary, 0, binary.length);
            }
            File n = new File(System.getProperty("user.dir") + "/" +"temp.pdf");
            String RealPath = "Receive"+"_"+RFax_No_Seq+".pdf";
            s3Uploader.upload(n, "receive",RealPath);
            result.put("filePath",globalVariables.getFilePath()+RealPath);
        }else{
            String Message = (String) ObjToJson.get("Message");
            result.put("Message",Message);
        }
        result.put("Result",Result);
        return result;

    }
}
