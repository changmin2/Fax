<template>
  <section class="section section-shaped section-hero login-section section-lg my-0">
    <div class="container-fluid mt-5">
      <div class="row">
        <div class="col left-content"></div>
        <div class="col right-content">
          <div class="send-title font-weight-700 text-default">받은팩스</div>
          <div class="table-container">
            <hr />
            <table class="no-approval-table">
              <tr class="ApprArea-header">
                <th>
                  <input type="checkbox" />
                </th>
                <th>구분</th>
                <th>제목</th>
                <th>결재상태</th>
                <th>요청자</th>
                <th>요청일</th>
              </tr>

              <tr v-for="(noApproval, index) in noApprovalList" :key="index">
                <td><input type="checkbox" /></td>
                <td>{{ noApproval.상태 }}</td>
                <td>{{ noApproval.제목 }}</td>
                <td>{{ noApproval.상태 }}</td>
                <td>{{ noApproval.보내는사람 }}</td>
                <td>{{ noApproval.요청일자 }}</td>
              </tr>
            </table>
            <hr />
          </div>
          <div class="send-title font-weight-700 text-default">보낸팩스함</div>
          <div class="table-container">
            <hr />
            <table class="no-approval-table">
              <tr class="ApprArea-header">
                <th>
                  <input type="checkbox" />
                </th>
                <th>구분</th>
                <th>제목</th>
                <th>결재상태</th>
                <th>요청자</th>
                <th>요청일</th>
              </tr>

              <tr v-for="(noApproval, index) in noApprovalList" :key="index">
                <td><input type="checkbox" /></td>
                <td>{{ noApproval.상태 }}</td>
                <td>{{ noApproval.제목 }}</td>
                <td>{{ noApproval.상태 }}</td>
                <td>{{ noApproval.보내는사람 }}</td>
                <td>{{ noApproval.요청일자 }}</td>
              </tr>
            </table>
            <hr />
          </div>

          <div class="send-title font-weight-700 text-default">결재문서</div>

          <div class="table-container">
            <hr />
            <table class="no-approval-table">
              <tr class="ApprArea-header">
                <th>
                  <input type="checkbox" />
                </th>
                <th>구분</th>
                <th>제목</th>
                <th>결재상태</th>
                <th>요청자</th>
                <th>요청일</th>
              </tr>

              <tr v-for="(noApproval, index) in noApprovalList" :key="index">
                <td><input type="checkbox" /></td>
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
      noApprovalList: {},
    };
  },
  computed: {
    ...mapGetters({
      userInfo: "getUserInfo",
    }),
  },
  created() {
    this.noApproval();
  },
  methods: {
    // 결재함 리스트
    async noApproval() {
      let formData = new FormData();
      formData.append("userId", this.userInfo.userId);
      try {
        let response = await http.post(`/payRecieve`, formData);
        console.log(response);
        let { data } = response;

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
.ApprArea-header th {
  background-color: rgb(224, 224, 224);
  border: 1px solid;
  font-weight: bold;
  /* padding: 12px; */
}
.no-approval-table {
}
.no-approval-table th,
.no-approval-table td {
  /* text-align: center;
  height: 35px;
  line-height: 0px; */
  height: 30px;
  /* width: 150px; */
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 120px;
  padding: 0px 8px 0px 8px;
}

.form-select {
  width: 180px;
  border: 0.0625rem solid rgb(169, 169, 169);
}

hr {
  margin-top: 15px;
  margin-bottom: 15px;
}
</style>
