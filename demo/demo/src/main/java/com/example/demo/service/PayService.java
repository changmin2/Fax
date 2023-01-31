package com.example.demo.service;

import com.example.demo.GlobalVariables;
import com.example.demo.VO.SendRes;
import com.example.demo.domain.Approval.Approval;
import com.example.demo.domain.Form.ApprovalForm;
import com.example.demo.domain.Send.Send;
import com.example.demo.domain.User.User;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.UserTokenHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayService {

    private final ApprovalRepository approvalRepository;
    private final UserRepository userRepository;
    private final SendRepository sendRepository;
    private final SendService sendService;
    private final GlobalVariables globalVariables;

    //결제 수신 리스트
    public List<HashMap<String,Object>> apprList(String userId,String status){
        List<Object[]> test = new ArrayList<>();
        if(status.equals("전체")){ //결재함-전체
            approvalRepository.recieveAll(userId);
        }else {
            approvalRepository.recieve(userId,status);
        }
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

    public List<HashMap<String,Object>> apprDetail(String apprNo) {
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

    @Transactional
    public String apprOk(String apprNo) throws IOException {

        Approval find = approvalRepository.findById(apprNo).get();
        find.setSTATUS("완료");
        String Date_End = globalVariables.getNow();
        find.setAPPR_DATE(Date_End);

        return apprUpOrSend(find.getUSER_KEY(),find.getAPPR_PERSON());
    }

    //결재 완료하거나 전결했을때 send할지 지점장에게 결재 올릴지
    @Transactional
    public String apprUpOrSend(String userKey,String userId) throws IOException {
        Send send = sendRepository.findById(userKey).get();
        //결재자 정보 가져오기
        User user = userRepository.findById(userId).get();
        //GRADE_CODE:  3 지점장
        if(send.getPRIVATE_INFO_YN().equals("Y") && user.getGRADE_CODE()<3){ //개인정보 포함이면 지점장에게 결재
            Object[] apprUser = userRepository.getHigherApprUser(userId).get(0);
            //더 높은 결재자 정보
            String apprUSER_ID = (String) apprUser[0];
            String apprUSER_NAME = (String) apprUser[1];
            String apprCOMM_NAME = (String) apprUser[2];

            //결재 테이블 insert
            int i = approvalRepository.getMaxApprNo(send.getUSER_KEY()); //seq따기
            log.info("결재 seq : "+i);
            Approval approval = new Approval();
            approval.setAPPR_NO(send.getUSER_KEY()+i);
            approval.setAPPR_PERSON(apprUSER_ID);
            approval.setUSER_KEY(send.getUSER_KEY());
            approval.setPRIVATE_INFO_YN(send.getPRIVATE_INFO_YN());
            approval.setUSER_NO(send.getUSER_NO());
            approval.setSTATUS("대기");
            approvalRepository.save(approval);

            //SEND 테이블  update
            send.setAPPR_NO(approval.getAPPR_NO());
            send.setAPPR_USER_NO(approval.getAPPR_PERSON());

            return  "개인정보 포함 문서로, "+apprUSER_NAME+"("+apprCOMM_NAME+")에게 결재요청되었습니다.";
        }

        //아니면 팩스전송
        //SEND 테이블  update
        send.setSTATUS("결재완료");
        sendRepository.save(send);
        //발송시작
        return sendService.sendApi(send);
    }

    @Transactional
    public String apprReturn(String apprNo,String apprRemark){
        log.info("apprReturn접근");
        Approval find = approvalRepository.findById(apprNo).get();
        find.setSTATUS("반려");
        find.setAPPR_REMARK(apprRemark); //사유등록

        String Date_End = globalVariables.getNow();
        log.info("Date_End : "+Date_End);
        find.setAPPR_DATE(Date_End);

        //SEND 테이블에도 결재정보 update
        Send send = sendRepository.findById(find.getUSER_KEY()).get();
        send.setSTATUS("반려");
        sendRepository.save(send);

        log.info("apprReturn성공");
        return "반려처리 되었습니다.";
    }

    public List<HashMap<String,Object>> sendRecieve(String userId){
        List<Object[]> objects = approvalRepository.sendRecieve(userId);
        List<HashMap<String,Object>> lists = new ArrayList<>();
        String[] arr = {"발송번호","상태","전송일자","제목","팩스번호","발송자","등록일자","결재상태","결재자ID","사유","결재자이름","실패메세지"};

        for (Object[] objArr : objects) {
            int idx =-1;
            HashMap<String,Object> map = new HashMap<>();
            for (Object obj : objArr) {
                // 형 변환 후 출력
                idx+=1;
                map.put(arr[idx],obj);
            }
            lists.add(map);
        }
        log.info(lists.toString());
        return lists;
    }


    public List<HashMap<String,Object>> sendRecieveDetail(String userKey) {
        //보내는 사람 정보
        List<Object[]> detail = approvalRepository.detail2(userKey);
        List<Object[]> total = approvalRepository.totalDetail2(userKey);
        String[] arr2 =  {"발송번호","상태","전송일자","예약여부","개인정보보호여부","제목","등록일자","발송자","결재상태","결재자ID","사유","결재자이름","결재일자", "팩스번호","파일명"};
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
    //회수
    @Transactional
    public void withdraw(String userKey){
        Approval appr = approvalRepository.findAppr(userKey);
        appr.setSTATUS("회수");
        appr.setAPPR_DATE(globalVariables.getNow());
        Send send = sendRepository.findById(userKey).get();
        send.setSTATUS("회수");
        send.setAPPR_NO("");
        send.setAPPR_USER_NO("");
    }
    //회수 -> 삭제
    @Transactional
    public void apprNoDelete(String apprNo) {
        approvalRepository.deleteApproNo(apprNo);
    }

    //회수->수정
    public Send sendInfoFind(String userKey){
        return sendRepository.findById(userKey).get();
    }

    //회수 -> 삭제 -> 사용여부 변경
    @Transactional
    public void updateUseGbn(String userKey) {
        sendRepository.findById(userKey).get().setUSE_GBN("N");
        approvalRepository.findAppr(userKey).setUSE_GBN("N");
    }
}
