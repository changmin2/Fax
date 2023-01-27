package com.example.demo.repository;

import com.example.demo.domain.Send.Send_detail;
import com.example.demo.domain.Send.Send_detailPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SendDRepository extends JpaRepository<Send_detail, Send_detailPK> {
    List<Send_detail> findByIdUserKey(String userKey);
}
