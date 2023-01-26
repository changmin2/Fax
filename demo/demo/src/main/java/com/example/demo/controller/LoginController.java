package com.example.demo.controller;

import com.example.demo.domain.User.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public User login(@RequestParam("userId") String userId,@RequestParam("userpassword") String userpassword){
        User result = userService.login(userId, userpassword);
        if(result==null){
            return null;
        }
        return result;
    }
}
