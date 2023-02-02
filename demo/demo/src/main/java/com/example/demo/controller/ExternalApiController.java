package com.example.demo.controller;

import com.example.demo.domain.ExternalApi.ApiRes;
import com.example.demo.domain.Recieve.Recieve;
import com.example.demo.service.ExternalApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api")
public class ExternalApiController {

    private final ExternalApiService externalApiService;

    //외부 기업을 위한 Api
    @PostMapping("/externalApi")
    public List<ApiRes> requireApi(@RequestBody Map<String,String> map) throws IOException {
        return externalApiService.externalApi(map.get("faxNo"));
    }
}
