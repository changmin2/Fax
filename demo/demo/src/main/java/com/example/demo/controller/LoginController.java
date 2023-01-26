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

    @PostMapping("/login")
    @ResponseBody
    public HashMap<String,Object> login(@ModelAttribute Member member,
                                        HttpServletRequest request, HttpServletResponse response){
        HashMap<String, Object> re = new HashMap<>();
        User result = userService.login(member.getUserId(), member.getUserpassword());

        log.info("userID"+member.getUserId());
        log.info("pass"+member.getUserpassword());


        //로그인 실패시
        if(result==null){
            re.put("flag",false);
            re.put("message","로그인에 실패하셨습니다.");
            return re;
        }
        //로그인 성공시 세션 생성
        sessionManager.createSession(result,response);
        Object findUser = sessionManager.getSession(request);
        log.info("user"+findUser);
        re.put("flag",true);
        re.put("message","로그인에 성공하셨습니다.");
        return re;
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request){
        sessionManager.expire(request);
    }
}
