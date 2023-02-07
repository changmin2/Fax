<template>
  <div class="main-container">
    <div class="login-logo" style="display: flex; justify-content: center">
      <img src="img/brand/logo6.png" />
    </div>
    <card
      type="secondary"
      shadow
      header-classes="bg-white pb-5"
      body-classes="px-lg-5 py-lg-5"
      class="border-0 login-card"
    >
      <div class="display-4 text-left mb-4 font-weight-800 text-default">로그인</div>

      <form role="form">
        <base-input
          alternative
          class="mb-3"
          placeholder="사번"
          addon-left-icon="ni ni-email-83"
          v-model="userId"
        >
        </base-input>
        <base-input
          alternative
          type="password"
          placeholder="비밀번호"
          addon-left-icon="ni ni-lock-circle-open"
          v-model="userPassword"
          @keyup.enter="login"
        >
        </base-input>

        <div class="text-center">
          <base-button type="danger" class="my-4 col-12" @click="login">로그인</base-button>
        </div>
      </form>
    </card>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import http from "@/common/axios.js";
import alertify from "alertifyjs";
// import LoadingSpinner from "./LoadingSpinner.vue";
// alertify.set('notifier','position', 'top-center');
export default {
  components: {
    // LoadingSpinner,
  },
  computed: {
    ...mapGetters({
      isLoading: "getIsLoading",
    }),
  },
  data() {
    return {
      userId: "",
      userPassword: "",
    };
  },

  methods: {
    async login() {
      if (this.userId == "") {
        alertify.error("사번을 입력해주세요.", 1.5);
        return;
      }
      if (this.userPassword == "") {
        alertify.error("비밀번호를 입력해주세요.", 1.5);
        return;
      }
      this.$store.commit("SET_LOADING_TRUE");
      try {
        let response = await http.post("/login", {
          userId: this.userId.toUpperCase(),
          userPassword: this.userPassword.toUpperCase(),
        });

        let { data } = response;
        this.$store.commit("SET_LOADING_FALSE");

        if (data.flag == true) {
          // 로그인 성공
          this.$store.commit("SET_USER_LOGIN", { isLogin: true });
          this.$store.dispatch("getUserInfo");
          // this.$store.commit("SET_USER_INFO", { userId: this.userId,userName: data.userInfo.user_NAME });
          this.$router.push("/");
          alertify.success("로그인이 완료되었습니다.", 1.5);
        } else {
          // 로그인 실패
          alertify.error("로그인에 실패했습니다.", 1.5);
        }
      } catch (error) {
        this.$store.commit("SET_LOADING_FALSE");
        console.log(error);
        console.log("에러1");
      }
    },
  },

  created() {
    if (localStorage.getItem("isLogin") == true) {
      //로그인 돼있으면 메인으로
      this.$store.dispatch("getUserInfo");
      this.$router.push("/");
    }
  },
};
</script>

<style scoped>
.login-section::before {
  content: "";
  /* background-image: url("../../public/img/bg3.jpg"); */
  background-repeat: no-repeat;
  background-size: cover;
  background-position: center;
  opacity: 0.7;
  position: absolute;
  top: 0px;
  left: 0px;
  right: 0px;
  bottom: 0px;
}
.main-container {
  display: flex;
  flex-direction: column;
  /* justify-content: center; */
  align-items: center;
  /* max-width: 100vw; */
  /* width: 100%; */
  /* margin-top: 8rem; */
}
.login-logo {
  width: 20rem;
  margin-bottom: 1rem;
}
.login-logo img {
  width: 100%;
}
.login-card {
  width: 30vw;
}

@media screen and (max-width: 991px) {
  .main-container {
    /* width: 100%; */
    /* margin-left: 0px; */

    margin-top: 8rem;
  }
  .login-card {
    /* width: 40%; */
    width: 80vw;
    margin: 0px;
    /* margin-right: 0.5rem; */
  }
  .login-logo {
    width: 50%;
  }
}
</style>
