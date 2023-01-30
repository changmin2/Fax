<template>
  <section class="section section-shaped section-hero login-section section-lg my-0">
    <div class="shape shape-style-1"></div>
    <div class="container pt-lg-md">
      <div
        class="send-title display-4 mb-4 font-weight-800 text-default"
        style="text-shadow: 2px 1px 2px rgba(0, 0, 0, 0.2)"
      >
        팩스보내기
      </div>

      <div class="row">
        <div class="col-lg-5">
          <card type="secondary" body-classes="px-lg-5 py-lg-5" class="send-main border-0">
            <form role="form">
              <table>
                <tr>
                  <th>제목</th>
                  <td><base-input v-model="title"> </base-input></td>
                </tr>
                <tr>
                  <th>발신 팩스번호</th>
                  <td><base-input alternative v-text="faxNo"> </base-input></td>
                </tr>
                <tr>
                  <th>수신처</th>
                  <td>
                    <input
                      alternative
                      v-model="receiveCompany"
                      style="width: 30%"
                      placeholder="상호"
                    />
                    <input
                      alternative
                      v-model="receiveName"
                      style="width: 30%"
                      placeholder="이름"
                    />
                    <input
                      alternative
                      v-model="receiveFax"
                      style="width: 30%"
                      placeholder="팩스번호"
                    />
                    <br />
                  </td>
                </tr>
                <tr>
                  <th></th>

                  <td style="height: 100px">
                    <textarea class="send-textarea"></textarea>
                  </td>
                </tr>
                <tr>
                  <th>첨부파일</th>
                  <td>
                    <input
                      type="file"
                      multiple
                      value="fileData"
                      @change="changeFile"
                      id="inputFileUploadInsert"
                      accept=".hwp, .hwpml, .doc, .rtf, .xls, .ppt, .pdf, .txt, .docx, .xlsx, .pptx, .tif, .htm, .html, .jpg, .gif, .png , .bmp, .gul"
                    />
                    <base-checkbox class="mt-2" v-model="privateInfo"
                      >개인정보 포함여부</base-checkbox
                    >
                  </td>
                </tr>
                <tr>
                  <th>결재자</th>
                  <td>
                    <select v-model="apprUserNo" style="width: 40%">
                      <option v-for="(item, index) in apprUsers" :key="index" :value="item.id">
                        {{ item.name }}
                      </option>
                    </select>
                  </td>
                </tr>
                <tr>
                  <th>예약설정</th>
                  <td>
                    <div class="ml-3">
                      <label class="ml-1">
                        <input
                          type="radio"
                          class="ml-1"
                          v-model="reserve"
                          value=""
                          style="position: relative; top: 2px"
                        />
                        즉시</label
                      >
                      <label class="ml-1">
                        <input
                          type="radio"
                          class="ml-3"
                          v-model="reserve"
                          value="reserve"
                          @click="getNow"
                          style="position: relative; top: 2px"
                        />
                        예약</label
                      >
                      <div v-if="reserve">
                        예약일시
                        <datetime
                          type="datetime"
                          :week-start="7"
                          v-model="sendDate"
                          use12-hour
                        ></datetime>
                        <span class="ml-3">예약 value: {{ sendDate }}</span>
                      </div>
                    </div>
                  </td>
                </tr>
              </table>
              <div class="text-center mt-3">
                <base-button type="danger" @click="send">전송</base-button>
                <base-button type="danger">미리보기 후 전송</base-button>
              </div>
            </form>
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
import "vue-datetime/dist/vue-datetime.css";

export default {
  components: {},
  data() {
    return {
      title: "",
      receiver: "",
      fileData: "",
      privateInfo: false,
      apprUserNo: "",
      reserve: "",
      receiveCompany: "",
      receiveName: "",
      receiveFax: "",
      sendDate: "",
      userId: "",
      faxNo: "",
      files: [],
      apprUsers: [],
    };
  },
  computed: {
    ...mapGetters({
      isLogin: "getIsLogin",
      userKey: "getUserKey",
      userInfo: "getUserInfo",
      getterReceiveList: "getReceiveList",
    }),
  },
  created() {
    this.$store.commit("SET_USER_KEY_INIT");
  },
  methods: {
    getNow() {
      const today = new Date();
      this.sendDate = today.toISOString();
    },
    printTest(e) {
      console.log(this.privateInfo);
    },
    async changeFile(fileEvent) {
      // 1. userKey값 없을 때, userKey값 가져오기
      // 2. userKey값 있을 때는 userKey값과 함께 파일을 서버로 전송

      // 파일이랑 같이 userId 요청했는데 만약 userKey가 None이라면 백 쪽에서 세팅 해줌.
      // 프론트 쪽에서 userKey가 "" 라면 세팅 해주기
      // if (this.userKey == "None") {
      //   // userKey를 프론트쪽에 저장
      // this.$store.commit("SET_USER_KEY", data);
      // } else {
      //   // userKey로 file 업로드

      // }

      let formData = new FormData();
      formData.append("userId", this.userId);
      formData.append("userKey", this.userKey);
      console.log("userKey: ", this.userKey);
      this.fileList = [];
      const fileArray = Array.from(fileEvent.target.files);
      fileArray.forEach((file) => {
        this.fileList.push(URL.createObjectURL(file));
      });
      console.log("fileArray", fileArray);

      // file upload
      let attachFiles = document.querySelector("#inputFileUploadInsert").files;
      console.log(attachFiles);
      if (attachFiles.length > 5) {
        alertify.alert("오류", "파일은 최대 5개까지 업로드가능합니다.", 1.5);
        return;
      }
      if (attachFiles.length > 0) {
        const fileArray = Array.from(attachFiles);
        fileArray.forEach((file) => formData.append("files", file));
      }

      let options = {
        headers: { "Content-Type": "multipart/form-data" },
      };

      try {
        let { data } = await http.post("/upload", formData, options);
        console.log(data);

        if (this.userKey == "None") {
          this.$store.commit("SET_USER_KEY", data);
        }
        if (data != null) {
          console.log("업로드 성공");
        } else {
          console.log("업로드 실패");
        }
      } catch (error) {
        console.log(error);
      }
    },

    // 팩스보내기
    async send() {
      console.log();
      // file upload
      let attachFiles = document.querySelector("#inputFileUploadInsert").files;
      if (attachFiles.length == 0) {
        //파일선택 필수조건
        alertify.alert("첨부하실 파일을 선택해주세요.", 1.5);
        return;
      }
      // console.log(this.userId);
      let sendData = {
        destinationList: [
          { company: this.receiveCompany, name: this.receiveName, fax: this.receiveFax },
        ],
        // destinationList: [{ company: "회사명", name: this.receiver, fax: this.receiveNo }],
        userKey: this.userKey,
        userID: this.userId,
        title: this.title,
        send_Date: this.reserve ? this.sendDate : "",
        reserve_yn: this.reserve ? "Y" : "N",
        private_info_yn: this.privateInfo ? "Y" : "N",
        appr_person: this.apprUserNo,
        faxNo: this.faxNo,
      };

      try {
        let response = await http.post("/Send", sendData);

        let { data } = response;

        if (data != null) {
          // 전송 성공
          console.log(data);
          console.log("전송 성공");
          this.$router.push("/");
          alertify.alert("팩스 전송 결재신청이 완료되었습니다.", 1.5);
        } else {
          console.log("전송 실패");
        }
      } catch (error) {
        // 전송 실패
        console.log("오류메시지 - ", data.Message);
        alertify.alert("팩스 전송 결재신청에 실패했습니다.", 1.5);
      }
    },
    async getApprUsers() {
      //결재자 가져오기
      try {
        // console.log(this.userId);
        let response = await http.post("/getApprUsers", {
          userId: this.userId,
        });
        let { data } = response;
        this.apprUsers = data;
        // console.log(this.apprUsers);
      } catch (error) {
        console.log(error);
        console.log("에러1");
      }
    },
  },
  mounted() {
    let userInfo = this.userInfo;
    this.userId = userInfo.userId;
    this.faxNo = userInfo.faxNo;
    this.getApprUsers();
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
