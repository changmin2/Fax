package com.example.demo.controller;

import com.example.demo.SessionManager;
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
<<<<<<< HEAD
@RequestMapping("/fax-api")
@Slf4j
=======
@RequestMapping("/api")
>>>>>>> 03aa19874b790af8c6d66cec9764288f6a0b42d0
public class LoginController {

    @Autowired
    UserService userService;
    @Autowired
    SessionManager sessionManager;

    @PostMapping("/login")
    @ResponseBody
<<<<<<< HEAD
    public HashMap<String,Object> login(@RequestParam("userId") String userId, @RequestParam("userpassword") String userpassword,
                                        HttpServletRequest request, HttpServletResponse response){
        HashMap<String, Object> re = new HashMap<>();

        User result = userService.login(userId, userpassword);

        //로그인 실패시
=======
    public HashMap<String,Object> login(@RequestBody Map<String, String> users){

        String userId = users.get("userId");
        String userPassword = users.get("userPassword");

        HashMap<String, Object> re = new HashMap<>();

        User result = userService.login(userId, userPassword);

>>>>>>> 03aa19874b790af8c6d66cec9764288f6a0b42d0
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
