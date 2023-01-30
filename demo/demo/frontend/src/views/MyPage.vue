<template>
  <section class="section section-shaped section-hero login-section section-lg my-0">
    <div class="shape shape-style-1"></div>
    <div class="container pt-lg-md">
      <div
        class="send-title display-4 mb-4 font-weight-800 text-default"
        style="text-shadow: 2px 1px 2px rgba(0, 0, 0, 0.2)"
      >
        마이페이지
      </div>

      <div class="row">
        <div class="col-lg-5">
          <card type="secondary" body-classes="px-lg-5 py-lg-5" class="send-main border-0">
              <table style="width: 100%">
                <colgroup>
                  <col class="col-1">
                  <col class="col-3">
                  <col class="col-1">
                  <col class="col-3">
                </colgroup>
                <tr>
                  <th class="pb-3">아이디</th>
                  <td><base-input v-model="USER_ID" class="mr-3" readonly></base-input></td>
                  <th class="pl-4 pb-3">이름</th>
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
                <tr v-if="isApprUser">
                  <th>부재여부</th>
                    <td colspan="2" v-if="isApprUser">
                    <div class="d-flex flex-row justify-content-start" style="height:50px">
                    <div class="align-self-center mt-1">
                      <label class="ml-1">
                        <input
                          type="radio"
                          class="ml-1"
                          v-model="IS_ABSENCE"
                          value="Y"
                        />
                        Y</label
                      >
                      <label class="ml-1">
                        <input
                          type="radio"
                          class="ml-3"
                          v-model="IS_ABSENCE"
                          value="N"
                        />
                        N</label
                      >
                    </div>
                      <div v-if="iS_ABSENCEflag" class="ml-3 d-flex flex-row flex-fill align-self-center">
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
                   <base-button type="danger"  @click="setAbsence">저장</base-button>
                  </td>
                </tr>
              </table>
              
          </card>
        </div>
      </div>
    </div>
  </section>
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
  computed: {
     iS_ABSENCEflag() {
      console.log(this.IS_ABSENCE);
      console.log(this.IS_ABSENCE=='Y');
      return this.IS_ABSENCE=='Y';
    },
    isApprUser() {
      return this.GRADE_CODE > 1;
    },
    // ...mapGetters({
    //   userInfo: "getUserInfo",
    // }),
  },
  methods: {
    async getUserInfo() {
       try {
          let response = await http.post("/getUserInfo", {
          });
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
            localStorage.setItem("isLogin",false);
            alertify.error("로그인 만료되었습니다. 다시 로그인해주세요.", 1.5);
            router.push("/login");
          }
        } catch (error) {
          console.log(error);
          localStorage.setItem("isLogin",false);
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
        if(data.flag){
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

<style>
.send-main {
  width: 100vh;
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
select {
  border: 1px solid #cad1d7;
  border-radius: 0.25rem;
  height: 35px;
  width: 150px;
  color: #525f7f;
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
</style>
