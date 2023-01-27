package com.example.demo.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SendRes {
    public String Message;
    public String Service;
    public int Point;
    public int Cash;
    public int Code;
    public String Result;
    public int Job_No;

    @Override
    public String toString() {
        return "SendRes{" +
                "Message='" + Message + '\'' +
                ", Service='" + Service + '\'' +
                ", Point=" + Point +
                ", Cash=" + Cash +
                ", Code=" + Code +
                ", Result='" + Result + '\'' +
                ", Job_No=" + Job_No +
                '}';
    }
}
