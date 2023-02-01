package com.example.demo.controller;

import com.example.demo.S3Config;
import com.example.demo.S3Uploader;
import com.example.demo.domain.Send.Send;
import com.example.demo.domain.Send.Send_detail;
import com.example.demo.domain.Upload.Upload;
import com.example.demo.repository.SendDRepository;
import com.example.demo.repository.SendRepository;
import com.example.demo.service.PayService;
import com.example.demo.service.ReceiveService;
import com.example.demo.service.SendService;
import com.example.demo.service.UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin(
        // localhost:5500 과 127.0.0.1 구분
        origins = "http://localhost:5500", // allowCredentials = "true" 일 경우, orogins="*" 는 X
        allowCredentials = "true",
        allowedHeaders = "*",
        methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PUT,RequestMethod.HEAD,RequestMethod.OPTIONS}
)
@RequestMapping("/api")
@RestController
@Slf4j
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;
    private final S3Uploader s3Uploader;
    private final ReceiveService receiveService;
    private final UploadService uploadService;
    private final SendDRepository sendDRepository;
    private final SendService sendService;
    @PostMapping("/testtest")
    public void testtest(@RequestBody Map<String,String> map){
//        log.info("test진입");
//        List<Object[]> result = sendDRepository.findByAllV2(map.get("userKey"));
//        for (Object[] objects : result) {
//            for (Object object : objects) {
//                log.info(String.valueOf(object));
//            }
//        }
        log.info("test진입");
        List<Map<String, Object>> userKey = sendDRepository.findByAllV3(map.get("userKey"));
        for (Map<String, Object> stringObjectMap : userKey) {
            log.info(String.valueOf(stringObjectMap.get("USER_KEY")));
        }
    }

    //결재함 목록
    @PostMapping("/payRecieve")
    @ResponseBody
    public List<HashMap<String, Object>> payRecieve(@RequestBody Map<String,String> map){
        List<HashMap<String, Object>> hashMaps = payService.apprList(map);
        return hashMaps;
    }

    //결재 상세정보
    @PostMapping("/payDetail")
    @ResponseBody
    public List<HashMap<String, Object>> payDetail(@RequestBody Map<String,String> map){
        String apprNo = map.get("apprNo");
        List<HashMap<String, Object>> hashMaps = payService.apprDetail(apprNo);
        return hashMaps;
    }

    //결재 승인
    @PostMapping("/apprOk")
    @ResponseBody
    public String apprOk(@RequestBody Map<String,String> map) throws IOException{
        String apprNo = map.get("apprNo");
        log.info("apprOk진입");
        return payService.apprOk(apprNo);
    }

    //결재 반려
    @PostMapping("/apprReturn")
    @ResponseBody
    public String apprReturn(@RequestBody Map<String,String> map){
        log.info("apprReturn진입");
        String apprNo = map.get("apprNo"); //결재번호
        String apprRemark = map.get("apprRemark"); //반려사유
        return payService.apprReturn(apprNo,apprRemark);
    }

    //보낸팩스함 목록
    @PostMapping("/sendRecieve")
    public List<HashMap<String, Object>> sendRecieve(@RequestBody Map<String,String> map){
        log.info("sendRecieve진입");
        List<HashMap<String, Object>> hashMaps = payService.sendRecieve(map);
        return hashMaps;
    }

    //부서팩스함 제목 저장
    @PostMapping("/titleSave")
    public void titleSave(@RequestBody Map<String,String> map){
        String title = map.get("Title");
        String RFax_No_Seq = map.get("RFax_No_Seq");
        receiveService.titleSave(title,RFax_No_Seq);
    }

    //부서팩스함 상세정보
    @PostMapping("/sendRecieveDetail")
    public List<HashMap<String, Object>> sendRecieveDetail(@RequestBody Map<String,String> map){
        String userKey = map.get("userKey");
        List<HashMap<String, Object>> hashMaps = payService.sendRecieveDetail(userKey);
        return hashMaps;
    }

    //회수
    @PostMapping("/withdraw")
    public String withdraw(@RequestBody Map<String,String> map){
        String userKey = map.get("userKey");
        payService.withdraw(userKey);
        return "회수완료";
    }

    //회수 -> 삭제
    @PostMapping("/withdrawDelete")
    public HashMap<String,Object> withdrawDelete(@RequestBody Map<String, String> map) {
        HashMap<String,Object> result = new HashMap<>();
        String userKey = map.get("userKey");

        if (userKey==null){
            result.put("flag",false);
            result.put("message","유저키오류");
            return result;
        }

        result.put("flag",true);
        result.put("message","성공");
        payService.updateUseGbn(userKey);
        return result;
    }

    //회수 -> 수정
    @PostMapping("/withdrawUpdate")
    public HashMap<String, Object> withdrawUpdate(@RequestBody Map<String,String > map){
        HashMap<String,Object> result = new HashMap<>();
        String userKey = map.get("userKey");
        Send find = payService.sendInfoFind(userKey);
        List<Map<String,Object>> details = payService.sendInfoDetail(userKey);
        result.put("Info",find);
        result.put("fileName",userKey+"_"+"1.pdf");
        result.put("details",details);
        return result;
    }

    //회수 -> 수정 -> 파일삭제시
    @PostMapping("/S3fileDelete")
    public boolean S3fileDelete(@RequestBody Map<String,String> map){
        String fileName = map.get("fileName");
        s3Uploader.removeS3File(fileName);
        return true;
    }

    //재사용
    @PostMapping("/reUse")
    public HashMap<String,Object> reUse(@RequestBody Map<String,String> map){
        HashMap<String,Object> result = new HashMap<>();
        String userKey = map.get("userKey");
        String userId = map.get("userId");
        String newUserKey = createKey(userId);

        //s3에서 새로운userKey로 파일 이름 변경하기
        String orgFileName = userKey+"_"+"1.pdf";
        String newFileName = newUserKey+"_"+"1.pdf";
        s3Uploader.changS3FileName(orgFileName,newFileName);

        //upload 테이블에 새로운 파일업데이트
        Upload newUpload = new Upload();
        newUpload.setUserKey(newUserKey);
        newUpload.setRealFileName(newFileName);
        newUpload.setUserFileName("temp");
        uploadService.register(newUpload);

        Send find = payService.sendInfoFind(userKey);
        List<Map<String,Object>> details = payService.sendInfoDetail(userKey);
        result.put("Info",find);
        result.put("fileName",newFileName);
        result.put("userKey",newUserKey);
        result.put("details",details);
        return result;
    }

    //유저 키 생성 함수
    private String createKey(String userId) {
        String userKey;
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());

        DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
        String Date_End = df.format(now.getTime());
        userKey = userId + Date_End;
        return userKey;
    }
}