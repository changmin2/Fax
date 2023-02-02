package com.example.demo.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@SpringBootTest
class BatchServiceTest {

    @Autowired
    BatchService batchService;

    @Test
    @Rollback(value = false)
    public void batchTest() throws IOException {
        batchService.batchProgram();
    }
}