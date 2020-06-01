<template>
  <div class="flex-column unspaced">
    <u-logo></u-logo>
    <u-card>
      <u-label style="display: inline; font-size: 2em;">
        <img style="height: 1em" src="../assets/fresh.svg" />Fresh
      </u-label>

      <u-label style="display: inline; font-size: 2em;">
        <img style="height: 1em" src="../assets/favorites.svg" />Favorites
      </u-label>
    </u-card>
  </div>
</template>

<script>
// import store from "../store";

export default {
  name: "Sidebar",
  data() {
    return {};
  },
  methods: {
    setState(e) {
      this.state = e;
    }
  }
};
</script>

<style></style>
