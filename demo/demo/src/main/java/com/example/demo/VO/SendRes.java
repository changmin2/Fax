package com.example.demo.VO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SendRes {
    @JsonProperty("Message")
    public String Message;
    @JsonProperty("Service")
    public String Service;
    @JsonProperty("Point")
    public int Point;
    @JsonProperty("Cash")
    public int Cash;
    @JsonProperty("Code")
    public int Code;
    @JsonProperty("Result")
    public String Result;
    @JsonProperty("Job_No")
    public int Job_No;
}
