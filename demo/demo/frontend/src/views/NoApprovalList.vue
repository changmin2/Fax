<template>
  <section class="section">
    <div class="main-container">
      <div class="send-title display-4 mb-4 font-weight-800 text-default">
        <!-- <i class="fa fa-exclamation-circle" aria-hidden="true"></i> -->
        결재함 - <span style="color: #d7191f; display: inline">미결재</span>
      </div>

      <div class="fax-input">
        <div class="fax-input-row">
          <div class="fax-input-box">조회기간</div>
          <div class="fax-input-content">
            <input
              type="date"
              id="searchFrom"
              value="today"
              class="fax-form-input fax-form-input-date"
              v-model="searchFrom"
              @change="setDateInfo(searchFrom)"
            />
            ~
            <input
              type="date"
              id="searchTo"
              value="today"
              class="fax-form-input fax-form-input-date"
              v-model="searchTo"
              @change="setDateInfo(searchTo)"
            />
          </div>
        </div>
        <div class="fax-input-row">
          <div class="fax-input-box">받는사람</div>
          <div class="fax-input-content">
            <input
              type="text"
              id="searchGubunData"
              name="searchGubunData"
              class="fax-form-input fax-form-input-receiver"
              v-model="senderNo"
              placeholder="이름 또는 팩스번호"
            />
          </div>
          <div class="text-center">
            <base-button type="danger" class="mobile-btn" @click="noApproval">조회</base-button>
          </div>
        </div>
      </div>

      <!-- <form id="master" role="form" style="width: 100%">
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
                style="height: 30px; width: 6rem"
              >
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
      </form> -->

      <table class="fax-table table-hover" style="width: 100%">
        <tbody>
          <tr class="ApprArea-header">
            <th class="fax-table-display">요청일자</th>
            <th>제목</th>

            <th>보내는사람</th>
            <th>결재자</th>
            <th class="fax-table-display-none">팩스번호</th>

            <th class="fax-table-display-none">장수</th>
            <th>결재상태</th>

            <th class="fax-table-display-none">결재구분</th>
            <th class="fax-table-display-none">결재일시</th>
          </tr>

          <tr
            v-for="(noApproval, index) in noApprovalList"
            :key="index"
            @click="setNoApproval(noApproval.결제고유번호)"
          >
            <td class="fax-table-display">{{ noApproval.요청일자 }}</td>
            <td>{{ noApproval.제목 }}</td>
            <td>{{ noApproval.보내는사람 }}</td>
            <td>{{ noApproval.받는사람 }}</td>
            <td class="fax-table-display-none">{{ noApproval.팩스번호 }}</td>

            <td class="fax-table-display-none">{{ noApproval.페이지수 }}</td>
            <td>{{ noApproval.상태 }}</td>
            <td class="fax-table-display-none">{{ noApproval.결제고유번호 }}</td>
            <td class="fax-table-display-none">{{ noApproval.결재일자 }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <!-- modal 모달창 -->
    <modal :show.sync="isModalState" modal-classes="modal-dialog-centered modal-big">
      <h6 slot="header" class="modal-title" id="modal-title-default"></h6>
      <no-approval
        :noApprDetail="noApprDetail"
        :isComplete="false"
        :noApproval="noApproval"
      ></no-approval>
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
      senderNo: "",
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

      // var searchFrom = new Date(year, month, day - 6);
      // this.searchFrom = searchFrom.toISOString().split("T")[0];
      // this.searchTo = today.toISOString().split("T")[0];
      // var searchFrom = new Date(year, month, day - 6);
      this.searchFrom = new Intl.DateTimeFormat("fr-CA", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
      }).format(new Date(year, month, day - 6));
      this.searchTo = new Intl.DateTimeFormat("fr-CA", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
      }).format(Date.now());
      this.setDateInfo(this.searchFrom);
      this.setDateInfo(this.searchTo);
      console.log(this.searchFrom + " ~ " + this.searchTo);
    },

    //팩스번호 포맷화
    setFormatting(fax_no) {
      let data = fax_no.replace(/^(\d{3})(\d{4})(\d)/, `$1-$2-$3`);
      return data;
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

          //발신자 팩스번호 포맷지정
          for (let i in data[0].받는사람정보) {
            this.noApprDetail.받는사람정보[i].팩스번호 = this.setFormatting(
              data[0].받는사람정보[i].팩스번호
            );
          }

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
.fax-table-input {
  width: 16rem;
}

.fax-form-input {
  height: 30px;
  font-size: small;
  margin: 2px;
}
.fax-input {
  display: flex;
  /* justify-content: space-around; */
  border-top: 1px solid rgb(224, 224, 224);
  border-bottom: 1px solid rgb(224, 224, 224);
}
.fax-input-row {
  display: flex;
  align-items: center;
  width: 100%;
}
.fax-input-box {
  background-color: rgb(224, 224, 224);
  font-size: small;
  font-weight: 600;
  min-width: 80px;
  width: 80px;
  height: 100%;
  line-height: 50px;
  text-align: center;
}
.fax-input-content {
  display: flex;
  justify-content: center;
  align-items: center;
  /* width: 100%; */
  margin: 0 auto;
}

@media screen and (max-width: 991px) {
  section {
    padding: 0px;
  }
  .fax-table {
    margin-right: 1rem;
    width: 100vw;
  }

  .fax-table-tr {
    display: flex;
    height: 100%;
  }
  .fax-table th {
    height: inherit;
    padding-top: 0.5rem;
    padding-bottom: 0.5rem;
    /* width: 100%; */
    width: 20px;
  }

  .fax-input {
    display: flex;
    flex-direction: column;
  }
  .fax-input-mobile {
    display: none;
  }

  .fax-form-input {
    /* width: 105px; */
  }
  .fax-form-input-receiver {
    /* width: 8rem; */
  }
  .fax-input-box {
    line-height: 45px;
  }
  .fax-input-content {
    display: flex;
    margin-right: 10px;
    margin-left: 20px;

    /* max-width: 350px; */
    /* width:inherit; */
    /* justify-content: right; */
  }
  .fax-table-input {
    /* width: 10rem; */
  }
  .mobile-btn {
    padding: 5px 20px 5px 20px;
  }
  .fax-form-input-date {
    width: 105px;
  }
  th {
    padding-left: 4px;
    padding-right: 4px;
  }
  td {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    max-width: 5rem;
    padding-left: 4px;
    padding-right: 4px;
  }
}
</style>
