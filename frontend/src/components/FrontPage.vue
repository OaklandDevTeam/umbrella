<template>
  <div>
    <u-card>
      <u-label style="font-size: 2.5em;">Welcome to Umbrella!</u-label>
      <u-button @click.native="newDrop()">Create a Drop</u-button>
    </u-card>
    <u-drop-card v-for="drop in drops" v-bind:key="drop.drop_id" v-bind:drop="drop"></u-drop-card>
  </div>
</template>

<script>
import store from "../store";

export default {
  name: "FrontPage",
  data() {
    return { drops: {} };
  },
  methods: {
    newDrop() {
      store.commit("setPage", "newDrop");
    }
  },
  created: function() {
    this.axios.get("/drops/list").then(response => {
      this.drops = response.data;
    });
  }
};
</script>

<style></style>
