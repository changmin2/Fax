package com.example.demo;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
@Getter
public class GlobalVariables {
    private String FaxId = "jungly5935";
    private String FaxPw = "sjrnfl0814!";
    private String filePath = "https://bnksys.s3.ap-northeast-2.amazonaws.com/";
    private String Service = "FAX";

    public String getNow(){
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(now.getTime());
    }
}

