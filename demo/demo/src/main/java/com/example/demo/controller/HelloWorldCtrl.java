package com.example.demo.controller;

import com.example.demo.service.ReceiveService;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HelloWorldCtrl {

    private final ReceiveService receiveService;

    @GetMapping("/hello")
    public String helloWorld() {
        return "hello!";
    }

}