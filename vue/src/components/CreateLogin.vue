<template>
    <form @submit.prevent="login" class="aol-form">

      <div class="content">

      <h1 class="aol-heading">RazorBack Login</h1>
      <div role="alert" v-if="invalidCredentials" class="aol-alert">
        Invalid username and password!
      </div>
      
      <div role="alert" v-if="this.$route.query.registration" class="aol-alert">
        Thank you for registering, please sign in.
      </div>
      
      <div class="aol-form-input">
        <label for="username">User Name:</label>
        <input type="text" id="username" v-model="user.username" required autofocus />
      </div>
      
      <div class="aol-form-input">
        <label for="password">Password:</label>
        <input type="password" id="password" v-model="user.password" required />
      </div>
      <br>

      <div class="button-container">
      <button type="submit" class="aol-signing-on">SIGN ON</button>
      <router-link :to="{ name: 'register' }" class="aol-signing-up">SIGN UP</router-link>
      </div>
      </div>
    
    </form>
</template>
<script>
import authService from "../services/AuthService";

export default {
  name: "login",
  components: {},
  data() {
    return {
      user: {
        username: "",
        password: ""
      },
      invalidCredentials: false
    };
  },
  methods: {
    login() {
      authService
        .login(this.user)
        .then(response => {
          if (response.status == 200) {
            this.$store.commit("SET_AUTH_TOKEN", response.data.token);
            this.$store.commit("SET_USER", response.data.user);
            this.$router.push("/");
          }
        })
        .catch(error => {
          const response = error.response;

          if (response.status === 401) {
            this.invalidCredentials = true;
          }
        });
    }
  }
};
</script>

<style scoped>
/*login box format*/
.aol-form {
  display: flex;
  background-color: #FFFFFF;
  border: 3px solid #CCCCCC;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
  padding: 20px; /* Add padding for content spacing */
  max-width: 600px;
  width: 100%;
  margin: 20px auto; /* Adjust margin for centering */
  position: relative;
  border-radius: 15px; /* Add rounded edges */
}

.aol-heading {
  font-size: 28px;
  margin-bottom: 20px;
  text-align: center;
  color: #A03611;
}

.aol-form-input {
  margin-bottom: 1rem;
}

.aol-alert {
  background-color: #FFCCCC;
  border: 1px solid #FF6666;
  border-radius: 5px;
  color: #FF0000;
  padding: 10px;
  text-align: center;
  margin-bottom: 1rem;
}

.aol-form-input label {
  margin-right: 0.5rem;
}

.aol-form-input input {
  width: calc(100% - 20px);
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #CCCCCC;
  border-radius: 5px; /* Add rounded edges to input fields */
}

.button-container {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.aol-signing-up,
.aol-signing-on {
  position: absolute;
  text-decoration: none;
  bottom: 20px;
  color: #FFFFFF;
  padding: 10px 20px;
  border: none; /* Remove the border to create a cleaner appearance */
  border-radius: 25px; /* Add rounded corners */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* Add a subtle box shadow for depth */
  cursor: pointer;
  font-family: "MS Sans Serif", sans-serif;
  font-size: 14px;
  transition: background-color 0.3s, color 0.3s; /* Add transition for a smoother effect */
}

.aol-signing-on {
  left: 35px;
  background-color:#A03611;
}

.aol-signing-up {
  right: 35px;
  background-color: #A03611;
}

.aol-signing-on:hover,
.aol-signing-up:hover {
  text-decoration: underline;
}

.content {
  flex-grow: 1; /* Allow content to grow and fill available space */
  padding: 20px;
}

#panel {
  display: flex;
}

</style>