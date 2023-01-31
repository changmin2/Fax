import Vue from "vue";
import Vuex from "vuex";
Vue.use(Vuex);

import getters from "./getters";
import mutations from "./mutations";
import actions from "./actions";
import users from "./state/users";
import receives from "./state/receives";
import sends from "./state/sends";

export default new Vuex.Store({
  state: {
    headerDarkMode: false,

    // isLogin: false,
    // userInfo: {},
    isLoading: false,
    users: users,
    receives: receives,
    sends: sends,
  },

  getters,
  mutations,
  actions,
});
