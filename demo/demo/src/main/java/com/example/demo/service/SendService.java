package com.example.demo.service;

import classes.Multipart.HttpPostMultipart;
import com.example.demo.GlobalVariables;
import com.example.demo.VO.SendReq;
import com.example.demo.domain.Send;
import com.example.demo.domain.Upload;
import com.example.demo.repository.SendRepository;
import com.example.demo.repository.UploadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
@RequiredArgsConstructor
public class SendService {

    private final  GlobalVariables globalVariables;
    private final UploadService uploadService;
    private final SendRepository sendRepository;

    public  JSONObject sendInsert(SendReq req){
        Send send = new Send(req);
        int index = 1;
        List<SendReq.Destination> DestinationList = req.getDestinationList();
        for (SendReq.Destination dest:DestinationList) {
            send.setRECEIVE_FAX_NO(dest.getFax());
            send.setSTATUS("전송중");
            send.setSEND_NO(req.getUserKey()+index++);
        }
//        sendRepository.save(send);
        return null;
    }
    public  JSONObject sendTest(SendReq req) throws IOException, ParseException {
        //DB저장


        ////////////////////////////////////////////
        //[문자 - 발송 요청]
        ////////////////////////////////////////////
        // 헤더값 설정
        Map<String, String> headers = new HashMap<String, String>();
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

        String Result = (String) ObjToJson.get("Result");
        if (Result.equals("OK")) { //성공했을때

        }else{

        }
        //DB에 저장


        return ObjToJson;
    }
}
