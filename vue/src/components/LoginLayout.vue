<template>
  <div class="login-container">
    <create-login/>
  </div>
</template>

<script>
import CreateLogin from "../components/CreateLogin.vue";

export default {
  components: {
    CreateLogin
  }
};
</script>

<style scoped>
.login-container {
  position: relative;
  padding: 25px;
  z-index: 1; /* Ensure the login box is on top of the video */
}
</style>
