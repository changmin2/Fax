<template>
  <section
    class="section section-shaped section-hero login-section section-lg my-0"
  >
    <div class="shape shape-style-1 "></div>
   receiveList...

   <div
      v-for="(receive, index) in getterReceiveList"
      :key="index"
    >

    {{ receive }}
    
    </div>


  </section>
</template>

<script>
import { mapGetters } from "vuex";


export default {
  components: {
    
  },
  computed: {
    ...mapGetters({
      getterReceiveList: "getReceiveList",
    }),
  },
  data() {
    return {
      
      list: {
        
      }

    };
  },
  methods: {
    // 팩스보내기
    async send() {

      let formData = new FormData();
      formData.append("Date_Start", "2023-01-25");
      formData.append("Date_End", "2023-01-26");
      formData.append("List_EA", "5");
      formData.append("Page", "1");
      // formData.append("Search_Field", ""); // option
      // formData.append("Search_Word", ""); // option
      

      let options = {
        headers: { "Content-Type": "multipart/form-data" },
      };


      try {
        let response = await http.post("/receiveList", formData, options);
        let { data } = response;

        if (data.Result == "OK") {
          // 전송 성공
          this.list = data.List;
          // this.$store.commit("SET_RECEIVE_LIST", data.List);
          this.$router.push("/");
          alertify.success("팩스 전송이 완료되었습니다.", 1.5);
        }
      } catch (error) {
        // 전송 실패
        console.log('오류메시지 - ', data.Message);
        alertify.error("팩스 전송에 실패했습니다.", 1.5);
      }
      },
  }

};
</script>