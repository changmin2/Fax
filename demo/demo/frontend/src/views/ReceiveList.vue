<template>
  <section class="section">
    <div class="main-container">
      <div class="receive-title display-4 mb-4 font-weight-800 text-default">
        받은팩스함 - <span style="color: #d7191f; display: inline">{{ userInfo.deptName }}</span>
      </div>

      <form id="master" role="form" style="width: 100%">
        <table class="fax-table fax-table-input">
          <colgroup>
            <col style="width: 9%" />
            <col style="width: 37%" />
            <col style="width: 9%" />
            <col style="width: 37%" />
            <col />
          </colgroup>
          <tr>
            <th>조회기간</th>
            <td>
              <form>
                <input
                  type="date"
                  id="searchFrom"
                  value="today"
                  class="fax-form-input"
                  v-model="searchFrom"
                  @change="setDateInfo(searchFrom)"
                />
                ~
                <input
                  type="date"
                  id="searchTo"
                  value="today"
                  class="fax-form-input"
                  v-model="searchTo"
                  @change="setDateInfo(searchTo)"
                />
              </form>
            </td>
            <th>조건</th>
            <td>
              <select
                name="searchGubun"
                id="searchGubun"
                class="fax-form-input ml-1"
                style="width: 6rem"
              >
                <option selected>받는사람</option>
              </select>
              <input
                type="text"
                id="searchGubunData"
                name="searchGubunData"
                class="fax-form-input ml-2"
              />
            </td>
            <td>
              <div class="text-center" style="float: right">
                <base-button type="danger" @click="apprsearch">조회</base-button>
              </div>
            </td>
          </tr>
        </table>
      </form>

      <div class="body-content ApprArea" style="width: 100%">
        <table class="fax-table" style="width: 100%">
          <!-- <tr>
                  <td colspan = "7">
                      <div class="text-center" style="float:left;">
                        <base-button type="secondary" class="no-approval-btn btn float-left" @click="apprdelete">
                          삭제
                        </base-button>
                        <base-button type="secondary" class="no-approval-btn btn float-left"@click="restore">
                          확인복원
                        </base-button>
                      </div>
                  </td>
                </tr> -->
          <tr class="ApprArea-header">
            <!--<th>
                    <input type="checkbox">
                  </th> -->
            <th>확인</th>
            <th>상세보기</th>
            <th>발신자팩스번호</th>
            <th>받은날짜</th>
            <th>확인여부</th>
            <th>최초확인자</th>
            <th>최초확인날짜</th>
            <!--<th>주소록</th>-->
          </tr>

          <tr v-for="(receive, index) in receivelist" :key="index">
            <!--<td></td>-->
            <td>{{ receive.receive_No_SEQ }}</td>
            <td>
              <base-button @click="receiveDetail(receive.receive_No_SEQ)">상세</base-button>
            </td>
            <td>{{ receive.fax_NO }}</td>
            <td>{{ receive.receive_DATE }}</td>
            <td>{{ receive.read_YN }}</td>
            <td>{{ receive.read_USER }}</td>
            <td>{{ receive.read_DATE }}</td>
            <!--<td></td>-->
          </tr>
        </table>
      </div>
    </div>

    <!-- 컴포넌트 MyModal -->
    <modal
      :show.sync="modal"
      body-classes="p-1"
      modal-classes="modal-dialog-centered modal-big"
      class="modal-class"
      @search="apprsearch"
    >
      <h6 slot="header" class="modal-title" id="modal-title-default"></h6>
      <receive-detail :receivelistDetail="receivelistDetail"></receive-detail>
    </modal>
  </section>
</template>

<script>
import { mapGetters } from "vuex";
import http from "@/common/axios.js";
import alertify from "alertifyjs";
import Modal from "@/components/Modal.vue";
import ReceiveDetail from "@/views/ReceiveDetail.vue";

export default {
  name: "receive_list",
  components: {
    Modal,
    ReceiveDetail,
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
      receivelist: [],
      receivelistDetail: {},
      searchFrom: "",
      searchTo: "",
      modal: false,
      apprNo: "",
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

      var searchFrom = new Date(year, month, day - 6);
      this.searchFrom = searchFrom.toISOString().split("T")[0];
      this.searchTo = today.toISOString().split("T")[0];
      this.setDateInfo(this.searchFrom);
      this.setDateInfo(this.searchTo);
      console.log(searchFrom + "," + searchTo);
    },

    // 조회
    async apprsearch() {
      this.$store.commit("SET_LOADING_TRUE");
      try {
        let response = await http.post("/recieveList", {
          RFax_No: this.userInfo.faxNo,
          Date_Start: this.searchFrom,
          Date_End: this.searchTo,
        });

        let { data } = response;
        this.$store.commit("SET_LOADING_FALSE");

        if (data != null) {
          // 전송 성공
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

    // 상세보기
    async receiveDetail(apprNo) {
      this.modal = true;
      this.apprNo = apprNo;

      let user_id = this.userInfo;
      console.log(apprNo);
      try {
        let response = await http.post("/recieveDetail", {
          RFax_No_Seq: apprNo,
          userId: user_id.userId,
        });
        let { data } = response;

        if (data != null) {
          // 전송 성공
          this.receivelistDetail = data;
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

    async apprdelete() {},
    async restore() {},
    //모달 닫기
    closeModal() {
      this.modal = false;
      this.apprsearch();
    },
  },
  mounted() {
    this.getNow();
    this.apprsearch();
  },
};
</script>

<style scoped>
.fax-form-input {
  height: 30px;
}
</style>
