package com.example.demo.controller;

import com.example.demo.service.PayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;
    @PostMapping("/payRecieve")
    public HashMap<String,String> payRecieve(@RequestParam("userId")String userId){
        payService.queryTest(userId);
        return null;
    }
}
