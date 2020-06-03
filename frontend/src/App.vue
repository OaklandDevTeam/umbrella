<template>
  <div>
    <link href="https://fonts.googleapis.com/css?family=Roboto" rel="stylesheet" />
    <landing-page v-if="page == 'landing'"></landing-page>
    <div class="bg" v-if="page != 'landing'">
      <div class="flex-row">
        <u-sidebar></u-sidebar>
        <div style="flex-basis: 100%; margin-left: 5%;">
          <front-page v-if="page == 'front'"></front-page>
          <drop-page v-if="page == 'drop'"></drop-page>
        </div>
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
  }
};
</script>

<style src="./assets/stylesheet.css"></style>
