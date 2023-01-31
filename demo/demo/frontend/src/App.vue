<template>
  <div id="app">
    <!-- include a theme -->
    <router-view name="header" :userInfo="userInfo"></router-view>
    <router-view v-if="!isLogin"></router-view>
    <div v-else class="row">
      <div class="col-lg-2 col-md-2 col-sm-2">
        <router-view name="navbar"></router-view>
      </div>
      <div class="col-lg-10 col-md-9 col-sm-10">
        <main>
          <fade-transition origin="center" mode="out-in" :duration="250">
            <router-view @login-success="loginSuccess"></router-view>
          </fade-transition>
        </main>
      </div>
    </div>
    <!-- <button @click="test">test</button> -->
    <router-view name="footer"></router-view>
    <router-view v-if="isLoading" name="spinner"></router-view>
  </div>
</template>

<script>
import { FadeTransition } from "vue2-transitions";
import { mapGetters } from "vuex";
import LoadingSpinner from "./views/LoadingSpinner.vue";

export default {
  components: {
    FadeTransition,
    LoadingSpinner,
  },
  computed: {
    ...mapGetters({
      isLoading: "getIsLoading",
      isLogin: "getIsLogin",
    }),
  },
  created() {},
  data() {
    return {
      userInfo: {},
    };
  },

  methods: {
    test() {
      console.log(this.isLoading);
    },
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
