package com.example.demo.controller;

import com.example.demo.SessionManager;
import com.example.demo.domain.Form.Member;
import com.example.demo.domain.User.User;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(
        // localhost:5500 과 127.0.0.1 구분
        origins = "http://localhost:5500", // allowCredentials = "true" 일 경우, orogins="*" 는 X
        allowCredentials = "true",
        allowedHeaders = "*",
        methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PUT,RequestMethod.HEAD,RequestMethod.OPTIONS}
)
@Controller
@RequestMapping("/api")
@Slf4j
public class LoginController {

    @Autowired
    UserService userService;
    @Autowired
    SessionManager sessionManager;

    //로그인
    @PostMapping("/login")
    @ResponseBody
    public HashMap<String,Object> login(@RequestBody Map<String, String> member,
                                        HttpServletRequest request, HttpServletResponse response){
        HashMap<String, Object> re = new HashMap<>();
        User result = userService.login(member.get("userId"), member.get("userPassword"));

        //로그인 실패시
        if(result==null){
            re.put("flag",false);
            re.put("message","로그인에 실패하셨습니다.");
            return re;
        }
        //로그인 성공시 세션 생성
        String access_token = sessionManager.createSession(result,response);
//        Object findUser = sessionManager.getSession(request);
        log.info("user"+result);
        re.put("flag",true);
        re.put("message","로그인에 성공하셨습니다.");
//        re.put("userInfo",result);
        re.put("access_token",access_token);
        return re;
    }
    @PostMapping("/getUserInfo")
    @ResponseBody
    public HashMap<String,Object> getUserInfo(HttpServletRequest request){
        HashMap<String, Object> re = new HashMap<>();

        //로그인 성공시 세션 생성
        Object findUser = sessionManager.getSession(request);
        //토큰 없을때
        if(findUser==null){
            re.put("flag",false);
            re.put("message","유저정보를 불러올 수 없습니다.");
            return re;
        }
        User user = (User) findUser;
        log.info("user : "+user);
        re.put("flag",true);
        re.put("message","OK");
        re.put("userInfo",findUser);
        return re;
    }
    //로그아웃
    @PostMapping("/logout")
    public void logout(HttpServletRequest request){
        sessionManager.expire(request);
    }
}
