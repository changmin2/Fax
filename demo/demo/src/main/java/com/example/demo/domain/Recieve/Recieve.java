package com.example.demo.domain.Recieve;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="TB_RECEIVE")
public class Recieve {
    @Id
    String RECEIVE_No_SEQ;

    String FAX_NO;
    String TITLE;
    String RECEIVE_DATE;
    String SENDER_NO;
    String PAGE_CNT;
    String READ_YN;
    String FILE_DATA;
    String READ_USER;
    String READ_DATE;

}
