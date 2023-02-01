<template>
  <section class="section section-shaped section-hero login-section my-0">
    <div class="main-container">
      <div class="send-title display-4 mb-4 font-weight-800 text-default">
        결재함 - <span style="color: #d7191f; display: inline">결재완료</span>
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
                id="searchFrom"
                value="today"
                class="form-select"
                v-model="searchFrom"
                @change="setDateInfo(searchFrom)"
              />
              ~
              <input
                type="date"
                id="searchTo"
                value="today"
                class="form-select"
                v-model="searchTo"
                @change="setDateInfo(searchTo)"
              />
            </form>
          </div>
          <div class="row mt-3">
            결재구분
            <select v-model="apprStatus" class="form-select ml-2">
              <option value="전체" selected>전체</option>
              <option value="완료">완료</option>
              <option value="회수">회수</option>
              <option value="반려">반려</option>
            </select>
          </div>
        </div>
        <div class="col col-sm-2">
          <base-button type="default" class="no-approval-btn mt-2" @click="noApproval">
            조회
          </base-button>
        </div>
      </div>
      <hr />

      <div class="container-fluid">
        <div class="top-content search-area">
          <div class="body-content ApprArea">
            <table class="no-approval-table" style="width: 100%">
              <tr class="ApprArea-header">
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
                <td>{{ noApproval.상태 }}</td>
                <td>{{ noApproval.요청일자 }}</td>
                <td>{{ noApproval.보내는사람 }}</td>
                <td>{{ noApproval.받는사람 }}</td>
                <td>{{ noApproval.팩스번호 }}</td>
                <td>{{ noApproval.제목 }}</td>
                <td>
                  <base-button @click="setNoApproval(noApproval.결제고유번호)">상세</base-button>
                </td>
                <td>{{ noApproval.받는사람 }}</td>
                <td>{{ noApproval.상태 }}</td>
                <td>{{ noApproval.결제고유번호 }}</td>
                <td>{{ noApproval.결재일자 }}</td>
              </tr>
            </table>
          </div>
        </div>
      </div>
    </div>
    <!-- modal 모달창 -->
    <modal
      :show.sync="modals.modal3"
      body-classes="p-1"
      modal-classes="modal-dialog-centered modal-big"
      class="modal-class"
    >
      <h6 slot="header" class="modal-title" id="modal-title-default"></h6>
      <no-approval :noApprDetail="noApprDetail" :isComplete="true"></no-approval>
    </modal>
  </section>
</template>

<script>
import { mapGetters } from "vuex";
import http from "@/common/axios.js";
import alertify from "alertifyjs";
import Modal from "@/components/Modal.vue";
import NoApproval from "@/views/NoApproval.vue";

export default {
  name: "no-approval-list",
  components: {
    Modal,
    NoApproval,
  },
  data() {
    return {
      noApprovalList: [],

      modals: {
        modal3: false,
      },

      apprNo: "",
      noApprDetail: {},
      searchFrom: "",
      searchTo: "",
      apprStatus: "전체",
    };
  },
  computed: {
    ...mapGetters({
      isLogin: "getIsLogin",
      userKey: "getUserKey",
      userInfo: "getUserInfo",
    }),
  },
  created() {},
  mounted() {
    this.getNow();
    this.noApproval();
  },
  methods: {
    // date 설정
    setDateInfo(datedata) {
      let dateInfo = this.datedata;
      dateInfo = datedata.split("-");
      dateInfo = dateInfo.join("");
      console.log(dateInfo);
    },
    getNow() {
      const today = new Date();
      const year = today.getFullYear(); // 년
      const month = today.getMonth(); // 월
      const day = today.getDate(); // 일

      var searchFrom = new Date(year, month, day - 6);
      this.searchFrom = searchFrom.toISOString().split("T")[0];
      this.searchTo = today.toISOString().split("T")[0];
      this.setDateInfo(this.searchFrom);
      this.setDateInfo(this.searchTo);
      console.log(searchFrom + "," + searchTo);
    },

    // detail - noApproval 설정
    setNoApproval(apprNo) {
      this.apprNo = apprNo;
      this.noApprovalDetail(apprNo);
      console.log("현재 apprNo = ", apprNo);
      this.modals.modal3 = true;
    },
    // 결재함 리스트
    async noApproval() {
      this.$store.commit("SET_LOADING_TRUE");

      let formData = new FormData();
      formData.append("userId", this.userInfo.userId);
      formData.append("status", this.apprStatus); // 상태 변수 추가 [대기, 전체, 완료, 회수, 반려]
      formData.append("searchFrom", this.searchFrom); // 조회기간 (시작)
      formData.append("searchTo", this.searchTo); // 조회기간 (종료)
      try {
        let response = await http.post(`/payRecieve`, formData);
        console.log(response);
        let { data } = response;
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

    // 결재함 상세
    async noApprovalDetail(apprNo) {
      this.$store.commit("SET_LOADING_TRUE");

      let formData = new FormData();
      formData.append("apprNo", apprNo);

      try {
        let response = await http.post(`/payDetail`, formData);
        console.log(formData);
        console.log(response);
        let { data } = response;
        this.$store.commit("SET_LOADING_FALSE");

        if (data != null) {
          // 전송 성공

          console.log("결재함 상세 조회 성공");
          console.log(data);
          this.noApprDetail = data[0];

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
.main-container {
  margin-top: 100px;
  margin-left: 3vw;
  margin-right: 3vw;
}

.search-area {
  border-top: 2px solid black;
  border-bottom: 2px solid black;
}

.search-area th,
td {
  height: 40px;
  width: 80px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 5rem;
  padding: 0px 15px 0px 15px;
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
.no-approval-table {
  width: inherit;
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
  display: flex;
}

hr {
  margin-top: 15px;
  margin-bottom: 15px;
}
</style>
