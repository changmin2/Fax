package com.example.demo.domain.Upload;

import lombok.Data;

import java.io.Serializable;

@Data
public class UploadPK implements Serializable {
    private String userKey;
    private String realFileName;
}
