<template>
  <section class="section section-shaped section-hero login-section section-lg my-0">
    <div class="shape shape-style-1"></div>
    <div class="container pt-lg-md">
      <div
        class="send-title display-4 mb-4 font-weight-800 text-default"
        style="text-shadow: 2px 1px 2px rgba(0, 0, 0, 0.2)"
      >
        수신팩스정보
      </div>

      <div class="row">
        <div class="left-content col">
          <span class="mt-3">> 보낸곳</span>

          <table class="send-detail-table table">
            <thead>
              <tr>
                <th scope="col">보낸사람</th>
                <th scope="col">팩스번호</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td></td>
                <td>{{ receivelistDetail }}</td>
              </tr>
            </tbody>
          </table>

          <span class="mt-3">> 받는곳</span>
          <table class="send-detail-table table">
            <thead>
              <tr>
                <th scope="col">받는사람</th>
                <th scope="col">팩스번호</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td></td>
                <td></td>
              </tr>
            </tbody>
          </table>

          <span class="mt-3">> 수신 정보</span>
          <table class="send-detail-table table">
            <thead>
              <tr>
                <th scope="col">수신일시</th>
                <th scope="col">장수</th>
                <th scope="col">수신상태</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td></td>
                <td></td>
                <td></td>
              </tr>
            </tbody>
          </table>

          <span class="mt-3">> 제목</span>
          <div class="row ml-1">
            <base-input class="receive-detail-title-input" style=""> </base-input>
            <base-button type="secondary" class="send-detail-btn ml-2"> 제목저장 </base-button>
          </div>
        </div>

        <div class="col">
          <iframe
            src="https://bnksys.s3.ap-northeast-2.amazonaws.com/BNK00120230127024348_6.pdf"
            style="width: 100%; height: 100%;"
          ></iframe>
        </div>
      </div>

      <div class="col-lg-5"></div>
    </div>
  </section>
</template>

<script>
import http from "@/common/axios.js";
import alertify from "alertifyjs";

export default {
  name: "receive_detail",
  props: ["receivelistDetail"],
};
</script>

<style scoped>
.left-content {
  display: flex;
  flex-direction: column;
}
table {
  display: flex;
  /* display: -webkit-box; */
  /* display: -ms-flexbox; */
  /* overflow-x: auto; */
  /* overflow-y: hidden; */
  /* border: 1px solid gainsboro; */
}
tbody {
  display: flex;
}

th,
td {
  display: block;
}

th {
  background-color: rgb(224, 224, 224);
  font-weight: normal;
}

.send-detail-table {
  font-size: small;
}
.send-detail-table th,
.send-detail-table td {
  text-align: center;
  height: 35px;
  line-height: 0px;
}
.send-detail-table th {
  width: 100px;
}
.send-detail-table td {
  width: 250px;
  border-top: 0.0625rem solid #dee2e6;
  border-right: 0.0625rem solid #dee2e6;
}
.send-detail-table td:last-child {
  border-bottom: 0.0625rem solid #dee2e6;
  margin: 0px;
  padding: 0px;
}
.send-detail-btn-group {
  margin-left: 240px;
}
.send-detail-btn {
  padding: 5px;
  width: 70px;
  height: 40px;
}
.receive-detail-title-input {
  width: 270px;
}
.receive-detail-title-input >>> .form-control {
  height: 40px;
}
</style>
