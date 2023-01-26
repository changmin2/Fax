package com.example.demo.service;

import classes.Multipart.HttpPostMultipart;
import com.example.demo.GlobalVariables;
import com.example.demo.VO.SendReq;
import com.example.demo.domain.Send;
import com.example.demo.repository.SendRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class SendService {

    private final  GlobalVariables globalVariables;
    private final UploadService uploadService;
    private final SendRepository sendRepository;
    @Transactional
    public List<Integer> sendInsert(SendReq req){
        List<Integer> sendNoList = new ArrayList<Integer>();
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd- hh:mm");
        String Date_End = df.format(now.getTime());
        if(req.getSend_Date().equals("")){ //예약전송 아닐때 전송일 지금으로 입력
            req.setSend_Date(Date_End);
        }
        Send send = new Send(req);
        send.setINSERT_DATE(Date_End);
        send.setSTATUS("전송중");

        List<SendReq.Destination> DestinationList = req.getDestinationList();

        int index = sendRepository.getMaxSendNo();
        for (SendReq.Destination dest:DestinationList) {
            send.setRECEIVE_FAX_NO(dest.getFax());
            sendNoList.add(index);
            send.setSEND_NO((index++)+"");
            sendRepository.save(send);
        }
        return sendNoList;
    }
    public  JSONObject sendTest(SendReq req) throws IOException, ParseException {
        //DB저장 후 sendNo 받아오기
        List<Integer> sendNoList =sendInsert(req);

        ////////////////////////////////////////////
        //[문자 - 발송 요청]
        ////////////////////////////////////////////
        // 헤더값 설정
        Map<String, String> headers = new HashMap<>();
        HttpPostMultipart multipart = new HttpPostMultipart("https://balsong.com/Linkage/API/", "utf-8", headers);

        //데이터 (요청변수 대소문자 구분)
        multipart.addFormField("UserID", globalVariables.getFaxId());
        multipart.addFormField("UserPW", globalVariables.getFaxPw());
        multipart.addFormField("Service", req.getService());
        multipart.addFormField("Type", req.getType());
        multipart.addFormField("Send_Date", req.getSend_Date());

        String userKey = req.getUserKey();
        //파일명 가져오기
        List<String> fileNameList = uploadService.getFileName(userKey);
        String filePath = globalVariables.getFilePath();
        for (String fileSeq : fileNameList) {
            log.info("파일명 테스트  " + filePath+userKey+"_"+fileSeq+".pdf");
            //PDF1 -> 서버파일경로
            multipart.addFilePart("PDF1", new File(filePath+userKey+"_"+fileSeq+".pdf"));
        }

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

        //DB에 결과 저장
        String Result = (String) ObjToJson.get("Result");
        String STATUS = Result.equals("OK")? "전송완료" : "전송실패";
        String JOB_NO = Result.equals("OK")? (String) ObjToJson.get("JOB_NO") : "";
        for (int i = 0; i < sendNoList.size(); i++) {
            int updSend = sendRepository.updApiResult(STATUS,JOB_NO,i+1,sendNoList.get(i));
            log.info("업데이트 결과 "+i+" : "+updSend);
        }

        return ObjToJson;
    }
}
