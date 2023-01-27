package com.example.demo.domain.Form;

import com.example.demo.domain.Approval.Approval;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ApprovalForm {
    private String APPR_NO; //결재번호
    private String STATUS; //결재상태
    private String USER_NAME;

    public ApprovalForm(String APPR_NO, String STATUS, String USER_NAME, String APPR_DATE, String APPR_NAME, String TITLE, String FAX_NO, String INSERT_DATE) {
        this.APPR_NO = APPR_NO; //결제 고유번호
        this.STATUS = STATUS; //상태
        this.USER_NAME = USER_NAME; // 보내는사람
        this.APPR_DATE = APPR_DATE; //결제일자
        this.APPR_NAME = APPR_NAME; // 받는사람
        this.TITLE = TITLE; //제목
        this.FAX_NO = FAX_NO; //보내는사람 팩스번호
        this.INSERT_DATE = INSERT_DATE; // 발의(요청)일자
    }

    private String APPR_DATE; //결재일자
    private String APPR_NAME;
    private String TITLE;
    private String FAX_NO;
    private String INSERT_DATE;

}
