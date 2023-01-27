package com.example.demo.repository;

import com.example.demo.domain.Upload.Upload;
import com.example.demo.domain.Upload.UploadPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadRepository extends JpaRepository<Upload, UploadPK> {

    @Query(value= "select u.realFileName from Upload u where u.userKey = :userkey", nativeQuery = true)
    String getrealFileName(@Param(value = "userkey") String userkey);

}
