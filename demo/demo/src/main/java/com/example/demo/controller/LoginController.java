package com.example.demo.controller;

import com.example.demo.SessionManager;
import com.example.demo.domain.User.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(
        // localhost:5500 과 127.0.0.1 구분
        origins = "http://localhost:5500", // allowCredentials = "true" 일 경우, orogins="*" 는 X
        allowCredentials = "true",
        allowedHeaders = "*",
        methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PUT,RequestMethod.HEAD,RequestMethod.OPTIONS}
)
@Controller
@RequestMapping("/api")
@Slf4j
public class LoginController {

    @Autowired
    UserService userService;
    @Autowired
    SessionManager sessionManager;

    //로그인
    @PostMapping("/login")
    @ResponseBody
    public HashMap<String,Object> login(@RequestBody Map<String, String> member,
                                        HttpServletRequest request, HttpServletResponse response){
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
        HashMap<String, Object> re = new HashMap<>();
        Map<String,Object> result = userService.login(member.get("userId"), member.get("userPassword"));

        //로그인 실패시
        if(result==null){
            re.put("flag",false);
            re.put("message","로그인에 실패하셨습니다.");
            return re;
        }
        //로그인 성공시 세션 생성
        sessionManager.createSession(result,response);
        re.put("flag",true);
        re.put("message","로그인에 성공하셨습니다.");
//        stopWatch.stop();
//        log.info("로그인 총 시간"+stopWatch.prettyPrint());
        return re;
    }
    @PostMapping("/getUserInfo")
    @ResponseBody
    public HashMap<String,Object> getUserInfo(HttpServletRequest request){
        HashMap<String, Object> re = new HashMap<>();

        //로그인 성공시 세션 생성
        Object findUser = sessionManager.getSession(request);

        //세션값없을때
        if(findUser==null){
            re.put("flag",false);
            re.put("message","유저정보를 불러올 수 없습니다.");
            return re;
        }

        re.put("flag",true);
        re.put("message","OK");
        re.put("userInfo",findUser);
        return re;
    }
    //로그아웃
    @PostMapping("/logout")
    @ResponseBody
    public String logout(HttpServletRequest request){
        sessionManager.expire(request);
        return "로그아웃성공";
    }
    @PostMapping("/getApprUsers")
    @ResponseBody
    public List<HashMap<String, String>> getApprUsers(@RequestBody Map<String,String> map){
        String userId = map.get("userId"); //유저아이디
        if(userId==null){return null;}
        List<HashMap<String, String>> users = userService.getApprUsers(userId);
        return users;
    }
    @PostMapping("/getSubstituteUser")
    @ResponseBody
    public HashMap<String, Object> getSubstituteUser(@RequestBody Map<String,String> map){
        String userId = map.get("userId"); //유저아이디
        System.out.println("대체자 불러오기"+userId);
        HashMap<String, Object> result = userService.getSubstituteUser(userId);
        return result;
    }

    @PostMapping("/setAbsence")
    @ResponseBody
    public HashMap<String, Object> setAbsence(@RequestBody Map<String,String> map){
        String userId = map.get("userId"); //유저아이디
        String substitute = map.get("substitute"); //부재여부 Y일경우, 대체자 아이디 / N이면 ""
        String isAbsence = map.get("isAbsence"); //부재여부 (Y/N)
        HashMap<String, Object> result = userService.setAbsence(userId,substitute,isAbsence);
        return result;
    }
    @PostMapping("/getUserList")
    @ResponseBody
    public List<Map<String, Object>> getUserList(){
        List<Map<String, Object>> users = userService.getUserList();
        return users;
    }

    @PostMapping("/getDeptList")
    @ResponseBody
    public Map<String, Object> getDeptList(){
        Map<String, Object> users = userService.getDeptList();
        return users;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/userUpdate")
    @ResponseBody
    public String userUpdate(@RequestBody User param){
        String result = userService.userUpdate(param);
        return result;
    }
}
