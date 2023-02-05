package com.example.demo.service;

import classes.Multipart.HttpPostMultipart;
import com.example.demo.GlobalVariables;
import com.example.demo.S3Uploader;
import com.example.demo.VO.*;
import com.example.demo.domain.Approval.Approval;
import com.example.demo.domain.Send.Send;
import com.example.demo.domain.Send.Send_detail;
import com.example.demo.domain.Send.Send_detailPK;
import com.example.demo.domain.Upload.Upload;
import com.example.demo.domain.User.User;
import com.example.demo.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class SendService {
    // MemberRepository2.java
    private final EntityManager em;

    private final  GlobalVariables globalVariables;
    private final UploadService uploadService;
    private final SendRepository sendRepository;
    private final SendDRepository sendDRepository;
    private final ApprovalRepository approvalRepository;
    private final UploadRepository uploadRepository;
    private final UserRepository userRepository;
    private final SmsService smsService;
    private final S3Uploader s3Uploader;
    @Transactional
    public String sendInsert(SendReq req) throws ParseException, IOException {
        String Date_End = globalVariables.getNow();
        String userKey = req.getUserKey();
        if(req.getReserve_yn().equals("Y")){ //예약전송일때
            String sendDate = req.getSend_Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
            Date date = sdf.parse(sendDate);
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            sendDate = sdf.format(date);
            req.setSend_Date(sendDate);
        }
        Send send = new Send(req);
        send.setINSERT_DATE(Date_End);
        send.setSTATUS("결재대기");
        send.setUSE_GBN("Y");
        send.setPAGE_CNT(req.getPageCount()+"");
        List<SendReq.Destination> DestinationList = req.getDestinationList();
        int index = 1;
        for (SendReq.Destination dest:DestinationList) {
            //발송처 디테일 저장
            Send_detail sendD = new Send_detail(dest,req.getUserKey(),index++);
            sendDRepository.save(sendD);
        }

        if(!req.getAppr_person().equals("")){
            //결재 max값 가져오기
            int i = approvalRepository.getMaxApprNo(req.getUserKey());
            //결재 저장
            Approval approval = new Approval(req,i);
            approvalRepository.save(approval);

            //발송정보 저장
            send.setAPPR_NO(approval.getAPPR_NO());
            sendRepository.save(send);

            //SMS 보내기
            String userName = userRepository.getUserName(userKey);
            String phoneN = approvalRepository.getPhoneNumber(userKey);
            smsService.smsSend(req.getTitle()+" 건으로\n"+userName+"님의 결제 요청이 왔습니다.",phoneN);
            return "결재 신청 완료";

        }else{ //전결일때
            sendRepository.save(send);
            return apprUpOrSend(req.getUserKey(),req.getUserID());
        }

    }
    //그냥 발송
    public String sendApi(Send send) throws IOException{
        //gbn : 1 재전송 , 0 : 처음 전송

        ////////////////////////////////////////////
        //[팩스 - 발송 요청]
        ////////////////////////////////////////////
        // 헤더값 설정
        Map<String, String> headers = new HashMap<>();
        HttpPostMultipart multipart = new HttpPostMultipart("https://balsong.com/Linkage/API/", "utf-8", headers);

        //데이터 (요청변수 대소문자 구분)
        multipart.addFormField("UserID", globalVariables.getFaxId());
        multipart.addFormField("UserPW", globalVariables.getFaxPw());
        multipart.addFormField("Service", globalVariables.getService());
        multipart.addFormField("Type", "Send");
        multipart.addFormField("Subject",send.getTITLE());
        if(send.getRESERVE_YN().equals("Y")){ //예약전송시 입력
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String nowDateTime = df.format(now.getTime());
            Date d1 = null;
            Date d2 = null;

            try {
                d1 = df.parse(nowDateTime);
                d2 = df.parse(send.getSEND_DATE());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // d2은 d1보다 이전이다. (true)
            // 예약일이 지금 이순간 이전이면 즉시전송
            if(d2.before(d1)){
                multipart.addFormField("Send_Date","");
                send.setSEND_DATE("");
                log.info("예약일이 지금 이전이어서 즉시전송");
            }else{
                multipart.addFormField("Send_Date",send.getSEND_DATE());
            }
        }else{
            multipart.addFormField("Send_Date","");
        }

        String userKey = send.getUSER_KEY();
        //파일명 가져오기
        String fileName = uploadService.getFileName(userKey);
        String filePath = globalVariables.getFilePath();
        //PDF1 -> 서버파일
        File file = s3Uploader.download(fileName);
        multipart.addFilePart("PDF1", file);

        //파일첨부 후 삭제
        s3Uploader.removeNewFile(file);

        // 데이터 - 수신처
        List<Map<String,Object>> sendDetail = sendDRepository.findByAllByUserKey(userKey);
        JSONArray DestArr = new JSONArray();
        for (Map<String,Object> temp:sendDetail) {
            JSONObject Dest1 = new JSONObject();
            Dest1.put("Company",temp.get("RECEIVE_COMPANY"));
            Dest1.put("Name",temp.get("RECEIVE_NAME"));
            Dest1.put("Fax",temp.get("RECEIVE_FAX_NO"));
            DestArr.add(Dest1);
        }
        multipart.addFormField("Destination", DestArr.toString());

        // 응답 값
        String ResultJson = multipart.finish();

        ObjectMapper mapper = new ObjectMapper();
        SendRes res = mapper.readValue( ResultJson, SendRes.class);
        log.info("팩스 발송 결과 : "+res.toString());

        //DB에 결과 저장
        String Result = res.getResult();

        if(Result.equals("OK")) {
            if (send.getSEND_DATE().equals("")) { //예약전송아닐때 오늘날짜
                String Date_End = globalVariables.getNow();
                send.setSEND_DATE(Date_End);
            }
            send.setJOB_NO(res.getJob_No() + "");
            for (Map<String,Object> temp:sendDetail) {
                Send_detail dest = sendDRepository.findById(new Send_detailPK(userKey, (int) temp.get("USER_SEQ"))).get();
                log.info(dest.toString());
                dest.setJOB_NO(res.getJob_No() + "");
                sendDRepository.save(dest);
            }
        }else {
            send.setERROR_MSG(res.getMessage());
        }
        send.setSTATUS(Result.equals("OK")? "전송완료" : "전송실패");
        sendRepository.save(send);

        String msg = Result.equals("OK")? "팩스 발송완료되었습니다.":res.getMessage();
        return msg;
    }

    //재전송 전용 api
    public String reSendApi(Send send,String ReSend_List) throws IOException {
        Map<String, String> headers = new HashMap<>();
        HttpPostMultipart multipart = new HttpPostMultipart("https://balsong.com/Linkage/API/", "utf-8", headers);

        //데이터 (요청변수 대소문자 구분)
        multipart.addFormField("UserID", globalVariables.getFaxId());
        multipart.addFormField("UserPW", globalVariables.getFaxPw());
        multipart.addFormField("Service", globalVariables.getService());
        multipart.addFormField("Type", "ReSend");
        multipart.addFormField("Send_Date","");
        if(send.getRESERVE_YN().equals("Y")){ //예약전송시 입력
            multipart.addFormField("Send_Date",send.getSEND_DATE());
        }
        multipart.addFormField("Job_No", send.getJOB_NO());
        multipart.addFormField("Subject",send.getTITLE());
        multipart.addFormField("ReSend_List",ReSend_List);

        // 응답 값
        String ResultJson = multipart.finish();

        ObjectMapper mapper = new ObjectMapper();
        ReSendRes res = mapper.readValue( ResultJson, ReSendRes.class);
        log.info("팩스 재발송 결과 : "+res.toString());

        //DB에 결과 저장
        String Result = res.getResult();

        if(Result.equals("OK")) {
//            if (send.getRESERVE_YN().equals("N")) { //예약전송아닐때 오늘날짜
                String Date_End = globalVariables.getNow();
                send.setSEND_DATE(Date_End);
//            }
            send.setJOB_NO(res.getJob_No() + "");
        }else {
            send.setERROR_MSG(res.getMessage());
        }
        send.setSTATUS(Result.equals("OK")? "전송완료" : "전송실패");
        sendRepository.save(send);

        String msg = Result.equals("OK")? "팩스 재전송 완료되었습니다.":res.getMessage();
        return msg;

    }

    public String reSend(String userKey) throws IOException{
        Send send = sendRepository.findById(userKey).get();
        return sendApi(send);
    }
    @Transactional
    public String reSendJobNo(Map<String,String> map) throws IOException {

        String userKey = map.get("userKey");
        Send send = sendRepository.findById(userKey).get();
        String userId = send.getUSER_NO();
        String newUserKey = globalVariables.createKey(userId);
        em.detach(send);
        send.setUSER_KEY(newUserKey);
        em.persist(send);

        List<Map<String,Object>> sendDetail = sendDRepository.findByAllByUserKey(map.get("userKey"));
        for (Map<String,Object> temp:sendDetail) {
            Send_detail dest = sendDRepository.findById(new Send_detailPK(userKey, (int) temp.get("USER_SEQ"))).get();
            em.detach(dest);
            dest.setId(new Send_detailPK(newUserKey,(int) temp.get("USER_SEQ")));
            em.persist(dest);
            dest.setJOB_NO(null);
            dest.setDONE_DATE(null);
            dest.setSTATUS_DETAIL(null);
            dest.setSTATUS(null);
            dest.setJOB_SEQ(null);
        }

        String fileName = uploadService.getFileName(userKey);
        Upload upload = new Upload();
        upload.setUserKey(newUserKey);
        upload.setRealFileName(fileName);
        upload.setUserFileName(userKey+"_resend");
        uploadRepository.save(upload);


        if(map.get("Send_Date")!=null){
            send.setSEND_DATE("");
            send.setRESERVE_YN("N");
        }
        if(map.get("Subject")!=null){
            send.setTITLE(map.get("Subject"));
        }

        return reSendApi(send,map.get("ReSend_List"));
    }

    public String getJobNo(String userKey) {
        return sendRepository.findById(userKey).get().getJOB_NO();
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
            approval.setUSE_GBN("Y");
            approval.setSTATUS("대기");
            approvalRepository.save(approval);

            //SEND 테이블  update
            send.setAPPR_NO(approval.getAPPR_NO());
            send.setAPPR_USER_NO(approval.getAPPR_PERSON());

            //SMS(문자) 보내기
            String userName = userRepository.getUserName(userKey);
            String phoneN = approvalRepository.getPhoneNumber(userKey);
            smsService.smsSend(send.getTITLE()+" 개인정보 포함 건으로\n"+userName+"님의 결제 요청이 왔습니다.",phoneN);

            return  "개인정보 포함 문서로, "+apprUSER_NAME+"("+apprCOMM_NAME+")에게 결재요청되었습니다.";
        }

        //아니면 팩스전송
        //SEND 테이블  update
        send.setSTATUS("결재완료");
//        sendRepository.save(send);
        //발송시작
        return sendApi(send);
    }

    //발송 상세 -> 결제 완료일 때 상세정보
    public Map<String,Object> sendDetail(String userKey) throws IOException {
        if(!sendRepository.existsById(userKey)){
            Map<String,Object> result = new HashMap<>();
            result.put("Result","fail");
            result.put("message","결과가 없습니다.");
            return result;
        }
        Send find = sendRepository.findById(userKey).get();
        return sendDetailApi(find);
    }

    //발송 상세정보 api
    public Map<String,Object> sendDetailApi(Send find) throws IOException {
        Map<String, String> headers = new HashMap<>();
        HttpPostMultipart multipart = new HttpPostMultipart("https://balsong.com/Linkage/API/", "utf-8", headers);
        Map<String,Object> result = new HashMap<>();

        //데이터 (요청변수 대소문자 구분)
        multipart.addFormField("UserID", globalVariables.getFaxId());
        multipart.addFormField("UserPW", globalVariables.getFaxPw());
        multipart.addFormField("Service", globalVariables.getService());
        multipart.addFormField("Type", "Report_Detail");
        multipart.addFormField("Job_No", find.getJOB_NO());
        multipart.addFormField("List_EA", "10");
        multipart.addFormField("Page", "1");

        // 응답 값
        String ResultJson = multipart.finish();

        ObjectMapper mapper = new ObjectMapper();
        SendDetailRes res = mapper.readValue( ResultJson, SendDetailRes.class);

        List<ETC> list = res.getList();
        int success = 0;
        int fail = 0;
        if (list != null){
            result.put("Result",res.getResult());
            result.put("Job_No",res.getJob_No());
            result.put("Subject",res.getSubject());
            result.put("Total_Cnt",res.getTotal_Cnt());
            result.put("Total_Page",res.getTotal_Page());
            result.put("Recives",list);
            for (ETC etc : list) {
                String status = etc.getStatus();
                if(status.equals("성공")) success++;
                else fail++;
            }
            result.put("sucess",success);
            result.put("fail",fail);
        } else{
            result.put("Result","fail");
            result.put("message","결과가 없습니다.");
        }

        return result;
    }

    @Transactional
    public void isNullSendDUser(List<ETC> etcs,String JobNo) {
        List<Send_detail> findSendD = sendDRepository.getSendD(JobNo);
        for (Send_detail send_detail : findSendD) {
            log.info(send_detail.toString());
            for (ETC etc : etcs) {
                if(etc.getName().equals(send_detail.getRECEIVE_NAME())){
                    if(etc.getDone_Date().isEmpty()) {
                        send_detail.setSTATUS_DETAIL("접수완료");
                        send_detail.setSTATUS(etc.getStatus_Detail());
                    }else{
                        send_detail.setSTATUS_DETAIL(etc.getStatus_Detail());
                        send_detail.setSTATUS(etc.getStatus());
                        send_detail.setJOB_SEQ(etc.getNo());
                        send_detail.setDONE_DATE(etc.getDone_Date());
                    }
                }
            }
        }
    }
}
