package com.example.demo.domain.Upload;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@IdClass(UploadPK.class)
@Table(name="Upload")
public class Upload {

    @Id
    private String userKey;
    @Id
    private String realFileName;

    private String userFileName;


}
