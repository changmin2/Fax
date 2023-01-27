package com.example.demo.domain.Send;


import com.example.demo.VO.SendReq;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "TB_SEND_D")
public class Send_detail {
    @EmbeddedId
    private Send_detailPK id; //고유키
    private String RECEIVE_NAME;
    private String RECEIVE_COMPANY;
    private String RECEIVE_FAX_NO;
    private String COMPLETE_DATE;
    private String SEND_Status;
    private String JOB_NO; //전송 성공시 발송닷컴에서 주는 NO
    private String JOB_SEQ; //임의 설정
    public Send_detail(SendReq.Destination dest,String userKey,int index) {
        this.RECEIVE_NAME = dest.getName();
        this.RECEIVE_COMPANY = dest.getCompany();
        this.RECEIVE_FAX_NO = dest.getFax();
        this.id = new Send_detailPK(userKey,index);
        this.SEND_Status = "1";
    }
}


