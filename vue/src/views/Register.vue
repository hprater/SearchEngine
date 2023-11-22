<template>
  <div id="app">
    <div class="video-container">
      <video loop autoplay muted class="video">
        <source src="https://arkansasrazorbacks.com/wp-content/uploads/2021/06/drone-video.mp4"
                type="video/mp4; codecs=avc1.42E01E, mp4a.40.2"/>
      </video>
    </div>
    <!-- Login box container -->
    <div class="reg-container">
      <create-register/>
    </div>
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
#app {
  position: relative;
  height: 100vh;
}

.video-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
  z-index: -1; /* Place the video behind other content */
}

.video {
  width: 100%;
  height: 100%;
  margin: 0; /* Ensure no margin on the video */
  padding: 0; /* Ensure no padding on the video */
  display: block; /* Ensure the video is displayed as a block element */
  object-fit: cover; /* Adjust the object-fit property as needed */
}

.reg-container {
  position: relative;
  padding: 25px;
  z-index: 1; /* Ensure the login box is on top of the video */
}
</style>
