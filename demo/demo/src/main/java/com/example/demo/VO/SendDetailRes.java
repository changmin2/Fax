package com.example.demo.VO;

import lombok.Data;

import java.util.List;

@Data
public class SendDetailRes {
    public String Result;
    public String Code;
    public String Message;
    public String Cash;
    public String Point;
    public String Service;
    public String Job_No;
    public String Subject;
    public String Total_Cnt;
    public String Total_Page;
    public String Now_Page;
    public List<ETC> List;

    @Override
    public String toString() {
        return "SendDetailRes{" +
                "Result='" + Result + '\'' +
                ", Code='" + Code + '\'' +
                ", Message='" + Message + '\'' +
                ", Cash='" + Cash + '\'' +
                ", Point='" + Point + '\'' +
                ", Service='" + Service + '\'' +
                ", Job_No='" + Job_No + '\'' +
                ", Subject='" + Subject + '\'' +
                ", Total_Cnt='" + Total_Cnt + '\'' +
                ", Total_Page='" + Total_Page + '\'' +
                ", Now_Page='" + Now_Page + '\'' +
                ", List=" + List +
                '}';
    }

}
