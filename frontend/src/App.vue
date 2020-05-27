<template>
  <div id="app">
    <u-logo></u-logo>
    <u-button>test</u-button>
    <u-flat-button>flat test</u-flat-button>
  </div>
</template>

<script>
import Logo from "./components/Logo.vue";
import Button from "./components/Button.vue";
import FlatButton from "./components/FlatButton.vue";
import Vue from 'vue';

export default {
  name: "App"
};

Vue.component('u-logo', Logo);
Vue.component('u-button', Button);
Vue.component('u-flat-button', FlatButton);
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
