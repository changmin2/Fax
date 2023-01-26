package com.example.demo.service;

import com.example.demo.domain.Upload;
import com.example.demo.repository.UploadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UploadService {

    @Autowired
    UploadRepository userRepository;

    @Transactional
    public void register(Upload user){
        userRepository.save(user);
    }

}
