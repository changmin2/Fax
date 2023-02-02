<template>
  <section class="section">
    <div class="container">
      <div v-if="isComplete" class="send-title display-4 mb-4 font-weight-800 text-default">
        결재정보
      </div>
      <div v-else class="send-title display-4 mb-4 font-weight-800 text-default">미결재정보</div>

      <div class="noApproval-detail">
        <div class="left-content col-4">
          <span class="mt-3">> 결재요청정보</span>

          <table class="fax-table fax-table-detail" style="width: 100%">
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
          <table class="fax-table fax-table-detail">
            <thead>
              <tr>
                <th scope="col">이름</th>
                <th scope="col">팩스번호</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(receive, index) in noApprDetail.받는사람정보" :key="index">
                <td>{{ receive.이름 }}</td>
                <td>{{ receive.팩스번호 }}</td>
              </tr>
            </tbody>
          </table>

          <!-- <span class="mt-3">> 개인정보검출내용</span> -->
          <div v-if="!isComplete">
            <span class="mt-3">> 결재 승인 및 반송</span>
            <table class="fax-table fax-table-detail">
              <thead>
                <tr>
                  <th scope="col">결재상태</th>
                  <th scope="col" style="height: 100px">반려 사유</th>
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
              반려
            </base-button>
          </div>
        </div>

        <div class="col-8" v-if="noApprDetail.발송고유번호">
          <iframe
            :src="`https://bnksys.s3.ap-northeast-2.amazonaws.com/${noApprDetail.파일명}`"
            style="width: 100%; height: 100%"
          ></iframe>
        </div>
        <div class="col" v-else>이미지 로딩중..</div>
      </div>
    </div>
  </section>
</template>

<script>
import http from "@/common/axios.js";
import alertify from "alertifyjs";
import Modal from "@/components/Modal.vue";

export default {
  name: "no-approval",
  props: ["noApprDetail", "isComplete"],
  components: {
    Modal,
  },
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
      this.$store.commit("SET_LOADING_TRUE");
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
          alertify.alert("성공", "결재 승인 완료되었습니다.", 1.5);
          // 승인 시 모달 닫고 리스트로 이동
          this.$store.commit("SET_MODAL_CLOSE");
          this.$store.commit("SET_LOADING_FALSE");
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
      this.$store.commit("SET_LOADING_TRUE");
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
          // 반려 시 모달 닫고 리스트로 이동
          this.$store.commit("SET_MODAL_CLOSE");
          this.$store.commit("SET_LOADING_FALSE");
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

.left-content {
  display: flex;
  flex-direction: column;
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
