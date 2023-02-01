package com.example.demo.VO;

import lombok.Data;

@Data
public class SendList {
    public String No;
    public String Job_No;       //발신 JobNo
    public String Subject;      //발신 발송제목
    public String Banner_NM;    //발신 배너명
    public String Send_Date;    //발신 보낸시각
    public String Page_Cnt;     //발신 보낸 장수
    public String Total_Cnt;    //발신 수신처수
    public String Sending_Cnt;  //발신 진행건수
    public String Sent_Cnt;     //발신 성공건수
    public String Fail_Cnt;     //발신 실패건수
    public String Status;       //발신 상태
    public String Insert_Date;  //발신 접수시각
    public String Done_Date;    //발신 완료시각

    @Override
    public String toString() {
        return "ETC{" +
                "No='" + No + '\'' +
                ", Job_No='" + Job_No + '\'' +
                ", Subject='" + Subject + '\'' +
                ", Banner_NM='" + Banner_NM + '\'' +
                ", Send_Date='" + Send_Date + '\'' +
                ", Page_Cnt='" + Page_Cnt + '\'' +
                ", Total_Cnt='" + Total_Cnt + '\'' +
                ", Sending_Cnt='" + Sending_Cnt + '\'' +
                ", Sent_Cnt='" + Sent_Cnt + '\'' +
                ", Fail_Cnt='" + Fail_Cnt + '\'' +
                ", Status='" + Status + '\'' +
                ", Insert_Date='" + Insert_Date + '\'' +
                ", Done_Date='" + Done_Date + '\'' +
                '}';
    }
}
