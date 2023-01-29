package com.example.demo.domain.Approval;


import com.example.demo.VO.SendReq;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApprUser {
    private String USER_ID; //아이디
    private String COMM_NAME; //직위명
    private String USER_NAME; //이름

    @Override
    public String toString() {
        return "ApprUser{" +
                "USER_ID='" + USER_ID + '\'' +
                ", COMM_NAME='" + COMM_NAME + '\'' +
                ", USER_NAME='" + USER_NAME + '\'' +
                '}';
    }
}


