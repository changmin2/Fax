<template>
    <div class="container receive-detail-container">
      <div
        class="receive-title display-4 mb-4 font-weight-800 text-default"
        style="text-shadow: 2px 1px 2px rgba(0, 0, 0, 0.2)"
      >
        부서 팩스함
      </div>

      <div class="receive-detail">
        <div class="left-content col">
          <span class="mt-3">> 보낸 곳</span>

          <table class="receive-detail-table table" style="width: 100%">
            <thead>
              <tr>
                <th scope="col">팩스번호</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>{{ receivelistDetail.sender_NO }}</td>
              </tr>
            </tbody>
          </table>

          <span class="mt-3">> 받는곳</span>
          <table class="receive-detail-table table">
            <thead>
              <tr>
                <th scope="col">부서명</th>
                <th scope="col">팩스번호</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>{{ this.userInfo.deptName }}</td>
                <td>{{ receivelistDetail.fax_NO }}</td>
              </tr>
            </tbody>
          </table>

          <span class="mt-3">> 수신 정보</span>
          <table class="receive-detail-table table">
            <colgroup>
              <col style="width: 50%" />
              <col style="width: 25%" />
              <col style="width: 25%" />
            </colgroup>
            <thead>
              <tr>
                <th scope="col">수신일시</th>
                <th scope="col">장수</th>
                <th scope="col">수신상태</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>{{ receivelistDetail.receive_DATE }}</td>
                <td>{{ receivelistDetail.page_CNT }}</td>
                <td>{{ receivelistDetail.Result }}</td>
              </tr>
            </tbody>
          </table>

          <span class="mt-3">> 제목</span>
          <div class="row ml-1">
            <base-input class="receive-detail-title-input" style=""
             v-model="receivelistDetail.title"
             >
             </base-input>
            <base-button type="secondary" class="receive-detail-btn ml-2" @click="savetitle"> 제목저장 </base-button>
          </div>
        </div>

        <div class="col col-8">
          <iframe
            v-bind:src="`${ receivelistDetail.filePath }`"
            style="width: 100%; height: 100%;"
          ></iframe>
        </div>
      </div>
    </div>
</template>

<script>
import { mapGetters } from "vuex";
import http from "@/common/axios.js";
import alertify from "alertifyjs";

export default {
  name: "receive_detail",
  props: ["receivelistDetail"],
  computed: {
    ...mapGetters({
      userInfo: "getUserInfo",
    }),
  },
  data() {
    return {
       title: '',
       RFax_No_Seq: '',
    };
  },
  updated(){
    //상세에서 props 넘긴 후 호출
     this.title = this.receivelistDetail.title;
     this.RFax_No_Seq = this.receivelistDetail.receive_No_SEQ;
  },
  methods: {
    //제목 값세팅
    getTitle: function() {
      if(this.title == ''){
        this.title = this.receivelistDetail.title;
      }
        return this.title;
    },
    setTitle: function(value) {
        this.title = value;
    },
    // 제목저장
    async savetitle() {
        console.log(this.title);
        console.log(this.RFax_No_Seq);
      try {
        let response = await http.post("/titleSave", {
          RFax_No_Seq : this.RFax_No_Seq,
          Title: this.title,

        });

        let { data } = response;

        if (data != null) {
          // 전송 성공
          console.log(data);
          console.log("전송 성공");
          alertify.success("제목 저장이 완료되었습니다.", 1.5);
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
.left-content {
  display: flex;
  flex-direction: column;
}
.receive-detail-container {
  max-width: 90%;
}
.receive-detail {
  display: flex;
  max-width: 100%;
}


th {
  background-color: rgb(224, 224, 224);
  font-weight: normal;
}

.receive-detail-table {
  font-size: small;
}
.receive-detail-table th,
.receive-detail-table td {
  text-align: center;
  height: 35px;
  line-height: 0px;
}
.receive-detail-table th {
  width: 100px;
}
.receive-detail-table td {
  width: 250px;
  border: 0.0625rem solid #dee2e6;
}
.no-approval-btn-group {
}
.no-approval-btn {
  padding: 5px;
  /* border: 0.0625rem solid #bcbcbc; */
}
.receive-detail-title-input {
  width: 270px;
}
.receive-detail-title-input >>> .form-control {
  height: 40px;
}
</style>
