<template>
  <div class="modal-container">
    <div class="send-title display-4 mb-4 font-weight-800 text-default">미결재정보</div>

    <div class="send-detail-container">
      <div class="left-content col">
        <span class="mt-3">> 결재요청정보</span>

        <table class="no-approval-table table" style="width: 100%">
          <thead>
            <tr>
              <th scope="col">보낸사람</th>
              <th scope="col">팩스번호</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>{{ sendDetail.발송자 }}</td>
              <td>{{ sendDetail.팩스번호 }}</td>
            </tr>
          </tbody>
        </table>

        <span class="mt-3">> 수신자정보</span>
        <table class="no-approval-table table">
          <thead>
            <tr>
              <th scope="col">받는사람</th>
              <th scope="col">팩스번호</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>{{ sendDetail.이름 }}</td>
              <td>{{ sendDetail.팩스번호 }}</td>
            </tr>
          </tbody>
        </table>

        <!-- <span class="mt-3">> 개인정보검출내용</span> -->
        <span class="mt-3">> 결재 승인 및 반송</span>
        <table class="no-approval-table table">
          <thead>
            <tr>
              <th scope="col">발신일시</th>
              <th scope="col">발신상태</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>{{ sendDetail.등록일자 }}</td>
              <td>{{ sendDetail.상태 }}</td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="col col-8">
        <iframe
          src="https://bnksys.s3.ap-northeast-2.amazonaws.com/BNK00120230127024348_6.pdf"
          style="width: 100%; height: 100%"
        ></iframe>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "send-detail",
  props: ["sendDetail"],

  data() {
    return {
      apprRemark: "",
    };
  },
  created() {},
  methods: {},
};
</script>

<style scoped>
.send-detail-container {
  display: flex;
}
.no-proval-info {
}
.left-content {
  display: flex;
  flex-direction: column;
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

.no-approval-table {
  font-size: small;
}
.no-approval-table th,
.no-approval-table td {
  text-align: center;
  height: 35px;
  line-height: 0px;
}
.no-approval-table th {
  width: 100px;
}
.no-approval-table td {
  width: 180px;
  border-top: 0.0625rem solid #dee2e6;
  border-right: 0.0625rem solid #dee2e6;
}
.no-approval-table td:last-child {
  border-bottom: 0.0625rem solid #dee2e6;
}
.no-approval-btn-group {
  margin-left: 240px;
}
.no-approval-btn {
  padding: 5px;
  width: 50px;
}
.no-approval-td-textarea {
  height: 80px;
}
.no-approval-textarea {
  height: 75px;
  width: 220px;
  resize: none;
  margin: 0px;
  border: none;
}
</style>
