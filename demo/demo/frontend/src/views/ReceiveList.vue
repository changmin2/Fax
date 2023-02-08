<template>
  <section class="section">
    <div class="main-container">
      <div class="receive-title display-4 mb-4 font-weight-800 text-default">
        받은팩스함 - <span style="color: #d7191f; display: inline">{{ userInfo.deptName }}</span>
      </div>

      <!---->
      <div class="fax-input">
        <div class="fax-input-row">
          <div class="fax-input-box">조회기간</div>
          <div class="fax-input-content fax-input-content-date">
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
          <div class="fax-input-content fax-input-content-receiver">
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
            <base-button type="danger" @click="apprsearch" class="mobile-btn">조회</base-button>
          </div>
        </div>
      </div>

      <!---->

      <!-- <table class="fax-table fax-table-input" style="width: 100%">
        <colgroup>
          <col style="width: 9%" />
          <col style="width: 37%" />
          <col style="width: 9%" />
          <col style="width: 37%" />
          <col />
        </colgroup>
        <tr class="fax-table-input-tr">
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
      </table>-->

      <div class="body-content ApprArea" style="width: 100%">
        <table class="fax-table table-hover" style="width: 100%">
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
          <tbody>
            <tr class="ApprArea-header fax-table-tr">
              <th class="fax-table-display">받은날짜</th>
              <th class="fax-table-display">제목</th>
              <th class="fax-table-display">발신자팩스번호</th>

              <th class="fax-table-display-none">확인여부</th>
              <th class="fax-table-display-none">최초확인자</th>
              <th class="fax-table-display-none">최초확인날짜</th>
            </tr>

            <tr
              v-for="(receive, index) in receivelist"
              :key="index"
              @click="receiveDetail(receive.receive_No_SEQ)"
              class="fax-table-tr"
            >
              <td class="fax-table-display">{{ receive.receive_DATE }}</td>
              <td class="fax-table-display">{{ receive.title }}</td>
              <td class="fax-table-display">{{ receive.sender_NO }}</td>

              <td class="fax-table-display-none">{{ receive.read_YN }}</td>
              <td class="fax-table-display-none">{{ receive.read_user_name }}</td>
              <td class="fax-table-display-none">{{ receive.read_DATE }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- 컴포넌트 MyModal -->
    <modal :show.sync="modal" modal-classes="modal-dialog-centered modal-big" @search="apprsearch">
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
      senderNo: "",
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
    async apprsearch() {
      this.$store.commit("SET_LOADING_TRUE");
      try {
        let response = await http.post("/recieveList", {
          RFax_No: this.userInfo.faxNo.replaceAll("-", ""),
          Date_Start: this.searchFrom,
          Date_End: this.searchTo,
          senderNo: this.senderNo,
        });

        let { data } = response;
        this.$store.commit("SET_LOADING_FALSE");

        if (data != null) {
          // 전송 성공
          console.log(data);
          console.log("전송 성공");
          this.receivelist = data;
          this.detailOpen = false;

          //발신자 팩스번호 포맷지정
          for (let i in data) {
            this.receivelist[i].sender_NO = this.setFormatting(data[i].sender_NO);
          }
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
      this.$store.commit("SET_LOADING_TRUE");
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
        this.$store.commit("SET_LOADING_FALSE");

        if (data != null) {
          // 전송 성공
          this.receivelistDetail = data;

          //발신자 팩스번호 포맷지정
          this.receivelistDetail.sender_NO = this.setFormatting(data.sender_NO);
          this.receivelistDetail.fax_NO = this.setFormatting(data.fax_NO);

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
    min-width: 3rem;
  }
  td {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    max-width: 6rem;
    padding-left: 4px;
    padding-right: 4px;
  }
}
</style>
