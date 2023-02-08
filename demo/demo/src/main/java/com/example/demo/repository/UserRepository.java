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
    @Query(value= "SELECT CASE WHEN u.IS_ABSENCE = 'Y' THEN u.SUBSTITUTE ELSE u.USER_ID END USER_ID,                                                 \n" +
            "       CASE WHEN u.IS_ABSENCE = 'Y' THEN (SELECT USER_NAME FROM TB_USER WHERE USER_ID = u.SUBSTITUTE)                             \n" +
            "             ELSE u.USER_NAME END USER_NAME,                                                                                      \n" +
            "       CASE WHEN u.IS_ABSENCE = 'Y' THEN '대체자' ELSE (SELECT COMM_NAME FROM TB_COMM WHERE u.GRADE_CODE = COMM_CODE) END COMM_NAM\n" +
            "  FROM TB_USER u                                                                                                                  \n" +
            " JOIN (SELECT u.DEPT_CODE, u.GRADE_CODE , (SELECT tc.LEVEL FROM TB_COMM tc WHERE u.GRADE_CODE = tc.COMM_CODE) AS \"LEVEL\" \n" +
            " FROM TB_USER u \n" +
            " WHERE u.USER_ID = :apprId ) u2                                             \n" +
            "   ON u.DEPT_CODE = u2.DEPT_CODE                                                       \n" +
            " WHERE (u.IS_ABSENCE != 'Y' OR u.SUBSTITUTE != '')       \n" +
            "  AND u.GRADE_CODE IN (SELECT tc.COMM_CODE FROM TB_COMM tc WHERE tc.LEVEL = u2.LEVEL+1 AND tc.COMM_CODE != 10)                                                                                            "
            , nativeQuery = true)
    List<Object[]> getHigherApprUser(@Param(value = "apprId") String apprId);
    @Query(value= "SELECT u.USER_ID,u.USER_NAME,\n" +
            "(SELECT COMM_NAME FROM TB_COMM WHERE COMM_CODE=u.GRADE_CODE) COMM_NAME \n" +
            "FROM TB_USER u\n" +
            "JOIN (SELECT u.DEPT_CODE, u.GRADE_CODE FROM TB_USER u WHERE u.USER_ID = :userId ) u2  \n" +
            "ON u.DEPT_CODE = u2.DEPT_CODE                                        \n" +
            "WHERE u.IS_ABSENCE != 'Y'    \n" +
            "AND u.GRADE_CODE IN\n" +
            "WHERE (tc.LEVEL = u2.GRADE_CODE-1 OR tc.LEVEL = u2.GRADE_CODE) AND tc.COMM_CODE != 10 AND tc.LEVEL != 1)\n" +
            "AND u.USER_ID != :userId    "
            , nativeQuery = true)
    List<Object[]> getSubstituteUser(@Param(value = "userId") String userId);

    @Query(value= "SELECT u.USER_ID,u.USER_NAME,                                                    \n" +
            "            (SELECT COMM_NAME FROM TB_COMM WHERE COMM_CODE = u.GRADE_CODE) COMM_NAME, \n" +
            "            u.GRADE_CODE,u.IS_ABSENCE,u.SUBSTITUTE,                                   \n" +
            "            d.FAX_NO,d.DEPT_NAME,(SELECT tc.LEVEL FROM TB_COMM tc WHERE u.GRADE_CODE = tc.COMM_CODE) AS \"LEVEL\"                                                      \n" +
            "       FROM TB_USER u,TB_DEPT d                                                         \n" +
            "      WHERE u.DEPT_CODE = d.DEPT_CODE AND u.CUSTOMER_CODE = d.CUSTOMER_CODE             \n" +
            "        AND u.USER_ID = :userId                                                             "
            , nativeQuery = true)
    Map<String,Object> getUserInfo(@Param(value = "userId") String userId);

    @Query(value= "SELECT REPLACE(d.FAX_NO,'-','') FAX_NO FROM TB_DEPT d \n" +
            " WHERE d.DEPT_CODE = \n" +
            "(SELECT u.DEPT_CODE FROM TB_USER u WHERE u.USER_ID = :userId )"
            , nativeQuery = true)
    String getFaxNo(@Param(value = "userId") String userId);

    @Query(value= "SELECT u.*,\n" +
            "(SELECT DEPT_NAME FROM TB_DEPT WHERE DEPT_CODE=u.DEPT_CODE) DEPT_NAME,\n" +
            "(SELECT COMM_NAME FROM TB_COMM WHERE COMM_CODE=u.GRADE_CODE) COMM_NAME\n" +
            "FROM TB_USER u WHERE u.GRADE_CODE != 10 \n" +
            "ORDER BY u.DEPT_CODE,u.GRADE_CODE DESC"
            , nativeQuery = true)
    List<Map<String,Object>> getUserList();

    @Query(value= "SELECT DEPT_CODE,DEPT_NAME FROM TB_DEPT"
            , nativeQuery = true)
    List<Map<String,Object>> getDeptList();

    @Query(value= "SELECT COMM_CODE,COMM_NAME FROM TB_COMM WHERE COMM_CODE != 10"
            , nativeQuery = true)
    List<Map<String,Object>> getCommList();

    @Query(value = " select USER_NAME\n" +
            "   from TB_USER\n" +
            "where USER_ID =(\n" +
            " select USER_NO\n" +
            " from TB_APPROVAL\n" +
            " where USER_KEY =:userKey)",nativeQuery = true)
    String getUserName(@Param(value = "userKey")String userKey);

    @Query(value = " select CONCAT(u.USER_NAME,'(',(SELECT\n" +
            "            COMM_NAME \n" +
            "        FROM\n" +
            "            TB_COMM \n" +
            "        WHERE\n" +
            "            COMM_CODE = u.GRADE_CODE),')') USER_NAME,u.USER_ID\n" +
            "   from TB_USER u \n" +
            "where DEPT_CODE = (\n" +
            " select DEPT_CODE \n" +
            " from TB_USER \n" +
            " where USER_ID =:userId)",nativeQuery = true)
    List<Map<String,Object>> getDeptUsers(@Param(value = "userId")String userId);
}
