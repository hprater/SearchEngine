<template>
     <form @submit.prevent="register" class="aol-form">


      <div class="content">

      <h1 class="aol-heading">Create Account</h1>
      <div role="alert" v-if="registrationErrors" class="aol-alert">
        {{ registrationErrorMsg }}
      </div>

      <div class="aol-form-input">
        <label for="username">User Name:</label>
        <input type="text" id="username" v-model="user.username" required autofocus />
      </div>

      <div class="aol-form-input">
        <label for="password">Password:</label>
        <input type="password" id="password" v-model="user.password" required />
      </div>

      <div class="aol-form-input">
        <label for="confirmPassword">Confirm Password:</label>
        <input type="password" id="confirmPassword" v-model="user.confirmPassword" required />
      </div>

      <br>
      <button type="submit" class="aol-create-button">Create Account</button>
      <p class="link"><router-link :to="{ name: 'login' }">Already have an account? Log in.</router-link></p>
    </div>
    </form>
</template>
<script>
import authService from '../services/AuthService';

export default {
  name: 'register',
  data() {
    return {
      user: {
        username: '',
        password: '',
        confirmPassword: '',
        role: 'user',
      },
      registrationErrors: false,
      registrationErrorMsg: 'There were problems registering this user.',
    };
  },
  methods: {
    register() {
      if (this.user.password != this.user.confirmPassword) {
        this.registrationErrors = true;
        this.registrationErrorMsg = 'Password & Confirm Password do not match.';
      } else {
        authService
          .register(this.user)
          .then((response) => {
            if (response.status == 201) {
              this.$router.push({
                path: '/login',
                queryWords: { registration: 'success' },
              });
            }
          })
          .catch((error) => {
            const response = error.response;
            this.registrationErrors = true;
            if (response.status === 400) {
              this.registrationErrorMsg = 'Bad Request: Validation Errors';
            }
          });
      }
    },
    clearErrors() {
      this.registrationErrors = false;
      this.registrationErrorMsg = 'There were problems registering this user.';
    },
  },
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

.aol-create-button {
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
  left: 35px;
  margin-bottom: 70px;
  background-color: #A03611;
}

.aol-create-button:hover {
  text-decoration: underline;
}


.link {
    margin-top: 40px;
    margin-bottom: 10px;
}

.content {
  flex-grow: 1; /* Allow content to grow and fill available space */
  padding: 20px; 
}
</style>