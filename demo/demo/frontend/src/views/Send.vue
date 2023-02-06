<template>
  <section class="section">
    <div class="send-container">
      <div class="send-title display-4 mb-4 font-weight-800 text-default">
        팩스보내기
        <i class="fa fa-print" aria-hidden="true"></i>
        <!-- getterSendDetail 데이터가 있으면 재사용 버튼 클릭했을 경우 -->
        <!-- getterUpdate 데이터가 있으면 수정 버튼 클릭했을 경우 -->
        <span v-if="getterSendDetail" class="d-inline-flex" style="color: #d7191f">- 재사용</span>
        <span v-if="getterUpdate" class="d-inline-flex" style="color: #d7191f">- 수정</span>
      </div>

      <div class="row">
        <div class="col-lg-5">
          <card type="secondary" body-classes="px-lg-5 py-lg-5" class="send-main border-0">
            <form role="form" style="width: 100%">
              <table>
                <colgroup>
                  <!-- <col style="width: 15%" /> -->
                  <!-- <col style="width: 85%" /> -->
                </colgroup>
                <tr>
                  <th>제목</th>
                  <td>
                    <base-input alternative v-model="title" class="my-1"> </base-input>
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
                    <div class="w-100 d-flex flex-row align-items-start">
                      <base-input
                        alternative
                        v-model="receiveCompany"
                        class="my-1 flex-fill"
                        placeholder="상호"
                      />
                      <base-input
                        alternative
                        v-model="receiveName"
                        class="my-1 flex-fill"
                        placeholder="이름"
                      />
                      <base-input
                        alternative
                        v-model="receiveFax"
                        class="my-1 flex-fill"
                        @keyup="getMask2(receiveFax)"
                        placeholder="팩스번호"
                      />
                      <div class="flex-fill d-flex flex-row align-items-center align-self-stretch">
                        <base-button
                          icon="fa fa-plus fa-lgr"
                          class="px-3 py-2.5"
                          @click="setReceiveJSON"
                        ></base-button>
                        <base-button
                          icon="fa fa-address-book fa-lg"
                          class="px-3 py-2.5"
                          title="주소록에서 불러오기"
                          @click="callAddress"
                        ></base-button>
                      </div>
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
                        style="width: 84%; font-size: 0.9em"
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
                    <div v-else class="mt-2 d-lg-inline" style="width: 200px">
                      <a v-bind:href="`${fileUrl}`" target="_new" download=""
                        >PDF변환파일.pdf - {{ pageCount }}장</a
                      >
                      <i
                        class="fa fa-trash-o ml-3 mt-1 d-lg-inline d-sm-none text-default"
                        @click="deleteFile"
                        style="cursor: pointer"
                      ></i>
                      <div class="mt-1 ml-5" style="display: inline">
                        ※ 개인정보 포함 :

                        <badge v-if="privateInfo" class="m-0" type="success" rounded>Y</badge>
                        <badge v-if="!privateInfo" class="m-0" type="warning" rounded>N</badge>
                      </div>
                      <!-- <base-checkbox class="mt-2 d-inline-flex ml-4" v-model="privateInfo" disabled
                        >개인정보 포함여부</base-checkbox
                      > -->
                    </div>
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
                    <span v-if="grade == 2">책임자는 전결처리됩니다.(개인정보 검출시 지점장급 결재)</span>
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
                <base-button v-if="getterUpdate" type="light" @click="deleteSend">삭제</base-button>
                <!-- <span v-if="getterUpdate" class="d-inline-flex" style="color: #d7191f"
                    >- 수정</span
                  > -->
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
          <base-button type="link" class="ml-auto" @click="modal2 = false">닫기 </base-button>
        </template>
      </modal>
    </div>
    <modal :show.sync="modal2">
      <h6 slot="header" class="modal-title" id="modal-title-default">주소록</h6>
      <div style="width: 100%">
        <table class="fax-table table-hover" style="width: 100%">
          <colgroup>
            <col style="width: 15%" />
            <col style="width: 30%" />
            <col style="width: 25%" />
            <col style="width: 30%" />
            <col />
          </colgroup>
          <tr class="ApprArea-header">
            <th>선택</th>
            <th>회사명</th>
            <th>이름</th>
            <th>팩스번호</th>
          </tr>
          <tbody>
            <tr v-for="(address, index) in addressList" :key="index">
              <td><input type="checkbox" v-model="checkedAddress" :value="index" /></td>
              <td>{{ address.company }}</td>
              <td>{{ address.name }}</td>
              <td>{{ address.fax }}</td>
            </tr>
          </tbody>
        </table>
      </div>
      <template slot="footer">
        <base-button type="danger" @click="addAddress">수신처에 추가</base-button>
        <base-button type="link" class="ml-auto" @click="modal2 = false">닫기 </base-button>
      </template>
    </modal>
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
      // fileUrl: "",
      newFileName: "",
      files: [],
      apprUsers: [],
      receiveJSON: [],
      addressList: [],
      checkedAddress: [],
      showJSON: "",
      modal: false,
      modal2: false,
      detailOpen: false,
      fileFlag: true,
      pageCount: 0,
      grade: 0,
    };
  },
  computed: {
    ...mapGetters({
      isLogin: "getIsLogin",
      isLoading: "getIsLoading",
      userKey: "getUserKey",
      userInfo: "getUserInfo",
      getterReceiveList: "getReceiveList",
      getterSendDetail: "getSendDetail",
      getterUpdate: "getSendUpdate",
    }),
    fileUrl() {
      return "https://bnksys.s3.ap-northeast-2.amazonaws.com/" + this.newFileName;
    },
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
      this.$store.commit("SET_LOADING_TRUE");
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
      // console.log("userKey: ", this.userKey);
      this.fileList = [];
      const fileArray = Array.from(fileEvent.target.files);
      fileArray.forEach((file) => {
        this.fileList.push(URL.createObjectURL(file));
      });
      // console.log("fileArray", fileArray);

      // file upload
      let attachFiles = document.querySelector("#inputFileUploadInsert").files;
      console.log(attachFiles);
      if (attachFiles.length > 5) {
        alertify.alert("오류", "파일은 최대 5개까지 업로드가능합니다.", 1.5);
        this.$store.commit("SET_LOADING_FALSE");
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
        let reqUrl = this.getterUpdate ? "/updateUpload" : "/upload"; //수정요청인지 아닌지

        let { data } = await http.post(reqUrl, formData, options);
        this.$store.commit("SET_LOADING_FALSE");
        // console.log(data);

        if (data.Result == "OK") {
          alertify.success("업로드 성공", 1.5);
          if (this.userKey == "None") {
            this.$store.commit("SET_USER_KEY", data.userKey);
          }
          this.newFileName = data.newFileName;
          this.fileFlag = false;
          this.pageCount = data.pageCount;
          this.privateInfo = data.detection;
          console.log("업로드 성공", data.newFileName);
        } else {
          alertify.error("업로드 실패", 1.5);
          this.newFileName = "";
          this.fileFlag = true;
        }
      } catch (error) {
        this.$store.commit("SET_LOADING_FALSE");
        console.log(error);
      }
    },

    // 팩스보내기
    async send() {
      // file upload
      // let attachFiles = document.querySelector("#inputFileUploadInsert").files;
      if (this.fileFlag || this.userKey == "None") {
        //파일선택 필수조건
        alertify.error("첨부하실 파일을 선택해주세요.", 1.5);
        return;
      }
      if (this.receiveJSON.length == 0) {
        //수신처 필수조건private_info_yn
        alertify.error("수신처를 등록해주세요.", 1.5);
        return;
      }
      if (this.apprUserNo == "" && this.grade == 1) {
        //결재자 필수조건
        alertify.error("결재자를 등록해주세요.", 1.5);
        return;
      }

      this.$store.commit("SET_LOADING_TRUE");
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
        pageCount: this.pageCount,
        newFileName: this.newFileName,
      };

      try {
        let reqUrl = this.getterUpdate ? "/updateSend" : "/Send"; //수정요청인지 아닌지
        let response = await http.post(reqUrl, sendData);

        let msg = this.getterUpdate ? "수정" : "전송";
        let msg2 = this.getterUpdate ? "수정" : "전송신청";

        let { data } = response;
        this.$store.commit("SET_LOADING_FALSE");

        if (data != null) {
          // 전송 성공
          console.log(data);
          console.log("전송 성공");
          this.$router.push("/send-list");
          alertify.alert(msg, "팩스 " + msg2 + "이 완료되었습니다.");
        } else {
          console.log("전송 실패");
        }
      } catch (error) {
        // 전송 실패
        this.$store.commit("SET_LOADING_FALSE");
        console.log("오류메시지 - ", data.Message);
        alertify.alert(msg, "팩스 전송 결재신청에 실패했습니다.");
      }
    },
    async getApprUsers() {
      //결재자 가져오기
      try {
        this.$store.commit("SET_LOADING_TRUE");
        // console.log(this.userId);
        let response = await http.post("/getApprUsers", {
          userId: this.userId,
        });
        let { data } = response;
        this.$store.commit("SET_LOADING_FALSE");
        this.apprUsers = data;
        // console.log(this.apprUsers);
      } catch (error) {
        this.$store.commit("SET_LOADING_FALSE");
        console.log(error);
        console.log("에러1");
      }
    },
    async deleteFile() {
      if (this.getterUpdate) {
        //삭제시키는척만
        this.newFileName = "";
        this.fileFlag = true;
        alertify.success("파일 재등록이 가능합니다.", "");
        return;
      }
      //첨부파일 삭제
      try {
        // console.log(this.userId);
        this.$store.commit("SET_LOADING_TRUE");
        let response = await http.post("/S3fileDelete", {
          fileName: this.newFileName,
        });
        let { data } = response;
        this.$store.commit("SET_LOADING_FALSE");
        if (data) {
          this.newFileName = "";
          this.fileFlag = true;
          this.privateInfo = false;
          alertify.success("파일삭제 성공", 1.5);
        } else {
          this.fileFlag = false;
          alertify.error("파일삭제 실패", 1.5);
        }
      } catch (error) {
        this.$store.commit("SET_LOADING_FALSE");
        console.log(error);
        console.log("에러1");
      }
    },
    async deleteSend() {
      //팩스보내기 삭제
      try {
        // console.log(this.userId);
        this.$store.commit("SET_LOADING_TRUE");
        let response = await http.post("/withdrawDelete", {
          userKey: this.userKey,
        });
        let { data } = response;
        this.$store.commit("SET_LOADING_FALSE");
        if (data) {
          this.$router.push("/send-list");
          alertify.alert("삭제가 완료되었습니다.", 1.5);
        } else {
          alertify.error("삭제 실패했습니다.", 1.5);
        }
      } catch (error) {
        this.$store.commit("SET_LOADING_FALSE");
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
      const regexr = /([0-9]|[-])/g;
      if (!regexr.test(this.receiveFax)) {
        //팩스번호 숫자랑 -만
        alertify.error("팩스번호는 숫자와 -기호만 사용가능합니다", 1.5);
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
    addAddress() {
      if (this.checkedAddress.length > 0) {
        let temp = this.checkedAddress;
        let arr = this.addressList.filter(function (_, index) {
          // filter 안에 인자로 함수를 받고, index 만 필요하니 명시해주자
          return temp.includes(index); // 배열을 돌며 인덱스1 이 아닌 나머지만 다시 소집한다
        });
        arr.forEach((element) => {
          this.receiveJSON.push({
            company: element.company,
            name: element.name,
            fax: element.fax.replaceAll("-", ""),
          });
          this.showJSON +=
            "상호 : " +
            element.company +
            ", 이름 : " +
            element.name +
            ", 팩스번호 : " +
            element.fax +
            "\n";
        });
        this.checkedAddress = [];
        this.modal2 = false;
      } else {
        alertify.error("수신처에 추가할<br>주소록을 선택해주세요.", 1.5);
      }
    },
    // 주소록 가져오기
    async callAddress() {
      this.$store.commit("SET_LOADING_TRUE");
      try {
        let response = await http.post("/getAddressList", {
          userId: this.userId,
        });

        let { data } = response;
        this.$store.commit("SET_LOADING_FALSE");

        if (data != null) {
          // 전송 성공
          console.log("조회 성공");
          this.addressList = data.list;
          this.modal2 = true;
        } else {
          console.log("조회 실패");
        }
      } catch (error) {
        // 전송 실패
        console.log("오류메시지 - ", data.Message);
        alertify.error("실패했습니다.", 1.5);
      }
    },
    getMask2(faxNumber) {
      if (!faxNumber) return "";
      faxNumber = faxNumber.replace(/[^0-9]/g, "");

      let res = "";
      if (faxNumber.length < 4) {
        res = faxNumber;
      } else {
        if (faxNumber.length < 8) {
          res = faxNumber.substr(0, 4) + "-" + faxNumber.substr(4);
        } else {
          res = faxNumber.substr(0, 4) + "-" + faxNumber.substr(4, 3) + "-" + faxNumber.substr(7);
        }
      }
      this.receiveFax = res;
    },
  },
  mounted() {
    setTimeout(() => {
      let userInfo = this.userInfo;
      this.userId = userInfo.userId;
      this.faxNo = userInfo.faxNo;
      this.grade = userInfo.grade;
      this.getApprUsers();
      let originData;
      if (this.getterUpdate || this.getterSendDetail) {
        if (this.getterUpdate) {
          originData = this.getterUpdate;
          this.$store.commit("SET_USER_KEY", originData.Info.user_KEY);
        } else if (this.getterSendDetail) {
          originData = this.getterSendDetail;
          this.$store.commit("SET_USER_KEY", originData.userKey);
        }
        this.title = originData.Info.title;
        this.apprUserNo = originData.Info.appr_USER_NO;
        let tempArr = originData.details;
        tempArr.forEach((value, index, array) => {
          let tempJson = {
            company: value.RECEIVE_COMPANY,
            name: value.RECEIVE_NAME,
            fax: value.RECEIVE_FAX_NO,
          };
          this.receiveJSON.push(tempJson);
          let tempFaxNo =
            tempJson.fax.substr(0, 4) +
            "-" +
            tempJson.fax.substr(4, 3) +
            "-" +
            tempJson.fax.substr(7);
          this.showJSON +=
            "상호 : " +
            tempJson.company +
            ", 이름 : " +
            tempJson.name +
            ", 팩스번호 : " +
            tempFaxNo +
            "\n";
        });
        this.fileFlag = false;
        this.pageCount = originData.Info.page_CNT || 0;
        this.privateInfo = originData.Info.private_INFO_YN == "Y";
        this.reserve = originData.Info.reserve_YN == "Y" ? "reserve" : "";
        // this.sendDate = originData.Info.send_DATE;
        if (originData.Info.send_DATE != "") {
          var date = new Date(originData.Info.send_DATE);
          this.sendDate = date.toISOString();
        }
        this.newFileName = originData.fileName;
      }
    }, 0);
  },
  destroyed() {
    this.$store.commit("INIT_SEND_DETAIL");
    this.$store.commit("INIT_SEND_UPDATE");
  },
};
</script>

<style scoped>
.send-container {
  margin-top: 6rem;
  margin-left: 15vw;
  margin-right: 3vw;
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
.fax-table {
  width: 100%;
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
@media screen and (max-width: 991px) {
  .send-container {
    margin-right: 8rem;
  }
  .send-main {
    width: 100%;
  }

  .input-group-alternative {
    width: 100%;
    max-width: 50vw;
  }
  /* .input-group-alternative >>> input {
    width: 100%;
    max-width: 20vw;
  } */
}

input[type=checkbox]
{
  /* Double-sized Checkboxes */
  -ms-transform: scale(1.5); /* IE */
  -moz-transform: scale(1.5); /* FF */
  -webkit-transform: scale(1.5); /* Safari and Chrome */
  -o-transform: scale(1.5); /* Opera */
  padding: 5px;
}
</style>
