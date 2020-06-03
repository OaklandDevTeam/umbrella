<template>
  <div>
    <link href="https://fonts.googleapis.com/css?family=Roboto" rel="stylesheet" />
    <landing-page v-if="page == 'landing'"></landing-page>
    <div class="bg" v-if="page != 'landing'">
      <div class="flex-row">
        <u-sidebar></u-sidebar>
        <front-page v-if="page == 'front'"></front-page>
        <debug-page v-if="page === 'debug'"></debug-page>
      </div>
    </div>
  </div>
</template>

<script>
import store from "./store";

export default {
  name: "App",
  data() {
    return {};
  },
  computed: {
    page() {
      return store.state.page;
    }
  },
  methods: {}
};
</script>

<style src="./assets/stylesheet.css"></style>
