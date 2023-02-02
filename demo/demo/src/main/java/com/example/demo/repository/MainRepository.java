package com.example.demo.repository;

import com.example.demo.domain.Send.Send_detail;
import com.example.demo.domain.Send.Send_detailPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface MainRepository extends JpaRepository<Send_detail, Send_detailPK> {

    @Query(value = "SELECT n.TITLE,n.CONTENT,n.WRITER,n.`DATE`,n.END_DATE,\n" +
            "(SELECT u.USER_NAME FROM TB_USER u WHERE USER_ID = n.WRITER) AS \"WRITER_NAME\"\n" +
            "FROM TB_NOTICE n\n" +
            "WHERE n.END_DATE >= DATE_FORMAT(NOW(), '%Y-%m-%d')",nativeQuery = true)
    List<Map<String,Object>> selectNotice();



}
