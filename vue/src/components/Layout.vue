<template>
  <div>
    <div class="video-container">
      <video loop autoplay muted class="video">
        <source src="https://arkansasrazorbacks.com/wp-content/uploads/2021/06/drone-video.mp4"
                type="video/mp4; codecs=avc1.42E01E, mp4a.40.2"/>
      </video>
    </div>
    <div class="content-container">
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
export default {
};

</script>

<style scoped>
/* Add styles for the video container if needed */
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

/* Add styles for the content container if needed */
.content-container {
  position: relative;
  z-index: 1; /* Ensure the content is on top of the video */
}
</style>
