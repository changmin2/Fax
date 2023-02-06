<template>
  <section class="section">
    <div class="main-container">
      <div class="send-title display-4 mb-4 font-weight-800 text-default">
        <!-- <i class="fa fa-envelope" aria-hidden="true"></i> -->
        발신팩스함
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
              v-model="receiver"
              placeholder="이름 또는 팩스번호"
            />
          </div>
        </div>

        <div class="fax-input-row">
          <div class="fax-input-box">결재구분</div>
          <div class="fax-input-content">
            <select v-model="sendStatus" class="fax-table-input" style="height: 30px">
              <option value="전체" selected>전체</option>
              <option value="결재완료">결재완료</option>
              <option value="결재대기">결재대기</option>
              <option value="전송완료">전송완료</option>
              <option value="전송실패">전송실패</option>
              <option value="반려">반려</option>
              <option value="회수">회수</option>
            </select>
          </div>
        </div>
        <div class="fax-input-row">
        <div class="fax-input-box">작성자</div>
        <div class="fax-input-content">
          <select v-model="senderId" class="fax-table-input" style="height: 30px">
            <option value="All" selected>전체</option>
            <option
                    v-for="(item, index) in deptUsers"
                    :key="index"
                    :value="item.USER_ID"
                  >
                    {{ item.USER_NAME }}
            </option>
          </select>
        </div>
          <div class="text-center">
            <base-button type="danger" class="mobile-btn" @click="getSendList">조회</base-button>
          </div>
        </div>
      </div>

      <!---->

      <!-- <table class="fax-table fax-table-input" style="width: 100%">
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
              style="height: 30px; width: 6rem"
              id="searchGubun"
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
          <th>결재구분</th>
          <td>
            <select v-model="sendStatus" class="fax-table-input" style="height: 30px">
              <option value="전체" selected>전체</option>
              <option value="결재완료">결재완료</option>
              <option value="결재대기">결재대기</option>
              <option value="전송완료">전송완료</option>
              <option value="전송실패">전송실패</option>
              <option value="반려">반려</option>
              <option value="회수">회수</option>
            </select>
          </td>
          <td></td>
          <td></td>
          <td>
            <div class="text-center" style="float: right">
              <base-button type="danger" @click="getSendList">조회</base-button>
            </div>
          </td>
        </tr>
      </table> -->

      <table class="fax-table table-hover" style="width: 100%">
        <tr class="ApprArea-header">
          <th class="fax-table-display">제목</th>
          <th class="fax-table-display">요청일자</th>
          <th class="fax-table-display">작성자</th>
          <th class="fax-table-display-none">장수</th>
          <th class="fax-table-display">결재자</th>
          <th class="fax-table-display">결재여부</th>
          <th class="fax-table-display">결과</th>
          <th class="fax-table-display-none">대기/실패/성공/전체</th>
        </tr>
        <tbody>
          <tr v-for="(send, index) in sendList" :key="index" @click="getSendDetail(send.발송번호)">
            <td class="fax-table-display">{{ send.제목 }}</td>
            <td class="fax-table-display">{{ send.등록일자 }}</td>
            <td class="fax-table-display">{{ send.발송자 }}</td>
            <td class="fax-table-display-none">{{ send.페이지수 }}</td>
            <td class="fax-table-display">{{ send.결재자이름 }}</td>
            <td class="fax-table-display">{{ send.결재상태 }}</td>
            <td class="fax-table-display">{{ send.상태 }}</td>
            <td class="fax-table-display-none">
              {{ send.대기 }} / {{ send.실패 }} / {{ send.성공 }} / {{ send.전체 }}
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 컴포넌트 MyModal -->
    <modal :show.sync="isModalState" modal-classes="modal-dialog-centered modal-big">
      <h6 slot="header" class="modal-title" id="modal-title-default"></h6>
      <send-detail :sendDetail="sendDetail" :getSendList="getSendList"></send-detail>
    </modal>
  </section>
</template>

<script>
import { mapGetters } from "vuex";
import http from "@/common/axios.js";
import alertify from "alertifyjs";
import Modal from "@/components/Modal.vue";
import SendDetail from "@/views/SendDetail.vue";

export default {
  name: "send-list",
  components: {
    Modal,
    SendDetail,
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
  data() {
    return {
      sendList: [],
      sendDetail: [],
      searchFrom: "",
      searchTo: "",
      modal: false,
      sendStatus: "전체",
      /*전송결과 상세*/
      sendStatusDetail: [],
      deptUsers: [],
      receiver: "",
      senderId: "All",
    };
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

    // 조회
    async getSendList() {
      this.$store.commit("SET_LOADING_TRUE");

      try {
        console.log("status===>", this.sendStatus);
        let response = await http.post("/sendRecieve", {
          userId: this.userInfo.userId,
          searchFrom: this.searchFrom,
          searchTo: this.searchTo,
          status: this.sendStatus,
          senderId: this.senderId,
          receiver: this.receiver,
        });

        let { data } = response;
        this.$store.commit("SET_LOADING_FALSE");

        if (data != null) {
          // 전송 성공
          console.log(data);
          this.sendList = data;
        } else {
          console.log("전송 실패");
        }
      } catch (error) {
        // 전송 실패
        console.log("오류메시지 - ", data.Message);
        alertify.error("실패했습니다.", 1.5);
      }
    },

    // 상세보기
    async getSendDetail(apprNo) {
      this.$store.commit("SET_LOADING_TRUE");

      this.modal = true;
      try {
        let response = await http.post("/sendRecieveDetail", {
          userKey: apprNo,
        });
        // console.log(response);
        let { data } = response;
        this.$store.commit("SET_LOADING_FALSE");

        if (data != null) {
          // 전송 성공
          console.log(data);
          this.sendDetail = data[0];

          //발신자 팩스번호 포맷지정
          for (let i in data[0].받는사람정보) {
            this.sendDetail.받는사람정보[i].팩스번호 = this.setFormatting(
              data[0].받는사람정보[i].팩스번호
            );
          }

          this.$store.commit("SET_MODAL_OPEN");

          alertify.success("상세 조회가 완료되었습니다.", 1.5);
        } else {
          console.log("상세 조회 실패");
        }
      } catch (error) {
        // 전송 실패
        console.log("오류메시지 - ", error);
        alertify.error("상세 조회가 실패했습니다.", 1.5);
      }
    },
    async getDeptUsers() { //부서 내 작성자 가져오기
      this.modal = true;
      try {
        let response = await http.post("/getDeptUsers", {
          userId: this.userInfo.userId,
        });

        let { data } = response;

        if (data != null) {
          // 전송 성공
          // console.log(data);
          this.deptUsers = data;
        } else {
          console.log("상세 조회 실패");
        }
      } catch (error) {
        // 전송 실패
        console.log("오류메시지 - ", error);
        alertify.error("상세 조회가 실패했습니다.", 1.5);
      }
    },

    //모달 닫기
    closeModal() {
      this.modal = false;
    },
  },
  mounted() {
    this.getNow();
    this.getSendList();
    this.getDeptUsers();
  },
};
</script>

<style scoped>
.popup-view {
  opacity: 0;
  display: none;
  visibility: hidden;
}
.active {
  opacity: 1;
  display: block;
  visibility: visible;
}
.fax-table-input {
  /* width: 10rem; */
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
  margin: 0 auto;
}
.fax-input-box {
  background-color: rgb(224, 224, 224);
  font-size: small;
  font-weight: 600;
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
    width: 100%;
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
    width: 14rem;
  }

  .fax-input-box {
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
