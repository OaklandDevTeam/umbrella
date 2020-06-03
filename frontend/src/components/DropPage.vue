<template>
  <div>
    <u-modal v-if="newPost">
      <u-label style="font-size: 2em; line-height: 1.5em;">New Post</u-label>
      <u-label>{{this.error}}</u-label>
      <u-text-field placeholder="Title" v-model="newPostTitle"></u-text-field>
      <u-text-field placeholder="Content" v-model="newPostContent"></u-text-field>
      <div class="flex-row">
        <u-button @click.native="createPost();">Post</u-button>
        <u-button @click.native="newPost=false">Cancel</u-button>
      </div>
    </u-modal>
    <u-card>
      <u-label style="font-size: 2.5em; line-height: 1.5em;">{{drop.title}}</u-label>
      <u-label>{{drop.topic}}</u-label>
      <div class="flex-row">
        <u-button @click.native="goBack()">Go Back</u-button>
        <u-button @click.native="newPost = true">newPost</u-button>
      </div>
    </u-card>
    <u-post-card
      v-for="post in posts.slice().reverse()"
      v-bind:key="post.id_string"
      v-bind:post="post"
    ></u-post-card>
  </div>
</template>

<script>
import store from "../store";

export default {
  name: "DropPage",
  data() {
    return {
      posts: {},
      newPostTitle: "",
      newPostContent: "",
      newPost: false,
      error: ""
    };
  },
  methods: {
    goBack() {
      store.commit("setPage", "front");
    },
    refresh() {
      this.axios.get(`/posts/${this.drop.title}/list`).then(response => {
        this.posts = response.data.posts;
      });
    },
    createPost() {
      this.axios
        .post(
          "/posts/create",
          {
            title: this.newPostTitle,
            body: this.newPostContent,
            drop_id: this.drop.drop_id
          },
          { headers: { "Content-Type": "application/json" } }
        )
        .then(response => {
          if (response.status == 200) {
            this.error = "";
            this.refresh();
            this.newPost = false;
          } else {
            this.error = "error creating post";
          }
        });
    }
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
