package com.example.demo.repository;

import com.example.demo.domain.Upload.Upload;
import com.example.demo.domain.Upload.UploadPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UploadRepository extends JpaRepository<Upload, UploadPK> {

    @Query(value= "select u.realFileName from Upload u where u.userKey = :userkey", nativeQuery = true)
    String getrealFileName(@Param(value = "userkey") String userkey);

    @Query(value = "UPDATE Upload a\n" +
            "SET a.realFileName =:newFileName\n" +
            "WHERE a.userKey=:userKey",nativeQuery = true)
    @Modifying
    @Transactional
    void updateFileName(@Param(value="newFileName") String newFileName,@Param(value="userKey") String userKey);
}
