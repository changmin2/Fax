<template>
  <div class="send-container">
    <div class="send-title display-4 mb-4 font-weight-800 text-default">마이페이지</div>

    <!-- <card type="secondary" class="send-main">
      <table style="width: 100%" class="send-table">
        <tr>
          <th>아이디</th>
          <td><base-input v-model="USER_ID" class="send-input" readonly></base-input></td>
          <th>이름</th>
          <td><base-input v-model="USER_NAME" class="send-input" readonly> </base-input></td>
        </tr>
        <tr>
          <th>부서</th>
          <td><base-input v-model="DEPT_NAME" class="send-input" readonly> </base-input></td>
          <th>직급</th>
          <td><base-input v-model="COMM_NAME" class="send-input" readonly> </base-input></td>
        </tr>
        <tr>
          <th>팩스번호</th>
          <td><base-input v-model="FAX_NO" class="send-input" readonly></base-input></td>
          <td colspan="2"></td>
        </tr>
        <tr v-if="GRADE_CODE > 1">
          <th>부재여부</th>
          <td colspan="2" v-if="GRADE_CODE > 1" style="display: flex">
            <div class="mt-1">
              <label>
                <input type="radio" v-model="IS_ABSENCE" value="Y" />
                Y</label
              >
              <label class="ml-1">
                <input type="radio" class="ml-3" v-model="IS_ABSENCE" value="N" />
                N</label
              >
            </div>
            <div v-if="IS_ABSENCE == 'Y'" class="ml-3">
              <span class="mt-1 ml-3">대체자</span>
              <select v-model="SUBSTITUTE" class="ml-3 flex-fill">
                <option v-for="(item, index) in subUsers" :key="index" :value="item.id">
                  {{ item.name }}
                </option>
              </select>
            </div>
          </td>
        </tr>
      </table>

      <div class="text-center mt-3">
        <base-button type="danger" @click="setAbsence">저장</base-button>
      </div>
    </card> -->

    <card type="secondary" class="user-detail-card border-0 send-main">
      <table style="width: 100%" class="send-table">
        <tr>
          <th>아이디</th>
          <td>
            <base-input v-model="USER_ID" readonly> </base-input>
          </td>
        </tr>
        <tr>
          <th>이름</th>
          <td>
            <base-input v-model="USER_NAME" readonly></base-input>
          </td>
        </tr>

        <tr>
          <th>부서</th>
          <td>
            <base-input v-model="DEPT_NAME" readonly> </base-input>
          </td>
        </tr>
        <tr>
          <th>직급</th>
          <td>
            <base-input v-model="COMM_NAME" readonly> </base-input>
          </td>
        </tr>

        <tr>
          <th>팩스번호</th>
          <td>
            <base-input v-model="FAX_NO" readonly> </base-input>
          </td>
        </tr>
        <tr v-if="GRADE_CODE > 1">
          <th>부재여부</th>
          <td colspan="2" v-if="GRADE_CODE > 1" style="display: flex">
            <div class="align-self-center mt-1 absence">
              <label class="ml-1">
                <input type="radio" class="ml-1" v-model="IS_ABSENCE" value="Y" />
                Y</label
              >
              <label class="ml-1">
                <input type="radio" class="ml-3" v-model="IS_ABSENCE" value="N" />
                N</label
              >
            </div>
            <div v-if="IS_ABSENCE == 'Y'" class="ml-3 substitute">
              <span class="mt-1 ml-3">대체자</span>
              <select v-model="SUBSTITUTE" class="ml-3 flex-fill">
                <option v-for="(item, index) in subUsers" :key="index" :value="item.id">
                  {{ item.name }}
                </option>
              </select>
            </div>
          </td>
        </tr>
      </table>
      <div class="text-center mt-5">
        <base-button type="danger" @click="setAbsence">저장</base-button>
      </div>
    </card>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import http from "@/common/axios.js";
import alertify from "alertifyjs";

export default {
  components: {},
  data() {
    return {
      USER_ID: "",
      FAX_NO: "",
      IS_ABSENCE: "N",
      USER_NAME: "",
      SUBSTITUTE: "",
      DEPT_NAME: "",
      COMM_NAME: "",
      GRADE_CODE: 0,
      subUsers: [],
    };
  },
  methods: {
    async getUserInfo() {
      try {
        let response = await http.post("/getUserInfo", {});
        let { data } = response;

        if (data.flag == true) {
          let userInfo = data.userInfo;
          console.log(userInfo);
          this.USER_ID = userInfo.USER_ID;
          this.FAX_NO = userInfo.FAX_NO;
          this.IS_ABSENCE = userInfo.IS_ABSENCE;
          console.log(userInfo.IS_ABSENCE);
          this.USER_NAME = userInfo.USER_NAME;
          this.SUBSTITUTE = userInfo.SUBSTITUTE;
          this.DEPT_NAME = userInfo.DEPT_NAME;
          this.COMM_NAME = userInfo.COMM_NAME;
          this.GRADE_CODE = userInfo.GRADE_CODE;
          this.getSubstituteUser();
        } else {
          // 세션값 없음
          // console.log(data.message);
          localStorage.setItem("isLogin", false);
          alertify.error("로그인 만료되었습니다.<br>다시 로그인해주세요.", 1.5);
          router.push("/login");
        }
      } catch (error) {
        console.log(error);
        localStorage.setItem("isLogin", false);
        router.push("/login");
      }
    },
    async getSubstituteUser() {
      //결재자 가져오기
      try {
        let response = await http.post("/getSubstituteUser", {
          userId: this.USER_ID,
        });
        let { data } = response;
        if (data.flag) {
          this.subUsers = data.users;
        }
      } catch (error) {
        console.log(error);
        console.log("에러1");
      }
    },
    async setAbsence() {
      //대체여부 저장
      try {
        console.log(this.USER_ID);
        console.log(this.SUBSTITUTE);
        console.log(this.IS_ABSENCE);
        console.log(this.USER_NAME);
        let response = await http.post("/setAbsence", {
          userId: this.USER_ID,
          substitute: this.SUBSTITUTE,
          isAbsence: this.IS_ABSENCE,
          userName: this.USER_NAME,
        });
        let { data } = response;
        if (data.flag == true) {
          alertify.success(data.msg, 1.5);
        } else {
          alertify.error(data.msg, 1.5);
        }
      } catch (error) {
        console.log(error);
        console.log("에러1");
      }
    },
  },
  mounted() {
    this.getUserInfo();
  },
};
</script>

<style scoped>
.send-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin-top: 10rem;
  margin-left: 10rem;
  margin-right: 10rem;
  margin-bottom: 10rem;
  /* margin-top: 2rem; */
}

.send-main {
  width: 100%;
  max-width: 40vw;
  padding: 2rem 3rem 1rem 3rem;
}

.send-input {
  width: 8rem;
}

th,
td {
}
th {
  font-weight: normal;
}
td {
  vertical-align: middle;
  width: 25vw;
}
tr {
  padding-left: 3rem;
  padding-right: 3rem;
}

.send-receiver-input-group-1 {
  display: flex;
}

.send-table {
  display: flex;
  flex-direction: column;
}
.send-table tr {
  display: flex;
  flex-direction: column;
}
.send-table td {
  width: 100%;
}
.send-receiver {
  /* width: 100%; */
  height: 70px;
}
.send-receiver-input-group {
  /* display: flex;
    flex-direction: column;
    flex-wrap: wrap; */
}
.send-receiver-input-group-1 {
  /* display: flex; */
}
.send-receiver-input {
  width: 60px;
}
.send-receiver-tr {
  margin-bottom: 0px !important;
}
.send-button {
  padding: 10px !important;
  margin: 2px !important;
}

.send-main-form {
  /* margin-left: 2rem;
    margin-right: 2rem; */
}

.send-textarea {
  height: 100%;
}
.send-text {
  color: #8898aa;
}
.send-button-minus {
  height: 4rem;
}
td {
  height: inherit;
}
th {
}

/* .input-group-alternative >>> input {
    width: 100%;
    max-width: 20vw;
  } */
@media screen and (max-width: 991px) {
  .send-container {
    margin: 2rem;
    margin-top: 5rem;
    font-size: small;
  }
  .send-main {
    width: 100%;
    min-width: none;
    max-width: none;
    padding: 0px;
  }
  tr {
    padding: 0px;
  }
  .substitute {
    margin: 0px !important;
  }
  .absence {
    width: 250px;
  }
}
</style>
