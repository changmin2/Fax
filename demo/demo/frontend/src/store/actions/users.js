
import http from "@/common/axios.js";
import router from '../../routers/router';
export default {
    async getUserInfo({commit, state}) {
        console.log('getUserInfo진입');
        try {
          let response = await http.post("/getUserInfo", {
          });
          let { data } = response;
          console.log("getUserInfo :",data);
  
          if (data.flag == true) {
            console.log('잘받아왔어요');
            console.log(state.isLogin);
            // 로그인 성공
            commit("SET_USER_LOGIN", { isLogin: true });
            // commit("SET_USER_LOGIN", { isLogin: true });
            // commit("SET_USER_INFO", { userId: data.userInfo.user_ID,userName: data.userInfo.user_NAME });
            commit("SET_USER_INFO", { userId: data.userInfo.user_ID,userName: data.userInfo.user_NAME });
            router.push("/");
            // sessionStorage.setItem("isLogin", true);
            // sessionStorage.setItem("userId", this.userId);
            // sessionStorage.setItem("userName", this.userId);
          } else {
            // 로그인 실패
            console.log(data.message);
            localStorage.setItem("isLogin",false);
            state.isLogin = false;
            router.push("/login");
            alertify.error("로그인 만료되었습니다. 다시 로그인해주세요.", 1.5);
          }
        } catch (error) {
          localStorage.setItem("isLogin",false);
          state.isLogin = false;
          console.log(error);
          console.log("에러2");
        }
    },
};
