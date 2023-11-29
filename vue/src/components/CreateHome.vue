<template>
  <div>
    <!-- Image, Header, and Logout Button -->
    <div class="header-elements">
      <img src="../../src/Assets/Resources/uofa.jpg" alt="University of Arkansas Logo" class="logo-image">
      <h1 class="aol-heading">RazorBack Search</h1>
      <div class="aol-logout-container">
        <router-link v-bind:to="{ name: 'logout' }" class="aol-logout">Logout</router-link>
      </div>
    </div>

    <!-- Query and Response Box -->
    <div class="aol-box">
      <div class="content">
        <div class="aol-box-input">
          <input v-model="userWithInput.queryWords" @keyup.enter="submitInput" placeholder="Enter Query">
        </div>

        <loading-overlay :loading="loading"/>
        <!-- Display the response area if there is a response -->
        <div class="response-area" v-if="responseVisible">
          <div v-html="formattedResponse"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import messageService from '../services/MessageService';
import LoadingOverlay from './CreateLoading.vue';

export default {
  name: 'home',
  data() {
    return {
      userWithInput: {
        id: this.$store.state.user.id,
        type: 'false', // false being that it is a query and not indexing
        queryWords: '',
      },
      loading: false,
      responseVisible: false,
      response: ''
    };
  },
  methods: {
    submitInput() {
      if (!this.loading) {
        this.loading = true;

        const formattedData = {
          id: this.userWithInput.id,
          type: this.userWithInput.type,
          queryWords: this.userWithInput.queryWords
        };

        messageService
            .sendQuestion(formattedData)
            .then(response => {
              this.response = response.data;
              this.userWithInput.queryWords = '';
              this.responseVisible = true;
            })
            .catch(error => {
              console.error('Error fetching data:', error);
            })
            .finally(() => {
              this.loading = false;
            });
      }
    }
  },
  components: {
    LoadingOverlay
  },
  computed: {
    formattedResponse() {
      // Format response text with HTML line breaks
      return this.response.replace(/\n/g, '<br>');
    }
  }
};
</script>

<style scoped>
/* Your existing styles here */
.header-elements {
  display: flex;
  justify-content: space-between;
  padding: 25px; /* Add padding to the header elements */
  background-color: transparent; /* Set background color to transparent */
  max-width: 1300px;
  width: 100%;
  margin: 0 auto;
}

.logo-image {
  max-width: 110px;
  height: auto;
  border-radius: 25px;
}

.aol-heading {
  font-size: 35px;
  color: #A03611;

}

.aol-logout {
  text-decoration: none;
  color: #FFFFFF;
  padding: 11px 20px;
  border: 1px solid #000000;
  box-shadow: 7px 7px 0px rgba(100, 98, 98, 0.2);
  cursor: pointer;
  font-family: "MS Sans Serif", sans-serif;
  font-size: 14px;
  transition: text-decoration 0.3s;
  border-radius: 25px;
}

.aol-logout {
  background-color: #A03611;
}

.aol-box {
  display: flex;
  background-color: #FFFFFF;
  border: 3px solid #CCCCCC;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
  padding: 20px;
  max-width: 1100px;
  width: 100%;
  margin: 0 auto;
  position: relative;
  border-radius: 30px;
}

.aol-box-input label {
  margin-right: 0.5rem;
}

.aol-box-input input {
  width: calc(100% - 27px);
  padding: 10px;
  border: 2px solid #CCCCCC;
  font-family: Verdana, sans-serif;
  font-size: 16px;
  border-radius: 10px;
}

.content {
  flex-grow: 1;
}

.response-area {
  margin-top: 20px;
  height: calc(560px - 40px);
  border: 2px solid #CCCCCC;
  padding: 10px;
  overflow: auto;
  word-wrap: break-word;
  border-radius: 10px;
  font-size: 18px;
}

</style>