<template>
  <div>
    <u-modal v-if="newDrop">
      <u-label style="font-size: 2em; line-height: 1.5em;">New Drop</u-label>
      <u-label>{{this.error}}</u-label>
      <u-text-field placeholder="Title" v-model="newDropTitle"></u-text-field>
      <u-text-field placeholder="Topic" v-model="newDropTopic"></u-text-field>
      <div class="flex-row">
        <u-button @click.native="createDrop();">Submit</u-button>
        <u-button @click.native="newDrop=false">Cancel</u-button>
      </div>
    </u-modal>
    <u-card>
      <u-label style="font-size: 2.5em;">Welcome to Umbrella!</u-label>
      <u-button @click.native="newDrop = true">Create a Drop</u-button>
    </u-card>
    <u-drop-card v-for="drop in drops" v-bind:key="drop.drop_id" v-bind:drop="drop"></u-drop-card>
  </div>
</template>

<script>
import store from "../store";

export default {
  name: "FrontPage",
  data() {
    return {
      drops: {},
      error: "",
      newDrop: false,
      newDropTitle: "",
      newDropTopic: ""
    };
  },
  methods: {
    refresh() {
      this.axios.get("/drops/list").then(response => {
        this.drops = response.data;
      });
    },
    createDrop() {
      this.axios
        .post(
          "/drops/create",
          {
            title: this.newDropTitle,
            topic: this.newDropTopic,
            owner: this.user //won't be needed in the future
          },
          { headers: { "Content-Type": "application/json" } }
        )
        .then(response => {
          if (response.status == 200) {
            this.error = "";
            this.refresh();
            this.newDrop = false;
          } else {
            this.error = "Title already in use.";
          }
        });
    }
  },
  computed: {
    user() {
      return store.state.user;
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
