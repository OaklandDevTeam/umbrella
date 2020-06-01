<template>
  <div>
    <link href="https://fonts.googleapis.com/css?family=Roboto" rel="stylesheet" />
    <landing-page v-if="page == 'landing'"></landing-page>
    <div class="bg" v-if="page!='landing'">
      <div class="flex-row">
        <u-sidebar></u-sidebar>
        <front-page v-if="page == 'front'"></front-page>
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

<style>
.bg {
  background-color: F8F8F8;
  display: inline-block;
  height: 100%;
  width: 100%;
  position: fixed;
  left: 0;
  top: 0;
}

.flex-row {
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  margin: 1%;
  padding: 0;
}
</style>
