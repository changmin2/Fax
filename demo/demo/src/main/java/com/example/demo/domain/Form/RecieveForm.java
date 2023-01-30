package com.example.demo.domain.Form;

import lombok.Data;

@Data
public class RecieveForm {
//{Page_Cnt=1, Read=Y, No=2, RFax_No=05049260237, Sender_NM=, Recv_Date=2023-01-25 16:13, RFax_No_Seq=2, Sender_No=0151801049655017, Memo=}
    String Page_Cnt;
    String Read;
    String No;
    String RFax_No;
    String Sender_NM;
    String Recv_Date;
    String RFax_No_Seq;
    String Sender_No;
    String Memo;



}
