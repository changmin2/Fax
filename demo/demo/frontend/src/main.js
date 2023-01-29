import Vue from "vue";
import App from "./App.vue";

import router from "./routers/router";
import store from "./store/store";

import Argon from "./plugins/argon-kit";
import "./registerServiceWorker";

import Datetime from 'vue-datetime';

Vue.config.productionTip = false;

Vue.use(Datetime);
Vue.use(Argon);

new Vue({
  render: (h) => h(App),
  router,
  store,
}).$mount("#app");
