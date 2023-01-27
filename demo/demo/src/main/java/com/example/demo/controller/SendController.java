package com.example.demo.controller;

import com.example.demo.VO.SendReq;
import com.example.demo.domain.Upload;
import com.example.demo.service.SendService;
import com.example.demo.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@CrossOrigin(
        // localhost:5500 과 127.0.0.1 구분
        origins = "ttp://localhost:5500h", // allowCredentials = "true" 일 경우, orogins="*" 는 X
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
    public JSONObject Send(@RequestBody SendReq req) throws IOException, ParseException {
        System.out.println("req=====================================" + req);

        log.info(req.toString());
        return sendService.sendTest(req);
    }

    @PostMapping("/convertPDF")
    @ResponseBody
    public JSONObject convertPDF(@RequestParam("files") List<MultipartFile> files) throws Exception {
        return sendService.convertPDF(files);
    }
}