import Vue from "vue";
import Router from "vue-router";
import AppHeader from "../layout/AppHeader";
import AppFooter from "../layout/AppFooter";
import AppNavbar from "../layout/AppNavBar";
import Landing from "../views/Landing.vue";
import Login from "../views/Login.vue";
import Send from "../views/Send.vue";
import ReceiveDetail from "../views/ReceiveDetail.vue";
import NotFoundPageView from "../views/NotFoundPageView.vue";
import SendList from "../views/SendList.vue";
import ReceiveList from "../views/ReceiveList.vue";
import store from "@/store/store"; //로그인 여부를 위한 store import
import NoApproval from "../views/NoApproval.vue";
import NoApprovalList from "../views/NoApprovalList.vue";
import ApprovalCompleteList from "../views/ApprovalCompleteList.vue";
import MyPage from "../views/MyPage.vue";
import SendWait from "../views/SendWait.vue";

Vue.use(Router);
const requireAuth = () => (from, to, next) => {
  // console.log("라우터 로그인확인 :",localStorage.getItem('isLogin'));
  if (localStorage.getItem("isLogin")) {
    store.dispatch("getUserInfo");
    return next();
  } // isLogin === true면 페이지 이동
  next("/login"); // isLogin === false면 다시 로그인 화면으로 이동
};

export default new Router({
  linkExactActiveClass: "active",
  routes: [
    {
      path: "/",
      name: "components",
      components: {
        header: AppHeader,
        navbar: AppNavbar,
        default: Landing,
        // footer: AppFooter,
      },
      beforeEnter: requireAuth(),
    },
    {
      path: "/login",
      name: "login",
      components: {
        default: Login,
      },
    },
    {
      path: "/send",
      name: "send",
      components: {
        header: AppHeader,
        navbar: AppNavbar,
        default: Send,
        // footer: AppFooter
      },
      beforeEnter: requireAuth(), // 로그인 해야 볼 수 있는 페이지
    },
    {
      path: "/receive-detail",
      name: "receive-detail",
      components: {
        header: AppHeader,
        navbar: AppNavbar,
        default: ReceiveDetail,
        // footer: AppFooter
      },
      beforeEnter: requireAuth(),
    },
    {
      path: "/send-list",
      name: "send-list",
      components: {
        header: AppHeader,
        navbar: AppNavbar,
        default: SendList,
        // footer: AppFooter
      },
    },
    {
      path: "/send-wait",
      name: "send-wait",
      components: {
        header: AppHeader,
        navbar: AppNavbar,
        default: SendWait,
        // footer: AppFooter
      },
    },
    {
      path: "/receive-list",
      name: "receive-list",
      components: {
        header: AppHeader,
        navbar: AppNavbar,
        default: ReceiveList,
        // footer: AppFooter
      },
      beforeEnter: requireAuth(),
    },
    {
      path: "/no-approval",
      name: "no-approval",
      components: {
        header: AppHeader,
        navbar: AppNavbar,
        default: NoApproval,
        // footer: AppFooter
      },
    },
    {
      path: "/no-approval-list",
      name: "no-approval-list",
      components: {
        header: AppHeader,
        navbar: AppNavbar,
        default: NoApprovalList,
        // footer: AppFooter
      },
    },
    {
      path: "/approval-complete-list",
      name: "approval-complete-list",
      components: {
        header: AppHeader,
        navbar: AppNavbar,
        default: ApprovalCompleteList,
        // footer: AppFooter
      },
    },
    {
      path: "/myPage",
      name: "myPage",
      components: {
        header: AppHeader,
        navbar: AppNavbar,
        default: MyPage,
        // footer: AppFooter
      },
      beforeEnter: requireAuth(), // 로그인 해야 볼 수 있는 페이지
    },
    {
      path: "*",
      components: {
        header: AppHeader,
        default: NotFoundPageView,
        footer: AppFooter,
      },
    },
  ],
});
