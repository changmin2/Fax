package com.example.demo.repository;

import com.example.demo.domain.Approval.Approval;
import com.example.demo.domain.Recieve.Recieve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface RecieveRepository extends JpaRepository<Recieve, String> {

    @Query(value = "select d.*\n" +
            "  from TB_RECEIVE d\n" +
            " where d.FAX_NO =:faxNo" +
            "ORDER BY d.RECEIVE_DATE DESC",nativeQuery = true)
    List<Recieve> findByFaxNo(@Param(value = "faxNo") String faxNo);

    @Query(value = "select d.*,(SELECT USER_NAME FROM TB_USER WHERE USER_ID = d.READ_USER) read_user_name\n" +
            "  from TB_RECEIVE d\n" +
            " where d.FAX_NO =:faxNo" +
            " AND (DATE_FORMAT(d.RECEIVE_DATE, '%Y-%m-%d') BETWEEN :searchFrom AND :searchTo)" +
            "ORDER BY d.RECEIVE_DATE DESC",nativeQuery = true)
    List<Map<String,Object>> findByFaxNo2(@Param(value = "faxNo") String faxNo, @Param(value = "searchFrom") String searchFrom, @Param(value = "searchTo") String searchTo);

    @Query(value = "select d.*,(SELECT USER_NAME FROM TB_USER WHERE USER_ID = d.READ_USER) read_user_name\n" +
            "  from TB_RECEIVE d\n" +
            " where d.FAX_NO =:faxNo \n"  +
            " AND (DATE_FORMAT(d.RECEIVE_DATE, '%Y-%m-%d') BETWEEN :searchFrom AND :searchTo)" +
            " AND d.SENDER_NO like CONCAT('%', :senderNo, '%') \n" +
            "ORDER BY d.RECEIVE_DATE DESC",nativeQuery = true)
    List<Map<String,Object>> findByFaxNoWhere(@Param(value = "faxNo") String faxNo,@Param(value = "senderNo") String senderNo ,@Param(value = "searchFrom") String searchFrom,@Param(value = "searchTo") String searchTo);

}
