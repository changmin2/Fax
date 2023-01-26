package com.example.demo.controller;

import com.example.demo.VO.SendReq;
import com.example.demo.service.SendService;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@Slf4j
@RequestMapping("/")
public class SendController {

    @RequestMapping(method = RequestMethod.POST, path = "/Send")
    @ResponseBody
    public JSONObject Send(@RequestBody SendReq req) throws IOException, ParseException {
        log.info(req.toString());
        return SendService.sendTest(req);

    }

}