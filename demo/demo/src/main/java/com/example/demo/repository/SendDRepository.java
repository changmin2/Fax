package com.example.demo.repository;

import com.example.demo.domain.Send.Send_detail;
import com.example.demo.domain.Send.Send_detailPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SendDRepository extends JpaRepository<Send_detail, Send_detailPK> {
    List<Send_detail> findByIdUserKey(String userKey);

    //결재함 상세 - 수신자리스트
    @Query(value = "select a.RECEIVE_NAME,a.RECEIVE_COMPANY,a.RECEIVE_FAX_NO from TB_SEND_D a \n" +
            "where a.USER_KEY =:userKey",nativeQuery = true)
    List<Send_detail> findByUserKey(@Param(value = "userKey")String userKey);
}
