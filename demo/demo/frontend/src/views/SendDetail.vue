<template>
  <div class="modal-container">
    <div class="send-title display-4 mb-4 font-weight-800 text-default">{{ sendDetail.제목 }}</div>

    <div class="send-detail-container">
      <div class="left-content col">
        <div>
          <span class="mt-3" style="display: inline; float: left"
            ><i class="fa fa-caret-right" aria-hidden="true"></i> 팩스 발신 정보</span
          >
          <div class="mt-3" style="display: inline; float: right; font-size: small">
            ※ 개인정보 포함 :

            <badge v-if="sendDetail.개인정보보호 == 'Y'" class="m-0" type="success" rounded
              >Y</badge
            >
            <badge v-if="sendDetail.개인정보보호 == 'N'" class="m-0" type="warning" rounded
              >N</badge
            >
          </div>
        </div>
        <table class="fax-table fax-table-detail" style="width: 100%">
          <thead>
            <tr>
              <th scope="col">발신일시</th>
              <th scope="col">장수</th>
              <th scope="col">발신상태</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>{{ sendDetail.등록일자 }}</td>
              <td>{{ sendDetail.페이지수 }}</td>
              <td>{{ sendDetail.상태 }}</td>
            </tr>
          </tbody>
        </table>

        <div>
          <span class="mt-3" style="display: inline; width: 30%; float: left"
            ><i class="fa fa-caret-right" aria-hidden="true"></i> 수신자정보</span
          >
          <div
            class="mt-3"
            style="display: inline; float: right; font-size: small"
            v-if="sendDetail.상태 == '전송완료' || sendDetail.상태 == '전송실패'"
          >
            ※ 전체 : {{ sendDetail.받는사람정보.length }}건, 상태 : (<span class="status_s"
              >성공</span
            >
            / <span class="status_f">실패</span>)
          </div>
        </div>
        <table class="fax-table fax-table-detail">
          <thead>
            <tr>
              <th scope="col">받는사람</th>
              <th scope="col">팩스번호</th>
              <th scope="col" v-if="sendDetail.상태 == '전송완료' || sendDetail.상태 == '전송실패'">상태</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(sendDetailStatus, index) in sendDetail.받는사람정보" :key="index">
              <td>{{ sendDetailStatus.이름 }} &#40; {{ sendDetailStatus.상호 }} &#41;</td>
              <td>{{ sendDetailStatus.팩스번호 }}</td>

              <td v-if="sendDetail.상태 == '전송완료' || sendDetail.상태 == '전송실패'">
                <div>
                  <span
                    :class="{
                      status_s: sendDetailStatus.결과 == '성공',
                      status_f: sendDetailStatus.결과 == '실패',
                    }"
                  >
                    {{ sendDetailStatus.결과 }}
                  </span>
                  <span v-if="sendDetailStatus.결과 == '실패'">
                    <br />
                    ({{ sendDetailStatus.상세결과 }})
                  </span>
                </div>
              </td>
            </tr>
          </tbody>
        </table>

        <span class="mt-3"><i class="fa fa-caret-right" aria-hidden="true"></i> 결재요청정보</span>
        <table class="fax-table fax-table-detail">
          <thead>
            <tr>
              <th scope="col">결재자</th>
              <th scope="col">결재시간</th>
              <th scope="col">결재상태</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>{{ sendDetail.결재자이름 }}</td>
              <td>{{ sendDetail.결재일자 }}</td>
              <td>{{ sendDetail.결재상태 }}</td>
            </tr>
          </tbody>
        </table>

        <div class="no-approval-btn-group mt-3">
          <base-button type="secondary" class="no-approval-btn" @click="getReuse">
            <i class="fa fa-refresh" aria-hidden="true"></i>

            재사용
          </base-button>
          <base-button
            type="secondary"
            class="no-approval-btn"
            @click="resend"
            v-if="sendDetail.상태 == '전송실패' || sendDetail.상태 == '전송완료'"
          >
            <i class="fa fa-paper-plane" aria-hidden="true"></i>
            재전송
          </base-button>
          <base-button
            type="secondary"
            class="no-approval-btn"
            @click="getUpdate"
            v-if="sendDetail.상태 == '회수' || sendDetail.상태 == '반려'"
          >
            <i class="fa fa-reply" aria-hidden="true"></i> 수정
          </base-button>
          <base-button
            type="secondary"
            class="no-approval-btn"
            @click="getBack"
            v-if="sendDetail.상태 == '결재대기'"
          >
            <i class="fa fa-reply" aria-hidden="true"></i> 회수
          </base-button>
        </div>
      </div>

      <div class="col col-8">
        <iframe
          :src="`https://bnksys.s3.ap-northeast-2.amazonaws.com/${sendDetail.파일명}`"
          style="width: 100%; height: 100%"
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
  name: "send-detail",
  props: ["sendDetail"],

  data() {
    return {
      apprRemark: "",
      detail: [],
    };
  },
  computed: {
    ...mapGetters({
      userKey: "getUserKey",
      userInfo: "getUserInfo",
    }),
  },
  mounted() {
  },
  methods: {

    /* 재사용 */
    async getReuse() {
      this.$store.commit("SET_LOADING_TRUE");
      let formData = new FormData();
      formData.append("userKey", this.sendDetail.발송번호);
      formData.append("userId", this.userInfo.userId);
      // console.log("userKey, userId", this.sendDetail.발송번호, this.userInfo.userId);
      try {
        let response = await http.post(`/reUse`, formData);
        let { data } = response;
        this.$store.commit("SET_LOADING_FALSE");

        if (data != null) {
          // 전송 성공
          console.log("재사용 요청 성공", data);
          // console.log("재사용 요청 성공", data.Info);
          // console.log("재사용 요청 성공", data.fileName);
          // alertify.alert("성공", "재사용 요청 완료되었습니다.", 1.5);
          this.$store.commit("SET_SEND_DETAIL", data);
          this.$router.push("/send");
        } else {
          console.log("재사용 요청 실패");
        }
      } catch (error) {
        // 전송 실패
        console.log("오류메시지 - ", error);
        alertify.alert("재사용 요청에 실패했습니다.", 1.5);
      }
    },

    /* 수정 버튼 클릭 시 -> 팩스보내기에 데이터 전달 */
    async getUpdate() {
      this.$store.commit("SET_LOADING_TRUE");
      let formData = new FormData();
      formData.append("userKey", this.sendDetail.발송번호);
      console.log(this.sendDetail);

      try {
        let response = await http.post(`/withdrawUpdate`, formData);
        let { data } = response;

        if (data != null) {
          // 전송 성공
          // console.log("수정 요청 성공", data);
          // console.log("수정 요청 성공", data.Info);
          // console.log("수정 요청 성공", data.fileName);
          this.$store.commit("SET_SEND_UPDATE", data);
          // alertify.alert("성공", "수정 요청 완료되었습니다.", 1.5);
          this.$router.push("/send");
        } else {
          console.log("수정 요청 실패");
        }
      } catch (error) {
        // 전송 실패
        console.log("오류메시지 - ", error);
        alertify.alert("수정 요청에 실패했습니다.", 1.5);
      }
    },

    /* 회수 버튼 클릭 시 결재정보수정 */
    async getBack() {
      this.$store.commit("SET_LOADING_TRUE");
      let formData = new FormData();
      formData.append("userKey", this.sendDetail.발송번호);

      console.log("수정 요청 param", formData);

      try {
        let response = await http.post(`/withdraw`, formData);
        let { data } = response;
        this.$store.commit("SET_LOADING_FALSE");

        if (data != null) {
          // 전송 성공
          alertify.alert("성공", "회수 완료되었습니다.", 1.5);
          this.$store.commit("SET_MODAL_CLOSE");
        } else {
          console.log("회수 요청 실패");
        }
      } catch (error) {
        // 전송 실패
        console.log("오류메시지 - ", error);
        alertify.alert("회수 요청에 실패했습니다.", 1.5);
      }
    },

    /* 재전송 */
    async resend() {
      this.$store.commit("SET_LOADING_TRUE");
      let formData = new FormData();
      formData.append("userKey", this.sendDetail.발송번호);
      formData.append("ReSend_List", "All");

      console.log("재전송 요청 param", formData);

      try {
        let response = await http.post(`/reSend`, formData);
        let { data } = response;

        this.$store.commit("SET_LOADING_FALSE");
        console.log("재전송 요청 data", data);
        if (data != null) {
          // 전송 성공
          // console.log("재전송 요청 성공", data);
          // console.log("재전송 요청 성공", data.Info);
          // console.log("재전송 요청 성공", data.fileName);
          alertify.alert("성공", "재전송 성공했습니다.");
          this.$store.commit("SET_MODAL_CLOSE");
        } else {
          console.log("재전송 요청 실패");
        }
      } catch (error) {
        // 전송 실패
        console.log("오류메시지 - ", error);
        alertify.alert("실패", "재전송 요청에 실패했습니다.", 1.5);
      }
    },
  },
};
</script>

<style scoped>
.send-detail-container {
  display: flex;
}
.no-proval-info {
}
.left-content {
  display: flex;
  flex-direction: column;
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
  line-height: 15px;
}
.no-approval-table th {
  width: 100px;
}
.no-approval-table td {
  width: 180px;
  border: 0.0625rem solid #dee2e6;
}
.no-approval-btn-group {
}
.no-approval-btn {
  padding: 5px;
  /* border: 0.0625rem solid #bcbcbc; */
}
.no-approval-td-textarea {
  height: 80px;
}
.no-approval-textarea {
  height: 75px;
  width: 220px;
  resize: none;
  margin: 0px;
  border: none;
}

/* 상태 성공/실패*/
.status_s {
  font-weight: bold;
  color: blue;
}
.status_f {
  font-weight: bold;
  color: red;
}
</style>
