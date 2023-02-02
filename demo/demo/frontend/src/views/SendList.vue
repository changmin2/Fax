<template>
  <section class="section">
    <div class="main-container">
      <div class="send-title display-4 mb-4 font-weight-800 text-default">발신팩스함</div>

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
            <select name="searchGubun" class="fax-form-input" style="height: 30px" id="searchGubun">
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
      </table>

      <table class="fax-table" style="width: 100%">
        <tr class="ApprArea-header">
          <th>제목</th>
          <th>상세보기</th>
          <th>요청일자</th>
          <th>결재자</th>
          <th>결재여부</th>
          <th>결과(성공/실패)</th>
        </tr>

        <tr v-for="(send, index) in sendList" :key="index">
          <td>{{ send.제목 }}</td>
          <td>
            <base-button @click="getSendDetail(send.발송번호)">상세</base-button>
          </td>
          <td>{{ send.등록일자 }}</td>
          <td>{{ send.결재자이름 }}</td>
          <td>{{ send.결재상태 }}</td>
          <td>{{ send.상태 }}</td>
        </tr>
      </table>
    </div>

    <!-- 컴포넌트 MyModal -->
    <modal :show.sync="modal" modal-classes="modal-big" v-if="detailOpen" @search="getSendList">
      <h6 slot="header" class="modal-title" id="modal-title-default"></h6>
      <send-detail :sendDetail="sendDetail" ></send-detail>
    </modal>
  </section>
</template>

<script>
import { mapGetters } from "vuex";
import http from "@/common/axios.js";
import alertify from "alertifyjs";
import Modal from "@/components/Modal.vue";
import SendDetail from "./SendDetail.vue";

export default {
  components: {
    Modal,
    SendDetail,
  },
  computed: {
    ...mapGetters({
      isLogin: "getIsLogin",
      userKey: "getUserKey",
      userInfo: "getUserInfo",
    }),
  },
  data() {
    return {
      sendList: [],
      sendDetail: [],
      detailOpen: false,
      searchFrom: "",
      searchTo: "",
      modal: false,
      sendStatus: "전체",
      /*전송결과 상세*/
      sendStatusDetail: [],
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

      this.searchFrom = new Intl.DateTimeFormat("fr-CA", {year: "numeric", month: "2-digit", day: "2-digit"}).format(new Date(year, month, day - 6))
      this.searchTo = new Intl.DateTimeFormat("fr-CA", {year: "numeric", month: "2-digit", day: "2-digit"}).format(Date.now())
      this.setDateInfo(this.searchFrom);
      this.setDateInfo(this.searchTo);
      console.log(this.searchFrom +" ~ "+ this.searchTo);
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
        });

        let { data } = response;
        this.$store.commit("SET_LOADING_FALSE");

        if (data != null) {
          // 전송 성공
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
          this.detailOpen = true;

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

    //모달 닫기
    closeModal() {
      this.modal = false;
    },
  },
  mounted() {
    this.getNow();
    this.getSendList();
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

.modal-big {
  max-width: 1400px;
}
</style>
