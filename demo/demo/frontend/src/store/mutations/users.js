export default {
  /* 사용자 로그인 */
  SET_USER_LOGIN(state) {
    state.users.isLogin = true;
    localStorage.setItem("isLogin",true);
  },

  /* 로그인한 사용자 정보 받기 */
  SET_USER_INFO(state, payload) {
    state.users.userInfo = payload;
  },

  /* USER KEY 초기화 */
  SET_USER_KEY_INIT(state) {
    state.users.userKey = "None";
  },

  /* USER KEY 정보 설정 */
  SET_USER_KEY(state, payload) {
    state.users.userKey = payload;
  },
};
