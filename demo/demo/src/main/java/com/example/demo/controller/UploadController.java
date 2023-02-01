package com.example.demo.controller;

import com.example.demo.GlobalVariables;
import com.example.demo.S3Uploader;
import com.example.demo.domain.Upload.Upload;
import com.example.demo.service.UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
@Controller
@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
public class UploadController {

    //팩스 ID, pass, 경로 전역변수
    private final GlobalVariables globalVariables;
    private final UploadService userService;
    private final S3Uploader s3Uploader;

    private  int seq =0;
    // 유저아이디 , 키, 파일 -> 키 없으면 최초 (키 생성) 리턴 -> 다음부턴 키 받고 오게 유저아이디/시분초
    @PostMapping("/upload")
    @ResponseBody
    public HashMap<String,String> uploadSingle(@RequestParam("userId") String userId,
                                               @RequestParam(value = "userKey",defaultValue = "None") String userKey,
                                               @RequestParam("files") List<MultipartFile> files) throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Upload userForm = new Upload();
        log.info("UploadController, uploadSingle 메소드 진입");

        //처음 요청 시
        if(userKey.equals("None")){
            userKey = createKey(userId);
        }
        userForm.setUserKey(userKey);

        ++seq;
        String RealPath =userKey+"_"+"1.pdf";
        String userFileName = "temp"+seq;
        HashMap<String,String> result = userService.convertPDF(files,RealPath);
        if(result.get("Result").equals("ERROR")){
            return result;
        }
        stopWatch.stop();
        log.info("총 시간"+stopWatch.prettyPrint());
        userForm.setUserFileName(userFileName);
        userForm.setRealFileName(RealPath);
        userService.register(userForm);
        result.put("userKey",userKey);

        return result;
    }

    //회수 -> 수정 -> 파일첨부
    @PostMapping("/updateUpload")
    @ResponseBody
    public HashMap<String, String> updateUpload(@RequestParam("userId") String userId,
                                                @RequestParam(value = "userKey",defaultValue = "None") String userKey,
                                                @RequestParam("files") List<MultipartFile> files) throws IOException, ParseException {
        String fileName = userService.getFileName(userKey);
        String[] re = fileName.split("_");
        String[] re2 = re[1].split(".pdf");
        int newSeq = Integer.parseInt(re2[0])+1;
        String newFileName = createKey(userId)+"_"+String.valueOf(newSeq)+".pdf";
        log.info(newFileName);

        HashMap<String,String> result = userService.convertPDF(files,newFileName);

        result.put("newFileName",newFileName);

        return result;
    }

    //유저 키 생성 함수
    private String createKey(String userId) {
        log.info("커밋테스트");
        String userKey;
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());

        DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
        String Date_End = df.format(now.getTime());
        userKey = userId + Date_End;
        return userKey;
    }
}