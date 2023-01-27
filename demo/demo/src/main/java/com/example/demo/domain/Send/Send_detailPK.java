package com.example.demo.domain.Send;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Send_detailPK implements Serializable {
    @Column(name="USER_KEY")
    private String userKey;
    @Column(name="USER_SEQ")
    private int userKeySeq;
}


