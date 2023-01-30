package com.example.demo.repository;

import com.example.demo.domain.Approval.Approval;
import com.example.demo.domain.Recieve.Recieve;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecieveRepository extends JpaRepository<Recieve, String> {
}
