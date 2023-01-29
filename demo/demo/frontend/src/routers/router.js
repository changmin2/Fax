import Vue from "vue";
import Router from "vue-router";
import AppHeader from "../layout/AppHeader";
import AppFooter from "../layout/AppFooter";
import Landing from "../views/Landing.vue";
import Login from "../views/Login.vue";
import Send from "../views/Send.vue";
import ReceiveDetail from "../views/ReceiveDetail.vue"
import NotFoundPageView from "../views/NotFoundPageView.vue";
import SendList from "../views/SendList.vue";
import ReceiveList from "../views/ReceiveList.vue"
import store from "@/store/store"; //로그인 여부를 위한 store import

Vue.use(Router);
const requireAuth = () => (from, to, next) => {
	// console.log("라우터 로그인확인 :",localStorage.getItem('isLogin'));
	if (localStorage.getItem('isLogin')){ 
		store.dispatch("getUserInfo");
		return next()} // isLogin === true면 페이지 이동
	next('/login') // isLogin === false면 다시 로그인 화면으로 이동
}	

export default new Router({
	linkExactActiveClass: "active",
	routes: [
		{
			path: "/",
			name: "components",
			components: {
				header: AppHeader,
				default: Landing,
				// footer: AppFooter
			},
			beforeEnter: requireAuth(),
		},
		{
			path: "/login",
			name: "login",
			components: {
				// header: AppHeader,
				default: Login,
				// footer: AppFooter
			}
		},
		{
			path: "/send",
			name: "send",
			components: {
				header: AppHeader,
				default: Send,
				// footer: AppFooter
			},
			beforeEnter: requireAuth(),
		},
		{
			path: "/receive-detail",
			name: "receive-detail",
			components: {
				header: AppHeader,
				default: ReceiveDetail,
				// footer: AppFooter
			}
		},
		{
			path: "/send-list",
			name: "send-list",
			components: {
				header: AppHeader,
				default: SendList,
				// footer: AppFooter
			}
		},
		{
			path: "/receive-list",
			name: "receive-list",
			components: {
				header: AppHeader,
				default: ReceiveList,
				// footer: AppFooter
			}
		},
		{
			path: "*",
			components: {
				header: AppHeader,
				default: NotFoundPageView,
				footer: AppFooter
			}
		}
	],

	
});
