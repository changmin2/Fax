<template>
  <section class="section section-shaped section-hero login-section section-lg my-0">
    <div class="container-fluid mt-5">
      <div class="row">
        <div class="col left-content"></div>
        <div class="col right-content">
          <div class="send-title font-weight-700 text-default">받은팩스</div>
          <div class="table-container">
            <hr />
            <table class="fax-table">
              <tr class="ApprArea-header">
                <th>구분</th>
                <th>제목</th>
                <th>보낸사람</th>
                <th>읽은사람</th>
                <th>받은날짜</th>
              </tr>

              <tr v-for="(receive, index) in receivelist" :key="index">
                <td>{{ receive.read_YN }}</td>
                <td>{{ receive.title }}</td>
                <td>{{ receive.sender_NO }}</td>
                <td>{{ receive.read_USER }}</td>
                <td>{{ receive.receive_No_SEQ }}</td>
              </tr>
            </table>
            <hr />
          </div>
          <div class="send-title font-weight-700 text-default">보낸팩스</div>
          <div class="table-container">
            <hr />
            <table class="fax-table">
              <tr class="ApprArea-header">
                <th>구분</th>
                <th>제목</th>
                <th>결재상태</th>
                <th>요청자</th>
                <th>요청일</th>
              </tr>

              <tr v-for="(send, index) in sendList" :key="index">
                <td>{{ send.결재상태 }}</td>
                <td>{{ send.제목 }}</td>
                <td>{{ send.결재상태 }}</td>
                <td>{{ send.발송자 }}</td>
                <td>{{ send.전송일자 }}</td>
              </tr>
            </table>
            <hr />
          </div>

          <div class="send-title font-weight-700 text-default">결재문서</div>

          <div class="table-container">
            <hr />
            <table class="fax-table">
              <tr class="ApprArea-header">
                <th>구분</th>
                <th>제목</th>
                <th>결재상태</th>
                <th>요청자</th>
                <th>요청일</th>
              </tr>

              <tr v-for="(noApproval, index) in noApprovalList" :key="index">
                <td>{{ noApproval.상태 }}</td>
                <td>{{ noApproval.제목 }}</td>
                <td>{{ noApproval.상태 }}</td>
                <td>{{ noApproval.보내는사람 }}</td>
                <td>{{ noApproval.요청일자 }}</td>
              </tr>
            </table>
            <hr />
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import { mapGetters } from "vuex";
import http from "@/common/axios.js";
import alertify from "alertifyjs";

export default {
  name: "home",
  components: {},
  data() {
    return {
      receivelist: [],
      sendList: [],
      noApprovalList: [],
    };
  },
  computed: {
    ...mapGetters({
      userInfo: "getUserInfo",
    }),
  },
  created() {
    this.getReceiveList();

    this.getSendList();
    this.noApproval();
  },
  methods: {
    // 받은팩스
    async getReceiveList() {
      this.$store.commit("SET_LOADING_TRUE");
      try {
        let response = await http.post("/recieveList", {
          RFax_No: this.userInfo.faxNo,
          Date_Start: "2023-01-29",
          Date_End: "2023-02-01",
        });

        let { data } = response;
        this.$store.commit("SET_LOADING_FALSE");

        if (data != null) {
          // 전송 성공
          if (data.length > 3) data = data.splice(0, 3);
          console.log(data);
          console.log("전송 성공");
          this.receivelist = data;
          this.detailOpen = false;
        } else {
          console.log("전송 실패");
        }
      } catch (error) {
        // 전송 실패
        console.log("오류메시지 - ", data.Message);
        alertify.error("실패했습니다.", 1.5);
      }
    },

    // 보낸팩스함 조회
    async getSendList() {
      this.$store.commit("SET_LOADING_TRUE");
      try {
        let response = await http.post("/sendRecieve", {
          userId: this.userInfo.userId,
          // searchFrom: this.searchFrom,
          // searchTo: this.searchTo,
          searchFrom: "2023-01-29",
          searchTo: "2023-02-01",
          status: "전체",
        });

        let { data } = response;
        this.$store.commit("SET_LOADING_FALSE");

        if (data != null) {
          // 전송 성공
          if (data.length > 3) data = data.splice(0, 3);
          console.log(data);

          this.sendList = data;
          this.detailOpen = false;
        } else {
          console.log("전송 실패");
        }
      } catch (error) {
        // 전송 실패
        console.log("오류메시지 - ", data.Message);
        alertify.error("실패했습니다.", 1.5);
      }
    },
    // 결재함 리스트
    async noApproval() {
      this.$store.commit("SET_LOADING_TRUE");

      let formData = new FormData();
      formData.append("userId", this.userInfo.userId);
      formData.append("status", "전체"); // 상태 변수 추가 [대기, 전체, 완료, 회수, 반려]
      formData.append("searchFrom", "2023-01-29"); // 조회기간 (시작)
      formData.append("searchTo", "2023-02-01"); // 조회기간 (종료)
      try {
        let response = await http.post(`/payRecieve`, formData);
        console.log(response);
        let { data } = response;

        // 랜딩 페이지에서 보이는 개수 조정
        if (data.length > 3) data = data.splice(0, 3);

        this.$store.commit("SET_LOADING_FALSE");

        if (data != null) {
          // 전송 성공
          console.log(data);
          console.log("결재함 조회 성공");
          this.noApprovalList = data;

          alertify.success("결재함 조회 완료되었습니다.", 1.5);
        } else {
          console.log("결재함 조회  실패");
        }
      } catch (error) {
        // 전송 실패
        console.log("오류메시지 - ", error);
        alertify.error("결재함 조회에 실패했습니다.", 1.5);
      }
    },
  },
};
</script>
<style scoped>
.left-content {
  /* background-color: pink; */
  margin-left: 10px;
}
.right-content {
  /* background-color: aquamarine; */
  margin-right: 10px;
}
.send-title {
  font-size: large;
}
.table-container {
  width: 300px;
}
.search-area th,
.search-area td {
}

th {
  background-color: rgb(224, 224, 224);
  font-weight: normal;
  text-align: center;
}

.body-content {
  margin-top: 10px;
}
.fax-table {
  table-layout: fixed;
  width: 32rem;
  height: 100px;
}
.fax-table td {
  height: 2.3rem;
}
/* .ApprArea-header th {
  background-color: rgb(224, 224, 224);
  border: 1px solid;
  font-weight: bold;
}
.no-approval-table {
}
.no-approval-table th,
.no-approval-table td {
  height: 30px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 120px;
  padding: 0px 8px 0px 8px;
} */

.form-select {
  width: 180px;
  border: 0.0625rem solid rgb(169, 169, 169);
}

hr {
  width: 32rem;
  margin-top: 15px;
  margin-bottom: 15px;
}
</style>
