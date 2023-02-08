<template>
  <div v-show="userInfo.grade != 0" class="navbar-menu">
    <ul class="navbar fade-in">
      <span class="navbar-user-tool">사용자 도구</span>
      <li :class="{ 'navbar-click': toggleValue1 === true }">
        <a
          href="#"
          @click="navbarToggle1"
          class="navbar-li"
          :class="{ 'navbar-click': toggleValue1 === true }"
          >보낸팩스함</a
        >
        <ul class="navbar-sub">
          <li :class="{ 'li-a': toggleValue1 === false }">
            <a href="/#/send"><i class="fa fa-caret-right" aria-hidden="true"></i> 팩스보내기</a>
          </li>
          <!-- <li :class="{ 'li-a': toggleValue1 === false }">
            <a href="/#/send-wait">> 발신대기함</a>
          </li> -->
          <li :class="{ 'li-a': toggleValue1 === false }">
            <a href="/#/send-list"
              ><i class="fa fa-caret-right" aria-hidden="true"></i> 발신팩스함</a
            >
          </li>
        </ul>
      </li>
      <li :class="{ 'navbar-click': toggleValue2 === true }">
        <a href="#" @click="navbarToggle2" :class="{ 'navbar-click': toggleValue2 === true }"
          >받은팩스함</a
        >
        <ul class="navbar-sub">
          <li :class="{ 'li-a': toggleValue2 === false }">
            <a href="/#/receive-list"
              ><i class="fa fa-caret-right" aria-hidden="true"></i> 부서팩스함</a
            >
          </li>
        </ul>
      </li>

      <li :class="{ 'navbar-click': toggleValue3 === true }">
        <a href="#" @click="navbarToggle3" :class="{ 'navbar-click': toggleValue3 === true }"
          >결재함</a
        >
        <ul class="navbar-sub">
          <li :class="{ 'li-a': toggleValue3 === false }">
            <a href="/#/no-approval-list"
              ><i class="fa fa-caret-right" aria-hidden="true"></i> 미결재</a
            >
          </li>
          <li :class="{ 'li-a': toggleValue3 === false }">
            <a href="/#/approval-complete-list"
              ><i class="fa fa-caret-right" aria-hidden="true"></i> 결재함</a
            >
          </li>
        </ul>
      </li>

      <li :class="{ 'navbar-click': toggleValue4 === true }">
        <a href="#" @click="navbarToggle4" :class="{ 'navbar-click': toggleValue4 === true }"
          >주소록</a
        >
        <ul class="navbar-sub">
          <li :class="{ 'li-a': toggleValue4 === false }">
            <a href="/#/addressBook"><i class="fa fa-caret-right" aria-hidden="true"></i> 주소록</a>
          </li>
        </ul>
      </li>
    </ul>
  </div>
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
  created() {
    this.getData();
  },
  computed: {
    ...mapGetters({
      isLogin: "getIsLogin",
      userInfo: "getUserInfo",
    }),
  },

  data() {
    return {
      // fileRealPath: require("/Users/sk/Downloads/Cat03.jpg"),
      toggleValue1: false,
      toggleValue2: false,
      toggleValue3: false,
      toggleValue4: false,

      posts: [],
    };
  },
  methods: {
    async getData() {
      const response = await http.get("https://my-json-server.typicode.com/typicode/demo/posts");
      this.posts = response.data;
    },
    navbarToggle1() {
      this.toggleValue1 = !this.toggleValue1;
      if (this.toggleValue1 == true) {
        this.toggleValue2 = false;
        this.toggleValue3 = false;
        this.toggleValue4 = false;
      }
    },
    navbarToggle2() {
      this.toggleValue2 = !this.toggleValue2;
      if (this.toggleValue2 == true) {
        this.toggleValue1 = false;
        this.toggleValue3 = false;
        this.toggleValue4 = false;
      }
    },
    navbarToggle3() {
      this.toggleValue3 = !this.toggleValue3;
      if (this.toggleValue3 == true) {
        this.toggleValue1 = false;
        this.toggleValue2 = false;
        this.toggleValue4 = false;
      }
    },
    navbarToggle4() {
      this.toggleValue4 = !this.toggleValue4;
      if (this.toggleValue4 == true) {
        this.toggleValue1 = false;
        this.toggleValue2 = false;
        this.toggleValue3 = false;
      }
    },
  },
};
</script>

<style scoped>
.navbar-menu {
  margin-top: 160px;
  margin-left: 30px;
  /* width: 100%; */
}

.navbar-user-tool {
  padding: 16px 0px 16px 0px;
  font-size: large;
  /* background-color: #545443; */
  background-color: rgb(224, 224, 224);
  width: 100%;
  text-align: center;
  color: #172b4d;
}
ul {
  list-style: none;
  text-align: left;
  /* background-color: #f2f2f2; */
}
.navbar {
  display: flex;
  flex-direction: column;
  margin-top: 500px;
  padding: 0px;
  margin: 0;
  overflow: auto;
  /* width: 100%; */
  border: 1px solid #cad1d7;
  border-radius: 0.25rem;
}
.navbar-click {
  background-color: #d7191f;
  color: white;
  transition: all 300ms ease-out;
  width: 100%;
}

.navbar > li {
  width: 100%;
  border-bottom: 1px solid #cad1d7;
  transition: color 0.15s ease-in-out background-color 0.15s ease-in-out,
    border-color 0.15s ease-in-out, box-shadow 0.15s ease-in-out, border-radius 0.15s ease;
  /* background-color: transparent; */
}

/* .navbar > li:active {
  background-color: #d7191f;
}
.navbar > li:active > a {
  color: white;
} */
.navbar-sub {
  padding: 0px;
  transition: all 300ms ease-out;
}
.navbar-sub li {
  background-color: white;
  transition: all 300ms ease-out;
}
.navbar-sub li a {
  padding: 8px 12px 8px 12px;
  transition: all 300ms ease-out;
  font-size: 15px;
}
li:last-child {
  border: none;
}
li a {
  text-decoration: none;
  display: block;
  color: #000;
  padding: 12px 12px 12px 12px;
  margin-left: 10px;
  font-size: 1rem;
  /* background-color: transparent; */
  width: inherit;
  height: inherit;
  margin: 0px;
  transition: all 300ms ease-out;
  color: #172b4d;
}
.li-a {
  display: none;
  transition: all 300ms ease-out;
}
</style>
