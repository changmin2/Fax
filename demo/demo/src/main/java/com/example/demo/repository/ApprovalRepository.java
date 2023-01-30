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
    @Query(value = "select a.APPR_NO,a.STATUS,(SELECT USER_NAME FROM TB_USER WHERE USER_ID = a.USER_NO) as USER_NAME,\n" +
            "           DATE_FORMAT(a.APPR_DATE, '%Y-%m-%d %H:%i:%s') AS APPR_DATE,\n" +
            "           (SELECT USER_NAME FROM TB_USER WHERE USER_ID = a.APPR_PERSON) as APPR_NAME,\n" +
            "           s.TITLE,s.FAX_NO,DATE_FORMAT(s.INSERT_DATE, '%Y-%m-%d %H:%i:%s') AS INSERT_DATE \n" +
            "       from TB_APPROVAL a,TB_SEND s\n" +
            "       WHERE a.USER_KEY  = s.USER_KEY \n" +
            "         AND a.APPR_PERSON = :userId",nativeQuery = true)
    List<Object[]> recieve(@Param(value = "userId")String userId);

    //결재함 상세 - 수신자리스트
    @Query(value = "select d.RECEIVE_NAME,d.RECEIVE_COMPANY,d.RECEIVE_FAX_NO from TB_APPROVAL a \n" +
            "join TB_SEND_D d on a.USER_KEY = d.USER_KEY \n" +
            "where a.APPR_NO =:apprNo",nativeQuery = true)
    List<Object[]> detail(@Param(value = "apprNo")String apprNo);

    //결재함 상세
<<<<<<< HEAD
    @Query(value = "select b.APPR_NO \n" +
            "      ,b.STATUS\n" +
            "      ,b.APPR_DATE\n" +
            "      ,(select USER_NAME\n" +
            "           from TB_USER\n" +
            "          where user_id=b.USER_NO) as user_name\n" +
            "          \n" +
            "          \n" +
            "          \n" +
            "          \n" +
            "  from TB_SEND a\n" +
            "       ,TB_APPROVAL b \n" +
            " where a.STATUS='결재대기'\n" +
            "   and a.USER_KEY  = b.USER_KEY\n" +
            "   and b.APPR_PERSON = :apprNo",nativeQuery = true)
=======
    @Query(value = "select a.APPR_NO,a.APPR_PERSON,a.USER_NO,a.STATUS,a.PRIVATE_INFO_YN,\n" +
            "       a.USER_KEY,DATE_FORMAT(a.APPR_DATE, '%Y-%m-%d %H:%i:%s') AS APPR_DATE,\n" +
            "       a.APPR_REMARK,\n" +
            "       (SELECT USER_NAME FROM TB_USER WHERE USER_ID = a.USER_NO) as USER_NAME,\n" +
            "       (SELECT USER_NAME FROM TB_USER WHERE USER_ID = a.APPR_PERSON) as APPR_NAME,\n" +
            "       s.TITLE,s.FAX_NO,s.INSERT_DATE\n" +
            "from TB_APPROVAL a ,TB_SEND s\n" +
            "WHERE a.USER_KEY  = s.USER_KEY \n" +
            "AND a.APPR_NO =:apprNo",nativeQuery = true)
>>>>>>> a8cd730a705e8f71c7fbb4a0cedf53ba3d6ba671
    List<Object[]> totalDetail(@Param(value = "apprNo")String apprNo);

    //발송대기 현황
    @Query(value = "select a.USER_KEY ,a.STATUS ,DATE_FORMAT(a.SEND_DATE, '%Y-%m-%d %H:%i:%s') AS SEND_DATE  ,\n" +
            "              a.TITLE ,a.FAX_NO ,\n" +
            "              (SELECT USER_NAME FROM TB_USER WHERE USER_ID = a.USER_NO) as USER_NAME,\n" +
            "              DATE_FORMAT(a.INSERT_DATE, '%Y-%m-%d %H:%i:%s') AS INSERT_DATE,\n" +
            "              t.STATUS,t.APPR_PERSON,t.APPR_REMARK,\n" +
            "              (SELECT USER_NAME FROM TB_USER WHERE USER_ID = t.APPR_PERSON) as APPR_NAME\n" +
            "        from TB_SEND a ,TB_APPROVAL t\n" +
            "        where a.APPR_NO  = t.APPR_NO \n" +
            "          and a.USER_NO = :userId",nativeQuery = true)
    List<Object[]> sendRecieve(@Param(value = "userId")String userId);

    //발송대기 상세 - 수신자 목록
    @Query(value = "select d.RECEIVE_NAME,d.RECEIVE_COMPANY,d.RECEIVE_FAX_NO from TB_SEND_D d\n" +
            "where d.USER_KEY =:userKey",nativeQuery = true)
    List<Object[]> detail2(@Param(value = "userKey")String userKey);

    //발송대기 상세
    @Query(value = "select a.USER_KEY,a.STATUS,DATE_FORMAT(a.SEND_DATE, '%Y-%m-%d %H:%i:%s') AS SEND_DATE,\n" +
            "              a.RESERVE_YN,a.PRIVATE_INFO_YN,a.TITLE,\n" +
            "              DATE_FORMAT(a.INSERT_DATE, '%Y-%m-%d %H:%i:%s') AS INSERT_DATE,\n" +
            "              (SELECT USER_NAME FROM TB_USER WHERE USER_ID = a.USER_NO) as USER_NAME,\n" +
            "              t.STATUS,\n" +
            "              t.APPR_PERSON,\n" +
            "              t.APPR_REMARK,\n" +
            "              (SELECT USER_NAME FROM TB_USER WHERE USER_ID = t.APPR_PERSON) as APPR_NAME,\n" +
            "              DATE_FORMAT(t.APPR_DATE, '%Y-%m-%d %H:%i:%s') AS APPR_DATE\n" +
            "       from TB_SEND a,TB_APPROVAL t\n" +
            "       where t.APPR_NO = a.APPR_NO \n" +
            "         and a.USER_KEY =:userKey",nativeQuery = true)
    List<Object[]> totalDetail2(@Param(value = "userKey")String userKey);
}
