<template>
  <section class="section">
    <div class="container receive-detail-container">
      <div
        class="receive-title display-4 mb-4 font-weight-800 text-default"
        style="text-shadow: 2px 1px 2px rgba(0, 0, 0, 0.2)"
      >
        부서 팩스함
      </div>

      <div class="receive-detail">
        <div class="left-content col">
          <span class="mt-3">> 보낸 곳</span>

          <table class="receive-detail-table table" style="width: 100%">
            <thead>
              <tr>
                <th scope="col">팩스번호</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>{{ receivelistDetail.sender_NO }}</td>
              </tr>
            </tbody>
          </table>

          <span class="mt-3">> 받는곳</span>
          <table class="receive-detail-table table">
            <thead>
              <tr>
                <th scope="col">팩스번호</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>{{ receivelistDetail.fax_NO }}</td>
              </tr>
            </tbody>
          </table>

          <span class="mt-3">> 수신 정보</span>
          <table class="receive-detail-table table">
            <thead>
              <tr>
                <th scope="col">수신일시</th>
                <th scope="col">장수</th>
                <th scope="col">수신상태</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>{{ receivelistDetail.receive_DATE }}</td>
                <td>{{ receivelistDetail.page_CNT }}</td>
                <td>{{ receivelistDetail.Result }}</td>
              </tr>
            </tbody>
          </table>

          <span class="mt-3">> 제목</span>
          <div class="row ml-1">
            <base-input class="receive-detail-title-input" style=""> </base-input>
            <base-button type="secondary" class="receive-detail-btn ml-2"> 제목저장 </base-button>
          </div>
        </div>

        <div class="col col-8">
          <iframe
            v-bind:src="`${ receivelistDetail.filePath }`"
            style="width: 100%; height: 100%;"
          ></iframe>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import http from "@/common/axios.js";
import alertify from "alertifyjs";

export default {
  name: "receive_detail",
  props: ["receivelistDetail"],
  data() {
    return {
    };
  },
  created() {},
};
</script>

<style scoped>
.left-content {
  display: flex;
  flex-direction: column;
}
.receive-detail-container {
  max-width: 90%;
}
.receive-detail {
  display: flex;
  max-width: 100%;
}

table {
  display: flex;
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

.receive-detail-table {
  font-size: small;
}
.receive-detail-table th,
.receive-detail-table td {
  text-align: center;
  height: 35px;
  line-height: 0px;
}
.receive-detail-table th {
  width: 100px;
}
.receive-detail-table td {
  width: 250px;
  border-top: 0.0625rem solid #dee2e6;
  border-right: 0.0625rem solid #dee2e6;
}
.receive-detail-table td:last-child {
  border-bottom: 0.0625rem solid #dee2e6;
}
.receive-detail-btn-group {
  margin-left: 240px;
}
.receive-detail-btn {
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
