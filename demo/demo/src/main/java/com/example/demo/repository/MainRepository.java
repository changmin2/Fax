package com.example.demo.repository;

import com.example.demo.domain.Send.Send_detail;
import com.example.demo.domain.Send.Send_detailPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface MainRepository extends JpaRepository<Send_detail, Send_detailPK> {

    @Query(value = "SELECT n.TITLE,n.CONTENT,n.`DATE`,n.END_DATE,\n" +
            "(SELECT u.USER_NAME FROM TB_USER u WHERE USER_ID = n.WRITER) AS \"WRITER_NAME\"\n" +
            "FROM TB_NOTICE n\n" +
            "WHERE n.END_DATE >= DATE_FORMAT(NOW(), '%Y-%m-%d')",nativeQuery = true)
    List<Map<String,Object>> selectNotice();

    @Query(value = "SELECT (SELECT USER_NAME FROM TB_USER WHERE USER_ID = t.USER_NO) NAME,\n" +
            "\t\tDATE_FORMAT(s.INSERT_DATE , '%Y-%m-%d %H:%i:%s') AS INSERT_DATE,s.TITLE \n" +
            "FROM TB_APPROVAL t,TB_SEND s\n" +
            "WHERE t.USER_KEY = s.USER_KEY\n" +
            "AND t.STATUS = '대기' AND s.USE_GBN = 'Y'\n" +
            "AND t.APPR_PERSON = :userId\n" +
            "LIMIT 5 ",nativeQuery = true)
    List<Map<String,Object>> selectNotAppr(@Param(value = "userId")String userId);

    @Query(value = "SELECT COUNT(*) CNT\n" +
            "FROM TB_APPROVAL t,TB_SEND s\n" +
            "WHERE t.USER_KEY = s.USER_KEY\n" +
            "AND t.STATUS = '대기' AND s.USE_GBN = 'Y'\n" +
            "AND t.APPR_PERSON = :userId ",nativeQuery = true)
    int selectNotApprCount(@Param(value = "userId")String userId);

    @Query(value = "SELECT DATE_FORMAT(r.RECEIVE_DATE , '%Y-%m-%d %H:%i:%s') RECEIVE_DATE,r.PAGE_CNT,r.SENDER_NO \n" +
            "FROM TB_RECEIVE r\n" +
            "WHERE r.READ_YN = 'N'\n" +
            "AND r.FAX_NO = REPLACE((SELECT d.FAX_NO FROM TB_DEPT d \n" +
            " WHERE d.DEPT_CODE = \n" +
            "(SELECT u.DEPT_CODE FROM TB_USER u WHERE u.USER_ID = :userId)),'-','')\n" +
            "LIMIT 5;",nativeQuery = true)
    List<Map<String,Object>> selectReceiveNotRead(@Param(value = "userId")String userId);

    @Query(value = "SELECT COUNT(*) CNT\n" +
            "FROM TB_RECEIVE r\n" +
            "WHERE r.READ_YN = 'N'\n" +
            "AND r.FAX_NO = REPLACE((SELECT d.FAX_NO FROM TB_DEPT d \n" +
            "WHERE d.DEPT_CODE = \n" +
            "(SELECT u.DEPT_CODE FROM TB_USER u WHERE u.USER_ID = :userId)),'-','') ",nativeQuery = true)
    int selectReceiveNotReadCount(@Param(value = "userId")String userId);


}
