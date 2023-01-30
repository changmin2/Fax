<template>
  <div id="app">
    <!-- include a theme -->
    <router-view name="header" :userInfo="userInfo"></router-view>
    <div class="row">
      <div class="col-lg-2">
        <router-view name="navbar"></router-view>
      </div>
      <div class="col">
        <main>
          <fade-transition origin="center" mode="out-in" :duration="250">
            <router-view @login-success="loginSuccess" />
          </fade-transition>
        </main>
      </div>
    </div>

    <router-view name="footer"></router-view>
  </div>
</template>

<script>
import { FadeTransition } from "vue2-transitions";

export default {
  components: {
    FadeTransition,
  },
  created() {},
  data() {
    return {
      isLogin: false,
      userInfo: {},
    };
  },

  methods: {
    loginSuccess(userInfo) {
      console.log("userInfo:", userInfo);
      this.isLogin = true;
      this.userInfo = userInfo;
    },
  },
};
</script>

<style>
#app {
  cursor: default;
}

.ajs-message .ajs-custom {
  color: #31708f;
  background-color: #d9edf7;
  border-color: #31708f;
}
</style>
