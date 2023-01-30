<template>
  <section class="section section-shaped section-hero login-section section-lg my-0">
    <div class="shape shape-style-1"></div>
    <div class="container pt-lg-md">
      <div class="send-title display-4 mb-4 font-weight-800 text-default">
        결재함 - <span style="color: #d7191f; display: inline">미결재</span>
      </div>

      <hr />
      <div class="row ml-3">
        <div class="col col-sm-5">
          <div class="row">
            점번
            <select class="form-select ml-2">
              <option selected>IT개발부</option>
              <option value="1">One</option>
              <option value="2">Two</option>
            </select>
          </div>
          <div class="row mt-3">
            조건
            <select class="form-select ml-2" style="width: 100px">
              <option selected>보낸사람</option>
              <option value="1">One</option>
              <option value="2">Two</option>
            </select>
            <select class="form-select ml-2">
              <option selected></option>
              <option value="1">One</option>
              <option value="2">Two</option>
            </select>
          </div>
        </div>
        <div class="col col-sm-5 right-option">
          <div class="row">
            조회기간
            <form class="ml-2" style="border: none">
              <input
                type="date"
                value="today"
                class="form-select"
                v-model="dateInfo"
                @change="setDateInfo"
              />
            </form>
            {{ dateInfo }}
          </div>
          <div class="row mt-3">
            결재구분
            <select class="form-select ml-2">
              <option selected>전체</option>
              <option value="1">One</option>
              <option value="2">Two</option>
            </select>
          </div>
        </div>
        <div class="col col-sm-2">
          <base-button type="default" class="no-approval-btn mt-2"> 조회 </base-button>
        </div>
      </div>

      <hr />
      <div class="row" style="width: 100%">
        <div class="top-content search-area">
          <div class="body-content ApprArea" style="width: 100%">
            <table style="width: 100%" class="no-approval-table">
              <tr class="ApprArea-header">
                <th>
                  <input type="checkbox" />
                </th>
                <th>구분</th>
                <th>요청일자</th>
                <th>보내는사람</th>
                <th>받는사람</th>
                <th>팩스번호</th>
                <th>제목</th>
                <th>상세보기</th>
                <th>장수</th>
                <th>결재상태</th>
                <th>결재구분</th>
                <th>결재일시(요청/완료)</th>
              </tr>

              <tr v-for="(noApproval, index) in noApprovalList" :key="index">
                <td><input type="checkbox" /></td>
                <td>{{ noApproval }}</td>
                <td>dd</td>
              </tr>
              <tr>
                <td><input type="checkbox" /></td>
                <td>1</td>
                <td>2</td>
                <td>3</td>
                <td>4</td>
                <td>5</td>
                <td>6</td>
                <td>
                  <base-button @click="noApprovalDetail('05042089819')">상세test</base-button>
                </td>
                <td>8</td>
                <td>9</td>
                <td>10</td>
                <td>11</td>
              </tr>
            </table>
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
  data() {
    return {
      noApprovalList: [],

      dateInfo: "",
    };
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
    // date 설정
    setDateInfo() {
      let dateInfo = this.dateInfo;
      dateInfo = dateInfo.split("-");
      dateInfo = dateInfo.join("");
      console.log(dateInfo);
    },

    // 결재함
    async noApproval() {
      try {
        let response = await http.post(`/payRecieve?userId=${this.userInfo.userId}`);
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

    // 결재함 상세
    async noApprovalDetail(apprNo) {
      try {
        let response = await http.post(`/payDetail?apprNo=${apprNo}`);
        console.log(response);
        let { data } = response;

        if (data != null) {
          // 전송 성공
          console.log(data);
          console.log("결재함 상세 조회 성공");
          this.noApprovalList = data;

          alertify.success("결재함 상세 조회가 완료되었습니다.", 1.5);
        } else {
          console.log("결재함 상세 조회 실패");
        }
      } catch (error) {
        // 전송 실패
        console.log("오류메시지 - ", error);
        alertify.error("결재함 상세 조회가 실패했습니다.", 1.5);
      }
    },
  },
};
</script>

<style scoped>
.send-title {
}
.send-main {
  width: 100vh;
}

.top-content {
  display: flex;
  width: 100%;
}

.search-area {
  border-top: 2px solid black;
  border-bottom: 2px solid black;
}
.search-area th,
td {
  height: 40px;
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
  padding: 12px;
}

.no-approval-table th,
.no-approval-table td {
  text-align: center;
  height: 35px;
  line-height: 0px;
}

.form-select {
  width: 180px;
  border: 0.0625rem solid rgb(169, 169, 169);
}
.no-approval-btn-group {
}
.no-approval-btn {
  /* padding: 5px; */
  /* width: 50px; */
}
hr {
  margin-top: 15px;
  margin-bottom: 15px;
}
.right-option {
  /* margin-left: 100px; */
}
</style>
