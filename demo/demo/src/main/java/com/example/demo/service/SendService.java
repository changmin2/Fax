package com.example.demo.service;

import classes.Multipart.HttpPostMultipart;
import com.example.demo.GlobalVariables;
import com.example.demo.S3Uploader;
import com.example.demo.VO.SendReq;
import com.example.demo.VO.SendRes;
import com.example.demo.VO.ReSendRes;
import com.example.demo.domain.Approval.Approval;
import com.example.demo.domain.Send.Send;
import com.example.demo.domain.Send.Send_detail;
import com.example.demo.domain.User.User;
import com.example.demo.repository.ApprovalRepository;
import com.example.demo.repository.SendDRepository;
import com.example.demo.repository.SendRepository;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class SendService {

    private final  GlobalVariables globalVariables;
    private final UploadService uploadService;
    private final SendRepository sendRepository;
    private final SendDRepository sendDRepository;
    private final ApprovalRepository approvalRepository;
    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;
    @Transactional
    public String sendInsert(SendReq req) throws ParseException, IOException {
        String Date_End = globalVariables.getNow();
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
            return "결재 신청 완료";
        }else{ //전결일때
            sendRepository.save(send);
            return apprUpOrSend(req.getUserKey(),req.getUserID());
        }

    }
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
        if(send.getRESERVE_YN().equals("Y")){ //예약전송시 입력
            multipart.addFormField("Send_Date",send.getSEND_DATE());
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
        List<Send_detail> sendDetail = sendDRepository.findByIdUserKey(userKey);
        JSONArray DestArr = new JSONArray();
        for (Send_detail dest:sendDetail) {
            log.info("수신처 :"+dest.toString());
            JSONObject Dest1 = new JSONObject();
            Dest1.put("Company",dest.getRECEIVE_COMPANY());
            Dest1.put("Name",dest.getRECEIVE_NAME());
            Dest1.put("Fax",dest.getRECEIVE_FAX_NO());
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
            if (send.getRESERVE_YN().equals("N")) { //예약전송아닐때 오늘날짜
                String Date_End = globalVariables.getNow();
                send.setSEND_DATE(Date_End);
            }
            send.setJOB_NO(res.getJob_No() + "");
            for (Send_detail dest:sendDetail) {
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
        log.info("팩스 발송 결과 : "+res.toString());

        //DB에 결과 저장
        String Result = res.getResult();

        if(Result.equals("OK")) {
            if (send.getRESERVE_YN().equals("N")) { //예약전송아닐때 오늘날짜
                String Date_End = globalVariables.getNow();
                send.setSEND_DATE(Date_End);
            }
            send.setJOB_NO(res.getJob_No() + "");
        }else {
            send.setERROR_MSG(res.getMessage());
        }
        send.setSTATUS(Result.equals("OK")? "전송완료" : "전송실패");
        sendRepository.save(send);

        String msg = Result.equals("OK")? "팩스 발송완료되었습니다.":res.getMessage();
        return msg;

    }

    public String reSend(String userKey) throws IOException{
        Send send = sendRepository.findById(userKey).get();
        return sendApi(send);
    }

    public String reSendJobNo(Map<String,String> map) throws IOException {
        Send send = sendRepository.findById(map.get("userKey")).get();
        if(map.get("Send_Date")!=null){
            send.setSEND_DATE(map.get("Send_Date"));
            send.setRESERVE_YN("Y");
        }
        if(map.get("Subject")!=null){
            send.setTITLE(map.get("Subject"));
        }

        return reSendApi(sendRepository.findById(map.get("userKey")).get(),map.get("ReSend_List"));
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
        return sendApi(send);
    }
}
