import Vue from "vue";
import Vuex from "vuex";
Vue.use(Vuex);

import getters from "./getters";
import mutations from "./mutations";
import actions from "./actions";
import users from "./getters/users";
import receives from "./state/receives";

export default new Vuex.Store({
  state: {
    headerDarkMode: false,

    // isLogin: false,
    // userInfo: {},
    users: users,
    receives: receives,
  },

  getters,
  mutations,
  actions,
});
