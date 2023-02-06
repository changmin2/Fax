<template>
  <div id="app">
    <i class="menu-icon fa fa-bars" aria-hidden="true" style="display: none"></i>
    <router-view name="header" :userInfo="userInfo"></router-view>
    <router-view v-if="!isLogin"></router-view>
    <router-view v-else-if="userInfo.grade == 0" name="admin"></router-view>
    <div v-else style="display: flex">
      <div class="main-left">
        <router-view name="navbar"></router-view>
      </div>
      <div class="main-right">
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
      userInfo: "getUserInfo",
    }),
  },
  created() {},
  data() {
    return {};
  },

  methods: {
    test() {
      console.log(this.isLoading);
    },
    loginSuccess(userInfo) {
      console.log("userInfo:", userInfo);
      // this.isLogin = true;
      // this.userInfo = userInfo;
      this.$store.commit("SET_USER_LOGIN");
      this.$store.commit("SET_USER_INFO");
    },
  },
};
</script>

<style>
#app {
  cursor: default;
  display: flex;
  flex-direction: column;
  /* height: 200vh; */
}
.app-right-content {
  width: 90vw;
  /* background-color: #31708f; */
}
.main-container {
  margin-top: 6rem;
  margin-left: 4vw;
  margin-right: 4vw;
}
.main-left {
  width: 175px;
}
.main-right {
  width: 100%;
}
/* table css */
.fax-table {
  font-size: small;
  margin-top: 20px;
}

.fax-table th,
.fax-table td {
  text-align: center;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  height: 2.5rem;
  /* height: 35px; */
  /* line-height: 13px; */
}
.fax-table th {
  background-color: rgb(224, 224, 224);
  font-weight: 600;
  border-bottom: none;
  height: 3rem;
  border: none;
  padding-left: 1rem;
  padding-right: 1rem;
}
.fax-table td {
  /* width: 250px; */
  /* border: 0.0625rem solid #dee2e6; */
}

.fax-table-detail {
  margin-top: 2px;
  margin-bottom: 1rem;
}
.fax-table-detail th,
.fax-table-detail td {
  height: 35px;
}

/* 캘린더 input  */
.fax-form-input {
  border: 1px solid #cad1d7;
  border-radius: 0.25rem;
  padding: 0.2rem;
  text-align: center;
  color: #525f7f;
}

/* 테이블 위 input */
.fax-table-input {
  border-top: 1px solid #cad1d7;
  border-radius: 0.25rem;
  text-align: center;
}
select {
  border: 1px solid #cad1d7;
  border-radius: 0.25rem;
  height: 35px;
  width: 150px;
  color: #525f7f;
}

.ajs-message .ajs-custom {
  color: #31708f;
  background-color: #d9edf7;
  border-color: #31708f;
}

.modal-container {
  height: inherit;
}

.modal-container-detail {
  display: flex;
}
.modal-big {
  max-width: 1400px;
}

.left-content {
  display: flex;
  flex-direction: column;
}

@media screen and (max-width: 991px) {
  .main-left {
    display: none;
  }
  .main-container {
    display: flex;
    flex-direction: column;
    justify-content: center;
  }
  .main-right {
    width: 100%;
  }

  .fax-table-display-none {
    display: none;
    /* width: 100%; */
  }
  .fax-table-display {
    width: 100%;
  }
}
</style>
