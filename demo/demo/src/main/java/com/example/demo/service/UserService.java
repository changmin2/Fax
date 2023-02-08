package com.example.demo.service;

import com.example.demo.SessionManager;
import com.example.demo.VO.SendRes;
import com.example.demo.domain.User.User;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    @Autowired
    SessionManager sessionManager;

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
    public List<Map<String,Object>> getDeptUsers(String userId){

        List<Map<String,Object>> users = userRepository.getDeptUsers(userId);

        return users;
    }

    @Transactional(readOnly = true)
    public HashMap<String, Object> getSubstituteUser(String userId){
        HashMap<String, Object> resultMap = new HashMap<>();
        boolean flag = true;
        String msg = "";
        User user = userRepository.findById(userId).get();
        if(user.getGRADE_CODE()==1){
            flag = false;
            msg = "파트너는 대무자를 지정할 수 없습니다.";
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
            msg = "파트너는 부재여부를 지정할 수 없습니다.";
            resultMap.put("flag",flag);
            resultMap.put("msg",msg);
            return resultMap;
        }
        user.setIS_ABSENCE(isAbsence);
        user.setSUBSTITUTE(isAbsence.equals("Y")?substitute:"");

        resultMap.put("user",userRepository.getUserInfo(userId));
        resultMap.put("flag",flag);
        resultMap.put("msg","부재여부 설정이 완료되었습니다.");

        //로그인 성공시 세션 생성
        return resultMap;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getUserList(){
        List<Map<String, Object>> users = userRepository.getUserList();
        return users;
    }
    @Transactional(readOnly = true)
    public Map<String, Object> getDeptList(){
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> deptInfo = userRepository.getDeptList();
        List<Map<String, Object>> commInfo = userRepository.getCommList();
        result.put("deptInfo",deptInfo);
        result.put("commInfo",commInfo);
        return result;
    }


    public String userUpdate(User user){
        User originUser = userRepository.findById(user.getUSER_ID()).get();
        user.setPASS_WORD(originUser.getPASS_WORD());
        user.setCUSTOMER_CODE(originUser.getCUSTOMER_CODE());
        userRepository.save(user);
        return "사용자 정보 수정이 완료되었습니다.";
    }
}
