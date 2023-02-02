package com.example.demo.service;

import com.example.demo.domain.User.User;
import com.example.demo.repository.MainRepository;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MainService {

    @Autowired
    MainRepository mainRepository;
    @Transactional(readOnly = true)
    public HashMap<String,Object> mainInfo(String userId){
        HashMap<String,Object> result = new HashMap<>();
        result.put("noticeInfo",mainRepository.selectNotice());
        return null;
    }

}
