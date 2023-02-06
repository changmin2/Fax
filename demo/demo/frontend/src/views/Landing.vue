<template>
  <section class="section section-shaped section-hero login-section section-lg my-0">
    <div class="main-container mt-5">
      <div class="left-content">
        <div class="content-group">
          <div class="send-title font-weight-700 text-default">
            <i class="ni ni-chart-pie-35"></i>
            총건수
          </div>

          <hr />
          <div class="landing-total">
            <router-link to="/receive-list">
              <div class="row landing-total-group">
                <span style="text-align: center">미확인</span>
                <span class="landing-total-num" type="secondary">{{ data.ReceiveCount }}건</span>
              </div>
            </router-link>
            <router-link to="/no-approval-list">
              <div class="row landing-total-group">
                <span style="text-align: center">미결재</span>
                <span class="landing-total-num" type="secondary">{{ data.NotApprCount }}건</span>
              </div>
            </router-link>
          </div>
        </div>
        <div class="content-group">
          <div class="send-title font-weight-700 text-default">
            <i class="ni ni-notification-70"></i>
            공지사항
          </div>
          <div class="table-container">
            <hr />
            <table class="fax-table content-group-notice table-hover">
              <tbody>
                <tr
                  v-for="(notice, index) in data.NoticeInfo"
                  :key="index"
                  @click="openModal(notice)"
                  role="button"
                >
                  <td scope="row" class="text-left pl-2">
                    {{ notice.TITLE }}
                  </td>

                  <td style="width: 8rem" class="text-right pr-2">{{ notice.DATE }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
      <div class="right-content">
        <div class="content-group">
          <router-link to="/receive-list">
            <div class="send-title font-weight-700 text-default">
              <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
              미확인
            </div>
          </router-link>
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
          <router-link to="/no-approval-list">
            <div class="send-title font-weight-700 text-default">
              <i class="fa fa-exclamation-circle" aria-hidden="true"></i> 미결재
            </div>
          </router-link>
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
  </section>
</template>

<script
  src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"
  integrity="sha384-IQsoLXl5PILFhosVNubq5LC7Qb9DXgDA9i+tQ8Zj3iwWAwPtgFTxbJ8NT4GN1R8p"
  crossorigin="anonymous"
></script>
<script
  src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.min.js"
  integrity="sha384-cVKIPhGWiC2Al4u+LWgxfKTRIcfu0JTxR+EQDz/bgldoEyl4H0zUF0QKbrJ0EcQF"
  crossorigin="anonymous"
></script>
<script>
import { mapGetters } from "vuex";
import http from "@/common/axios.js";
import alertify from "alertifyjs";

export default {
  name: "home",
  components: {},
  data() {
    return {
      // test용
      receiveList: [
        {
          SENDER_NO: "123234345",
          PAGE_CNT: "2",
          RECEIVE_DATE: "2022-02-23",
        },
      ],
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
      alertify
        .alert(notice.TITLE, "<br />" + notice.CONTENT)
        .set({ transition: "fade", resizable: true });
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
          for (let i in data.ReceiveList) {
            this.data.ReceiveList[i].SENDER_NO = this.setFormatting(data.ReceiveList[i].SENDER_NO);
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
.main-container {
  display: flex;
  justify-content: center;
}
.landing-total {
  display: flex;
  justify-content: space-evenly;
  align-content: center;
  width: 100%;
  height: 100%;
}
.landing-total-group {
  display: flex;
  width: inherit;
  height: 10rem;
  margin: 1rem;
}
.landing-total-num {
  background-color: rgb(239, 239, 239);
  border: 1px solid rgb(200, 200, 200);
  border-radius: 5rem;

  height: 10rem;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 4rem;
  line-height: 3rem;
}
.left-content {
  /* background-color: pink; */
  margin-left: 4rem;
}
.right-content {
  /* background-color: aquamarine; */
  margin-right: 4rem;
  margin-left: 4rem;
}
.content-group {
  display: flex;
  flex-direction: column;
  min-height: 18rem;
  width: 100%;
}
.send-title {
  font-size: large;
}
.table-container {
  width: 100%;
  margin-bottom: 10px;
}
.body-content {
  margin-top: 10px;
}
.fax-table {
  table-layout: fixed;
  width: 100%;
  /* height: 100px; */
}

hr {
  width: 100%;
  margin-top: 5px;
  margin-bottom: 5px;
}

.content-group-notice {
  border: 1px solid gainsboro;
}

.admin-container {
  /* margin-right: 14rem; */
}

@media screen and (max-width: 991px) {
  .main-container {
    display: flex;
    flex-direction: column;
    justify-content: center;
  }
  .left-content {
    margin-right: 4rem;
  }
  .right-content {
    margin-left: 4rem;
  }
  .content-group {
    display: flex;
    flex-direction: column;
    margin-bottom: 2rem;
    width: 100%;
    align-items: center;
  }
  .landing-total {
    display: flex;
    /* flex-wrap: wrap; */
  }

  .landing-total-num {
    width: 100%;
    height: 60%;
    font-size: 10vw;
  }
}
</style>
