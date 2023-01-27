package com.example.demo;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class GlobalVariables {
    private String FaxId = "jungly5935";
    private String FaxPw = "sjrnfl0814!";
    private String filePath = "https://bnksys.s3.ap-northeast-2.amazonaws.com/";
    private String Service = "FAX";
}

