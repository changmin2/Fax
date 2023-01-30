<template>
  <div class="navbar-menu">
    <ul class="navbar">
      <span class="navbar-user-tool">사용자 도구</span>
      <li :class="{ 'navbar-click': toggleValue1 === true }">
        <a
          href="#"
          @click="navbarToggle1"
          class="navbar-li"
          :class="{ 'navbar-click': toggleValue1 === true }"
          >팩스발신함</a
        >
        <ul class="navbar-sub">
          <li :class="{ 'li-a': toggleValue1 === false }">
            <a href="/#/send">> 팩스보내기</a>
          </li>
          <li :class="{ 'li-a': toggleValue1 === false }">
            <a href="/#/send-wait">> 발신대기함</a>
          </li>
          <li :class="{ 'li-a': toggleValue1 === false }">
            <a href="/#/send-list">> 발신팩스함</a>
          </li>
        </ul>
      </li>
      <li :class="{ 'navbar-click': toggleValue2 === true }">
        <a href="#" @click="navbarToggle2" :class="{ 'navbar-click': toggleValue2 === true }"
          >팩스수신함</a
        >
        <ul class="navbar-sub">
          <li :class="{ 'li-a': toggleValue2 === false }">
            <a href="/#/receive-list">> 부서팩스함</a>
          </li>
        </ul>
      </li>

      <li :class="{ 'navbar-click': toggleValue3 === true }">
        <a href="#" @click="navbarToggle3" :class="{ 'navbar-click': toggleValue3 === true }"
          >결재함</a
        >
        <ul class="navbar-sub">
          <li :class="{ 'li-a': toggleValue3 === false }">
            <a href="/#/no-approval-list">> 미결재</a>
          </li>
          <li :class="{ 'li-a': toggleValue3 === false }">
            <a href="/#/approval-complete-list">> 결재완료</a>
          </li>
        </ul>
      </li>

      <li :class="{ 'navbar-click': toggleValue4 === true }">
        <a href="#" @click="navbarToggle4" :class="{ 'navbar-click': toggleValue4 === true }"
          >주소록</a
        >
      </li>

      <!-- <li>
        <base-dropdown tag="li" class="nav-item ml-5" position="right">
          <a slot="title" class="nav-link" data-toggle="dropdown" role="button">
            <div class="card-profile-image d-lg-block d-none">
              <a href="#"> 팩스수신함 </a>
            </div>

            <div class="nav-link-inner--text d-lg-none font-weight-600">MyProfile</div>
          </a>
          <router-link to="/send" class="dropdown-item">부서팩스함</router-link>
        </base-dropdown>
      </li>
      <li>
        <base-dropdown tag="li" class="nav-item ml-5" position="right">
          <a slot="title" class="nav-link" data-toggle="dropdown" role="button">
            <div class="card-profile-image d-lg-block d-none">
              <a href="#"> 팩스발신함 </a>
            </div>

            <div class="nav-link-inner--text d-lg-none font-weight-600">MyProfile</div>
          </a>
          <router-link to="/send" class="dropdown-item">결재함</router-link>
          <router-link to="/send-wait" class="dropdown-item">미결재</router-link>
          <router-link to="/send-list" class="dropdown-item">결재완료</router-link>
        </base-dropdown>
      </li>
      <li>
        <base-dropdown tag="li" class="nav-item ml-5" position="right">
          <a slot="title" class="nav-link" data-toggle="dropdown" role="button">
            <div class="card-profile-image d-lg-block d-none">
              <a href="#"> 주소록 </a>
            </div>

            <div class="nav-link-inner--text d-lg-none font-weight-600">MyProfile</div>
          </a>
          <router-link to="/send" class="dropdown-item">주소록...</router-link>
        </base-dropdown>
      </li> -->
    </ul>
  </div>
</template>

<script>
import http from "@/common/axios.js";
import BaseNav from "@/components/BaseNav";
import BaseDropdown from "@/components/BaseDropdown";
import CloseButton from "@/components/CloseButton";

import { mapGetters } from "vuex";

export default {
  components: {
    BaseNav,
    CloseButton,
    BaseDropdown,
  },

  computed: {
    ...mapGetters({
      isLogin: "getIsLogin",
    }),
  },
  data() {
    return {
      // fileRealPath: require("/Users/sk/Downloads/Cat03.jpg"),
      toggleValue1: false,
      toggleValue2: false,
      toggleValue3: false,
      toggleValue4: false,
    };
  },
  methods: {
    navbarToggle1() {
      this.toggleValue1 = !this.toggleValue1;
    },
    navbarToggle2() {
      this.toggleValue2 = !this.toggleValue2;
    },
    navbarToggle3() {
      this.toggleValue3 = !this.toggleValue3;
    },
    navbarToggle4() {
      this.toggleValue4 = !this.toggleValue4;
    },
  },
};
</script>

<style scoped>
.navbar-menu {
  margin-top: 160px;
  margin-left: 30px;
  width: 100%;
}
.navbar-user-tool {
  padding: 16px 0px 16px 0px;
  font-size: large;
  background-color: rgb(155, 155, 155);
  width: 100%;
  text-align: center;
  color: white;
}
ul {
  list-style: none;
  text-align: left;
  background-color: #f2f2f2;
}
.navbar {
  display: flex;
  flex-direction: column;
  margin-top: 500px;
  padding: 0px;
  margin: 0;
  overflow: auto;
  width: 100%;
  border: 1px solid #cad1d7;
  border-radius: 0.25rem;
}
.navbar-click {
  background-color: #d7191f;
  color: white;
}
.navbar > li {
  padding: 5px 0px 0px 0px;
}

/* .navbar > li:active {
  background-color: #d7191f;
}
.navbar > li:active > a {
  color: white;
} */
.navbar-sub {
  padding: 0px;
}
.navbar-sub li {
  background-color: white;
}

.navbar > li {
  width: 100%;
  border-bottom: 1px solid #cad1d7;
}
li:last-child {
  border: none;
}
li a {
  text-decoration: none;
  display: block;
  color: #000;
  padding: 8px 8px 8px 8px;

  margin-left: 10px;
  font-size: 1rem;
}
.li-a {
  display: none;
}
</style>
