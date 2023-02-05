package com.example.demo.repository;

import com.example.demo.domain.Approval.Approval;
import com.example.demo.domain.Recieve.Recieve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecieveRepository extends JpaRepository<Recieve, String> {

    @Query(value = "select d.*\n" +
            "  from TB_RECEIVE d\n" +
            " where d.FAX_NO =:faxNo" +
            "ORDER BY d.RECEIVE_DATE DESC",nativeQuery = true)
    List<Recieve> findByFaxNo(@Param(value = "faxNo") String faxNo);
}
