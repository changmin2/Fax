package com.example.demo.service;

import com.example.demo.domain.User.User;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Transactional(readOnly = true)
    public Map<String,Object> login(String userId,String userPassword){

        boolean exist = userRepository.existsById(userId);

        if(exist){
            User user = userRepository.findById(userId).get();
            if(user.getPASS_WORD().equals(userPassword)) {
                return userRepository.getUserInfo(userId);
            }
        }

        return null;
    }

    @Transactional(readOnly = true)
    public List<HashMap<String, String>> getApprUsers(String apprId){
        boolean flag = true;
        String msg = "";
        User user = userRepository.findById(apprId).get();
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

    @Transactional(readOnly = true)
    public HashMap<String, Object> getSubstituteUser(String userId){
        HashMap<String, Object> resultMap = new HashMap<>();
        boolean flag = true;
        String msg = "";
        User user = userRepository.findById(userId).get();
        if(user.getGRADE_CODE()==1){
            flag = false;
            msg = "계원은 대체자를 지정할 수 없습니다.";
            resultMap.put("flag",flag);
            resultMap.put("msg",msg);
            return resultMap;
        }

        List<HashMap<String, String>> list=new ArrayList<>();
        HashMap<String, String> tempMap = new HashMap<>();
        tempMap.put("id", "");
        tempMap.put("name", "미지정");
        list.add(tempMap);

        List<Object[]> users = userRepository.getSubstituteUser(userId);
        for (Object[] result : users) {
            HashMap<String, String> map = new HashMap<>();
            map.put("id", result[0].toString());
            map.put("name", result[1].toString()+"("+result[2].toString()+")");
//            map.put("comm", result[2].toString());
            list.add(map);
        }
        resultMap.put("users",list);
        resultMap.put("flag",flag);
        resultMap.put("msg",msg);
        return resultMap;
    }

    @Transactional
    public HashMap<String, Object> setAbsence(String userId,String substitute,String isAbsence){
        HashMap<String, Object> resultMap = new HashMap<>();
        boolean flag = true;
        String msg = "";
        User user = userRepository.findById(userId).get();
        if(user.getGRADE_CODE()==1){
            flag = false;
            msg = "계원은 부재여부를 지정할 수 없습니다.";
            resultMap.put("flag",flag);
            resultMap.put("msg",msg);
            return resultMap;
        }
        user.setIS_ABSENCE(isAbsence);
        user.setSUBSTITUTE(isAbsence.equals("Y")?substitute:"");

//        resultMap.put("user",user);
        resultMap.put("flag",flag);
        resultMap.put("msg","부재여부 설정이 완료되었습니다.");
        return resultMap;
    }
}
