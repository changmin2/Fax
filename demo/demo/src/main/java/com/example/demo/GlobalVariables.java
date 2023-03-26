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
    private String FaxId = "*******";
    private String FaxPw = "******";
    private String filePath = "https://*****.s3.ap-northeast-2.amazonaws.com/";
    private String Service = "FAX";

    public String getNow(){
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        now.add(Calendar.HOUR,9);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(now.getTime());
    }
    public String getNowDate(String format){
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        DateFormat df = new SimpleDateFormat(format);
        return df.format(now.getTime());
    }
    public String getBefore7Days(){
        Calendar c = Calendar.getInstance();
        c.add(c.DATE, -7);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(c.getTime());
    }
    //유저 키 생성 함수
    public String createKey(String userId) {
        String userKey;
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());

        DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
        String Date_End = df.format(now.getTime());
        userKey = userId + Date_End;
        return userKey;
    }
}

