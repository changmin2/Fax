package com.example.demo.domain.User;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "TB_USER")
public class User {

    @Id
    private String USER_ID;

    private String USER_NAME;
    private String PASS_WORD;
    private String EMAIL;
    private String HP_NUMBER;
    private int GRADE_CODE;
    private String DEPT_CODE;
    private String CUSTOMER_CODE;

}
