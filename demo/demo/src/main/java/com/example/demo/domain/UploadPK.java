package com.example.demo.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class UploadPK implements Serializable {
    private String userKey;
    private String realFileName;
}
