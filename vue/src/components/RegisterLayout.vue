<template>
  <div class="reg-container">
    <create-register/>
  </div>
</template>

<script>
import CreateRegister from "../components/CreateRegister.vue";

export default {
  components: {
    CreateRegister
  }
};
</script>

<style scoped>
.reg-container {
  position: relative;
  padding: 25px;
  z-index: 1; /* Ensure the login box is on top of the video */
}
</style>
