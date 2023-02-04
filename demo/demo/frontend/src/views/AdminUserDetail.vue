<template>
  <div class="modal-container">
    <div class="send-title display-4 mb-4 font-weight-800 text-default">사용자 정보 관리</div>
    <div class="row">
      <div class="col-lg-5">
        <card type="secondary" body-classes="px-lg-5 py-lg-5" class="user-detail-card border-0">
          <table style="width: 100%">
            <!-- <colgroup>
              <col class="col-1" />
              <col class="col-3" />
              <col class="col-1" />
              <col class="col-3" />
            </colgroup> -->
            <tr>
              <th class="pb-3">이름</th>
              <td>
                <base-input
                  v-model="userDetail.USER_NAME"
                  style="width: 20rem; height: 40px; font-size: 0.875rem; color: #525f7f"
                >
                </base-input>
              </td>
            </tr>
            <tr>
              <th class="pb-3">아이디</th>
              <td>
                <base-input
                  v-model="userDetail.USER_ID"
                  style="width: 20rem; height: 40px; font-size: 0.875rem; color: #525f7f"
                ></base-input>
              </td>
            </tr>

            <!-- <tr>
              <th class="pb-3">부서</th>
              <td>
                <base-input
                  v-model="userDetail.DEPT_NAME"
                  style="width: 20rem; height: 40px; font-size: 0.875rem; color: #525f7f"
                >
                </base-input>
              </td>
            </tr> -->
            <tr>
              <th class="pb-3">직급</th>
              <td>
                <!-- <base-input
                  v-model="userDetail.COMM_NAME"
                  style="width: 20rem; height: 40px; font-size: 0.875rem; color: #525f7f"
                >
                </base-input> -->
                <div style="height: 50px">
                  <select
                    v-model="userDetail.GRADE_CODE"
                    style="width: 20rem; height: 40px; font-size: 0.875rem; color: #525f7f"
                  >
                    <!-- <option selected :value="userDetail.GRADE_CODE">
                      {{ userDetail.COMM_NAME }}
                    </option> -->
                    <option
                      v-for="(item, index) in deptList.commInfo"
                      :key="index"
                      :value="item.COMM_CODE"
                    >
                      {{ item.COMM_NAME }}
                    </option>
                  </select>
                </div>
              </td>
            </tr>
            <tr>
              <th>부서</th>
              <td>
                <div style="height: 50px">
                  <select
                    v-model="userDetail.DEPT_CODE"
                    style="width: 20rem; height: 40px; font-size: 0.875rem; color: #525f7f"
                  >
                    <!-- <option selected :value="userDetail.DEPT_NAME">
                      {{ userDetail.DEPT_NAME }}
                    </option> -->
                    <option
                      v-for="(item, index) in deptList.deptInfo"
                      :key="index"
                      :value="item.DEPT_CODE"
                    >
                      {{ item.DEPT_NAME }}
                    </option>
                  </select>
                </div>
              </td>
            </tr>
            <tr v-if="userDetail.GRADE_CODE > 1">
              <th>부재여부</th>
              <td colspan="2" v-if="userDetail.GRADE_CODE > 1">
                <div class="d-flex flex-row justify-content-start" style="height: 50px">
                  <div class="align-self-center mt-1">
                    <label class="ml-1">
                      <input type="radio" class="ml-1" v-model="userDetail.IS_ABSENCE" value="Y" />
                      Y</label
                    >
                    <label class="ml-1">
                      <input type="radio" class="ml-3" v-model="userDetail.IS_ABSENCE" value="N" />
                      N</label
                    >
                  </div>
                </div>
              </td>
            </tr>
          </table>
          <div class="d-flex justify-content-center mt-4">
            <base-button type="danger" @click="setUserUpdate">수정</base-button>
          </div>
        </card>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import http from "@/common/axios.js";
import alertify from "alertifyjs";
import Modal from "@/components/Modal.vue";

export default {
  name: "admin",
  props: ["userDetail", "subUsers", "deptList", "getUserList", "modals"],
  components: {
    Modal,
  },
  computed: {
    ...mapGetters({
      isLogin: "getIsLogin",
      userKey: "getUserKey",
      userInfo: "getUserInfo",
    }),
  },
  created() {
    console.log(this.userDetail);
  },
  mounted() {
    // this.userDetail.COMM_NAME = ''
  },
  data() {
    return {
      detail: Object.assign({}, this.userDetail),

      IS_ABSENCE: "",
      GRADE_CODE: "",
      DEPT_CODE: "",
      SUBSTITUTE: "",
      CUSTOMER_CODE: "",
      EMAIL: "",
      COMM_NAME: "",
      USER_ID: "",
      HP_NUMBER: "",
      DEPT_NAME: "",
      USER_NAME: "",
    };
  },
  methods: {
    // 대체자 조회
    async getApprUsers() {
      this.$store.commit("SET_LOADING_TRUE");
      try {
        let response = await http.post("/getApprUsers", {
          userId: this.userInfo.userId,
        });

        let { data } = response;
        this.$store.commit("SET_LOADING_FALSE");

        if (data != null) {
          // 전송 성공
          console.log(data);
          console.log("전송 성공");
        } else {
          console.log("전송 실패");
        }
      } catch (error) {
        // 전송 실패
        console.log("오류메시지 - ", data.Message);
        alertify.error("실패했습니다.", 1.5);
      }
    },

    // 사용자 정보 수정
    async setUserUpdate() {
      this.$store.commit("SET_LOADING_TRUE");

      console.log("???????????");

      // 팀장4  본부장5
      console.log("기존", this.detail.GRADE_CODE);
      console.log("변경", this.userDetail.GRADE_CODE);
      // 직급 및 부재 여부 변경 시, 대체자 없애기
      if (
        this.detail.GRADE_CODE != this.userDetail.GRADE_CODE ||
        this.detail.IS_ABSENCE != this.userDetail.IS_ABSENCE
      ) {
        this.userDetail.SUBSTITUTE = "";
      }

      try {
        let response = await http.post("/userUpdate", {
          user_ID: this.userDetail.USER_ID,
          is_ABSENCE: this.userDetail.IS_ABSENCE,
          grade_CODE: this.userDetail.GRADE_CODE,
          dept_CODE: this.userDetail.DEPT_CODE,
          substitute: this.userDetail.SUBSTITUTE,
          email: this.userDetail.EMAIL,
          hp_NUMBER: this.userDetail.HP_NUMBER,
          user_NAME: this.userDetail.USER_NAME,
        });

        let { data } = response;
        this.$store.commit("SET_LOADING_FALSE");

        if (data != null) {
          // 전송 성공
          console.log(data);
          console.log("전송 성공");
          this.getUserList();
          // 모달 창 끄기
          this.modals.modal3 = false;
          alertify.alert("성공", "사용자 정보 수정이 완료되었습니다.");
        } else {
          console.log("전송 실패");
        }
      } catch (error) {
        // 전송 실패
        console.log("오류메시지 - ", error.Message);
        alertify.error("실패했습니다.", 1.5);
      }
    },
  },
};
</script>

<style scoped>
.modal-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 2rem;
}
.main-container {
}
.user-detail-card {
  width: 35rem;
}
</style>
