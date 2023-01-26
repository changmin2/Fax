<template>
  <section
    class="section section-shaped section-hero login-section section-lg my-0"
  >
    <div class="shape shape-style-1 "></div>
    sendList..
   
  </section>
</template>

<script>


export default {
  components: {
    
  },
  methods: {
    
    // 보낸팩스함
    async sendList() {
      
      try {
        let response = await http.post("/sendList", {
          
        });
        let { data } = response;

        if (data.result == "success") {
          // 전송 성공
          this.$router.push("/");
          alertify.success("팩스 전송이 완료되었습니다.", 1.5);
        }
      } catch (error) {
        // 전송 실패
        alertify.error("팩스 전송에 실패했습니다.", 1.5);
      }
    },
   
  },
};
</script>