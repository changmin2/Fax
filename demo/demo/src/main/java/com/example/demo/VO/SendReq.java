package com.example.demo.VO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
@Data
public class SendReq {
    private String UserID; //jungly5935
//    private String UserPW; //sjrnfl0814!
    private String Service; //"TAX"
    private String Type; //"Send"

    private String userKey; //userKey
    @JsonProperty("destinationList")
    private List<Destination> DestinationList;

    @Data
    public static class Destination{
        /*
                "destinationList":[{ "company": "테스트1", "name": "김진슬", "fax": "05043982431" },
                  { "company": "테스트2", "name": "이창민", "fax": "05043858497" },
                  { "company": "테스트3", "name": "제번", "fax": "05044865017" },
                  { "company": "테스트4", "name": "수경", "fax": "05042089819" }]
                 */

        public Destination(String company, String name, String fax) {
            this.company = company;
            this.name = name;
            this.fax = fax;
        }

        public String company; //상호명
        public String name; //수신자명
        public String fax; //숫자만 기입

        @Override
        public String toString() {
            return "Destination{" +
                    "company='" + company + '\'' +
                    ", name='" + name + '\'' +
                    ", fax='" + fax + '\'' +
                    '}';
        }
    }
    private String Send_Date; //발송시각 - 미입력 시 즉시발송예약 날짜형식) YYYY-MM-DD HH:NN

    @Override
    public String toString() {
        return "SendReq{" +
//                "UserID='" + UserID + '\'' +
//                ", UserPW='" + UserPW + '\'' +
                ", Service='" + Service + '\'' +
                ", Type='" + Type + '\'' +
                ", userKey='" + userKey + '\'' +
                ", Destination=" + DestinationList +
                ", Send_Date='" + Send_Date + '\'' +
                '}';
    }

}
