<template>
  <div>
    <u-label style="font-size: 2em; line-height: 2em; display:flex;">
      <img style="height: 2em;" src="../assets/placeholderavatar.svg" />
      {{user}}
    </u-label>
    <u-label>42 posts</u-label>
    <u-label>User Since June 2020</u-label>
  </div>
</template>

<script>
import store from "../store/";
export default {
  name: "UserCard",
  computed: {
    user() {
      return store.state.user;
    }
  }
};
</script>

<style>
</style>