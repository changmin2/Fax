<template>
  <section class="section section-shaped section-hero login-section section-lg my-0">
    <div class="shape shape-style-1"></div>
    <div class="container pt-lg-md">
      <div
        class="send-title display-4 mb-4 font-weight-800 text-default"
        style="text-shadow: 2px 1px 2px rgba(0, 0, 0, 0.2)"
      >
        결재함 - <span style="color: #d7191f; display: inline">미결재</span>
      </div>

      <div class="col-lg-5"></div>
    </div>
  </section>
</template>

<script>
import { mapGetters } from "vuex";

export default {
  data() {
    return {};
  },
  computed: {
    ...mapGetters({
      isLogin: "getIsLogin",
      userKey: "getUserKey",
      userInfo: "getUserInfo",
    }),
  },
  created() {
    this.noApproval();
  },
  methods: {
    // 팩스보내기
    async noApproval() {
      try {
        let response = await http.post(`/payRecieve/${this.userInfo.userId}`);

        let { data } = response;

        if (data != null) {
          // 전송 성공
          console.log(data);
          console.log("전송 성공");

          alertify.success("팩스 전송이 완료되었습니다.", 1.5);
        } else {
          console.log("전송 실패");
        }
      } catch (error) {
        // 전송 실패
        console.log("오류메시지 - ", data.Message);
        alertify.error("팩스 전송에 실패했습니다.", 1.5);
      }
    },
  },
};
</script>

<style scoped></style>
