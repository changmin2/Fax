package com.example.demo.repository;

import com.example.demo.domain.Send.Send_detail;
import com.example.demo.domain.Send.Send_detailPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface SendDRepository extends JpaRepository<Send_detail, Send_detailPK> {

    @Query(value = "select a.* from TB_SEND_D a \n" +
            "where a.USER_KEY =:userKey",nativeQuery = true)
    List<Map<String,Object>> findByAllByUserKey(@Param(value = "userKey")String userKey);

}
