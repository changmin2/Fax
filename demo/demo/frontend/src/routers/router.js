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

Vue.use(Router);

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
			}
		},
		{
			path: "/login",
			name: "login",
			components: {
				header: AppHeader,
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
			}
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
