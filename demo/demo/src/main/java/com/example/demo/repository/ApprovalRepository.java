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
            "           SUBSTR(a.APPR_DATE,1,16) AS APPR_DATE,\n" +
            "           (SELECT USER_NAME FROM TB_USER WHERE USER_ID = a.APPR_PERSON) as APPR_NAME,\n" +
            "           s.TITLE,s.FAX_NO,SUBSTR(s.INSERT_DATE,1,16) AS INSERT_DATE,s.PAGE_CNT \n" +
            "       from TB_APPROVAL a,TB_SEND s\n" +
            "       WHERE a.USER_KEY  = s.USER_KEY                       \n" +
            "         AND a.APPR_PERSON = :userId AND a.STATUS = :status \n" +
            "         AND s.USE_GBN = 'Y' \n" +
            "         AND STR_TO_DATE(s.INSERT_DATE, '%Y-%m-%d') BETWEEN :searchFrom AND :searchTo \n" +
            "        ORDER BY a.USER_KEY DESC",nativeQuery = true)
    List<Object[]> recieve(@Param(value = "userId")String userId,
                           @Param(value = "status")String status,
                           @Param(value = "searchFrom")String searchFrom,
                           @Param(value = "searchTo")String searchTo);
    //결재함 목록(전체)
    @Query(value = "select a.APPR_NO,a.STATUS,(SELECT USER_NAME FROM TB_USER WHERE USER_ID = a.USER_NO) as USER_NAME,\n" +
            "           SUBSTR(a.APPR_DATE,1,16) AS APPR_DATE,\n" +
            "           (SELECT USER_NAME FROM TB_USER WHERE USER_ID = a.APPR_PERSON) as APPR_NAME,\n" +
            "           s.TITLE,s.FAX_NO,SUBSTR(s.INSERT_DATE,1,16) AS INSERT_DATE,s.PAGE_CNT \n" +
            "       from TB_APPROVAL a,TB_SEND s\n" +
            "       WHERE a.USER_KEY  = s.USER_KEY \n" +
            "         AND a.APPR_PERSON = :userId AND a.STATUS IN ('완료','반려','회수') \n" +
            "         AND s.USE_GBN = 'Y'                                               \n" +
            "         AND STR_TO_DATE(s.INSERT_DATE, '%Y-%m-%d') BETWEEN :searchFrom AND :searchTo                 \n" +
            "        ORDER BY a.USER_KEY DESC",nativeQuery = true)
    List<Object[]> recieveAll(@Param(value = "userId")String userId,
                              @Param(value = "searchFrom")String searchFrom,
                              @Param(value = "searchTo")String searchTo);

    //결재함 상세 - 수신자리스트
    @Query(value = "select d.RECEIVE_NAME,d.RECEIVE_COMPANY,d.RECEIVE_FAX_NO from TB_APPROVAL a \n" +
            "join TB_SEND_D d on a.USER_KEY = d.USER_KEY \n" +
            "where a.APPR_NO =:apprNo",nativeQuery = true)
    List<Object[]> detail(@Param(value = "apprNo")String apprNo);

    //결재함 상세
    @Query(value = "select a.APPR_NO,a.APPR_PERSON,a.USER_NO,a.STATUS,a.PRIVATE_INFO_YN,\n" +
            "       a.USER_KEY,SUBSTR(a.APPR_DATE,1,16) AS APPR_DATE,\n" +
            "       a.APPR_REMARK,\n" +
            "       (SELECT USER_NAME FROM TB_USER WHERE USER_ID = a.USER_NO) as USER_NAME,\n" +
            "       (SELECT USER_NAME FROM TB_USER WHERE USER_ID = a.APPR_PERSON) as APPR_NAME,\n" +
            "       s.TITLE,s.FAX_NO,SUBSTR(a.INSERT_DATE,1,16) as INSERT_DATE,\n" +
            "       (SELECT realFileName FROM Upload WHERE userKey = a.USER_KEY) as FILE_NAME,s.PAGE_CNT \n" +
            "       ,s.PRIVATE_INFO_YN  \n" +
            "from TB_APPROVAL a ,TB_SEND s\n" +
            "WHERE a.USER_KEY  = s.USER_KEY \n" +
            "         AND s.USE_GBN = 'Y' \n" +
            "AND a.APPR_NO =:apprNo ",nativeQuery = true)
    List<Object[]> totalDetail(@Param(value = "apprNo")String apprNo);

    //발송대기 현황 전체
    @Query(value = "select a.USER_KEY ,a.STATUS ,SUBSTR(a.SEND_DATE, 1,16) AS SEND_DATE  ,\n" +
            "              a.TITLE ,a.FAX_NO ,\n" +
            "              (SELECT USER_NAME FROM TB_USER WHERE USER_ID = a.USER_NO) as USER_NAME,\n" +
            "              SUBSTR(a.INSERT_DATE,1,16) AS INSERT_DATE,\n" +
            "              t.STATUS,t.APPR_PERSON,t.APPR_REMARK,\n" +
            "              (SELECT USER_NAME FROM TB_USER WHERE USER_ID = t.APPR_PERSON) as APPR_NAME,\n" +
            "              a.ERROR_MSG    ,a.PAGE_CNT, d.SUCCESS, d.FAIL, d.WAIT , d.TOTAL   \n" +
            "            from TB_SEND a \n" +
            "   LEFT OUTER JOIN TB_APPROVAL t ON a.APPR_NO  = t.APPR_NO   \n" +
            "   JOIN (SELECT SUM(SUCCESS) SUCCESS, SUM(FAIL) FAIL, SUM(WAIT) WAIT           \n" +
            "   , COUNT(USER_KEY) TOTAL ,USER_KEY                                     \n" +
            "   FROM (                                                                \n" +
            "   SELECT CASE WHEN STATUS = '성공' THEN 1 ELSE 0 END SUCCESS,             \n" +
            "   CASE WHEN STATUS = '실패' THEN 1 ELSE 0 END FAIL,                       \n" +
            "   CASE WHEN STATUS IS NULL THEN 1 ELSE 0 END WAIT, USER_KEY, JOB_NO     \n" +
            "   FROM TB_SEND_D ) D                                                    \n" +
            "   GROUP BY USER_KEY) d ON a.USER_KEY  = d.USER_KEY                     \n" +
            "    where a.USER_NO  IN ( SELECT USER_ID\n" +
            "   FROM TB_USER\n" +
            "   WHERE DEPT_CODE = (SELECT DEPT_CODE FROM TB_USER WHERE USER_ID=:userId)) AND a.USE_GBN = 'Y'                     \n" +
            "         AND STR_TO_DATE(a.INSERT_DATE, '%Y-%m-%d') BETWEEN :searchFrom AND :searchTo \n" +
            "    ORDER BY a.USER_KEY DESC",nativeQuery = true)
    List<Object[]> sendRecieveAll(@Param(value = "userId")String userId,
                                  @Param(value = "searchFrom")String searchFrom,
                                  @Param(value = "searchTo")String searchTo);

    //발송대기 현황 상태값있을때
    @Query(value = "select a.USER_KEY ,a.STATUS ,SUBSTR(a.SEND_DATE, 1,16) AS SEND_DATE  ,\n" +
            "              a.TITLE ,a.FAX_NO ,\n" +
            "              (SELECT USER_NAME FROM TB_USER WHERE USER_ID = a.USER_NO) as USER_NAME,\n" +
            "              SUBSTR(a.INSERT_DATE,1,16) AS INSERT_DATE,\n" +
            "              t.STATUS,t.APPR_PERSON,t.APPR_REMARK,\n" +
            "              (SELECT USER_NAME FROM TB_USER WHERE USER_ID = t.APPR_PERSON) as APPR_NAME,\n" +
            "              a.ERROR_MSG  ,a.PAGE_CNT, d.SUCCESS, d.FAIL, d.WAIT , d.TOTAL   \n" +
            "            from TB_SEND a \n" +
            "   LEFT OUTER JOIN TB_APPROVAL t ON a.APPR_NO  = t.APPR_NO   \n" +
            "   JOIN (SELECT SUM(SUCCESS) SUCCESS, SUM(FAIL) FAIL, SUM(WAIT) WAIT           \n" +
            "   , COUNT(USER_KEY) TOTAL ,USER_KEY                                     \n" +
            "   FROM (                                                                \n" +
            "   SELECT CASE WHEN STATUS = '성공' THEN 1 ELSE 0 END SUCCESS,             \n" +
            "   CASE WHEN STATUS = '실패' THEN 1 ELSE 0 END FAIL,                       \n" +
            "   CASE WHEN STATUS IS NULL THEN 1 ELSE 0 END WAIT, USER_KEY, JOB_NO     \n" +
            "   FROM TB_SEND_D ) D                                                    \n" +
            "   GROUP BY USER_KEY) d ON a.USER_KEY  = d.USER_KEY                     \n" +
            "    where  a.STATUS = :status    \n" +
            "    and a.USER_NO   IN ( SELECT USER_ID    \n" +
            "       FROM TB_USER        \n" +
            "      WHERE DEPT_CODE = (SELECT DEPT_CODE FROM TB_USER WHERE USER_ID=:userId)) AND a.USE_GBN = 'Y'                  \n" +
           "         AND STR_TO_DATE(a.INSERT_DATE, '%Y-%m-%d') BETWEEN :searchFrom AND :searchTo \n" +
            "    ORDER BY a.USER_KEY DESC",nativeQuery = true)
    List<Object[]> sendRecieve(@Param(value = "userId")String userId,
                               @Param(value = "status")String status,
                               @Param(value = "searchFrom")String searchFrom,
                               @Param(value = "searchTo")String searchTo);

    //발송대기 현황 전체(+받는사람)
    @Query(value = "select a.USER_KEY ,a.STATUS ,SUBSTR(a.APPR_DATE,1,16) AS SEND_DATE  ,\n" +
            "              a.TITLE ,a.FAX_NO ,\n" +
            "              (SELECT USER_NAME FROM TB_USER WHERE USER_ID = a.USER_NO) as USER_NAME,\n" +
            "              SUBSTR(a.INSERT_DATE,1,16) AS INSERT_DATE,\n" +
            "              t.STATUS,t.APPR_PERSON,t.APPR_REMARK,\n" +
            "              (SELECT USER_NAME FROM TB_USER WHERE USER_ID = t.APPR_PERSON) as APPR_NAME,\n" +
            "              a.ERROR_MSG    ,a.PAGE_CNT, d.SUCCESS, d.FAIL, d.WAIT , d.TOTAL   \n" +
            "            from TB_SEND a \n" +
            "   LEFT OUTER JOIN TB_APPROVAL t ON a.APPR_NO  = t.APPR_NO   \n" +
            "   JOIN (SELECT SUM(SUCCESS) SUCCESS, SUM(FAIL) FAIL, SUM(WAIT) WAIT           \n" +
            "   , COUNT(USER_KEY) TOTAL ,USER_KEY                                     \n" +
            "   FROM (                                                                \n" +
            "   SELECT CASE WHEN STATUS = '성공' THEN 1 ELSE 0 END SUCCESS,             \n" +
            "   CASE WHEN STATUS = '실패' THEN 1 ELSE 0 END FAIL,                       \n" +
            "   CASE WHEN STATUS IS NULL THEN 1 ELSE 0 END WAIT, USER_KEY, JOB_NO     \n" +
            "   FROM TB_SEND_D  WHERE (RECEIVE_NAME LIKE CONCAT('%',:receiver,'%') OR RECEIVE_FAX_NO LIKE CONCAT('%',:receiver,'%')) ) D    \n" +
            "   GROUP BY USER_KEY) d ON a.USER_KEY  = d.USER_KEY                     \n" +
            "    where a.USER_NO  IN ( SELECT USER_ID\n" +
            "   FROM TB_USER\n" +
            "   WHERE DEPT_CODE = (SELECT DEPT_CODE FROM TB_USER WHERE USER_ID=:userId)) AND a.USE_GBN = 'Y'                     \n" +
            "         AND STR_TO_DATE(a.INSERT_DATE, '%Y-%m-%d') BETWEEN :searchFrom AND :searchTo \n" +
            "    ORDER BY a.USER_KEY DESC",nativeQuery = true)
    List<Object[]> sendRecieveAll(@Param(value = "userId")String userId,
                                  @Param(value = "searchFrom")String searchFrom,
                                  @Param(value = "searchTo")String searchTo,@Param(value = "receiver")String receiver);

    //발송대기 현황 상태값있을때(+받는사람)
    @Query(value = "select a.USER_KEY ,a.STATUS ,SUBSTR(a.SEND_DATE,1,16) AS SEND_DATE  ,\n" +
            "              a.TITLE ,a.FAX_NO ,\n" +
            "              (SELECT USER_NAME FROM TB_USER WHERE USER_ID = a.USER_NO) as USER_NAME,\n" +
            "              SUBSTR(a.INSERT_DATE,1,16) AS INSERT_DATE,\n" +
            "              t.STATUS,t.APPR_PERSON,t.APPR_REMARK,\n" +
            "              (SELECT USER_NAME FROM TB_USER WHERE USER_ID = t.APPR_PERSON) as APPR_NAME,\n" +
            "              a.ERROR_MSG  ,a.PAGE_CNT, d.SUCCESS, d.FAIL, d.WAIT , d.TOTAL   \n" +
            "            from TB_SEND a \n" +
            "   LEFT OUTER JOIN TB_APPROVAL t ON a.APPR_NO  = t.APPR_NO   \n" +
            "   JOIN (SELECT SUM(SUCCESS) SUCCESS, SUM(FAIL) FAIL, SUM(WAIT) WAIT           \n" +
            "   , COUNT(USER_KEY) TOTAL ,USER_KEY                                     \n" +
            "   FROM (                                                                \n" +
            "   SELECT CASE WHEN STATUS = '성공' THEN 1 ELSE 0 END SUCCESS,             \n" +
            "   CASE WHEN STATUS = '실패' THEN 1 ELSE 0 END FAIL,                       \n" +
            "   CASE WHEN STATUS IS NULL THEN 1 ELSE 0 END WAIT, USER_KEY, JOB_NO     \n" +
            "   FROM TB_SEND_D  WHERE (RECEIVE_NAME LIKE CONCAT('%',:receiver,'%') OR RECEIVE_FAX_NO LIKE CONCAT('%',:receiver,'%')) ) D      \n" +
            "   GROUP BY USER_KEY) d ON a.USER_KEY  = d.USER_KEY                     \n" +
            "    where  a.STATUS = :status    \n" +
            "    and a.USER_NO   IN ( SELECT USER_ID     \n" +
            "       FROM TB_USER        \n" +
            "      WHERE DEPT_CODE = (SELECT DEPT_CODE FROM TB_USER WHERE USER_ID=:userId)) AND a.USE_GBN = 'Y'                  \n" +
           "         AND STR_TO_DATE(a.INSERT_DATE, '%Y-%m-%d') BETWEEN :searchFrom AND :searchTo \n" +
            "    ORDER BY a.USER_KEY DESC",nativeQuery = true)
    //발송대기 현황 상태값있을때(+받는사람)
    List<Object[]> sendRecieve(@Param(value = "userId")String userId,
                               @Param(value = "status")String status,
                               @Param(value = "searchFrom")String searchFrom,
                               @Param(value = "searchTo")String searchTo,@Param(value = "receiver")String receiver);
    //발송대기 현황 상태값없을때(+보낸사람)
    @Query(value = "select a.USER_KEY ,a.STATUS ,SUBSTR(a.SEND_DATE,1,16) AS SEND_DATE  ,\n" +
            "              a.TITLE ,a.FAX_NO ,\n" +
            "              (SELECT USER_NAME FROM TB_USER WHERE USER_ID = a.USER_NO) as USER_NAME,\n" +
            "              SUBSTR(a.INSERT_DATE,1,16) AS INSERT_DATE,\n" +
            "              t.STATUS,t.APPR_PERSON,t.APPR_REMARK,\n" +
            "              (SELECT USER_NAME FROM TB_USER WHERE USER_ID = t.APPR_PERSON) as APPR_NAME,\n" +
            "              a.ERROR_MSG    ,a.PAGE_CNT, d.SUCCESS, d.FAIL, d.WAIT , d.TOTAL   \n" +
            "            from TB_SEND a \n" +
            "   LEFT OUTER JOIN TB_APPROVAL t ON a.APPR_NO  = t.APPR_NO   \n" +
            "   JOIN (SELECT SUM(SUCCESS) SUCCESS, SUM(FAIL) FAIL, SUM(WAIT) WAIT           \n" +
            "   , COUNT(USER_KEY) TOTAL ,USER_KEY                                     \n" +
            "   FROM (                                                                \n" +
            "   SELECT CASE WHEN STATUS = '성공' THEN 1 ELSE 0 END SUCCESS,             \n" +
            "   CASE WHEN STATUS = '실패' THEN 1 ELSE 0 END FAIL,                       \n" +
            "   CASE WHEN STATUS IS NULL THEN 1 ELSE 0 END WAIT, USER_KEY, JOB_NO     \n" +
            "   FROM TB_SEND_D  ) D    \n" +
            "   GROUP BY USER_KEY) d ON a.USER_KEY  = d.USER_KEY                     \n" +
            "  WHERE a.USER_NO = :senderId AND a.USE_GBN = 'Y' AND STR_TO_DATE(a.INSERT_DATE, '%Y-%m-%d') BETWEEN :searchFrom AND :searchTo \n" +
            "    ORDER BY a.USER_KEY DESC",nativeQuery = true)
    List<Object[]> sendRecieveAll2(
                                  @Param(value = "searchFrom")String searchFrom,
                                  @Param(value = "searchTo")String searchTo,@Param(value = "senderId")String senderId);

    //발송대기 현황 상태값있을때(+보낸사람)
    @Query(value = "select a.USER_KEY ,a.STATUS ,SUBSTR(a.SEND_DATE,1,16) AS SEND_DATE  ,\n" +
            "              a.TITLE ,a.FAX_NO ,\n" +
            "              (SELECT USER_NAME FROM TB_USER WHERE USER_ID = a.USER_NO) as USER_NAME,\n" +
            "              SUBSTR(a.INSERT_DATE,1,16) AS INSERT_DATE,\n" +
            "              t.STATUS,t.APPR_PERSON,t.APPR_REMARK,\n" +
            "              (SELECT USER_NAME FROM TB_USER WHERE USER_ID = t.APPR_PERSON) as APPR_NAME,\n" +
            "              a.ERROR_MSG  ,a.PAGE_CNT, d.SUCCESS, d.FAIL, d.WAIT , d.TOTAL   \n" +
            "            from TB_SEND a \n" +
            "   LEFT OUTER JOIN TB_APPROVAL t ON a.APPR_NO  = t.APPR_NO   \n" +
            "   JOIN (SELECT SUM(SUCCESS) SUCCESS, SUM(FAIL) FAIL, SUM(WAIT) WAIT           \n" +
            "   , COUNT(USER_KEY) TOTAL ,USER_KEY                                     \n" +
            "   FROM (                                                                \n" +
            "   SELECT CASE WHEN STATUS = '성공' THEN 1 ELSE 0 END SUCCESS,             \n" +
            "   CASE WHEN STATUS = '실패' THEN 1 ELSE 0 END FAIL,                       \n" +
            "   CASE WHEN STATUS IS NULL THEN 1 ELSE 0 END WAIT, USER_KEY, JOB_NO     \n" +
            "   FROM TB_SEND_D ) D                                                     \n" +
            "   GROUP BY USER_KEY) d ON a.USER_KEY  = d.USER_KEY                     \n" +
            "    where  a.STATUS = :status    \n" +
            "    and a.USER_NO = :senderId  AND a.USE_GBN = 'Y'                  \n" +
           "         AND STR_TO_DATE(a.INSERT_DATE, '%Y-%m-%d') BETWEEN :searchFrom AND :searchTo \n" +
            "    ORDER BY a.USER_KEY DESC",nativeQuery = true)
    List<Object[]> sendRecieve2(
                               @Param(value = "status")String status,
                               @Param(value = "searchFrom")String searchFrom,
                               @Param(value = "searchTo")String searchTo,@Param(value = "senderId")String senderId);
    //발송대기 현황 상태값없을때(+보낸사람+받는사람)
    @Query(value = "select a.USER_KEY ,a.STATUS ,SUBSTR(a.SEND_DATE,1,16) AS SEND_DATE  ,\n" +
            "              a.TITLE ,a.FAX_NO ,\n" +
            "              (SELECT USER_NAME FROM TB_USER WHERE USER_ID = a.USER_NO) as USER_NAME,\n" +
            "              SUBSTR(a.INSERT_DATE,1,16) AS INSERT_DATE,\n" +
            "              t.STATUS,t.APPR_PERSON,t.APPR_REMARK,\n" +
            "              (SELECT USER_NAME FROM TB_USER WHERE USER_ID = t.APPR_PERSON) as APPR_NAME,\n" +
            "              a.ERROR_MSG    ,a.PAGE_CNT, d.SUCCESS, d.FAIL, d.WAIT , d.TOTAL   \n" +
            "            from TB_SEND a \n" +
            "   LEFT OUTER JOIN TB_APPROVAL t ON a.APPR_NO  = t.APPR_NO   \n" +
            "   JOIN (SELECT SUM(SUCCESS) SUCCESS, SUM(FAIL) FAIL, SUM(WAIT) WAIT           \n" +
            "   , COUNT(USER_KEY) TOTAL ,USER_KEY                                     \n" +
            "   FROM (                                                                \n" +
            "   SELECT CASE WHEN STATUS = '성공' THEN 1 ELSE 0 END SUCCESS,             \n" +
            "   CASE WHEN STATUS = '실패' THEN 1 ELSE 0 END FAIL,                       \n" +
            "   CASE WHEN STATUS IS NULL THEN 1 ELSE 0 END WAIT, USER_KEY, JOB_NO     \n" +
            "   FROM TB_SEND_D  WHERE (RECEIVE_NAME LIKE CONCAT('%',:receiver,'%') OR RECEIVE_FAX_NO LIKE CONCAT('%',:receiver,'%')) ) D    \n" +
            "   GROUP BY USER_KEY) d ON a.USER_KEY  = d.USER_KEY                     \n" +
            "  WHERE a.USER_NO = :senderId AND a.USE_GBN = 'Y' AND STR_TO_DATE(a.INSERT_DATE, '%Y-%m-%d') BETWEEN :searchFrom AND :searchTo \n" +
            "    ORDER BY a.USER_KEY DESC",nativeQuery = true)
    List<Object[]> sendRecieveAll2(
            @Param(value = "searchFrom")String searchFrom,
            @Param(value = "searchTo")String searchTo,@Param(value = "senderId")String senderId,@Param(value = "receiver")String receiver);

    //발송대기 현황 상태값있을때(+보낸사람+받는사람)
    @Query(value = "select a.USER_KEY ,a.STATUS ,SUBSTR(a.SEND_DATE,1,16) AS SEND_DATE  ,\n" +
            "              a.TITLE ,a.FAX_NO ,\n" +
            "              (SELECT USER_NAME FROM TB_USER WHERE USER_ID = a.USER_NO) as USER_NAME,\n" +
            "              SUBSTR(a.INSERT_DATE,1,16) AS INSERT_DATE,\n" +
            "              t.STATUS,t.APPR_PERSON,t.APPR_REMARK,\n" +
            "              (SELECT USER_NAME FROM TB_USER WHERE USER_ID = t.APPR_PERSON) as APPR_NAME,\n" +
            "              a.ERROR_MSG  ,a.PAGE_CNT, d.SUCCESS, d.FAIL, d.WAIT , d.TOTAL   \n" +
            "            from TB_SEND a \n" +
            "   LEFT OUTER JOIN TB_APPROVAL t ON a.APPR_NO  = t.APPR_NO   \n" +
            "   JOIN (SELECT SUM(SUCCESS) SUCCESS, SUM(FAIL) FAIL, SUM(WAIT) WAIT           \n" +
            "   , COUNT(USER_KEY) TOTAL ,USER_KEY                                     \n" +
            "   FROM (                                                                \n" +
            "   SELECT CASE WHEN STATUS = '성공' THEN 1 ELSE 0 END SUCCESS,             \n" +
            "   CASE WHEN STATUS = '실패' THEN 1 ELSE 0 END FAIL,                       \n" +
            "   CASE WHEN STATUS IS NULL THEN 1 ELSE 0 END WAIT, USER_KEY, JOB_NO     \n" +
            "   FROM TB_SEND_D  WHERE (RECEIVE_NAME LIKE CONCAT('%',:receiver,'%') OR RECEIVE_FAX_NO LIKE CONCAT('%',:receiver,'%')) ) D    \n" +
            "   GROUP BY USER_KEY) d ON a.USER_KEY  = d.USER_KEY                     \n" +
            "    where  a.STATUS = :status    \n" +
            "    and a.USER_NO = :senderId  AND a.USE_GBN = 'Y'                  \n" +
            "         AND STR_TO_DATE(a.INSERT_DATE, '%Y-%m-%d') BETWEEN :searchFrom AND :searchTo \n" +
            "    ORDER BY a.USER_KEY DESC",nativeQuery = true)
    List<Object[]> sendRecieve2(
            @Param(value = "status")String status,
            @Param(value = "searchFrom")String searchFrom,
            @Param(value = "searchTo")String searchTo,@Param(value = "senderId")String senderId,@Param(value = "receiver")String receiver);
    //발송대기 상세 - 수신자 목록
    @Query(value = "select d.RECEIVE_NAME,d.RECEIVE_COMPANY,d.RECEIVE_FAX_NO,d.STATUS,d.STATUS_DETAIL, \n" +
            "SUBSTR(d.DONE_DATE,1,16) AS DONE_DATE  from TB_SEND_D d\n" +
            "where d.USER_KEY =:userKey ",nativeQuery = true)
    List<Object[]> detail2(@Param(value = "userKey")String userKey);

    //발송대기 상세
    @Query(value = "select a.USER_KEY,a.STATUS,SUBSTR(a.SEND_DATE,1,16) AS SEND_DATE,\n" +
            "              a.RESERVE_YN,a.PRIVATE_INFO_YN,a.TITLE,\n" +
            "              SUBSTR(a.INSERT_DATE,1,16) AS INSERT_DATE,\n" +
            "              (SELECT USER_NAME FROM TB_USER WHERE USER_ID = a.USER_NO) as USER_NAME,\n" +
            "              t.STATUS,\n" +
            "              t.APPR_PERSON,\n" +
            "              t.APPR_REMARK,\n" +
            "              (SELECT USER_NAME FROM TB_USER WHERE USER_ID = t.APPR_PERSON) as APPR_NAME,\n" +
            "              SUBSTR(t.APPR_DATE,1,16) AS APPR_DATE, a.FAX_NO" +
            "              ,(SELECT realFileName FROM Upload WHERE userKey = a.USER_KEY) as FILE_NAME,a.PAGE_CNT \n" +
            "              ,a.PRIVATE_INFO_YN  \n" +
            "       from TB_SEND a      \n" +
            "   LEFT OUTER JOIN TB_APPROVAL t ON a.APPR_NO  = t.APPR_NO   \n" +
            "   where a.USER_KEY = :userKey  AND a.USE_GBN = 'Y' ",nativeQuery = true)
    List<Object[]> totalDetail2(@Param(value = "userKey")String userKey);

    @Query(value = "SELECT * FROM TB_APPROVAL\n" +
            "where APPR_NO = (select APPR_NO from TB_SEND where USER_KEY =:userKey) \n", nativeQuery = true)
    Approval findAppr(@Param(value = "userKey")String userKey);


    @Query(value = "delete from fax.TB_APPROVAL\n" +
            "where APPR_NO =:apprNo ",nativeQuery = true)
    void deleteApproNo(@Param(value = "apprNo") String apprNo);

    //결제자 번호 추출
    @Query(value = "select HP_NUMBER\n" +
            "  from fax.TB_USER\n" +
            " where USER_ID = (\n" +
            "\tselect APPR_PERSON\n" +
            "      from fax.TB_APPROVAL\n" +
            "     where USER_KEY =:userKey\n" +
            " )",nativeQuery = true)
    String getPhoneNumber(@Param(value="userKey")String userKey);
}
