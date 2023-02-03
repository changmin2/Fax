<template>
  <header class="header-global">
    <base-nav
      v-if="userInfo.grade != 0"
      class="navbar-main mt-lg-0 mt-sm-3"
      transparent
      type=""
      effect="dark"
      expand
    >
      <router-link slot="brand" class="navbar-brand" to="/">
        <img :src="logoImage" alt="logo" />
      </router-link>

      <div class="row" slot="content-header">
        <div class="col-6 collapse-brand">
          <a href="https://demos.creative-tim.com/vue-argon-design-system/documentation/">
            <img src="img/brand/logo3.png" />
          </a>
        </div>
      </div>

      <!-- 왼쪽 -->
      <ul class="navbar-nav navbar-nav-hover align-items-lg-center">
        <li class="nav-item" menu-classes="dropdown-menu-xl ">
          <a slot="title" class="nav-link" data-toggle="dropdown" role="button">
            <i class="ni ni-ui-01 d-lg-none"></i>
            <router-link
              to="/send-list"
              :class="'nav-link-inner--text ml-0 font-weight-600 text-' + textColor"
            >
              보낸팩스함
            </router-link>
          </a>
        </li>
        <li class="nav-item" menu-classes="dropdown-menu-xl ">
          <a slot="title" class="nav-link" data-toggle="dropdown" role="button">
            <i class="ni ni-ui-01 d-lg-none"></i>
            <router-link
              to="/receive-list"
              :class="'nav-link-inner--text ml-0 font-weight-600 text-' + textColor"
            >
              받은팩스함
            </router-link>
          </a>
        </li>
        <li class="nav-item" menu-classes="dropdown-menu-xl ">
          <a slot="title" class="nav-link" data-toggle="dropdown" role="button">
            <i class="ni ni-ui-01 d-lg-none"></i>
            <router-link
              to="/send"
              :class="'nav-link-inner--text ml-0 font-weight-600 text-' + textColor"
            >
              팩스보내기
            </router-link>
          </a>
        </li>
      </ul>

      <!-- 오른쪽 -->
      <ul v-if="isLogin" class="navbar-nav align-items-lg-center ml-lg-auto">
        <span v-text="user_info"></span
        >님 반갑습니다.
        <a slot="title" class="nav-link" data-toggle="dropdown" role="button">
          <i class="fa fa-sign-out mr-0 d-lg-inline d-sm-none text-default"></i>
          <span
            @click="logout"
            :class="'nav-link-inner--text font-weight-600 text-' + textColor"
            style="cursor: pointer"
            >로그아웃</span
          >
          <i class="fa fa-user ml-3 mr-0 d-lg-inline d-sm-none text-default"></i>
          <router-link to="/mypage">
            <span
              :class="'nav-link-inner--text font-weight-600 text-' + textColor"
              style="cursor: pointer"
              >마이페이지</span
            >
          </router-link>
        </a>
      </ul>
      <ul v-else class="navbar-nav align-items-lg-center ml-lg-auto">
        <div class="text-center nav-item">
          <router-link to="/login">
            <base-button type="danger" :class="'my-3 col-12 text-binary'"> 로그인 </base-button>
          </router-link>
        </div>
      </ul>
    </base-nav>

    <!-- 관리자용 헤더 -->
    <base-nav v-else class="navbar-main mt-lg-0 mt-sm-3" transparent type="" effect="dark" expand>
      <router-link slot="brand" class="navbar-brand" to="/">
        <img :src="logoImage" alt="logo" />
      </router-link>

      <div class="row" slot="content-header">
        <div class="col-6 collapse-brand">
          <a href="https://demos.creative-tim.com/vue-argon-design-system/documentation/">
            <img src="img/brand/logo3.png" />
          </a>
        </div>
      </div>

      <!-- 왼쪽 -->
      <!-- <ul class="navbar-nav navbar-nav-hover align-items-lg-center">
        <li class="nav-item" menu-classes="dropdown-menu-xl ">
          <a slot="title" class="nav-link" data-toggle="dropdown" role="button">
            <i class="ni ni-ui-01 d-lg-none"></i>
            <router-link
              to="/user-list"
              :class="'nav-link-inner--text ml-0 font-weight-600 text-' + textColor"
            >
              <i class="fa fa-user mr-1 mr-0 d-lg-inline d-sm-none text-default"></i>
              사용자 관리
            </router-link>
          </a>
        </li>
      </ul> -->

      <!-- 오른쪽 -->
      <ul v-if="isLogin" class="navbar-nav align-items-lg-center ml-lg-auto">
        <badge class="m-0" style="width: 80px; font-size: small" type="yellow" rounded>
          관리자
        </badge>

        <a slot="title" class="nav-link" data-toggle="dropdown" role="button">
          <i class="fa fa-sign-out mr-0 d-lg-inline d-sm-none text-default"></i>
          <span
            @click="logout"
            :class="'nav-link-inner--text font-weight-600 text-' + textColor"
            style="cursor: pointer"
            >로그아웃</span
          >
        </a>
      </ul>
      <ul v-else class="navbar-nav align-items-lg-center ml-lg-auto">
        <div class="text-center nav-item">
          <router-link to="/login">
            <base-button type="danger" :class="'my-3 col-12 text-binary'"> 로그인 </base-button>
          </router-link>
        </div>
      </ul>
    </base-nav>
  </header>
</template>

<script>
import { mapGetters } from "vuex";
import http from "@/common/axios.js";
import BaseNav from "@/components/BaseNav";
import BaseDropdown from "@/components/BaseDropdown";
import CloseButton from "@/components/CloseButton";

export default {
  components: {
    BaseNav,
    CloseButton,
    BaseDropdown,
  },

  data() {
    return {};
  },

  computed: {
    ...mapGetters({
      userInfo: "getUserInfo",
    }),
    /* 다크모드 색 반전 */
    textColor() {
      return this.$store.state.headerDarkMode ? "yellow" : "default";
    },
    buttonColor() {
      return this.$store.state.headerDarkMode ? "yellow" : "default";
    },
    buttonTextColor() {
      return this.$store.state.headerDarkMode ? "default" : "secondary";
    },
    logoImage() {
      return this.$store.state.headerDarkMode ? "img/brand/logo6.png" : "img/brand/logo6.png";
    },

    isLogin() {
      // console.log("로그인 상태 :",this.$store.state.users.isLogin);
      return this.$store.state.users.isLogin;
    },
    user_info() {
      let user = this.$store.state.users.userInfo;
      // console.log(user);
      return user.userName + "(" + user.userId + ")";
    },
  },
  methods: {
    async logout() {
      try {
        let { data } = await http.post("/logout");
        // console.log(data);

        // if (data.result == "login") {
        //   this.$router.push("/login");
        // } else {
        this.doLogout();
        // }
      } catch (error) {
        console.log("BoardMainVue: error : ");
        console.log(error);
      }
    },
    doLogout() {
      console.log("로그아웃 실행");
      this.$store.state.isLogin = false;
      this.$store.commit("SET_USER_INFO", {});
      this.$store.commit("SET_USER_LOGOUT");
      localStorage.setItem("isLogin", false);
      this.$router.push("/login");
    },
  },
};
</script>

<style scoped>
.navbar-brand {
  left: 0px;
  margin-right: 30px;
}
</style>
