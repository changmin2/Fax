<template>
  <section class="section">
    <div class="main-container">
      <div class="receive-title display-4 mb-4 font-weight-800 text-default">
        주소록
      </div>

      <div class="text-center mt-3">
        <base-button type="light" @click="addressAdd">추가</base-button>
        <base-button type="danger" @click="addressSave">저장</base-button>
        <base-button type="light" @click="addressDel">삭제</base-button>
      </div>
      <div class="body-content ApprArea" style="width: 100%">
        <table class="fax-table" style="width: 100%">
          <tr class="ApprArea-header">
            <th>
            </th>
            <th>회사명</th>
            <th>이름</th>
            <th>팩스번호</th>
            <th>전화번호</th>
          </tr>

          <tr v-for="(address, index) in addressList.list" :key="index">
            <td> <input type="checkbox" v-model="checkedValues" :value="index"> </td>

            <td><input type="text" v-model="address.company"></td>
            <td><input type="text" v-model="address.name"></td>
            <td><input type="text" v-model="address.fax" @keyup="getNumMask(address.fax,index,'fax')"></td>
            <td><input type="text" v-model="address.hp_NUMBER" @keyup="getNumMask(address.hp_NUMBER,index,'phone')"></td>
          </tr>
        </table>
      </div>
    </div>

  </section>
</template>

<script>
import { mapGetters } from "vuex";
import http from "@/common/axios.js";
import alertify from "alertifyjs";
import Modal from "@/components/Modal.vue";
import ReceiveDetail from "@/views/ReceiveDetail.vue";

export default {
  name: "receive_list",
  components: {
    Modal,
    ReceiveDetail,
  },
  computed: {
    ...mapGetters({
      isLogin: "getIsLogin",
      userKey: "getUserKey",
      userInfo: "getUserInfo",
    }),
  },
  data() {
    return {
      addressList: [],
      checkedValues: [],
    };
  },
  methods: {
    // date 설정
    getNumMask(val, index, gubun) {
      let res;
      if(gubun == "fax"){
        res = this.getMask2(val);
        this.addressList.list[index].fax = res;
      }else if(gubun == "phone"){

        res = this.getMask(val);
        this.addressList.list[index].hp_NUMBER = res;
      }

    },

    getMask( phoneNumber ) {
        if(!phoneNumber) return phoneNumber
        phoneNumber = phoneNumber.replace(/[^0-9]/g, '')

        let res = ''
        if(phoneNumber.length < 3) {
            res = phoneNumber
        }
        else {
            if(phoneNumber.substr(0, 2) =='02') {

                if(phoneNumber.length <= 5) {//02-123-5678
                    res = phoneNumber.substr(0, 2) + '-' + phoneNumber.substr(2, 3)
                }
                else if(phoneNumber.length > 5 && phoneNumber.length <= 9) {//02-123-5678
                    res = phoneNumber.substr(0, 2) + '-' + phoneNumber.substr(2, 3) + '-' + phoneNumber.substr(5)
                }
                else if(phoneNumber.length > 9) {//02-1234-5678
                    res = phoneNumber.substr(0, 2) + '-' + phoneNumber.substr(2, 4) + '-' + phoneNumber.substr(6)
                }

            } else {
                if(phoneNumber.length < 8) {
                    res = phoneNumber.substr(0, 3) + '-' + phoneNumber.substr(3)
                }
                else if(phoneNumber.length == 8)
                {
                    res = phoneNumber.substr(0, 3) + '-' + phoneNumber.substr(3, 3) + '-' + phoneNumber.substr(6)
                }
                else if(phoneNumber.length == 9)
                {
                    res = phoneNumber.substr(0, 3) + '-' + phoneNumber.substr(3, 3) + '-' + phoneNumber.substr(6)
                }
                else if(phoneNumber.length == 10)
                {
                    res = phoneNumber.substr(0, 3) + '-' + phoneNumber.substr(3, 3) + '-' + phoneNumber.substr(6)
                }
                else if(phoneNumber.length > 10) { //010-1234-5678
                    res = phoneNumber.substr(0, 3) + '-' + phoneNumber.substr(3, 4) + '-' + phoneNumber.substr(7)
                }
            }
        }

        return res
    },

    getMask2( faxNumber ) {
        if(!faxNumber) return faxNumber
        faxNumber = faxNumber.replace(/[^0-9]/g, '')

        let res = ''
        if(faxNumber.length < 4) {
            res = faxNumber
        }
        else {
            if(faxNumber.length < 8) {
                res = faxNumber.substr(0, 4) + '-' + faxNumber.substr(4)
            }
            else
            {
                res = faxNumber.substr(0, 4) + '-' + faxNumber.substr(4, 3) + '-' + faxNumber.substr(7)
            }
        }

        return res
    },

    // 조회
    async addressSearch() {
      this.$store.commit("SET_LOADING_TRUE");
      try {
        let response = await http.post("/getAddressList", {
          userId: this.userInfo.userId,
        });

        let { data } = response;
        this.$store.commit("SET_LOADING_FALSE");

        if (data != null) {
          // 전송 성공
          console.log(data);
          console.log("조회 성공");
          this.addressList = data;

        } else {
          console.log("조회 실패");
        }
      } catch (error) {
        // 전송 실패
        console.log("오류메시지 - ", data.Message);
        alertify.error("실패했습니다.", 1.5);
      }
    },

    async addressAdd() {
        this.addressList.list.push({
            seq: "",
            name: "",
            company: "",
            fax: "",
            hp_NUMBER: "",
        });
    },
    async addressSave() {
          this.$store.commit("SET_LOADING_TRUE");
          try {
            let response = await http.post("/setAddress", {
              userId: this.userInfo.userId,
              addressList: this.addressList.list,
            });

            let { data } = response;

            if (data != null) {
              // 전송 성공
              console.log(data);
              console.log("저장 성공");
              this.$store.commit("SET_LOADING_FALSE");
              this.addressSearch();
              alertify.success("저장이 완료되었습니다.", 1.5);

            } else {
              console.log("저장 실패");
            }
          } catch (error) {
            // 전송 실패
            console.log("오류메시지 - ", data.Message);
            alertify.error("실패했습니다.", 1.5);
          }
    },
    async addressDel() {
        //체크박스 선택된 행을 지운다.
        this.$store.commit("SET_LOADING_TRUE");

        let delArray = [];
        let delIndex = 0;
        let arr;

        if ( this.checkedValues.length > 0 ) {
            for( let i in this.checkedValues){
                var deletedIndex = this.checkedValues[i];
                if (this.addressList.list[deletedIndex].seq != "" && this.addressList.list[deletedIndex].seq != undefined){
                    //삭제된 항목 배열
                    delArray.push(this.addressList.list[deletedIndex].seq);
                }
                delete this.addressList.list[deletedIndex];

                arr = this.addressList.list.filter(function(_, index) { // filter 안에 인자로 함수를 받고, index 만 필요하니 명시해주자
                    return index !== deletedIndex     // 배열을 돌며 인덱스1 이 아닌 나머지만 다시 소집한다
                });

                //console.log(arr);
            }
            //삭제후 초기화
            this.checkedValues = [];
            this.addressList.list = arr;


            //console.log(delArray);
            //삭제 쿼리호출
          try {
            let response = await http.post("/deleteAddress", {
              seqList: delArray,
            });

            let { data } = response;

            if (data != null) {
              // 전송 성공
              this.$store.commit("SET_LOADING_FALSE");
              console.log(data);
              console.log("전송 성공");
              alertify.success("삭제가 완료되었습니다.", 1.5);

            } else {
              console.log("전송 실패");
            }
          } catch (error) {
            // 전송 실패
            console.log("오류메시지 - ", data.Message);
            alertify.error("실패했습니다.", 1.5);
          }

        }

    },
  },
  mounted() {
    this.addressSearch();
  },
};
</script>

<style scoped>
.fax-form-input {
  height: 30px;
}
</style>
