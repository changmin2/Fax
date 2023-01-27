package com.example.demo.repository;

import com.example.demo.domain.Send.Send;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SendRepository extends JpaRepository<Send, String> {
    @Query(value= "select IFNULL(max(t.SEND_NO),0)+1 from TB_SEND t", nativeQuery = true)
    int getMaxSendNo();

    @Modifying
    @Transactional
    @Query(value= "UPDATE TB_SEND t SET t.STATUS = :status, t.JOB_NO = :jobno, t.JOB_SEQ = :jobseq WHERE t.USER_KEY = :userkey", nativeQuery = true)
    int updApiResult(@Param(value = "status") String status,@Param(value = "jobno") String jobno,@Param(value = "jobseq") int jobseq,@Param(value = "userkey") String userkey);
}
