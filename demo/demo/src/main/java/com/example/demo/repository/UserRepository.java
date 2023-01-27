package com.example.demo.repository;


import com.example.demo.domain.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;


public interface UserRepository extends JpaRepository<User, String> {
    @Query(value= "SELECT u.USER_ID,u.USER_NAME,c.COMM_NAME FROM TB_USER u                          \n" +
            "JOIN (SELECT u.DEPT_CODE, u.GRADE_CODE FROM TB_USER u WHERE u.USER_ID = :apprId ) u2   \n" +
            "ON u.DEPT_CODE = u2.DEPT_CODE AND u.GRADE_CODE = u2.GRADE_CODE+1                       \n" +
            "JOIN TB_COMM c ON u.GRADE_CODE = c.COMM_CODE                                           \n"
            , nativeQuery = true)
    Map<String,Object> getHigherApprUser(@Param(value = "apprId") String apprId);
}
