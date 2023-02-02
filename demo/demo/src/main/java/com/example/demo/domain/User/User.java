package com.example.demo.domain.User;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Data
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
    private String IS_ABSENCE;
    private String SUBSTITUTE;

    @Override
    public String toString() {
        return "User{" +
                "USER_ID='" + USER_ID + '\'' +
                ", USER_NAME='" + USER_NAME + '\'' +
                ", PASS_WORD='" + PASS_WORD + '\'' +
                ", EMAIL='" + EMAIL + '\'' +
                ", HP_NUMBER='" + HP_NUMBER + '\'' +
                ", GRADE_CODE=" + GRADE_CODE +
                ", DEPT_CODE='" + DEPT_CODE + '\'' +
                ", CUSTOMER_CODE='" + CUSTOMER_CODE + '\'' +
                ", IS_ABSENCE='" + IS_ABSENCE + '\'' +
                ", SUBSTITUTE='" + SUBSTITUTE + '\'' +
                '}';
    }
}
