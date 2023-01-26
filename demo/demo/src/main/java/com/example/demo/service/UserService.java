package com.example.demo.service;

import com.example.demo.domain.User.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User login(String userId,String userPassword){
        boolean exist = userRepository.existsById(userId);

        if(exist){
            User user = userRepository.findById(userId).get();
            if(user.getPASS_WORD() == userPassword) return user;
        }
        return null;
    }
}
