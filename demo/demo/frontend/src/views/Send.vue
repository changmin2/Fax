<template>
  <section class="section section-shaped section-hero login-section section-lg my-0">
    <div class="shape shape-style-1"></div>
    <div class="container pt-lg-md">
      <div class="send-title display-4 mb-4 font-weight-800 text-default">
        팩스보내기
        <!-- getterSendDetail 데이터가 있으면 재사용 버튼 클릭했을 경우 -->
        <!-- getterUpdate 데이터가 있으면 수정 버튼 클릭했을 경우 -->
        <span v-if="getterSendDetail">재사용</span>
        <span v-if="getterUpdate">수정</span>
      </div>

      <div class="row">
        <div class="col-lg-5">
          <card type="secondary" body-classes="px-lg-5 py-lg-5" class="send-main border-0">
            <form role="form">
              <table>
                <tr>
                  <th>제목</th>
                  <td>
                    <base-input
                      v-if="getterSendDetail"
                      alternative
                      v-model="getterSendDetail.Info.title"
                      class="my-1"
                    ></base-input>
                    <base-input
                      v-else-if="getterUpdate"
                      alternative
                      v-model="getterUpdate.제목"
                      class="my-1"
                    >
                    </base-input>
                    <base-input v-else alternative v-model="title" class="my-1"> </base-input>
                  </td>
                </tr>
                <tr>
                  <th>발신 팩스번호</th>
                  <td>
                    <base-input alternative v-model="faxNo" readonly class="my-1"> </base-input>
                  </td>
                </tr>
                <tr>
                  <th>수신처</th>
                  <td>
                    <div style="float: left">
                      <base-input
                        alternative
                        v-model="receiveCompany"
                        class="d-inline-flex my-1"
                        style="width: 30%; font-size: 2em"
                        placeholder="상호"
                      />
                      <base-input
                        alternative
                        v-model="receiveName"
                        class="d-inline-flex my-1"
                        style="width: 30%"
                        placeholder="이름"
                      />
                      <base-input
                        alternative
                        v-model="receiveFax"
                        class="d-inline-flex my-1"
                        style="width: 30%"
                        placeholder="팩스번호"
                      />
                      <base-button
                        icon="fa fa-plus fa-lg"
                        class="px-3 py-2.5"
                        @click="setReceiveJSON"
                      ></base-button>
                    </div>
                  </td>
                </tr>
                <tr>
                  <th></th>
                  <td style="height: 100px">
                    <div class="d-flex align-items-start">
                      <textarea
                        class="send-textarea p-3 shadow-none"
                        readonly
                        v-model="showJSON"
                        style="width: 90%; font-size: 0.9em"
                      ></textarea>
                      <base-button
                        icon="fa fa-minus fa-lg"
                        type="light"
                        class="px-3 py-2.5"
                        @click="setDeleteJSON"
                      />
                    </div>
                  </td>
                </tr>
                <tr>
                  <th>첨부파일</th>
                  <td>
                    <input
                      v-if="fileFlag"
                      type="file"
                      multiple
                      value="fileData"
                      @change="changeFile"
                      id="inputFileUploadInsert"
                      accept=".hwp, .hwpml, .doc, .rtf, .xls, .ppt, .pdf, .txt, .docx, .xlsx, .pptx, .tif, .htm, .html, .jpg, .gif, .png , .bmp, .gul"
                    />
                    <div v-else class="mt-2 d-inline-flex" style="width: 200px">
                      <a v-bind:href="`${fileUrl}`" target="_new" download="">PDF변환파일.pdf</a>
                      <i
                        class="fa fa-trash-o ml-3 mt-1 d-lg-inline d-sm-none text-default"
                        @click="deleteFile"
                        style="cursor: pointer"
                      ></i>
                    </div>

                    <base-checkbox
                      v-if="getterSendDetail"
                      class="mt-2 d-inline-flex"
                      v-model="getterSendDetail.Info.private_INFO_YN"
                      >개인정보 포함여부</base-checkbox
                    >
                    <base-checkbox
                      v-else-if="getterUpdate"
                      class="mt-2 d-inline-flex"
                      v-model="getterUpdate.개인정보보호여부"
                      >개인정보 포함여부</base-checkbox
                    >
                    <base-checkbox v-else class="mt-2 d-inline-flex" v-model="privateInfo"
                      >개인정보 포함여부</base-checkbox
                    >
                  </td>
                </tr>
                <tr v-if="grade < 3">
                  <th>결재자</th>
                  <td>
                    <select v-model="apprUserNo" style="width: 40%" v-if="grade == 1">
                      <option v-for="(item, index) in apprUsers" :key="index" :value="item.id">
                        {{ item.name }}
                      </option>
                    </select>
                    <span v-if="grade == 2">책임자는 전결처리됩니다.</span>
                  </td>
                </tr>
                <tr>
                  <th>예약설정</th>
                  <td>
                    <div class="ml-1">
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
                      <label class="ml-1 mr-3">
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
                      <div v-if="reserve" class="ml-5 d-inline-flex">
                        <soan class="mr-4 mt-1">예약일시</soan>
                        <datetime
                          type="datetime"
                          :week-start="7"
                          v-model="sendDate"
                          use12-hour
                        ></datetime>
                        <!-- <span class="ml-3">예약 value: {{ sendDate }}</span> -->
                      </div>
                    </div>
                  </td>
                </tr>
              </table>
              <div class="text-center mt-3">
                <base-button type="danger" @click="send">전송</base-button>
                <base-button type="danger" @click="openModal">미리보기 후 전송</base-button>
              </div>
            </form>
          </card>
        </div>
      </div>
    </div>
    <div>
      <modal :show.sync="modal">
        <h6 slot="header" class="modal-title" id="modal-title-default">팩스 미리보기</h6>
        <div style="width: 100%; height: 650px">
          <iframe v-bind:src="`${fileUrl}`" style="width: 100%; height: 100%"></iframe>
        </div>
        <template slot="footer">
          <base-button type="danger" @click="send">전송</base-button>
          <base-button type="link" class="ml-auto" @click="modal = false">Close </base-button>
        </template>
      </modal>
    </div>
  </section>
</template>

<script>
import { mapGetters } from "vuex";
import http from "@/common/axios.js";
import alertify from "alertifyjs";
import "vue-datetime/dist/vue-datetime.css";
import Modal from "@/components/Modal.vue";

export default {
  components: { Modal },
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
      fileUrl: "",
      files: [],
      apprUsers: [],
      receiveJSON: [],
      showJSON: "",
      modal: false,
      detailOpen: false,
      fileFlag: true,
    };
  },
  computed: {
    ...mapGetters({
      isLogin: "getIsLogin",
      userKey: "getUserKey",
      userInfo: "getUserInfo",
      getterReceiveList: "getReceiveList",
      getterSendDetail: "getSendDetail",
      getterUpdate: "getSendUpdate",
    }),
  },
  created() {
    this.$store.commit("SET_USER_KEY_INIT");

    console.log("isLogin", this.isLogin);
    console.log("userKey", this.userKey);
    console.log("userInfo", this.userInfo);
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
        if (data.Result == "OK") {
          alertify.success("업로드 성공", 1.5);
          if (this.userKey == "None") {
            this.$store.commit("SET_USER_KEY", data.userKey);
          }
          this.fileUrl =
            "https://bnksys.s3.ap-northeast-2.amazonaws.com/" + this.userKey + "_1.pdf";
          this.fileFlag = false;
        } else {
          alertify.error("업로드 실패", 1.5);
          this.fileUrl = "";
          this.fileFlag = true;
        }
      } catch (error) {
        console.log(error);
      }
    },

    // 팩스보내기
    async send() {
      // file upload
      let attachFiles = document.querySelector("#inputFileUploadInsert").files;
      if (attachFiles.length == 0 || this.userKey == "None") {
        //파일선택 필수조건
        alertify.error("첨부하실 파일을 선택해주세요.", 1.5);
        return;
      }
      if (this.receiveJSON.length == 0) {
        //수신처 필수조건
        alertify.error("수신처를 등록해주세요.", 1.5);
        return;
      }
      if (this.apprUserNo == "" && this.grade == 1) {
        //결재자 필수조건
        alertify.error("결재자를 등록해주세요.", 1.5);
        return;
      }
      // console.log(this.userId);
      let sendData = {
        destinationList: this.receiveJSON,
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
          alertify.alert("팩스 전송신청이 완료되었습니다.", 1.5);
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
    async deleteFile() {
      //결재자 가져오기
      try {
        // console.log(this.userId);
        let response = await http.post("/S3fileDelete", {
          fileName: this.userKey + "_1.pdf",
        });
        let { data } = response;
        if (data) {
          this.fileUrl = "";
          this.fileFlag = true;
          alertify.success("파일삭제 성공", 1.5);
        } else {
          this.fileFlag = false;
          alertify.error("파일삭제 실패", 1.5);
        }
      } catch (error) {
        console.log(error);
        console.log("에러1");
      }
    },
    //수신처 설정
    setReceiveJSON() {
      if (this.receiveFax == "" || this.receiveName == "" || this.receiveCompany == "") {
        //수신처 필수조건
        alertify.error("수신처를 입력해주세요.", 1.5);
        return;
      }
      let temp = {
        company: this.receiveCompany,
        name: this.receiveName,
        fax: this.receiveFax.replaceAll("-", ""),
      };
      this.receiveJSON.push(temp);
      this.showJSON +=
        "상호 : " +
        temp.company +
        ", 이름 : " +
        temp.name +
        ", 팩스번호 : " +
        this.receiveFax +
        "\n";
      this.receiveCompany = "";
      this.receiveName = "";
      this.receiveFax = "";
    },
    setDeleteJSON() {
      this.receiveJSON.pop();
      this.showJSON = "";
      for (let index = 0; index < this.receiveJSON.length; index++) {
        let temp = this.receiveJSON[index];
        this.showJSON +=
          "상호 : " + temp.company + ", 이름 : " + temp.name + ", 팩스번호 : " + temp.fax + "\n";
      }
    },
    openModal() {
      if (this.fileFlag) {
        alertify.error("파일을 등록해주세요.", 1.5);
        return;
      }
      if (this.receiveJSON.length == 0) {
        //수신처 필수조건
        alertify.error("수신처를 등록해주세요.", 1.5);
        return;
      }
      if (this.apprUserNo == "" && this.grade == 1) {
        //결재자 필수조건
        alertify.error("결재자를 등록해주세요.", 1.5);
        return;
      }
      this.modal = true;
    },
    //모달 닫기
    closeModal() {
      this.modal = false;
    },
  },
  mounted() {
    setTimeout(() => {
      let userInfo = this.userInfo;
      this.userId = userInfo.userId;
      this.faxNo = userInfo.faxNo;
      this.grade = userInfo.grade;
      this.getApprUsers();
    }, 0);
  },
  destroyed() {
    this.$store.commit("INIT_SEND_DETAIL");
    this.$store.commit("INIT_SEND_UPDATE");
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
.send-textarea:focus,
select:focus {
  outline: none !important;
}
.vdatetime-input {
  resize: none;
  border: 1px solid #cad1d7;
  padding: 3px 0px 3px 10px;
  border-radius: 0.25rem;
  color: #8898aa;
}

input[type="file"]::file-selector-button {
  width: 100px;
  height: 30px;

  border: 1px solid #cad1d7;
  border-radius: 0.25rem;
  cursor: pointer;
}
</style>
