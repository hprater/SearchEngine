<template>
  <layout>
    <div class="centered-content">
      <CreateHome/>
    </div>
  </layout>
</template>

<script>
import CreateHome from '../components/CreateHome.vue';
import Layout from "@/components/Layout.vue";

export default {
  components: {
    Layout,
    CreateHome,
  },
};

</script>

<style>
.centered-content {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh; /* Adjust if needed */
}

</style>