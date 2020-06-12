<template>
  <div>
    <u-modal v-if="newDrop">
      <div class="new-modal-wrapper">
        <u-label style="font-size: 2em; line-height: 1.5em;">New Drop</u-label>
        <u-label>{{this.error}}</u-label>
        <u-text-field placeholder="Title" v-model="newDropTitle"></u-text-field>
        <text-box
          class="shadowed slightly-responsive input-textbox"
          placeholder="Topic"
          v-model="newDropTopic"
          editable="true"
        ></text-box>
        <div class="flex-row">
          <u-button @click.native="createDrop();">Submit</u-button>
          <u-button @click.native="newDrop=false">Cancel</u-button>
        </div>
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
      if (this.newDropTitle == "" || this.newDropTopic == "") {
        this.error = "Please add a title and topic to your drop.";
        return;
      }
      this.axios
        .post(
          "/drops/create",
          {
            title: this.newDropTitle,
            topic: this.newDropTopic
          },
          { headers: { "Content-Type": "application/json" } }
        )
        .then(response => {
          if (response.status == 200 || response.status == 400) {
            this.error = "";
            this.refresh();
            this.newDrop = false;
<<<<<<< HEAD
=======
          } else {
            this.error =
              "Invalid or already used title (titles cannot contain spaces).";
>>>>>>> 32ae8a91d80d3f5c70480c3be90536bb9a428f3b
          }
        })
        .catch(() => {
          {
            this.error =
              "Invalid or already used title (titles cannot contain spaces).";
          }
        })
        .bind(this);
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

<style>
.new-modal-wrapper {
  width: 50vw;
  padding-left: 3vw;
  padding-right: 3vw;
  padding-top: 3vh;
  padding-bottom: 3vh;
}

.new-modal-wrapper > * {
  box-sizing: border-box;
  width: inherit;
}

.input-textbox {
  height: 30vh;
  padding: 0.5vh 0.5vw 0.5vh 0.5vw;
<<<<<<< HEAD
  border-radius: 10px;
=======
>>>>>>> 32ae8a91d80d3f5c70480c3be90536bb9a428f3b
}
</style>
