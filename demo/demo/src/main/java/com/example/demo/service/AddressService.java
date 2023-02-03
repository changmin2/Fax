package com.example.demo.service;

import com.example.demo.GlobalVariables;
import com.example.demo.domain.Address.Address;
import com.example.demo.domain.Form.RecieveForm;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.MainRepository;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
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
    public HashMap<String,Object> getAddessList(String userId) {

        List<Address> addresses = addressRepository.getListByUserId(userId);

        HashMap<String,Object> result = new HashMap<>();
        result.put("list",addresses);
        result.put("count",addresses.size());

        return result;
    }

    public HashMap<String,Object> setAddess(Address address) throws IOException, ParseException {

        log.info(address.toString());
        if(address.getSEQ()==0){
            int MaxNo = addressRepository.getMaxSeq();
            log.info("MAX SEQ : "+MaxNo);
            address.setSEQ(MaxNo);
        }
        addressRepository.save(address);

        HashMap<String,Object> result = new HashMap<>();
        result.put("address",address);
        return result;
    }

}
