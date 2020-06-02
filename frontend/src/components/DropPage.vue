<template>
  <div>
    <u-card>
      <u-label style="font-size: 2.5em; line-height: 1.5em;">{{drop.title}}</u-label>
      <u-label>{{drop.topic}}</u-label>
      <div class="flex-row">
        <u-button @click.native="goBack()">Go Back</u-button>
        <u-button @click.native="newPost()">newPost</u-button>
      </div>
    </u-card>
    <u-post-card v-for="post in posts" v-bind:key="post.id_string" v-bind:post="post"></u-post-card>
  </div>
</template>

<script>
import store from "../store";

export default {
  name: "DropPage",
  data() {
    return { posts: {} };
  },
  methods: {
    goBack() {
      store.commit("setPage", "front");
    },
    newPost() {}
  },
  created: function() {
    this.axios.get(`/posts/${this.drop.title}/list`).then(response => {
      this.posts = response.data.posts;
    });
  },
  computed: {
    drop() {
      return store.state.drop;
    }
  }
};
</script>

<style></style>
