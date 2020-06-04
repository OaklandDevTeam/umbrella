<template>
  <div>
    <u-label style="font-size: 2em; line-height: 2em; display:flex;">
      <img style="height: 2em;" src="../assets/placeholderavatar.svg" />
      {{user.username}}
    </u-label>
    <u-label>{{user.post_count}} posts</u-label>
    <u-label>User Since {{user.registration_date}}</u-label>
  </div>
</template>

<script>
import store from "../store/";

export default {
  name: "UserCard",
  computed: {
    user() {
      const months = [
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
      ];
      var i = store.state.user;
      i.registration_date = `${
        months[new Date(i.registration_date).getMonth()]
      } ${new Date(i.registration_date).getFullYear()}`;
      return store.state.user;
    }
  }
};
</script>

<style>
</style>