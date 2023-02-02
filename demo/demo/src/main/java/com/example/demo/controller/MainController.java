package com.example.demo.controller;

import com.example.demo.SessionManager;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.MainService;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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
public class MainController {

    @Autowired
    MainService mainService;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/mainInfo")
    @ResponseBody
    public HashMap<String, Object> mainInfo(@RequestBody Map<String,String> map){
        String userId = map.get("userId"); //유저아이디
        System.out.println("메인정보 불러오기"+userId);
        HashMap<String, Object> result = mainService.mainInfo(userId);
        return result;
    }
}