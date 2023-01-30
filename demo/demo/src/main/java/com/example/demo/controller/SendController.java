package com.example.demo.controller;

import com.example.demo.VO.SendReq;
import com.example.demo.service.SendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

public class SendController {
    @Autowired
    SendService sendService;

    @RequestMapping(method = RequestMethod.POST, path = "/Send")
    @ResponseBody
    public String Send(@RequestBody SendReq req) throws IOException, ParseException {
        System.out.println("req=====================================" + req);

        log.info(req.toString());

        return sendService.sendInsert(req);
    }

}