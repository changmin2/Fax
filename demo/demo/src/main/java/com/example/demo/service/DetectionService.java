package com.example.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//PDF개인정보 추출
@Service
@Slf4j
@RequiredArgsConstructor
public class DetectionService {

    public boolean ResidentNumberDetection(String fileURl) throws IOException {
        log.info("detection 진입");
        // PDFBox 설정
        InputStream stream = new URL(fileURl).openStream();
        PDDocument document = PDDocument.load(stream);
        PDFTextStripper stripper = new PDFTextStripper();
        log.info("stream수행");
        // 텍스트 추출
        String extractText = stripper.getText(document);

        // 공백 제거
        extractText = extractText.trim().replace(" ", "");

        log.info(extractText);
        // 특정 문자 추출 (예제: 이메일)
        Set<String> emails = new HashSet<>();
        // 주민등록번호 추출
        Set<String> residentNumber = new HashSet<>();
        // 휴대폰번호 추출
        Set<String> phoneNumber = new HashSet<>();

//        // 이메일 정규식
//        String rexEmail = "^[A-Za-z0-9+_.-]+@(.+)$";
//        // 휴대폰번호 정규식
//        String rexPhoneNumber = "^01(?:0|1|[6-9])-(?:\\\\d{3}|\\\\d{4})-\\\\d{4}$";
        // 주민등록번호 정규식
        String rexResidentNumber = "\\d{2}([0]\\d|[1][0-2])([0][1-9]|[1-2]\\d|[3][0-1])[-]*[1-4]\\d{6}";


        //주민등록번호 추출
        Pattern pattern = Pattern.compile(rexResidentNumber);
        Matcher matcher = pattern.matcher(extractText);

//        //이메일 추출
//        Pattern pattern2 = Pattern.compile(rexEmail);
//        Matcher matcher2 = pattern2.matcher(extractText);
//
//        //휴대폰번호 추출
//        Pattern pattern3 = Pattern.compile(rexPhoneNumber);
//        Matcher matcher3 = pattern3.matcher(extractText);
        boolean result = false;
        // 추출한 텍스트에서 정규식에 의한 특정 문자 추출(주민등록번호)
        while (matcher.find()) {
            emails.add(matcher.group());
        }
//        //이메일
//        while (matcher2.find()) {
//            emails.add(matcher2.group());
//        }
//        //휴대폰번호
//        while (matcher3.find()) {
//            phoneNumber.add(matcher3.group());
//        }
        log.info(emails.toString());
        return result;
    }
}
