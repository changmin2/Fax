package com.example.demo.repository;


import com.example.demo.domain.Approval.ApprUser;
import com.example.demo.domain.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


public interface UserRepository extends JpaRepository<User, String> {
    @Query(value= "SELECT CASE WHEN u.IS_ABSENCE = 'Y' THEN u.SUBSTITUTE ELSE u.USER_ID END USER_ID,                                                  \n" +
            "             CASE WHEN u.IS_ABSENCE = 'Y' THEN (SELECT USER_NAME FROM TB_USER WHERE USER_ID = u.SUBSTITUTE)                              \n" +
            "                   ELSE u.USER_NAME END USER_NAME,                                                                                             \n" +
            "             CASE WHEN u.IS_ABSENCE = 'Y' THEN '대체자' ELSE (SELECT COMM_NAME FROM TB_COMM WHERE u.GRADE_CODE = COMM_CODE) END COMM_NAME   \n" +
            "        FROM TB_USER u                                                                                                                   \n" +
            "       JOIN (SELECT u.DEPT_CODE, u.GRADE_CODE FROM TB_USER u WHERE u.USER_ID = :apprId ) u2                                             \n" +
            "         ON u.DEPT_CODE = u2.DEPT_CODE AND u.GRADE_CODE = u2.GRADE_CODE+1                                                               \n" +
            "       WHERE u.IS_ABSENCE != 'Y' OR u.SUBSTITUTE != ''                                                                                                                           "
            , nativeQuery = true)
    List<Object[]> getHigherApprUser(@Param(value = "apprId") String apprId);
    @Query(value= "SELECT u.USER_ID,u.USER_NAME,c.COMM_NAME FROM TB_USER u                          \n" +
            "JOIN (SELECT u.DEPT_CODE, u.GRADE_CODE FROM TB_USER u WHERE u.USER_ID = :userId ) u2   \n" +
            "ON u.DEPT_CODE = u2.DEPT_CODE AND u.GRADE_CODE = u2.GRADE_CODE-1                       \n" +
            "JOIN TB_COMM c ON u.GRADE_CODE = c.COMM_CODE                                            \n" +
            "WHERE u.IS_ABSENCE = 'Y'                                                                 "
            , nativeQuery = true)
    List<Object[]> getSubstituteUser(@Param(value = "userId") String userId);

    @Query(value= "SELECT u.USER_ID,u.USER_NAME,                                                    \n" +
            "            (SELECT COMM_NAME FROM TB_COMM WHERE COMM_CODE = u.GRADE_CODE) COMM_NAME, \n" +
            "            u.GRADE_CODE,u.IS_ABSENCE,u.SUBSTITUTE,                                   \n" +
            "            d.FAX_NO,d.DEPT_NAME                                                      \n" +
            "       FROM TB_USER u,TB_DEPT d                                                         \n" +
            "      WHERE u.DEPT_CODE = d.DEPT_CODE AND u.CUSTOMER_CODE = d.CUSTOMER_CODE             \n" +
            "        AND u.USER_ID = :userId                                                             "
            , nativeQuery = true)
    Map<String,Object> getUserInfo(@Param(value = "userId") String userId);
}
