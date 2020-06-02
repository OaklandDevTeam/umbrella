<template>
  <div class="flex-column unspaced">
    <u-logo></u-logo>
    <u-card>
      <u-user-card></u-user-card>
      <div style="margin: 2vh;">
        <u-label style="display: inline; font-size: 2em; display:flex;">
          <img style="height: 1em" src="../assets/fresh.svg" />Fresh
        </u-label>
        <u-label>lorem ipsum dolor</u-label>
      </div>
      <div style="margin: 2vh;">
        <u-label style="display: inline; font-size: 2em; display:flex;">
          <img style="height: 1em" src="../assets/favorites.svg" />Favorites
        </u-label>
        <u-label>sit amet consectetur</u-label>
        <u-label>exercitation ullamco</u-label>
        <u-label>qui officia deserunt</u-label>
      </div>
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
