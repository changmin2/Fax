package com.example.demo.repository;

import com.example.demo.domain.Upload;
import com.example.demo.domain.UploadPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UploadRepository extends JpaRepository<Upload, UploadPK> {

    @Query(value= "select u.real_file_name from upload u where u.user_key = :userkey", nativeQuery = true)
    List<String> getrealFileName(@Param(value = "userkey") String userkey);
}
