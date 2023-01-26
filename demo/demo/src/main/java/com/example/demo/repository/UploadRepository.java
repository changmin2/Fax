package com.example.demo.repository;

import com.example.demo.domain.Upload;
import com.example.demo.domain.UploadPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadRepository extends JpaRepository<Upload, UploadPK> {
}
