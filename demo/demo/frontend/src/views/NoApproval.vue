<template>
  <section class="section">
    <div class="container">
      <div v-if="isComplete" class="send-title display-4 mb-4 font-weight-800 text-default">
        결재정보
      </div>
      <div v-else class="send-title display-4 mb-4 font-weight-800 text-default">미결재정보</div>

      <div class="noApproval-detail">
        <div class="left-content col">
          <span class="mt-3">> 결재요청정보</span>

          <table class="no-approval-table table" style="width: 100%">
            <thead>
              <tr>
                <th scope="col">결재요청자</th>
                <th scope="col">제목</th>
                <th scope="col">요청일시</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>{{ noApprDetail.보내는사람 }}</td>
                <td>{{ noApprDetail.제목 }}</td>
                <td>{{ noApprDetail.요청일자 }}</td>
              </tr>
            </tbody>
          </table>

          <span class="mt-3">> 수신자정보</span>
          <table class="no-approval-table table">
            <thead>
              <tr>
                <th scope="col">이름</th>
                <th scope="col">팩스번호</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>{{ noApprDetail.받는사람 }}</td>
                <td>{{ noApprDetail.팩스번호 }}</td>
              </tr>
            </tbody>
          </table>

          <!-- <span class="mt-3">> 개인정보검출내용</span> -->
          <div v-if="!isComplete">
            <span class="mt-3">> 결재 승인 및 반송</span>
            <table class="no-approval-table table">
              <thead>
                <tr>
                  <th scope="col">결재상태</th>
                  <th scope="col" style="height: 100px">사유</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>{{ noApprDetail.상태 }}</td>
                  <td style="height: 100px">
                    <!-- <textarea
                      class="no-approval-textarea"
                      v-model="apprRemark"
                      @change="setRemark"
                      cols="30"
                      rows="5"
                    ></textarea> -->
                    <textarea
                      name="opinion"
                      class="no-approval-textarea"
                      cols="30"
                      rows="5"
                    ></textarea
                    ><br />
                  </td>
                </tr>
              </tbody>
            </table>
          </div>

          <!-- 승인 반송은 완료에서는 보이지 않게 -->
          <div v-if="!isComplete" class="no-approval-btn-group">
            <base-button type="secondary" class="no-approval-btn" @click="apprConfirm">
              승인
            </base-button>
            <base-button type="secondary" class="no-approval-btn" @click="apprBack">
              반송
            </base-button>
          </div>
        </div>

        <div class="col">
          <iframe
            src="https://bnksys.s3.ap-northeast-2.amazonaws.com/BNK00120230127024348_6.pdf"
            style="width: 600px; height: 500px"
          ></iframe>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import http from "@/common/axios.js";
import alertify from "alertifyjs";

export default {
  name: "no-approval",
  props: ["noApprDetail", "isComplete"],

  data() {
    return {
      apprRemark: "",
    };
  },
  created() {},
  methods: {
    setRemark() {
      console.log(this.apprRemark);
    },

    /* 승인 */
    async apprConfirm() {
      let formData = new FormData();
      formData.append("apprNo", this.noApprDetail.결재고유번호);
      try {
        let response = await http.post(`/apprOk`, formData);
        console.log(response);
        let { data } = response;

        if (data != null) {
          // 전송 성공
          console.log(data);
          console.log("결재 승인 성공");
          this.noApprovalList = data;

          alertify.alert("결재 승인 완료되었습니다.", 1.5);
        } else {
          console.log("결재 승인  실패");
        }
      } catch (error) {
        // 전송 실패
        console.log("오류메시지 - ", error);
        alertify.alert("결재 승인에 실패했습니다.", 1.5);
      }
    },

    /* 반송 */
    async apprBack() {
      let formData = new FormData();
      formData.append("apprNo", this.noApprDetail.결재고유번호);
      formData.append("apprRemark", this.apprRemark);
      try {
        let response = await http.post(`/apprReturn`, formData);
        console.log(response);
        let { data } = response;

        if (data != null) {
          // 전송 성공
          console.log(data);
          console.log("결재 반려 성공");
          this.noApprovalList = data;

          alertify.alert("성공", "결재 반려 완료되었습니다.", 1.5);
        } else {
          console.log("결재 반려  실패");
        }
      } catch (error) {
        // 전송 실패
        console.log("오류메시지 - ", error);
        alertify.alert("결재 반려에 실패했습니다.", 1.5);
      }
    },
  },
};
</script>

<style scoped>
.noApproval-detail {
  display: flex;
}
.no-proval-info {
}
.left-content {
  display: flex;
  flex-direction: column;
}
table {
  display: flex;
}
tbody {
  display: flex;
}

th,
td {
  display: block;
}

th {
  background-color: rgb(224, 224, 224);
  font-weight: normal;
}

.no-approval-table {
  font-size: small;
}
.no-approval-table th,
.no-approval-table td {
  text-align: center;
  height: 35px;
  line-height: 0px;
}
.no-approval-table th {
  width: 100px;
}
.no-approval-table td {
  width: 250px;
  border-top: 0.0625rem solid #dee2e6;
  border-right: 0.0625rem solid #dee2e6;
}
.no-approval-table td:last-child {
  border-bottom: 0.0625rem solid #dee2e6;
}
.no-approval-btn-group {
  margin-left: 240px;
}
.no-approval-btn {
  padding: 5px;
  width: 50px;
}
.no-approval-td-textarea {
  height: 80px;
}
.no-approval-textarea {
  max-height: 100%;
  height: inherit;
  width: 12rem;
  resize: none;
  margin: 0px;
  border: none;
  line-height: 10px;
}
</style>
