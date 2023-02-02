package com.example.demo.domain.ExternalApi;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.plaf.multi.MultiInternalFrameUI;
import java.io.File;

@Data
public class ApiRes {
    String FAX_NO;
    String TITLE;
    String RECEIVE_DATE;
    String SENDER_NO;
    String PAGE_CNT;
    byte[] File;
}
