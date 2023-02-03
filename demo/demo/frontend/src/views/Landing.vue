<template>
  <section class="section section-shaped section-hero login-section section-lg my-0">
    <div class="container-fluid mt-5">
      <div class="row">
        <div class="col left-content">
          <div class="content-group">
            <div class="send-title font-weight-700 text-default">총건수</div>
            <hr />
            <div class="landing-total">
              <div class="row landing-total-group" style="width: 5rem">
                <span style="text-align: center">미확인</span>
                <span class="landing-total-num" type="secondary">{{ data.ReceiveCount }}건</span>
              </div>
              <div class="row landing-total-group" style="width: 5rem">
                <span style="text-align: center">미결재</span>
                <span class="landing-total-num" type="secondary">{{ data.NotApprCount }}건</span>
              </div>
            </div>
          </div>
          <div class="content-group">
            <div class="send-title font-weight-700 text-default">
              <i class="fa fa-volume-down" aria-hidden="true"></i> 공지사항
            </div>
            <div class="table-container">
              <hr />
              <table class="fax-table content-group-notice table-hover">
                 <tbody>
                <tr v-for="(notice, index) in data.NoticeInfo" :key="index" @click="openModal(notice)" role="button">
                  <td scope="row" class="text-left pl-2">
                    {{ notice.TITLE }}</td>

                  <td style="width: 8rem" class="text-right pr-2">{{ notice.DATE }}</td>
                </tr>
                 </tbody>
                <!-- <tr class="ApprArea-header">
                  <th>내용</th>
                  <th>제목</th>
                </tr> -->
              </table>
            </div>
          </div>
        </div>
        <div class="col right-content">
          <div class="content-group">
            <div class="send-title font-weight-700 text-default">미확인</div>
            <div class="table-container">
              <hr />
              <table class="fax-table">
                <tr class="ApprArea-header">
                  <th>발신자팩스번호</th>
                  <th>페이지수</th>
                  <th>수신일시</th>
                </tr>
                <tr v-for="(receive, index) in data.ReceiveList" :key="index">
                  <td>{{ receive.SENDER_NO }}</td>
                  <td>{{ receive.PAGE_CNT }}</td>
                  <td>{{ receive.RECEIVE_DATE }}</td>
                </tr>
              </table>
            </div>
          </div>
          <div class="content-group">
            <div class="send-title font-weight-700 text-default">
              <i class="fa fa-exclamation-circle" aria-hidden="true"></i> 미결재
            </div>

            <div class="table-container">
              <hr />
              <table class="fax-table">
                <tr class="ApprArea-header">
                  <th>제목</th>
                  <th>기안자</th>
                  <th>등록일</th>
                </tr>

                <tr v-for="(noApproval, index) in data.NotApprList" :key="index">
                  <td>{{ noApproval.TITLE }}</td>
                  <td>{{ noApproval.NAME }}</td>
                  <td>{{ noApproval.INSERT_DATE }}</td>
                </tr>
              </table>
            </div>
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

      searchFrom: "",
      searchTo: "",
      data: [],
    };
  },
  computed: {
    ...mapGetters({
      userInfo: "getUserInfo",
    }),
  },
  created() {
    this.getMainInfo();
  },
  methods: {
    // landing 페이지 날짜 -> 현재시간 ~ 일주일 전까지
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

    //공지사항 dialog
    openModal(notice) {
      alertify.alert(notice.TITLE, notice.CONTENT).set({transition:'fade', resizable:true}); 
    },

    // mainInfo
    async getMainInfo() {
      this.$store.commit("SET_LOADING_TRUE");
      try {
        let response = await http.post("/mainInfo", {
          userId: this.userInfo.userId,
        });

        let { data } = response;
        this.$store.commit("SET_LOADING_FALSE");

        if (data != null) {
          // 전송 성공
          // if (data.length > 3) data = data.splice(0, 3);
          console.log(data);
          console.log("공지사항정보", data.NoticeInfo);
          console.log("전송 성공");
          this.data = data;

          //발신자 팩스번호 포맷지정
          for(let i in data.ReceiveList){
             this.data.ReceiveList[i].SENDER_NO = this.setFormatting(data.ReceiveLsit[i].SENDER_NO);
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
  },
};
</script>
<style scoped>
.landing-total {
  display: flex;
  justify-content: space-around;
  align-content: center;
  margin-top: 2rem;
  /* width: 100%; */
}
.landing-total-group {
  width: 30rem;
  margin: 1rem;
}
.landing-total-num {
  background-color: rgb(239, 239, 239);
  border: 1px solid rgb(200, 200, 200);
  border-radius: 1rem;
  width: 15rem;
  height: 3rem;
  text-align: center;
  font-size: x-large;
  line-height: 3rem;
}
.left-content {
  /* background-color: pink; */
  margin-left: 10px;
}
.right-content {
  /* background-color: aquamarine; */
  margin-right: 10px;
}
.content-group {
  width: 30rem;
  height: 20rem;
}
.send-title {
  font-size: large;
}
.table-container {
  width: 300px;
  margin-bottom: 10px;
}
.body-content {
  margin-top: 10px;
}
.fax-table {
  table-layout: fixed;
  width: 32rem;
  /* height: 100px; */
}
.fax-table td {
  height: 2.3rem;
}
.fax-table-landing {
  width: 12rem;
  text-align: center;
}

.fax-table-landing {
}
.fax-table-landing th {
  background-color: white;
}
.fax-table-landing td {
  background-color: gainsboro;
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
  margin-top: 5px;
  margin-bottom: 5px;
}

.content-group-notice {
  border: 1px solid gainsboro;
}

.admin-container {
  /* margin-right: 14rem; */
}
</style>
