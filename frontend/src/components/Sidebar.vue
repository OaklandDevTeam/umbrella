<template>
  <div class="flex-column unspaced" style="
    justify-content: flex-start;">
    <u-logo @click.native="goHome()"></u-logo>
    <u-card>
      <u-user-card></u-user-card>
      <div style="margin: 2vh;">
        <u-label style="display: inline; font-size: 2em; display:flex;">
          <img style="height: 1em" src="../assets/fresh.svg" />Fresh
        </u-label>
        <u-label>PLACEHOLDER</u-label>
      </div>
      <div style="margin: 2vh;">
        <u-label style="display: inline; font-size: 2em; display:flex;">
          <img style="height: 1em" src="../assets/favorites.svg" />Favorites
        </u-label>
        <u-label>PLACEHOLDER</u-label>
      </div>
    </u-card>
  </div>
</template>

<script>
import store from "../store";

export default {
  name: "Sidebar",
  data() {
    return {};
  },
  methods: {
    goHome() {
      store.commit("setPage", "front");
    }
  }
};
</script>

<style></style>
