<template>
  <div class="landing-section">
    <div class="main-container">
      <div class="receive-title display-4 mb-4 font-weight-800 text-default">사용자 관리</div>
      <div class="body-content ApprArea" style="width: 100%">
        <table class="fax-table" style="width: 100%">
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
          <tr class="ApprArea-header">
            <th>이름</th>
            <th>아이디</th>
            <th>이메일</th>
            <th>전화번호</th>
            <th>부서</th>
            <th>부재 여부</th>
            <th>직급</th>
            <th>관리</th>
          </tr>

          <tr v-for="(user, index) in userList" :key="index">
            <td>{{ user.USER_NAME }}</td>
            <td>{{ user.USER_ID }}</td>
            <td>{{ user.EMAIL }}</td>
            <td>{{ user.HP_NUMBER }}</td>
            <td>{{ user.DEPT_NAME }}</td>
            <td>{{ user.IS_ABSENCE }}</td>
            <td>{{ user.COMM_NAME }}</td>
            <td>
              <base-button @click="getUserDetail(index)">상세</base-button>
            </td>
          </tr>
        </table>
      </div>
    </div>

    <!-- 컴포넌트 MyModal -->
    <modal :show.sync="modals.modal3" modal-classes="modal-dialog-centered modal-lg">
      <h6 slot="header" class="modal-title" id="modal-title-default"></h6>
      <admin-user-detail
        :userDetail="userDetail"
        :subUsers="subUsers"
        :deptList="deptList"
        :getUserList="getUserList"
      ></admin-user-detail>
    </modal>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import http from "@/common/axios.js";
import alertify from "alertifyjs";
import Modal from "@/components/Modal.vue";
import AdminUserDetail from "./AdminUserDetail.vue";
export default {
  name: "admin",
  components: {
    Modal,
    AdminUserDetail,
  },
  computed: {
    ...mapGetters({
      isLogin: "getIsLogin",
      userKey: "getUserKey",
      userInfo: "getUserInfo",
    }),
  },
  created() {
    this.getUserList();
    this.getApprUsers();
    this.getDeptList();
  },
  data() {
    return {
      userList: [],
      subUsers: [],
      deptList: [],
      userDetail: {},

      modals: {
        modal3: false,
      },
    };
  },
  methods: {
    // 사용자 조회
    async getUserList() {
      this.$store.commit("SET_LOADING_TRUE");
      try {
        let response = await http.post("/getUserList", {});

        let { data } = response;
        this.$store.commit("SET_LOADING_FALSE");

        if (data != null) {
          // 전송 성공
          console.log(data);
          console.log("전송 성공");
          this.userList = data;
          this.userListCopy = data;
        } else {
          console.log("전송 실패");
        }
      } catch (error) {
        // 전송 실패
        console.log("오류메시지 - ", data.Message);
        alertify.error("실패했습니다.", 1.5);
      }
    },
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
          this.subUsers = data;
        } else {
          console.log("전송 실패");
        }
      } catch (error) {
        // 전송 실패
        console.log("오류메시지 - ", data.Message);
        alertify.error("실패했습니다.", 1.5);
      }
    },
    // 부서,직급 목록 조회
    async getDeptList() {
      this.$store.commit("SET_LOADING_TRUE");
      try {
        let response = await http.post("/getDeptList", {});

        let { data } = response;
        this.$store.commit("SET_LOADING_FALSE");

        if (data != null) {
          // 전송 성공
          console.log(data);
          console.log("전송 성공");
          this.deptList = data;
        } else {
          console.log("전송 실패");
        }
      } catch (error) {
        // 전송 실패
        console.log("오류메시지 - ", data.Message);
        alertify.error("실패했습니다.", 1.5);
      }
    },

    // 사용자 정보 get 후 모달 띄우기
    getUserDetail(index) {
      this.modals.modal3 = true;
      this.userDetail = Object.assign({}, this.userList[index]);
      // this.userDetail = this.userList[index];
    },
  },
};
</script>

<style scoped>
.landing-section {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 90vh;
}
.main-container {
}
.fax-form-input {
  height: 30px;
}
</style>
