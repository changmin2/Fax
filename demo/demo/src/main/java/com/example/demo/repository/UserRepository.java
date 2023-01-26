package com.example.demo.repository;

import com.example.demo.domain.Upload;
import com.example.demo.domain.UploadPK;
import com.example.demo.domain.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
