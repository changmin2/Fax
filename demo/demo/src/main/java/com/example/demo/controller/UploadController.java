package com.example.demo.controller;

import com.example.demo.GlobalVariables;
import com.example.demo.domain.Upload;
import com.example.demo.service.UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
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

    private final GlobalVariables globalVariables;
    private final UploadService userService;

    private  int seq =0;
    // 유저아이디 , 키, 파일 -> 키 없으면 최초 (키 생성) 리턴 -> 다음부턴 키 받고 오게 유저아이디/시분초
    @PostMapping("/upload")
    @ResponseBody
    public String uploadSingle(@RequestParam("userId") String userId,@RequestParam(value = "userKey",defaultValue = "None") String userKey, @RequestParam("files") MultipartFile file) throws Exception {
        Upload userForm = new Upload();
        log.info("=== 이미지파일 수신 거래발생 ===");
        log.info("userKey"+userKey);

        //처음 요청 시
        if(userKey.equals("None")){
            userKey = createKey(userId);
            userForm.setUserKey(userKey);
        }
        ++seq;
        String RealPath = globalVariables.getFilePath()+userKey+"_"+seq+".pdf";
        File dest = new File(RealPath);
        file.transferTo(dest); // 파일 업로드 작업 수행
        System.out.println("file RealPath.........."+ RealPath);
        System.out.println("file Dest.........."+ dest);
        String userFileName = file.getOriginalFilename().replace(".pdf","");

        userForm.setUserFileName(userFileName);
        userForm.setRealFileName(String.valueOf(seq));

        //DB저장
        userService.register(userForm);

        return userKey;
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