package com.example.demo.service;

import com.example.demo.GlobalVariables;
import com.example.demo.VO.AddressVO;
import com.example.demo.domain.Address.Address;
import com.example.demo.domain.Form.RecieveForm;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.MainRepository;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AddressService {

    @Autowired
    AddressRepository addressRepository;
    @Autowired
    GlobalVariables globalVariables;
    @Transactional(readOnly = true)
    public HashMap<String,Object> getAddressList(String userId) {

        List<Address> addresses = addressRepository.getListByUserId(userId);

        HashMap<String,Object> result = new HashMap<>();
        result.put("list",addresses);
        result.put("count",addresses.size());

        return result;
    }

    public void setAddress(AddressVO vo) {
        System.out.println("setAddress 진입");
        String userId = vo.getUserId();
        List<Address> addressList = vo.getAddressList();

        for (Address address: addressList) {
            address.setUSER_ID(userId);
            log.info(address.toString());
            if(address.getSEQ()==0){
                int MaxNo = addressRepository.getMaxSeq();
                log.info("MAX SEQ : "+MaxNo);
                address.setSEQ(MaxNo);
            }
            addressRepository.save(address);
        }

    }

    public void deleteAddress(AddressVO vo) {
        System.out.println("setAddress 진입");
        List<Integer> seqList = vo.getSeqList();

        for (Integer integer: seqList) {
            addressRepository.deleteById(integer);
        }
    }
}
