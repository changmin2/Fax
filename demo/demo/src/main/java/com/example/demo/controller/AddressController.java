package com.example.demo.controller;

import com.example.demo.domain.Address.Address;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AddressService;
import com.example.demo.service.MainService;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
public class AddressController {

    @Autowired
    AddressService addressService;

    @PostMapping("/getAddessList")
    @ResponseBody
    public HashMap<String, Object> getAddessList(@RequestBody Map<String, String> map) throws IOException, ParseException {
        String userId = map.get("userId"); //유저아이디
        HashMap<String, Object> result = addressService.getAddessList(userId);
        return result;
    }
    @PostMapping("/setAddess")
    @ResponseBody
    public String setAddess(@RequestBody Address address) throws IOException, ParseException {
//        String userId = map.get("userId"); //유저아이디
//        System.out.println("메인정보 불러오기"+userId);
        HashMap<String, Object> result = addressService.setAddess(address);
        return "OK";
    }
}
