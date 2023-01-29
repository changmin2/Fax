package com.example.demo.service;

import com.example.demo.domain.Approval.ApprUser;
import com.example.demo.domain.User.User;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User login(String userId,String userPassword){

        boolean exist = userRepository.existsById(userId);

        if(exist){
            User user = userRepository.findById(userId).get();
            if(user.getPASS_WORD().equals(userPassword)) return user;
        }
        return null;
    }

    public List<HashMap<String, String>> getApprUsers(String apprId){
        List<HashMap<String, String>> list=new ArrayList<>();
        List<Object[]> users = userRepository.getHigherApprUser(apprId);
        for (Object[] result : users) {
            HashMap<String, String> map = new HashMap<>();
            map.put("id", result[0].toString());
            map.put("name", result[1].toString()+"("+result[2].toString()+")");
//            map.put("comm", result[2].toString());
            list.add(map);
        }
        return list;
    }
}
