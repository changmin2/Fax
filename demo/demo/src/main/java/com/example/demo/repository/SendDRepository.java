package com.example.demo.repository;

import com.example.demo.domain.Send.Send_detail;
import com.example.demo.domain.Send.Send_detailPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface SendDRepository extends JpaRepository<Send_detail, Send_detailPK> {
<<<<<<< HEAD

    //결재함 상세 - 수신자리스트
    @Query(value = "select a.RECEIVE_NAME,a.RECEIVE_COMPANY,a.RECEIVE_FAX_NO from TB_SEND_D a \n" +
            "where a.USER_KEY =:userKey",nativeQuery = true)
    List<Send_detail> findByUserKey(@Param(value = "userKey")String userKey);

    //결재함 상세 - 수신자리스트
    @Query(value = "select a.* from TB_SEND_D a \n" +
            "where a.USER_KEY =:userKey",nativeQuery = true)
    List<Object[]> findByAllV2(@Param(value = "userKey")String userKey);

=======
>>>>>>> ce7e7520a1f8bb37c8d5f16876b5a5de285ca7ab
    @Query(value = "select a.* from TB_SEND_D a \n" +
            "where a.USER_KEY =:userKey",nativeQuery = true)
    List<Map<String,Object>> findByAllByUserKey(@Param(value = "userKey")String userKey);

}
