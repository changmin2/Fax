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
              제목
              <base-input alternative v-model="title"> </base-input>
              수신처
              <base-input alternative v-model="receiveNo"> </base-input>
              받는사람
              <base-input alternative v-model="receiver"> </base-input>
              첨부파일

              <input
                type="file"
                accept=".pdf"
                multiple
                value="fileData"
                @change="changeFile"
                id="inputFileUploadInsert"
              />

              <base-checkbox   v-model="privateInfo">개인정보 포함여부 </base-checkbox>
              <br />
              결재자
              <base-input v-model="apprUserNo"> </base-input>
              예약설정
              <base-radio value="reserve">즉시 </base-radio>
              <base-radio value="reserve">예약 </base-radio>

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

export default {
  components: {},
  data() {
    return {
      title: "",
      receiveNo: "",
      receiver: "",
      fileData: "",
      privateInfo: "",
      apprUserNo: "",
      reserve: "",

      files: [],
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
      formData.append("userId", this.userInfo.userId);
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
      let formData = new FormData();
      // formData.append("Service", "FAX");
      // formData.append("Type", "Send");
      // formData.append("Send_Date", ""); // 미입력시 즉시 발송
      // formData.append("DestinationList", { Company: "테스트1", Name: "수경", Fax: "05042089819" }); //"테스트4#수경#05042089819"
      // formData.append("userKey", this.userKey);

      let sendData = {
        destinationList: [{ company: "테스트4", name: "수경", fax: "05042089819" }],
        userKey: this.userKey,
        Send_Date: "",
        private_info_yn: "N",
      };

      try {
        let response = await http.post("/Send", sendData);

        let { data } = response;

        if (data != null) {
          // 전송 성공
          console.log(data);
          console.log("전송 성공");
          this.$router.push("/");
          alertify.success("팩스 전송이 완료되었습니다.", 1.5);
        } else {
          console.log("전송 실패");
        }
      } catch (error) {
        // 전송 실패
        console.log("오류메시지 - ", data.Message);
        alertify.error("팩스 전송에 실패했습니다.", 1.5);
      }
    },

    // async send() {
    //   try {
    //     let response = await http.post("/send", {
    //       // UserID: this.isLogin.userEmail,
    //       // UserPW: this.isLogin.userPassword,
    //       UserID: 'jungly5935',
    //       UserPW: 'sjrnfl0814!',
    //       Service: "FAX",
    //       Type: "Send",
    //       Send_Date:"", // 미입력시 즉시발송
    //       PDF1: "", // PDF 파일만 가능 - "PDF로 문서 변환(Convert)" API를 통해 회신받은 PDF를 첨부
    //       Destination: [
    //       { "Company": "테스트1", "Name": "이수경", "Fax": "05042089819" },
    //       ],
    //     });
    //     let { data } = response;

    //     if (data.result == "success") {
    //       // 전송 성공
    //       this.$router.push("/");
    //       alertify.success("팩스 전송이 완료되었습니다.", 1.5);
    //     }
    //   } catch (error) {
    //     // 전송 실패
    //     alertify.error("팩스 전송에 실패했습니다.", 1.5);
    //   }
    // },
  },
};
</script>

<style>
.send-title {
}
.send-main {
  width: 100vh;
}
</style>
