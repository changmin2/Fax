package com.example.demo.service;

import com.example.demo.GlobalVariables;
import com.example.demo.domain.Form.RecieveForm;
import com.example.demo.domain.Recieve.Recieve;
import com.example.demo.domain.User.User;
import com.example.demo.repository.MainRepository;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MainService {

    @Autowired
    MainRepository mainRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ReceiveService receiveService;
    @Autowired
    GlobalVariables globalVariables;
    @Transactional(readOnly = true)
    public HashMap<String,Object> mainInfo(String userId) throws IOException, ParseException {


        Map<String,String> map = new HashMap<>();
        String RFax_No = userRepository.getFaxNo(userId).replaceAll("-","");
        map.put("RFax_No", RFax_No);
        map.put("Date_Start", globalVariables.getBefore7Days());
        map.put("Date_End", globalVariables.getNowDate("yyyy-MM-dd"));
//        map.put()
        //api호출을 통해 얻은 전체 수신 목록
        List<RecieveForm> receive = receiveService.Receive(map);
        //DB에 저장되어 있지 않은 수신 목록 업데이트
        receiveService.DBListUpdate(receive);

        HashMap<String,Object> result = new HashMap<>();
        result.put("NoticeInfo",mainRepository.selectNotice());
        result.put("NotApprList",mainRepository.selectNotAppr(userId));
        result.put("NotApprCount",mainRepository.selectNotApprCount(userId));
        result.put("ReceiveList",mainRepository.selectReceiveNotRead(RFax_No));
        result.put("ReceiveCount",mainRepository.selectReceiveNotReadCount(RFax_No));
        return result;
    }

}
