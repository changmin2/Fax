package com.example.demo.VO;

import com.example.demo.domain.Address.Address;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
@Data
public class AddressVO {
    @JsonProperty("addressList")
    private List<Address> addressList;
    private String userId;
    private List<Integer> seqList;
    @Override
    public String toString() {
        return "AddressVO{" +
                "addressList=" + addressList +
                ", userId='" + userId + '\'' +
                '}';
    }
}
