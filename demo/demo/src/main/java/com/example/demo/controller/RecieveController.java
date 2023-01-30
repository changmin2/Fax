package com.example.demo.controller;


import com.example.demo.domain.Form.RecieveForm;
import com.example.demo.service.ReceiveService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
public class RecieveController {

    private final ReceiveService receiveService;

    @RequestMapping("/recieveList")
    public void recieveList() throws IOException, ParseException {
        List<RecieveForm> receive = receiveService.Receive();

    }
}
