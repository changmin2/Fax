package com.example.demo.controller;

import com.example.demo.domain.User.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/fax-api")
public class LoginController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    @ResponseBody
    public HashMap<String,Object> login(@RequestParam("userId") String userId, @RequestParam("userpassword") String userpassword){
        HashMap<String, Object> re = new HashMap<>();

        User result = userService.login(userId, userpassword);
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
