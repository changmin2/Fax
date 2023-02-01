package com.example.demo.controller;

import com.example.demo.GlobalVariables;
import com.example.demo.service.ReceiveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.aop.framework.adapter.GlobalAdvisorAdapterRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class HelloWorldCtrl {

    private final ReceiveService receiveService;
    private final GlobalVariables globalVariables;
    @GetMapping("/hello")
    public String helloWorld() {
        log.info(globalVariables.getNow());
        return "hello!";
    }

}