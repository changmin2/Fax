package com.example.demo.service;

import com.example.demo.VO.ETC;
import com.example.demo.domain.Send.Send;
import com.example.demo.repository.SendDRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class BatchService {

    private final SendDRepository sendDRepository;
    private final SendService sendService;


    //Dont_Date가 없는 사용자의 Job_no 가져오기
    public void batchProgram() throws IOException {
        List<String> job_no = sendDRepository.getJob_no();
        if(job_no!=null){
            for (String no : job_no) {
                Send send = new Send();
                send.setJOB_NO(no);
                Map<String, Object> result = sendService.sendDetailApi(send);
                log.info("JobNo로 불러온 결과"+result.toString());
                List<ETC> etc = (List<ETC>) result.get("Recives");
                sendService.isNullSendDUser(etc,no);
            }
        }

    }
}
