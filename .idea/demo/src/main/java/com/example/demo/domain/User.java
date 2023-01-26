package com.example.demo.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@IdClass(UserPK.class)
public class User {

    @Id
    private String userKey;
    @Id
    private String realFileName;


    private String userFileName;


}
