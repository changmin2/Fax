package com.example.demo.service;

import com.example.demo.S3Uploader;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class DetectionServiceV2 {

    private final S3Uploader s3Uploader;

    public boolean pdfTopng(String fileName) throws IOException {

        byte[] file = s3Uploader.getFile(fileName);
        PDDocument pdfDoc = PDDocument.load(file); //Document 생성
        PDFRenderer pdfRenderer = new PDFRenderer(pdfDoc);

        String resultImgPath = System.getProperty("user.dir") + "/"; //이미지가 저장될 경로
        log.info(resultImgPath+"이미지경로");

        //순회하며 이미지로 변환 처리
        int count = 0; //저장된 png개수 저장
        for (int i=0; i<pdfDoc.getPages().getCount(); i++) {
            count+=1;
            String imgFileName = resultImgPath + "/" + i + ".png";

            //DPI 설정
            BufferedImage bim = pdfRenderer.renderImageWithDPI(i, 300, ImageType.RGB);

            // 이미지로 만든다.
            ImageIOUtil.writeImage(bim, imgFileName , 300);

        }
        pdfDoc.close(); //모두 사용한 PDF 문서는 닫는다.
        return detection(count,resultImgPath);
    }

    public boolean detection(int count,String path){
        try {
            String imageFilePath ="";
            for(int i=0;i<count;i++){

                imageFilePath = path+String.valueOf(i)+".png";

                List<AnnotateImageRequest> requests = new ArrayList<>();

                ByteString imgBytes = ByteString.readFrom(new FileInputStream(imageFilePath));

                Image img = Image.newBuilder().setContent(imgBytes).build();
                Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
                AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
                requests.add(request);

                try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
                    BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
                    List<AnnotateImageResponse> responses = response.getResponsesList();

                    for (AnnotateImageResponse res : responses) {
                        if (res.hasError()) {
                            System.out.printf("Error: %s\n", res.getError().getMessage());
                            return false;
                        }

                        System.out.println("Text : ");
                        String result = res.getTextAnnotationsList().get(0).getDescription();
                        result = result.trim().replace(" ", "");

                        // 주민등록번호 추출
                        Set<String> residentNumber = new HashSet<>();

                        // 주민등록번호 정규식
                        String rexResidentNumber = "\\d{2}([0]\\d|[1][0-2])([0][1-9]|[1-2]\\d|[3][0-1])[-]*[1-4]\\d{6}";

                        //주민등록번호 추출
                        Pattern pattern = Pattern.compile(rexResidentNumber);
                        Matcher matcher = pattern.matcher(result);

                        // 추출한 텍스트에서 정규식에 의한 특정 문자 추출(주민등록번호)
                        while (matcher.find()) {
                            residentNumber.add(matcher.group());
                            if(residentNumber.size()>0){
                                log.info(residentNumber.toString());
                                return true;

                            }
                        }

                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}