<template>
  <div>
    <landing-page v-if="page == 'landing'"></landing-page>
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
    },
  },
  methods: {},
};
</script>

<style></style>
