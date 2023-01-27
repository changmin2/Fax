<template>
  <section class="section section-shaped section-hero login-section section-lg my-0">
    <div class="shape shape-style-1"></div>
    <div class="container pt-lg-md">
      <div
        class="send-title display-4 mb-4 font-weight-800 text-default"
        style="text-shadow: 2px 1px 2px rgba(0, 0, 0, 0.2)"
      >
        발신대기함
      </div>

      <div class="row" style="width: 100%;">
        <div class="top-content search-area">
            <form id ="master" role="form" style="width: 100%;">
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
                    <input type="text" id="searchFrom" name = "searchFrom" style="width:120px;"/>
                    ~
                    <input type="text" id="searchTo" name = "searchTo" style="width:120px;"/>
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
            </form>
          </div>
          <div class = "body-content ApprArea" style="width: 100%;">
            <table style="width: 100%;">
                <tr>
                  <td colspan = "7">
                      <div class="text-center" style="float:left;">
                        <base-button type="secondary" class="no-approval-btn btn float-left" @click="apprdelete">
                          삭제
                        </base-button>
                        <base-button type="secondary" class="no-approval-btn btn float-left"@click="reuse">
                          재사용
                        </base-button>
                        <base-button type="secondary" class="no-approval-btn btn float-left" @click="resend">
                          재전송
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

               <tr v-for="(receive, index) in getterReceiveList"
                  :key="index"
                >
                <td>{{ receive.No }}</td>
                <td>{{ receive.No }}</td>
                </tr>
            </table>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import { mapGetters } from "vuex";

export default {
  components: {

  },
  computed: {
    ...mapGetters({
      getterReceiveList: "getReceiveList",
    }),
  },
  data() {
    return {
        list: {},
    };
  },
  methods: {
    // 조회
    async apprsearch() {

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
  }

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

</style>