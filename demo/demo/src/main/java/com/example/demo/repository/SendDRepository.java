package com.example.demo.repository;

import com.example.demo.domain.Send.Send;
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


    @Query(value = "SELECT DISTINCT d.JOB_NO\n" +
            "   FROM TB_SEND_D d,TB_SEND s\n" +
            " WHERE d.JOB_NO != '' AND d.DONE_DATE IS NULL\n" +
            " AND d.USER_KEY = s.USER_KEY \n" +
            " AND s.SEND_DATE < (SELECT DATE_ADD(NOW(), INTERVAL 9 HOUR))",nativeQuery = true)
    List<String> getJob_no();

    @Query(value = " select d.*\n" +
            "   from TB_SEND_D d\n" +
            "  where d.JOB_NO =:JobNo",nativeQuery = true)
    List<Send_detail> getSendD(@Param(value ="JobNo") String JobNo);
}
