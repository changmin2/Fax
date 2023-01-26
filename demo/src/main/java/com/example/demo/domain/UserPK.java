package com.example.demo.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserPK implements Serializable {
    private String userKey;
    private String realFileName;
}
