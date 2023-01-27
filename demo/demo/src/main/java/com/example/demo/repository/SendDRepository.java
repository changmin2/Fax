package com.example.demo.repository;

import com.example.demo.domain.Send.Send_detail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SendDRepository extends JpaRepository<Send_detail, String> {

}
