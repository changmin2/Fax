package com.example.demo.domain.Approval;


import com.example.demo.VO.SendReq;
import com.example.demo.domain.Send.Send;
import com.example.demo.domain.User.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "TB_APPROVAL")
public class Approval {

    @Id
    private String APPR_NO; //결재번호
//    private String APPR_NAME; //결재명
    private String USER_NO; //기안자(유저번호)
    private String APPR_PERSON; //결재자
    private String STATUS; //결재상태
    private String PRIVATE_INFO_YN; //개인정보 포함 여부
    private String USER_KEY; //발송번호
    private String APPR_DATE; //결재일자
    private String APPR_REMARK; //사유
    private String USE_GBN; //사용구분

    public Approval(SendReq req,int i) {
        this.USER_KEY = req.getUserKey();
        this.PRIVATE_INFO_YN = req.getPrivate_info_yn();
        this.STATUS = "대기";
        this.USER_NO = req.getUserID();
        this.APPR_PERSON = req.getAppr_person();
        this.APPR_NO = req.getUserKey()+i;
    }
}


