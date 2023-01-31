<template>
  <div>
    <div class="spinner-border text-danger spinner" role="status">
      <span class="visually-hidden">Loading...</span>
    </div>
  </div>
</template>

<script>
export default {
  name: "spinner",
};
</script>

<style>
.spinner-bg {
}
.spinner {
  display: block;
  position: fixed;
  z-index: 1031;
  top: 40%;
  left: 50%;
}
</style>
