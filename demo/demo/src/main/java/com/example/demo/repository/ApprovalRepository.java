package com.example.demo.repository;

import com.example.demo.domain.Approval.Approval;
import com.example.demo.domain.Form.ApprovalForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
public interface ApprovalRepository extends JpaRepository<Approval, String> {
    //승인번호 seq따기 위한 쿼리
    @Query(value= "select IFNULL(COUNT(*),0)+1 from TB_APPROVAL a where a.USER_KEY = :userkey", nativeQuery = true)
    int getMaxApprNo(@Param(value = "userkey") String userkey);

    //결재함 목록
    @Query(value = "select a.APPR_NO,a.STATUS,u1.USER_NAME,DATE_FORMAT(a.APPR_DATE, '%Y-%m-%d %H:%i:%s') AS APPR_DATE,u2.USER_NAME APPR_NAME,s.TITLE,s.FAX_NO,DATE_FORMAT(s.INSERT_DATE, '%Y-%m-%d %H:%i:%s') AS INSERT_DATE from TB_APPROVAL a \n" +
            "join TB_USER u1 on a.USER_NO = u1.USER_ID \n" +
            "join TB_USER u2 on a.APPR_PERSON = u2.USER_ID \n" +
            "join TB_SEND s on a.USER_KEY = s.USER_KEY \n" +
            "where a.APPR_PERSON = :userId",nativeQuery = true)
    List<Object[]> recieve(@Param(value = "userId")String userId);

    //결재함 상세 - 수신자리스트
    @Query(value = "select d.RECEIVE_NAME,d.RECEIVE_COMPANY,d.RECEIVE_FAX_NO from TB_APPROVAL a \n" +
            "join TB_SEND_D d on a.USER_KEY = d.USER_KEY \n" +
            "where a.APPR_NO =:apprNo",nativeQuery = true)
    List<Object[]> detail(@Param(value = "apprNo")String apprNo);

    //결재함 상세
    @Query(value = "select \n" +
            "a.APPR_NO,\n" +
            "a.APPR_PERSON,\n" +
            "a.USER_NO,\n" +
            "a.STATUS,\n" +
            "a.PRIVATE_INFO_YN,\n" +
            "a.USER_KEY,\n" +
            "DATE_FORMAT(a.APPR_DATE, '%Y-%m-%d %H:%i:%s') AS APPR_DATE,\n" +
            "a.APPR_REMARK,\n" +
            "u1.USER_NAME,u2.USER_NAME APPR_NAME,s.TITLE,s.FAX_NO,s.INSERT_DATE\n" +
            "from TB_APPROVAL a \n" +
            "join TB_USER u1 on a.USER_NO = u1.USER_ID \n" +
            "join TB_USER u2 on a.APPR_PERSON = u2.USER_ID \n" +
            "join TB_SEND s on a.USER_KEY = s.USER_KEY \n" +
            "where a.APPR_NO =:apprNo",nativeQuery = true)
    List<Object[]> totalDetail(@Param(value = "apprNo")String apprNo);

    //발송대기 현황
    @Query(value = "select a.USER_KEY ,a.STATUS ,DATE_FORMAT(a.SEND_DATE, '%Y-%m-%d %H:%i:%s') AS SEND_DATE  ,a.TITLE ,a.FAX_NO ,u1.USER_NAME,DATE_FORMAT(a.INSERT_DATE, '%Y-%m-%d %H:%i:%s') AS INSERT_DATE,t.STATUS,t.APPR_PERSON,t.APPR_REMARK,u2.USER_NAME APPR_NAME \n" +
            "from TB_SEND a \n" +
            "join TB_USER u1 on a.USER_NO = u1.USER_ID \n" +
            "join TB_APPROVAL t on t.APPR_NO = a.APPR_NO \n" +
            "join TB_USER u2 on t.APPR_PERSON = u2.USER_ID \n" +
            "where a.USER_NO = :userId",nativeQuery = true)
    List<Object[]> sendRecieve(@Param(value = "userId")String userId);

    //발송대기 상세 - 수신자 목록
    @Query(value = "select d.RECEIVE_NAME,d.RECEIVE_COMPANY,d.RECEIVE_FAX_NO from TB_SEND_D d\n" +
            "where d.USER_KEY =:userKey",nativeQuery = true)
    List<Object[]> detail2(@Param(value = "userKey")String userKey);

    //발송대기 상세
    @Query(value = "select \n" +
            "a.USER_KEY,\n" +
            "a.STATUS,\n" +
            "DATE_FORMAT(a.SEND_DATE, '%Y-%m-%d %H:%i:%s') AS SEND_DATE,\n" +
            "a.RESERVE_YN,\n" +
            "a.PRIVATE_INFO_YN,\n" +
            "a.TITLE,DATE_FORMAT(a.INSERT_DATE, '%Y-%m-%d %H:%i:%s') AS INSERT_DATE,\n" +
            "u.USER_NAME,t.STATUS,t.APPR_PERSON,t.APPR_REMARK,u2.USER_NAME APPR_NAME,DATE_FORMAT(t.APPR_DATE, '%Y-%m-%d %H:%i:%s') AS APPR_DATE\n" +
            "from fax.TB_SEND a \n" +
            "join fax.TB_USER u on a.USER_NO = u.USER_ID \n" +
            "join fax.TB_APPROVAL t on t.APPR_NO = a.APPR_NO \n" +
            "join fax.TB_USER u2 on t.APPR_PERSON = u2.USER_ID \n \n" +
            "where a.USER_KEY =:userKey",nativeQuery = true)
    List<Object[]> totalDetail2(@Param(value = "userKey")String userKey);
}
