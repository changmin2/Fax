package com.example.demo.service;

import com.example.demo.GlobalVariables;
import com.example.demo.S3Uploader;
import com.example.demo.domain.ExternalApi.ApiRes;
import com.example.demo.domain.Recieve.Recieve;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExternalApiService {

    private final ReceiveService receiveService;
    private final S3Uploader s3Uploader;

    public List<ApiRes> externalApi(String FaxNo) throws IOException {
        List<ApiRes> result = new ArrayList<>();
        List<Recieve> allReceive = receiveService.findByFaxNo(FaxNo);
        for (Recieve recieve : allReceive) {
            ApiRes apiRes = new ApiRes();
            apiRes.setFAX_NO(recieve.getFAX_NO());
            apiRes.setPAGE_CNT(recieve.getPAGE_CNT());
            apiRes.setRECEIVE_DATE(recieve.getRECEIVE_DATE());
            apiRes.setTITLE(recieve.getTITLE());
            apiRes.setSENDER_NO(recieve.getSENDER_NO());
            apiRes.setFile(s3Uploader.getFile("Receive_"+recieve.getRECEIVE_No_SEQ()+".pdf"));
            result.add(apiRes);
        }
        return result;
    }
}
