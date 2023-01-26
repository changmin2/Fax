export default {
  getUserInfo(state) {
    return state.users.userInfo;
  },
  getIsLogin(state) {
    return state.users.isLogin;
  },
  getUserKey(state) {
    return state.users.userKey;
  },
  getIsAdminUser(state) {
    return state.userInfo.code === "001" ? true : false;
  },
};
