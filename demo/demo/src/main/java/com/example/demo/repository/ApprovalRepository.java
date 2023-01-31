package com.example.demo.repository;

import com.example.demo.domain.Approval.Approval;
import com.example.demo.domain.Form.ApprovalForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
            "       WHERE a.USER_KEY  = s.USER_KEY                       \n" +
            "         AND a.APPR_PERSON = :userId AND a.STATUS = :status \n" +
            "        ORDER BY a.USER_KEY",nativeQuery = true)
    List<Object[]> recieve(@Param(value = "userId")String userId,@Param(value = "status")String status);
    //결재함 목록(전체)
    @Query(value = "select a.APPR_NO,a.STATUS,(SELECT USER_NAME FROM TB_USER WHERE USER_ID = a.USER_NO) as USER_NAME,\n" +
            "           DATE_FORMAT(a.APPR_DATE, '%Y-%m-%d %H:%i:%s') AS APPR_DATE,\n" +
            "           (SELECT USER_NAME FROM TB_USER WHERE USER_ID = a.APPR_PERSON) as APPR_NAME,\n" +
            "           s.TITLE,s.FAX_NO,DATE_FORMAT(s.INSERT_DATE, '%Y-%m-%d %H:%i:%s') AS INSERT_DATE \n" +
            "       from TB_APPROVAL a,TB_SEND s\n" +
            "       WHERE a.USER_KEY  = s.USER_KEY \n" +
            "         AND a.APPR_PERSON = :userId AND a.STATUS IN ('완료','반려','회수') \n" +
            "        ORDER BY a.USER_KEY",nativeQuery = true)
    List<Object[]> recieveAll(@Param(value = "userId")String userId);

    //결재함 상세 - 수신자리스트
    @Query(value = "select d.RECEIVE_NAME,d.RECEIVE_COMPANY,d.RECEIVE_FAX_NO from TB_APPROVAL a \n" +
            "join TB_SEND_D d on a.USER_KEY = d.USER_KEY \n" +
            "where a.APPR_NO =:apprNo",nativeQuery = true)
    List<Object[]> detail(@Param(value = "apprNo")String apprNo);

    //결재함 상세
    @Query(value = "select a.APPR_NO,a.APPR_PERSON,a.USER_NO,a.STATUS,a.PRIVATE_INFO_YN,\n" +
            "       a.USER_KEY,DATE_FORMAT(a.APPR_DATE, '%Y-%m-%d %H:%i:%s') AS APPR_DATE,\n" +
            "       a.APPR_REMARK,\n" +
            "       (SELECT USER_NAME FROM TB_USER WHERE USER_ID = a.USER_NO) as USER_NAME,\n" +
            "       (SELECT USER_NAME FROM TB_USER WHERE USER_ID = a.APPR_PERSON) as APPR_NAME,\n" +
            "       s.TITLE,s.FAX_NO,s.INSERT_DATE\n" +
            "from TB_APPROVAL a ,TB_SEND s\n" +
            "WHERE a.USER_KEY  = s.USER_KEY \n" +
            "AND a.APPR_NO =:apprNo",nativeQuery = true)
    List<Object[]> totalDetail(@Param(value = "apprNo")String apprNo);

    //발송대기 현황
    @Query(value = "select a.USER_KEY ,a.STATUS ,DATE_FORMAT(a.SEND_DATE, '%Y-%m-%d %H:%i:%s') AS SEND_DATE  ,\n" +
            "              a.TITLE ,a.FAX_NO ,\n" +
            "              (SELECT USER_NAME FROM TB_USER WHERE USER_ID = a.USER_NO) as USER_NAME,\n" +
            "              DATE_FORMAT(a.INSERT_DATE, '%Y-%m-%d %H:%i:%s') AS INSERT_DATE,\n" +
            "              t.STATUS,t.APPR_PERSON,t.APPR_REMARK,\n" +
            "              (SELECT USER_NAME FROM TB_USER WHERE USER_ID = t.APPR_PERSON) as APPR_NAME,\n" +
            "              a.ERROR_MSG       \n" +
            "            from TB_SEND a \n" +
            "    left outer join TB_APPROVAL t on a.APPR_NO  = t.APPR_NO      \n" +
            "    where a.USER_NO = :userId",nativeQuery = true)
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
            "              DATE_FORMAT(t.APPR_DATE, '%Y-%m-%d %H:%i:%s') AS APPR_DATE, a.FAX_NO" +
            "              ,(SELECT realFileName FROM Upload WHERE userKey = a.USER_KEY) as FILE_NAME\n" +
            "       from TB_SEND a \n" +
            "    left outer join TB_APPROVAL t on a.APPR_NO  = t.APPR_NO     \n" +
            "    where a.USER_KEY = :userKey ",nativeQuery = true)
    List<Object[]> totalDetail2(@Param(value = "userKey")String userKey);

    @Query(value = "SELECT * FROM TB_APPROVAL\n" +
            "where APPR_NO = (select APPR_NO from TB_SEND where USER_KEY =:userKey) \n", nativeQuery = true)
    Approval findAppr(@Param(value = "userKey")String userKey);


    @Query(value = "delete from fax.TB_APPROVAL\n" +
            "where APPR_NO =:apprNo",nativeQuery = true)
    void deleteApproNo(@Param(value = "apprNo") String apprNo);

}
