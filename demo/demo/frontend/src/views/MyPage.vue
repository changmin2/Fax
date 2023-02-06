<template>
  <div class="send-container pt-lg-md">
    <div class="send-title display-4 mb-4 font-weight-800 text-default">마이페이지</div>

    <card type="secondary">
      <table style="width: 100%" class="send-table">
        <tr>
          <th>아이디</th>
          <td><base-input v-model="USER_ID" class="mr-3" readonly></base-input></td>
          <th>이름</th>
          <td><base-input v-model="USER_NAME" readonly> </base-input></td>
        </tr>
        <tr>
          <th class="pb-3">부서</th>
          <td><base-input v-model="DEPT_NAME" class="mr-3" readonly> </base-input></td>
          <th class="pl-4 pb-3">직급</th>
          <td><base-input v-model="COMM_NAME" readonly> </base-input></td>
        </tr>
        <tr>
          <th class="pb-3">팩스번호</th>
          <td><base-input v-model="FAX_NO" class="mr-3" readonly></base-input></td>
          <td colspan="2"></td>
        </tr>
        <tr v-if="GRADE_CODE > 1">
          <th>부재여부</th>
          <td colspan="2" v-if="GRADE_CODE > 1">
            <div class="d-flex flex-row justify-content-start" style="height: 50px">
              <div class="align-self-center mt-1">
                <label class="ml-1">
                  <input type="radio" class="ml-1" v-model="IS_ABSENCE" value="Y" />
                  Y</label
                >
                <label class="ml-1">
                  <input type="radio" class="ml-3" v-model="IS_ABSENCE" value="N" />
                  N</label
                >
              </div>
              <div
                v-if="IS_ABSENCE == 'Y'"
                class="ml-3 d-flex flex-row flex-fill align-self-center"
              >
                <span class="mt-1 ml-3">대체자</span>
                <select v-model="SUBSTITUTE" class="ml-3 flex-fill">
                  <option v-for="(item, index) in subUsers" :key="index" :value="item.id">
                    {{ item.name }}
                  </option>
                </select>
              </div>
            </div>
          </td>
          <td class="d-flex justify-content-end">
            <base-button type="danger" @click="setAbsence">저장</base-button>
          </td>
        </tr>
      </table>
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
.main-container {
  margin-top: 6rem;
  margin-left: 15vw;
  margin-right: 6rem;
}
.send-main {
  width: 100%;
  min-width: 50vw;
  /* max-width: 100vw; */
}
.input-content {
  display: inline;
}
.input-content >>> input {
  height: 35px;
  width: 250px;
}
.input-content-appr >>> input {
  width: 120px;
  height: 35px;
}
.send-form-select {
  width: 100px;
}

th,
td {
  height: 50px;
}
th {
  width: 100px;
  font-weight: normal;
}
td {
  vertical-align: middle;
}

.send-textarea {
  height: 85px;
  width: 600px;
  resize: none;
  border: 1px solid #cad1d7;
  border-radius: 0.25rem;
}

input[type="file"]::file-selector-button {
  width: 100px;
  height: 30px;

  border: 1px solid #cad1d7;
  border-radius: 0.25rem;
  cursor: pointer;
}
.send-receiver-input-group-1 {
  display: flex;
}

@media screen and (max-width: 991px) {
  .send-container {
    margin: 2rem;
    margin-top: 5rem;
    font-size: small;
  }
  .send-main {
    width: 100%;
  }
  .input-group-alternative {
    width: 100%;
    /* max-width: 50vw; */
  }
  .input-group-alternative >>> input {
    font-size: 10px;
    /* max-width: 50vw; */
  }
  .send-table {
    display: flex;
    flex-direction: column;
  }
  .send-table tr {
    display: flex;
    flex-direction: column;
    margin-bottom: rem;
  }
  .send-table th {
    width: 30%;
    height: 100%;
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

  /* .input-group-alternative >>> input {
    width: 100%;
    max-width: 20vw;
  } */
}
</style>
