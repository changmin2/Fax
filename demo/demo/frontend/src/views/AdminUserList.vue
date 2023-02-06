<template>
  <div class="landing-section">
    <div class="main-container">
      <div class="receive-title display-4 mb-4 font-weight-800 text-default">
        사용자 관리
        <i class="fa fa-gear" aria-hidden="true"></i>
      </div>
      <div class="body-content ApprArea" style="width: 100%">
        <table class="fax-table" style="width: 100%">
          <tr class="ApprArea-header">
            <th>이름</th>
            <th>아이디</th>
            <th class="fax-table-display-none">이메일</th>
            <th class="fax-table-display-none">전화번호</th>
            <th>부서</th>
            <th class="fax-table-display-none">부재 여부</th>
            <th>직급</th>
          </tr>

          <tr v-for="(user, index) in userList" :key="index" @click="getUserDetail(index)">
            <td>{{ user.USER_NAME }}</td>
            <td>{{ user.USER_ID }}</td>
            <td class="fax-table-display-none">{{ user.EMAIL }}</td>
            <td class="fax-table-display-none">{{ user.HP_NUMBER }}</td>
            <td>{{ user.DEPT_NAME }}</td>
            <td class="fax-table-display-none">{{ user.IS_ABSENCE }}</td>
            <td>{{ user.COMM_NAME }}</td>
          </tr>
        </table>
      </div>
    </div>

    <!-- 컴포넌트 MyModal -->
    <modal
      :show.sync="modals.modal3"
      modal-classes="modal-dialog-centered modal-sm"
      body-classes="p-0"
    >
      <h6 slot="header" class="modal-title" id="modal-title-default">사용자 정보 관리</h6>
      <admin-user-detail
        :userDetail="userDetail"
        :subUsers="subUsers"
        :deptList="deptList"
        :getUserList="getUserList"
        :modals="modals"
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
        let response = await http.post("/getUserList");

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
  margin-top: 3rem;
  width: 90vw;
  margin-left: 3rem;
}
.main-container {
}

.fax-form-input {
  height: 30px;
}

.fax-table-input {
  width: 16rem;
}

.fax-form-input {
  height: 30px;
  font-size: small;
  margin: 2px;
}
.fax-input {
  display: flex;
  /* justify-content: space-around; */
  border-top: 1px solid rgb(224, 224, 224);
  border-bottom: 1px solid rgb(224, 224, 224);
}
.fax-input-row {
  display: flex;
  align-items: center;
  width: 100%;
}
.fax-input-box {
  background-color: rgb(224, 224, 224);
  font-size: small;
  font-weight: 600;
  width: 80px;
  height: 100%;
  line-height: 50px;
  text-align: center;
}
.fax-input-content {
  display: flex;
  justify-content: center;
  align-items: center;
  /* width: 100%; */
  margin: 0 auto;
}

@media screen and (max-width: 991px) {
  .landing-section {
    margin: 1rem;
  }
  section {
    padding: 0px;
  }
  .fax-table {
    margin-right: 1rem;
    width: 100vw;
  }

  .fax-table-tr {
    display: flex;
    height: 100%;
  }
  .fax-table th {
    height: inherit;
    padding-top: 0.5rem;
    padding-bottom: 0.5rem;
    width: 100%;
  }

  .fax-input {
    display: flex;
    flex-direction: column;
  }
  .fax-input-mobile {
    display: none;
  }

  .fax-form-input {
    /* width: 105px; */
  }
  .fax-form-input-receiver {
    /* width: 8rem; */
  }

  .fax-input-box {
  }
  .fax-input-content {
    display: flex;
    margin-right: 10px;
    margin-left: 20px;

    /* max-width: 350px; */
    /* width:inherit; */
    /* justify-content: right; */
  }
  .fax-table-input {
    /* width: 10rem; */
  }
  .mobile-btn {
    padding: 5px 20px 5px 20px;
  }
  .fax-form-input-date {
    width: 105px;
  }
  th {
    padding-left: 4px;
    padding-right: 4px;
  }
  td {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    max-width: 5rem;
    padding-left: 4px;
    padding-right: 4px;
  }
}
</style>
