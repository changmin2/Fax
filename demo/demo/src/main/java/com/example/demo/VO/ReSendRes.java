package com.example.demo.VO;

import lombok.Data;

@Data
public class ReSendRes {
    public String Result;
    public int Code;
    public String Message;
    public int Cash;
    public String Service;
    public int Job_No;

    public int Point;
}
