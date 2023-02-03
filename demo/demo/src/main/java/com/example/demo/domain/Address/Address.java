package com.example.demo.domain.Address;


import com.example.demo.VO.SendReq;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@Table(name = "TB_ADDRESS")
public class Address {

    @Id
    private int SEQ; //고유번호
    private String USER_ID; //아이디
    private String FAX; //팩스번호
    private String NAME; //이름
    private String COMPANY; //회사명
    private String HP_NUMBER; //전화번호

    @Override
    public String toString() {
        return "Address{" +
                "SEQ='" + SEQ + '\'' +
                ", USER_ID='" + USER_ID + '\'' +
                ", FAX='" + FAX + '\'' +
                ", NAME='" + NAME + '\'' +
                ", COMPANY='" + COMPANY + '\'' +
                ", HP_NUMBER='" + HP_NUMBER + '\'' +
                '}';
    }
}


