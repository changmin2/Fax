<template>
  <section class="section">
    <div class="main-container">
      <div class="send-title display-4 mb-4 font-weight-800 text-default">
        결재함 - <span style="color: #d7191f; display: inline">미결재</span>
      </div>

      <form id="master" role="form" style="width: 100%">
        <table class="fax-table fax-table-input" style="width: 100%">
          <colgroup>
            <col style="width: 9%" />
            <col style="width: 38%" />
            <col style="width: 9%" />
            <col style="width: 38%" />
            <col />
          </colgroup>
          <tr>
            <th>조회기간</th>
            <td>
              <input
                type="date"
                id="searchFrom"
                value="today"
                class="fax-form-input"
                style="height: 30px"
                v-model="searchFrom"
                @change="setDateInfo(searchFrom)"
              />
              ~
              <input
                type="date"
                id="searchTo"
                value="today"
                class="fax-form-input"
                style="height: 30px"
                v-model="searchTo"
                @change="setDateInfo(searchTo)"
              />
            </td>
            <th>조건</th>
            <td>
              <select
                name="searchGubun"
                class="fax-form-input"
                id="searchGubun"
                style="height: 30px"
              >
                <!-- <option value="" selected>받는사람/팩스번호</option>
                  <option value="1">받는사람</option> -->
                <option value="2">팩스번호</option>
              </select>
              <input
                type="text"
                class="fax-form-input ml-1"
                id="searchGubunData"
                name="searchGubunData"
                style="height: 30px"
              />
            </td>
            <td></td>
          </tr>
          <tr>
            <th>점번</th>
            <td>
              <select class="fax-table-input" style="height: 30px">
                <option value="IT개발부" selected>IT개발부</option>
              </select>
            </td>
            <td></td>
            <td></td>
            <td>
              <div class="text-center" style="float: right">
                <base-button type="danger" @click="noApproval">조회</base-button>
              </div>
            </td>
          </tr>
        </table>
      </form>

      <table class="fax-table" style="width: 100%">
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
          <th>결재일시<br />(요청/완료)</th>
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

    <!-- modal 모달창 -->
    <modal
      :show.sync="isModalState"
      body-classes="p-1"
      modal-classes="modal-dialog-centered modal-big"
      class="modal-class"
    >
      <h6 slot="header" class="modal-title" id="modal-title-default"></h6>
      <no-approval :noApprDetail="noApprDetail" :isComplete="false"></no-approval>
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

      // modals: {
      //   modal3: this.isModalOpen,
      // },

      apprNo: "",
      noApprDetail: {},
      searchFrom: "",
      searchTo: "",
      apprStatus: "대기",
    };
  },
  computed: {
    ...mapGetters({
      isLogin: "getIsLogin",
      userKey: "getUserKey",
      userInfo: "getUserInfo",
      isModalOpen: "getModalState",
    }),

    isModalState: {
      set: function () {
        this.$store.commit("SET_MODAL_CLOSE");
      },
      get: function () {
        return this.isModalOpen;
      },
    },
  },

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
      // this.modals.modal3 = true;
      this.$store.commit("SET_MODAL_OPEN");
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

<style scoped></style>
