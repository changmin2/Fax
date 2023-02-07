package com.example.demo.controller;

import com.example.demo.GlobalVariables;
import com.example.demo.domain.Upload.Upload;
import com.example.demo.service.DetectionService;
import com.example.demo.service.UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
@CrossOrigin(
        // localhost:5500 과 127.0.0.1 구분
        origins = "https://69a7-175-215-98-68.jp.ngrok.io", // allowCredentials = "true" 일 경우, orogins="*" 는 X
        allowCredentials = "true",
        allowedHeaders = "*",
        methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PUT,RequestMethod.HEAD,RequestMethod.OPTIONS}
)
@Controller
@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
public class UploadController {

    //팩스 ID, pass, 경로 전역변수
    private final GlobalVariables globalVariables;
    private final UploadService userService;
    private final DetectionService detectionService;

    private  int seq =0;
    // 유저아이디 , 키, 파일 -> 키 없으면 최초 (키 생성) 리턴 -> 다음부턴 키 받고 오게 유저아이디/시분초


//    @PostMapping("/upload")
//    @ResponseBody
//    public HashMap<String,Object> uploadSingle(@RequestParam("userId") String userId,
//                                               @RequestParam(value = "userKey",defaultValue = "None") String userKey,
//                                               @RequestParam("files") List<MultipartFile> files) throws Exception {
//
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        String url = "http://192.168.0.118:8080/api/upload";
//
//        // parameter 세팅
//        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
//        map.add("userId", userId);
//        map.add("userKey",userKey);
//        for (MultipartFile file : files) {
//            map.add("files",file);
//        }
//        HttpEntity<MultiValueMap<String, Object>> requestEntity
//                = new HttpEntity<>(map, headers);
//        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
//        log.info(response.getBody());
//        return null;
//    }
    @PostMapping("/upload")
    @ResponseBody
    public HashMap<String,Object> uploadSingle(@RequestParam("userId") String userId,
                                               @RequestParam(value = "userKey",defaultValue = "None") String userKey,
                                               @RequestParam("files") List<MultipartFile> files) throws Exception {

        Upload userForm = new Upload();
        log.info("UploadController, uploadSingle 메소드 진입");

        //처음 요청 시
        if(userKey.equals("None")){
            userKey = globalVariables.createKey(userId);
        }
        userForm.setUserKey(userKey);

        ++seq;
        String RealPath =userKey+"_"+"1.pdf";
        String userFileName = "temp"+seq;

        HashMap<String,Object> result = userService.convertPDF(files,RealPath);
        if(result.get("Result").equals("ERROR")){
            return result;
        }
        userForm.setUserFileName(userFileName);
        userForm.setRealFileName(RealPath);
        userService.register(userForm);
        result.put("newFileName",RealPath);
        result.put("userKey",userKey);

        return result;
    }

    //회수 -> 수정 -> 파일첨부
    @PostMapping("/updateUpload")
    @ResponseBody
    public HashMap<String, Object> updateUpload(@RequestParam("userId") String userId,
                                                @RequestParam(value = "userKey",defaultValue = "None") String userKey,
                                                @RequestParam("files") List<MultipartFile> files) throws IOException, ParseException {
        String fileName = userService.getFileName(userKey);
        String[] re = fileName.split("_");
        String[] re2 = re[1].split(".pdf");
        int newSeq = Integer.parseInt(re2[0])+1;
        String newFileName = globalVariables.createKey(userId)+"_"+String.valueOf(newSeq)+".pdf";
        log.info(newFileName);

        HashMap<String,Object> result = userService.convertPDF(files,newFileName);

        result.put("newFileName",newFileName);

        return result;
    }


}