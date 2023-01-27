package com.example.demo.service;

import com.example.demo.domain.Approval.Approval;
import com.example.demo.domain.Form.ApprovalForm;
import com.example.demo.repository.ApprovalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayService {

    private final ApprovalRepository approvalRepository;

    //결제 수신 리스트
    public List<HashMap<String,Object>> apprList(@RequestParam("userId")String userId){
        List<Object[]> test = approvalRepository.recieve(userId);
        List<HashMap<String,Object>> lists = new ArrayList<>();
        String[] arr = { "결제고유번호","상태","보내는사람","결제일자","받는사람","제목","팩스번호","요청일자"};

        for (Object[] objArr : test) {
            int idx =-1;
            HashMap<String,Object> map = new HashMap<>();
            for (Object obj : objArr) {
                // 형 변환 후 출력
                idx+=1;
                map.put(arr[idx],obj);
            }
            lists.add(map);
        }
        return lists;
    }

    public List<HashMap<String,Object>> apprDetail(@RequestParam("apprNo")String apprNo) {
        //보내는 사람 정보
        List<Object[]> detail = approvalRepository.detail(apprNo);
        List<Object[]> total = approvalRepository.totalDetail(apprNo);
        String[] arr2 =  {"결재고유번호","받는사람ID","보내는사람ID","상태","개인정보보호여부","발송고유번호","결재일자","사유","보내는사람","받는사람","제목","팩스번호","요청일자"};
        List<HashMap<String,Object>> lists = new ArrayList<>();
        List<HashMap<String,Object>> lists2 = new ArrayList<>();

        String[] arr = {"이름","상호","팩스번호"};
        for (Object[] objArr : detail) {
            int idx =-1;
            HashMap<String,Object> map = new HashMap<>();
            for (Object obj : objArr) {
                // 형 변환 후 출력
                idx+=1;
                map.put(arr[idx],obj);
            }
            lists2.add(map);
        }
        for (Object[] objArr : total) {
            int idx =-1;
            HashMap<String,Object> map = new HashMap<>();
            for (Object obj : objArr) {
                // 형 변환 후 출력
                idx+=1;
                map.put(arr2[idx],obj);
            }
            map.put("받는사람정보",lists2);
            lists.add(map);
        }

        log.info(lists.toString());
        return lists;
    }


}