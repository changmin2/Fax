<template>
  <section class="section section-shaped section-hero login-section section-lg my-0">
    <div class="shape shape-style-1"></div>
    <div class="container pt-lg-md">
      <div
        class="send-title display-4 mb-4 font-weight-800 text-default"
      >
        발신대기함
      </div>

      <div class="row" style="width: 100%;">
        <div class="top-content search-area">
              <table style="width: 100%;">
                <colgroup>
                    <col style="width:10%;">
                    <col style="width:36%;">
                    <col style="width:10%;">
                    <col style="width:36%;">
                    <col>
                </colgroup>
                <tr>
                  <th>조회기간</th>
                  <td>
                    <form style="border: none">
                      <input
                        type="date"
                        id = "searchFrom"
                        value="today"
                        class="form-select"
                        v-model="searchFrom"
                        @change="setDateInfo(searchFrom)"
                      />
                      ~
                      <input
                        type="date"
                        id = "searchTo"
                        value="today"
                        class="form-select"
                        v-model="searchTo"
                        @change="setDateInfo(searchTo)"
                      />
                    </form>
                  </td>
                  <th>조건</th>
                  <td>
                    <select name="searchGubun" id="searchGubun">
                       <option value="" selected>받는사람/팩스번호</option>
                       <option value="1">받는사람</option>
                       <option value="2">팩스번호</option>
                    </select>
                    <input type="text" id="searchGubunData" name = "searchGubunData" style=""/>
                  </td>
                  <td>
                      <div class="text-center" style="float:right;">
                        <base-button type="danger" @click="apprsearch">조회</base-button>
                      </div>
                  </td>
                </tr>
              </table>
          </div>
          <div class = "body-content ApprArea" style="width: 100%;">
            <table style="width: 100%;">
                <tr>
                  <td colspan = "7">
                      <div class="text-center" style="float:left; margin-bottom:5   px;">
            <!--           <base-button type="secondary" class="no-approval-btn btn float-left" @click="apprdelete">
                          삭제
                        </base-button>  -->
                        <base-button type="secondary" class="no-approval-btn btn float-left"@click="reuse">
                          재사용
                        </base-button>
                        <base-button type="secondary" class="no-approval-btn btn float-left" @click="resend">
                          재전송
                        </base-button>
                        <base-button type="secondary" class="no-approval-btn btn float-left" @click="back">
                          회수
                        </base-button>
                      </div>
                  </td>
                </tr>
                <tr class="ApprArea-header">
                  <th>
                    <input type="checkbox">
                  </th>
                  <!--<th>수신자</th>
                  <th>수신번호</th> -->
                  <th>제목</th>
                  <!--<th>장수</th> -->
                  <th>상세보기</th>
                  <th>요청일자</th>
                  <th>결재자</th>
                  <th>결재여부</th>
                  <th>결과(성공/실패)</th>
                </tr>

                <tr v-for="(receive, index) in receivelist" :key="index" >
                <td><input type="checkbox" /></td>
                <td>{{ receive.제목 }}</td>
                <td>
                  <base-button @click="receiveDetail( receive.발송번호 )">상세test</base-button>
                </td>
                <td>{{ receive.등록일자 }}</td>
                <td>{{ receive.결재자이름 }}</td>
                <td>{{ receive.결재상태 }}</td>
                <td>{{ receive.상태 }}</td>
                </tr>
            </table>
          </div>
        </div>
      </div>

        <!-- 컴포넌트 MyModal -->
        <modal :show.sync="modal"
           modal-classes="modal-big"
           v-if="detailOpen" 
        >
            <h6 slot="header" class="modal-title" id="modal-title-default">발신팩스내역</h6>
            <table style="width:100%;">
               <colgroup>
                   <col style="width:40%;">
                   <col style="width:60%;">
               </colgroup>
               <tr>
                 <td>
                    <div class="left-content col">
                       <span class="mt-3">> 보낸사람</span>
                       <table class="no-approval-table table">
                           <colgroup>
                               <col style="width:50%;">
                               <col style="width:50%;">
                           </colgroup>
                         <thead>
                           <tr>
                             <th scope="col">보낸사람</th>
                             <th scope="col">팩스번호</th>
                           </tr>
                         </thead>
                         <tbody>
                             <tr>
                                 <td>{{ receivelistDetail[0].발송자}}</td>
                                 <td>{{ receivelistDetail[0].팩스번호}}</td>
                             </tr>
                         </tbody>
                       </table>

                       <span class="mt-3">> 받는곳</span>
                       <table class="no-approval-table table">
                           <colgroup>
                               <col style="width:50%;">
                               <col style="width:50%;">
                           </colgroup>
                         <thead>
                           <tr>
                             <th scope="col">받는사람</th>
                             <th scope="col">팩스번호</th>
                           </tr>
                         </thead>
                         <tbody>
                           <tr v-for="(detail, index) in receivelistDetail[0].받는사람정보" :key="index" >
                             <td>{{ detail.이름 }}</td>
                             <td>{{ detail.팩스번호 }}</td>
                           </tr>
                         </tbody>
                       </table>

                       <span class="mt-3">> 발신 정보</span>
                       <table class="no-approval-table table">
                           <colgroup>
                               <col style="width:50%;">
                               <col style="width:50%;">
                           </colgroup>
                         <thead>
                           <tr>
                             <th scope="col">발신일시</th>
                             <th scope="col">발신상태</th>
                           </tr>
                         </thead>
                         <tbody>
                           <tr>
                             <td>{{ receivelistDetail[0].등록일자 }}</td>
                             <td>{{ receivelistDetail[0].상태 }}</td>
                           </tr>
                         </tbody>
                       </table>
                     </div>

                 </td>
                 <td>
                     <div class="col" style="width:100%; height:100%;">
                       <iframe
                         src="https://bnksys.s3.ap-northeast-2.amazonaws.com/BNK00120230127024348_6.pdf"
                         style="width: 100%; height: 95%;"
                       ></iframe>
                     </div>
                 </td>
               </tr>
            </table>
            </div>
        </modal>

    </div>
  </section>
</template>

<script>
import { mapGetters } from "vuex";
import http from "@/common/axios.js";
import alertify from "alertifyjs";
import Modal from "@/components/Modal.vue";

export default {
  components: {
    Modal
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
        receivelistDetail: ["받는사람정보"],
        detailOpen : false,
        searchFrom: '',
        searchTo: '',
        modal: false,
    };
  },
  methods: {
    // date 설정
    setDateInfo( datedata ) {
      let dateInfo = this.datedata;
      dateInfo = datedata.split("-");
      dateInfo = dateInfo.join("");
      console.log(dateInfo);
    },
    getNow() {
      const today = new Date();

      const year = today.getFullYear(); // 년
      const month = today.getMonth();   // 월
      const day = today.getDate();      // 일

      var searchFrom = new Date(year, month, day - 7)
      this.searchFrom = searchFrom.toISOString().split("T")[0];
      var searchTo = new Date(year, month, day + 7)
      this.searchTo = searchTo.toISOString().split("T")[0];
      this.setDateInfo(this.searchFrom);
      this.setDateInfo(this.searchTo);
      console.log(searchFrom + ","+ searchTo);
    },

    // 조회
    async apprsearch() {

      try {
        let response = await http.post("/sendRecieve", {
          userId: this.userInfo.userId,
          searchFrom: this.searchFrom,
          searchTo: this.searchTo,
        });

        let { data } = response;

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
      try {
        let response = await http.post("/sendRecieveDetail", {
          userKey: apprNo,
        });
        console.log(response);
        let { data } = response;

        if (data != null) {
          // 전송 성공
          console.log(data);
          console.log("상세 조회 성공");
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
    // 삭제
    async apprdelete() {

      },
    // 재사용
    async reuse() {

      },
    // 재발송
    async resend() {

      },

    // 회수
    async back() {

      },

    openModal() {
      this.modal = true
    },
    //모달 닫기
    closeModal() {
      this.modal = false
    },

  },
  mounted() {
    this.getNow();
    this.apprsearch();
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
  border-top : 2px solid black;
  border-bottom : 2px solid black;
}
.search-area th, td {
  height: 40px;
}


th {
  background-color: rgb(224, 224, 224);
  font-weight: normal;
  text-align: center;
}

.body-content {
  margin-top : 10px;
}
.ApprArea-header th{

  background-color: rgb(224, 224, 224);
  border : 1px solid;
}

.popup-view{
  opacity: 0;
  display: none;
  visibility: hidden;
}
.active{
  opacity: 1;
  display: block;
  visibility: visible;
}

.modal-big{
  max-width : 1400px;
}
</style>