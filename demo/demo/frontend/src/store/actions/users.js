
import http from "@/common/axios.js";
import router from '../../routers/router';
export default {
    async getUserInfo({commit, state}) {
        // console.log('getUserInfo진입');
        try {
          let response = await http.post("/getUserInfo", {
          });
          let { data } = response;
        //   console.log("getUserInfo :",data);
  
          if (data.flag == true) {
            let userInfo = data.userInfo;
            // 유저정보 받아오기
            commit("SET_USER_LOGIN", { isLogin: true });
            commit("SET_USER_INFO", { userId: userInfo.USER_ID,userName: userInfo.USER_NAME,
              faxNo: userInfo.FAX_NO,grade: userInfo.GRADE_CODE, deptName:userInfo.DEPT_NAME });
          } else {
            // 세션값 없음
            // console.log(data.message);
            localStorage.setItem("isLogin",false);
            state.isLogin = false;
            router.push("/login");
            alertify.error("로그인 만료되었습니다.\n다시 로그인해주세요.", 1.5);
          }
        } catch (error) {
          localStorage.setItem("isLogin",false);
          state.isLogin = false;
          console.log(error);
        //   console.log("에러2");
          router.push("/login");
        }
    },
};
