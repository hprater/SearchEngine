<template>
  <div>
    <div class="animated-background"></div>
    <div class="centered-content">
    <CreateHome />
    </div>
  </div>
</template>

<script>
import CreateHome from '../components/CreateHome.vue';

export default {
  components: {
    CreateHome,
  },
};

</script>

<style>
.animated-background {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
  z-index: -1; /* Place the background behind other content */
  background: linear-gradient(45deg, #A03611, #222222);
  animation: streakingAnimation 5s linear infinite; /* Adjust the animation duration */
}

.centered-content {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh; /* Adjust if needed */
}

</style>