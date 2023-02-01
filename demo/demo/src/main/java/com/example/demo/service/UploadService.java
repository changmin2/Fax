package com.example.demo.service;

import classes.Multipart.HttpPostMultipart;
import com.example.demo.GlobalVariables;
import com.example.demo.S3Uploader;
import com.example.demo.domain.Upload.Upload;
import com.example.demo.repository.UploadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UploadService {


    private  final UploadRepository userRepository;
    private  final GlobalVariables globalVariables;
    private final S3Uploader s3Uploader;

    @Transactional
    public void register(Upload user){
        userRepository.save(user);
    }

    public String getFileName(String userkey){
        return userRepository.getrealFileName(userkey);
    }

    //파일이름 업데이트
    @Transactional
    public void updateFileName(String newFileName,String userKey){
        userRepository.updateFileName(newFileName,userKey);
    }

    public  HashMap<String,String>  convertPDF(List<MultipartFile> files,String RealPath) throws IOException, ParseException {
        HashMap<String,String> result = new HashMap<>();
        ////////////////////////////////////////////
        //[팩스 - 변환 요청]
        ////////////////////////////////////////////
        // 헤더값 설정
        Map<String, String> headers = new HashMap<>();
        HttpPostMultipart multipart = new HttpPostMultipart("https://balsong.com/Linkage/API/", "utf-8", headers);

        //데이터 (요청변수 대소문자 구분)
        multipart.addFormField("UserID", globalVariables.getFaxId());
        multipart.addFormField("UserPW", globalVariables.getFaxPw());
        multipart.addFormField("Service", globalVariables.getService());
        multipart.addFormField("Type", "Convert");


        for (int i = 0; i < files.size(); i++) {
            MultipartFile multipartFile = files.get(i);
            File convFile = new File(System.getProperty("user.dir") + "/" +multipartFile.getOriginalFilename());
            multipartFile.transferTo(convFile);
//            File convFile = new File(multipartFile.getOriginalFilename());
//            convFile.createNewFile();
//            FileOutputStream fos = new FileOutputStream(convFile);
//            fos.write(multipartFile.getBytes());
//            fos.close();
            multipart.addFilePart("Doc_File"+(i+1), convFile);
            s3Uploader.removeNewFile(convFile); //파일삭제
        }

        // 응답 값
        String ResultJson = multipart.finish();
        // Json parse (json.simple 라이브러리)
        JSONParser jsonParse = new JSONParser();
        JSONObject ObjToJson = (JSONObject) jsonParse.parse(ResultJson);
        String Result = (String) ObjToJson.get("Result");
//        data:application/pdf;base64,
        Result = Result.replace("|","");
        if(Result.equals("OK")){
            String PDF = (String) ObjToJson.get("PDF");
            PDF = PDF.substring(28);
            File f = new File(PDF);
            log.info("진입");
            byte[] binary = Base64.getDecoder().decode(PDF);
            // 그대로 파일로 생성한다.
            try (FileOutputStream stream = new FileOutputStream(System.getProperty("user.dir") + "/" +"temp.pdf")) {
                stream.write(binary, 0, binary.length);
            }
            File n = new File(System.getProperty("user.dir") + "/" +"temp.pdf");

//            페이지 가져오기
            PDDocument pdfDoc;
            pdfDoc = PDDocument.load(n);
            int pageCount = pdfDoc.getNumberOfPages();
//            log.error("페이지 수 읽어오기 : "+pageCount);
            result.put("pageCount",pageCount+"");
            pdfDoc.close();
            s3Uploader.upload(n, "static",RealPath);
        }else{
            String Message = (String) ObjToJson.get("Message");
            result.put("Message",Message);
        }
        result.put("Result",Result);
        return result;
    }
}
