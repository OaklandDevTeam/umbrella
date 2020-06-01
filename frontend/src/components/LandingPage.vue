<template>
  <div>
    <link
      href="https://fonts.googleapis.com/css?family=Roboto"
      rel="stylesheet"
    />
    <u-video-bg
      src="http://d33ys4trmuj47l.cloudfront.net/rain-bg.mp4"
    ></u-video-bg>
    <div class="flex-row">
      <div class="flex-column">
        <u-card>
          <u-logo></u-logo>
          <u-text-field
            id="name"
            v-model="username"
            placeholder="username"
          ></u-text-field>
          <u-label
            id="username-errors"
            class="error-box"
            v-if="usernameErrors != ''"
            >{{ usernameErrors }}</u-label
          >
          <u-label id="signup-fail-label" hidden="true"
            >This username is taken!</u-label
          >
          <u-label id="name-len-label" hidden="true"
            >username must be at least 5 characters</u-label
          >
          <u-label id="name-chars-label1" hidden="true"
            >can't contain special characters other than</u-label
          >
          <u-label id="name-chars-label2" hidden="true">-=^+@,._</u-label>

          <u-text-field
            id="pass"
            type="password"
            v-model="password"
            placeholder="password"
          ></u-text-field>
          <u-label
            id="password-errors"
            class="error-box"
            v-if="passwordErrors != ''"
            >{{ passwordErrors }}</u-label
          >
          <u-label id="pass-len-label" hidden="true"
            >password must be at least 5 characters</u-label
          >
          <u-label id="pass-chars-label1" hidden="true"
            >can't contain special characters other than</u-label
          >
          <u-label id="pass-chars-label2" hidden="true">-=^+@,._</u-label>
          <u-label id="pass-no-match-label" hidden="true"
            >passwords do not match</u-label
          >
          <u-label id="no-name-pass-label" hidden="true"
            >enter a username and password</u-label
          >

          <u-label id="signup-success-label" hidden="true"
            >Sign-up successful!</u-label
          >
          <u-label v-if="loginErrors != ''" hidden="true">
            {{ loginErrors }}
          </u-label>
          <u-label v-if="signupSuccess">Signup successful!</u-label>

          <div id="login-stuff" class="stuff-container" v-if="state == 'login'">
            <div class="flex-row">
              <u-button id="login" @click.native="login()">Login</u-button>
              <u-button id="new account" @click.native="setState('register')"
                >sign up</u-button
              >
            </div>
          </div>

          <div
            id="reg-stuff"
            class="stuff-container"
            v-if="state == 'register'"
          >
            <u-text-field
              id="confirm-pass"
              type="password"
              v-model="confirmPassword"
              placeholder="confirm password"
            ></u-text-field>

            <u-text-field
              id="email"
              v-model="email"
              placeholder="email address"
            ></u-text-field>
            <u-label
              id="email-errors"
              class="error-box"
              v-if="emailErrors != ''"
              >{{ emailErrors }}</u-label
            >
            <u-label id="no-email-label" hidden="true"
              >please enter your email address</u-label
            >

            <u-check-box id="age-check" v-model="ageCheck"
              >Are you over 13?</u-check-box
            >
            <u-label id="age-errors" class="error-box" v-if="ageErrors != ''">
              {{ ageErrors }}
            </u-label>
            <u-label id="age-label" hidden="true"
              >You must be over 13 to register.</u-label
            >

            <div class="flex-row">
              <u-button id="back" @click.native="setState('login')"
                >Back</u-button
              >
              <u-button id="register" @click.native="register()"
                >Register</u-button
              >
            </div>
          </div>
        </u-card>
      </div>
    </div>
  </div>
</template>

<script>
import store from "../store";

export default {
  name: "LandingPage",
  data() {
    return {
      state: "login",
      ageCheck: "",
      username: "",
      password: "",
      confirmPassword: "",
      email: "",
      usernameErrors: "",
      passwordErrors: "",
      emailErrors: "",
      ageErrors: "",
      loginErrors: "",
      signupSuccess: false,
    };
  },
  computed: {
    page() {
      return store.state.page;
    },
  },
  methods: {
    clearErrorLabels() {
      this.usernameErrors = "";
      this.passwordErrors = "";
      this.emailErrors = "";
      this.ageErrors = "";
      this.loginErrors = "";
      this.signupSuccess = false;
    },
    setState(e) {
      this.clearErrorLabels();
      this.state = e;
    },
    login() {
      this.clearErrorLabels();
      this.axios
        .post("/login", `username=${this.username}&password=${this.password}`)
        .then(
          function(response) {
            if (response.status == 200) {
              store.commit("setPage", "frontpage");
            }
          }.bind(this)
        )
        .catch(
          function(error) {
            if (error.response.status == 403) {
              this.passwordErrors = "invalid username/password";
            }
          }.bind(this)
        );
    },
    register() {
      this.clearErrorLabels();

      var usernameRegex = new RegExp(/^[a-zA-Z0-9=^+@,._]+$/);
      var validUsername = usernameRegex.test(this.username);
      var passwordRegex = new RegExp(/^[a-zA-Z0-9=^+@,._ ]+$/);
      var validPassword = passwordRegex.test(this.password);
      var emailRegex = new RegExp(/^[^\s@]+@[^\s@]+\.[^\s@]+$/);
      var validEmail = emailRegex.test(this.email);

      if (!this.ageCheck)
        this.ageErrors += "\nYou must be over 13 to register.";
      if (this.username.length < 5)
        this.usernameErrors += "\nusername must be at least 5 characters";
      if (this.password.length < 5)
        this.passwordErrors += "\npassword must be at least 5 characters";
      if (!validUsername)
        this.usernameErrors +=
          "\ncan't contain special characters other than\n-=^+@,._";
      if (!validPassword)
        this.passwordErrors +=
          "\ncan't contain special characters other than\n-=^+@,._";
      if (this.password != this.confirmPassword)
        this.passwordErrors += "\npasswords don't match!";
      if (!validEmail) this.emailErrors += "\nInvalid email!";

      if (
        this.usernameErrors +
          this.passwordErrors +
          this.emailErrors +
          this.ageErrors ==
        ""
      ) {
        this.axios
          .post(
            "/register",
            `username=${this.username}&password=${this.password}&email=${this.email}`
          )
          .then(
            function(response) {
              if (response.status == 200) {
                this.setState("login");
                this.signupSuccess = true;
              }
            }.bind(this)
          )
          .catch(
            function(error) {
              if (error.response.status == 400) {
                this.usernameErrors += "Username is already in use!";
              }
            }.bind(this)
          );
      }

      this.usernameErrors = this.usernameErrors.trim();
      this.passwordErrors = this.passwordErrors.trim();
      this.emailErrors = this.emailErrors.trim();
      this.ageErrors = this.ageErrors.trim();
    },
  },
};
</script>

<style>
.error-text {
}
</style>
