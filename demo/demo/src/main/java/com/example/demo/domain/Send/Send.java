package com.example.demo.domain.Send;


import com.example.demo.VO.SendReq;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "TB_SEND")
public class Send {
    @Id
    private String USER_KEY; //발송번호(고유키)
    private String USER_NO; //사용자ID
    private String FAX_NO; //사용자팩스번호
    private String TITLE; //제목
    private String PRIVATE_INFO_YN; //개인정보 포함 여부
    private String APPR_USER_NO; //결재
    private String RESERVE_YN; //예약여부
    private String SEND_DATE; //발송일
    private String APPR_NO; //결재
    private String STATUS; //상태
    private String INSERT_DATE; //생성일시
    private String JOB_NO; //전송 성공시 발송닷컴에서 주는 NO
    private String ERROR_MSG; //전송 실패 메세지
    private String USE_GBN;

    public Send(SendReq req) {
        this.USER_NO = req.getUserID();
        this.SEND_DATE = req.getSend_Date();
        this.USER_KEY = req.getUserKey();
        this.TITLE = req.getTitle();
        this.PRIVATE_INFO_YN = req.getPrivate_info_yn();
        this.RESERVE_YN = req.getReserve_yn();
        this.APPR_USER_NO = req.getAppr_person();
        this.FAX_NO = req.getFaxNo();
    }

    @Override
    public String toString() {
        return "Send{" +
                "USER_KEY='" + USER_KEY + '\'' +
                ", USER_NO='" + USER_NO + '\'' +
                ", FAX_NO='" + FAX_NO + '\'' +
                ", TITLE='" + TITLE + '\'' +
                ", PRIVATE_INFO_YN='" + PRIVATE_INFO_YN + '\'' +
                ", APPR_USER_NO='" + APPR_USER_NO + '\'' +
                ", RESERVE_YN='" + RESERVE_YN + '\'' +
                ", SEND_DATE='" + SEND_DATE + '\'' +
                ", APPR_NO='" + APPR_NO + '\'' +
                ", STATUS='" + STATUS + '\'' +
                ", INSERT_DATE='" + INSERT_DATE + '\'' +
                ", JOB_NO='" + JOB_NO + '\'' +
                '}';
    }

}


