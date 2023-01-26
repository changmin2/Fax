package com.example.demo.controller;

import com.example.demo.domain.User.User;
import com.example.demo.service.UserService;
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
public class LoginController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    @ResponseBody
    public HashMap<String,Object> login(@RequestBody Map<String, String> users){

        String userId = users.get("userId");
        String userPassword = users.get("userPassword");

        HashMap<String, Object> re = new HashMap<>();

        User result = userService.login(userId, userPassword);

        if(result==null){
            re.put("flag",false);
            re.put("message","로그인에 실패하셨습니다.");
            return re;
        }
        re.put("flag",true);
        re.put("message","로그인에 성공하셨습니다.");
        return re;
    }
}
