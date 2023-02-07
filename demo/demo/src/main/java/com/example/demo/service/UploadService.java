package com.example.demo.service;

import classes.Multipart.HttpPostMultipart;
import com.example.demo.GlobalVariables;
import com.example.demo.S3Uploader;
import com.example.demo.domain.Upload.Upload;
import com.example.demo.repository.UploadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UploadService {


    private  final UploadRepository uploadRepository;
    private  final GlobalVariables globalVariables;
    private final S3Uploader s3Uploader;
    private final DetectionServiceV2 detectionServiceV2;

    @Transactional
    public void register(Upload user){
        uploadRepository.save(user);
    }

    public String getFileName(String userkey){
        return uploadRepository.getrealFileName(userkey);
    }

    //파일이름 업데이트
    @Transactional
    public void updateFileName(String newFileName,String userKey){
        String originFileName = uploadRepository.getrealFileName(userKey);
        if(!originFileName.equals(newFileName)){
            s3Uploader.removeS3File(originFileName);
            uploadRepository.updateFileName(newFileName,userKey);
        };
    }

    public  HashMap<String,Object>  convertPDF(List<MultipartFile> files,String RealPath) throws IOException, ParseException {
        log.info("converPDF진입");
        HashMap<String,Object> result = new HashMap<>();
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
            System.out.println(multipartFile);
            File convFile = new File(System.getProperty("user.dir") + "/" +multipartFile.getOriginalFilename());
            multipartFile.transferTo(convFile);
            multipart.addFilePart("Doc_File"+(i+1), convFile);
            s3Uploader.removeNewFile(convFile); //파일삭제
        }

        // 응답 값
        String ResultJson = multipart.finish();

        // Json parse (json.simple 라이브러리)
        JSONParser jsonParse = new JSONParser();
        JSONObject ObjToJson = (JSONObject) jsonParse.parse(ResultJson);
        System.out.println("변환결과 : "+ObjToJson);
        String Result = (String) ObjToJson.get("Result");
        boolean detectionResult =false;
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
            detectionResult = detectionServiceV2.pdfTopng(RealPath);
            log.info(String.valueOf(detectionResult));
        }else{
            String Message = (String) ObjToJson.get("Message");
            result.put("Message",Message);
        }
        result.put("Result",Result);
        result.put("detection",detectionResult);
        return result;
    }


    public  HashMap<String,Object>  notConvertPDF(List<MultipartFile> files,String RealPath) throws IOException, ParseException {
        log.info("converPDF진입");
        HashMap<String,Object> result = new HashMap<>();
        boolean detectionResult =false;
        for (int i = 0; i < files.size(); i++) {
            MultipartFile multipartFile = files.get(i);
            System.out.println(multipartFile);
            File convFile = new File(System.getProperty("user.dir") + "/" +multipartFile.getOriginalFilename());
            multipartFile.transferTo(convFile);

//            페이지 가져오기
            PDDocument pdfDoc;
            pdfDoc = PDDocument.load(convFile);

            int pageCount = pdfDoc.getNumberOfPages();
//            log.error("페이지 수 읽어오기 : "+pageCount);
            result.put("pageCount",pageCount+"");
            pdfDoc.close();
            s3Uploader.upload(convFile, "static",RealPath);
            detectionResult = detectionServiceV2.pdfTopng(RealPath);
            log.info(String.valueOf(detectionResult));
        }
        result.put("Result","OK");
        result.put("detection",detectionResult);
        return result;
    }

    public  HashMap<String,Object>  getSCAN(String RealPath) throws IOException {
        HashMap<String,Object> result = new HashMap<>();
        boolean detectionResult =false;
        File file = new File("C:\\BNK_IFAX\\SCAN.tiff");
        if(file.isFile()){
            //tiff=>PDF 변환
            PDDocument document=new PDDocument();
            ImageInputStream isb = ImageIO.createImageInputStream(file);

            Iterator<ImageReader> iterator = ImageIO.getImageReaders(isb);
            if (iterator == null || !iterator.hasNext())
            {
                throw new IOException("Image file format not supported by ImageIO: ");
            }

            ImageReader reader = (ImageReader) iterator.next();
            iterator = null;
            reader.setInput(isb);

            int nbPages = reader.getNumImages(true);

            System.out.println(nbPages);

            for(int p=0;p<nbPages;p++)
            {
                BufferedImage bufferedImage = reader.read(p);

                PDPage page = new PDPage();
                document.addPage(page);

                PDImageXObject i = LosslessFactory.createFromImage(document, bufferedImage);

                PDPageContentStream content =new PDPageContentStream(document, page);
                content.drawImage(i, 0,0 ,page.getMediaBox().getWidth(),page.getMediaBox().getHeight());

                content.close();
            }
            document.save(System.getProperty("user.dir") + "/" +"temp.pdf"); //Enter path to save your file with .pdf extension
            document.close();
            //변환 끝
            isb.close();
            s3Uploader.removeNewFile(file); //tiff 파일삭제

            File n = new File(System.getProperty("user.dir") + "/" +"temp.pdf");

            //            페이지 가져오기
            PDDocument pdfDoc;
            pdfDoc = PDDocument.load(n);
            int pageCount = pdfDoc.getNumberOfPages();
//            log.error("페이지 수 읽어오기 : "+pageCount);
            result.put("pageCount",pageCount+"");
            pdfDoc.close();
            s3Uploader.upload(n, "static",RealPath);
            detectionResult = detectionServiceV2.pdfTopng(RealPath);
            log.info(String.valueOf(detectionResult));

            result.put("Result","OK");
            result.put("detection",detectionResult);
        }else{
            result.put("Result","ERROR");
            System.out.println("경로에 파일 없음");
            return result;
        }

        return result;
    }
}
