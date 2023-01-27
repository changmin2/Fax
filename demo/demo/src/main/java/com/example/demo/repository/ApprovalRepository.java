package com.example.demo.repository;

import com.example.demo.domain.Approval.Approval;
import com.example.demo.domain.Form.ApprovalForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApprovalRepository extends JpaRepository<Approval, String> {
    @Query(value= "select IFNULL(COUNT(*),0)+1 from TB_APPROVAL a where a.USER_KEY = :userkey", nativeQuery = true)
    int getMaxApprNo(@Param(value = "userkey") String userkey);

    @Query(value = "select a.APPR_NO,a.STATUS,u1.USER_NAME,a.APPR_DATE,u2.USER_NAME APPR_NAME,s.TITLE,s.FAX_NO,s.INSERT_DATE from TB_APPROVAL a \n" +
            "join TB_USER u1 on a.USER_NO = u1.USER_ID \n" +
            "join TB_USER u2 on a.APPR_PERSON = u2.USER_ID \n" +
            "join TB_SEND s on a.USER_KEY = s.USER_KEY \n" +
            "where a.APPR_PERSON = :userId",nativeQuery = true)
    List<ApprovalForm> test(@Param(value = "userId")String userId);
}
