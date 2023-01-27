<template>
  <section class="section section-shaped section-hero login-section section-lg my-0">
    <div class="shape shape-style-1"></div>
    <div class="container pt-lg-md">
      <div
        class="send-title display-4 mb-4 font-weight-800 text-default"
        style="text-shadow: 2px 1px 2px rgba(0, 0, 0, 0.2)"
      >
        미결재정보
      </div>

      <div class="row">
        <div class="left-content col">
          <span class="mt-3">> 결제요청정보</span>

          <table class="no-approval-table table">
            <thead>
              <tr>
                <th scope="col">결재요청자</th>
                <th scope="col">제목</th>
                <th scope="col">요청일시</th>
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

          <span class="mt-3">> 수신자정보</span>
          <table class="no-approval-table table">
            <thead>
              <tr>
                <th scope="col">이름</th>
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

          <!-- <span class="mt-3">> 개인정보검출내용</span> -->
          <span class="mt-3">> 결재 승인 및 반송</span>
          <table class="no-approval-table table">
            <thead>
              <tr>
                <th scope="col">결재상태</th>
                <th scope="col">사유</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td></td>
                <td></td>
              </tr>
            </tbody>
          </table>
          <div class="row">
            <base-button type="secondary" class="no-approval-btn btn float-right">
              승인
            </base-button>
            <base-button type="secondary" class="no-approval-btn btn float-right">
              반송
            </base-button>
          </div>
        </div>

        <div class="col">
          <iframe
            src="https://bnksys.s3.ap-northeast-2.amazonaws.com/BNK00120230127024348_6.pdf"
            style="width: 600px; height: 500px"
          ></iframe>
        </div>
      </div>

      <div class="col-lg-5"></div>
    </div>
  </section>
</template>

<script>
export default {
  data() {
    return {};
  },
};
</script>

<style scoped>
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
  width: 200px;
  border-top: 0.0625rem solid #dee2e6;
  border-right: 0.0625rem solid #dee2e6;
}
.no-approval-table td:last-child {
  border-bottom: 0.0625rem solid #dee2e6;
}
.no-approval-btn-group {
}
.no-approval-btn {
  padding: 5px;
  width: 50px;
}
</style>
