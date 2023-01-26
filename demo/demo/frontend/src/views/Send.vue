<template>
  <section
    class="section section-shaped section-hero login-section section-lg my-0"
  >
    <div class="shape shape-style-1 "></div>
    <div class="container pt-lg-md">

      <div
        class="send-title display-4 mb-4 font-weight-800 text-default"
        style="text-shadow: 2px 1px 2px rgba(0, 0, 0, 0.2)"
      >
      팩스보내기
      </div>
      
      <div class="row">
        

        <div class="col-lg-5">
          <card
            type="secondary"
            body-classes="px-lg-5 py-lg-5"
            class="send-main  border-0"
          >
             

              <form role="form">
                <!-- 제목 -->
                제목
                <base-input
                  alternative
                  v-model="title"
                >
                </base-input>
                <!-- 수신처 -->
                수신처
                <base-input
                  alternative
                  v-model="receiveNo" 
                >
                </base-input>
                 <!-- 받는사람 -->
                 받는사람
                 <base-input
                  alternative
                  v-model="receiver" 
                >
                </base-input>
                <!-- 첨부파일 -->
                첨부파일
                
                <input type="file" accept='.pdf' multiple value="fileData" @change="changeFile" id="inputFileUploadInsert" />
                

                <!-- 개인정보 포함여부 -->
                개인정보 포함여부
                <base-checkbox
                  v-model="privateInfo"
                >
                <br/>
                </base-checkbox>
                <!-- 결재자 -->
                결재자
                <base-input
                  v-model="apprUserNo" 
                >
                </base-input>
                 <!-- 예약설정 -->
                 예약설정
                 <base-radio
                 value="reserve"
                >즉시
                 </base-radio>
                 <base-radio
                 value="reserve"
                >예약
                 </base-radio>

                <div class="text-center mt-3">
                  <base-button 
                    type="danger" 
                    @click="send"
                    >전송</base-button
                  >
                  <base-button type="danger"
                    >미리보기 후 전송</base-button
                  >
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
import alertify from "alertifyjs";

export default {
  components: {
    
  },
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
    }),
  },
  created() {
    this.$store.commit("SET_USER_KEY_INIT");
  },
  methods: {
    
    async fetchFiles() {
      const response = await axios.get("/files")
      this.files = response.data;
    },

    // 첨부파일 -> 바로 서버에 올리기
    // async changeFile(fileEvent) {
    //   this.fileList = [];

    //   const fileArray = Array.from(fileEvent.target.files);
    //   fileArray.forEach((file) => {
    //     this.fileList.push(URL.createObjectURL(file));
    //   });
    //   console.log('fileArray', fileArray)

    //   let options = {
    //     headers: { "Content-Type": "multipart/form-data" },
    //   };

    //   let response = await http.post("/files", formData, options);
    //   let { data } = response;

    // },

     async changeFile(fileEvent) {
    
       // 1. userKey값 없을 때, userKey값 가져오기
       if(this.userKey == "" || this.userKey == null ){
          // get으로 요청  
          try {
              let { data } = await http.get("/userKey");
              console.log(data);

              if (data.Result == "OK") { 
                console.log("userKey: ", data.userKey);
                this.$store.commit("SET_USER_KEY", data.userKey);
              }
            } catch (error) {
              console.log(error);
          } 
      }
      // 2. userKey값 있을 때는 userKey값과 함께 파일을 서버로 전송
      else {
        
        let formData = new FormData();
        formData.append("userKey", this.userKey);

        this.fileList = [];
        const fileArray = Array.from(fileEvent.target.files);
        fileArray.forEach((file) => {
          this.fileList.push(URL.createObjectURL(file));
        });
        console.log('fileArray', fileArray)

        // file upload
        let attachFiles = document.querySelector("#inputFileUploadInsert").files;
        console.log(attachFiles);
        if (attachFiles.length > 0) {
          const fileArray = Array.from(attachFiles);
          fileArray.forEach((file) => formData.append("file", file));
        }

             
        // let options = {
        //   headers: { "Content-Type": "multipart/form-data" },
        // };

        // try {
        //   let { data } = await http.post("/files", formData, options);
        //   console.log(data);

        //   if (data.Result == "OK") { 
        //     console.log("성공")
          
        //   }
        // } catch (error) {
        //   console.log(error);
        // }


      }

      
    },
    
    // 팩스보내기
    async send() {

      let formData = new FormData();
      // formData.append("Send_Date", ""); // 미입력시 즉시 발송
      //formData.append("DestinationList",  "테스트4#수경#05042089819");

      let options = {
        headers: { "Content-Type": "multipart/form-data" },
      };

      try {
        let response = await http.post("/send", formData, options);
        let { data } = response;

        if (data.Result == "OK") {
          // 전송 성공
          this.$router.push("/");
          alertify.success("팩스 전송이 완료되었습니다.", 1.5);
        }
      } catch (error) {
        // 전송 실패
        console.log('오류메시지 - ', data.Message);
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