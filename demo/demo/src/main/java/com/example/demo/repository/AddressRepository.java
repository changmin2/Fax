package com.example.demo.repository;

import com.example.demo.domain.Address.Address;
import com.example.demo.domain.Send.Send;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    @Query(value= "select IFNULL(max(t.SEQ),0)+1 from TB_ADDRESS t", nativeQuery = true)
    int getMaxSeq();
    @Query(value= "select t.* from TB_ADDRESS t where USER_ID = :userId", nativeQuery = true)
    List<Address> getListByUserId(@Param(value = "userId")String userId);

}
