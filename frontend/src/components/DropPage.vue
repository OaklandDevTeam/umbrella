<template>
  <div>
    <u-modal v-if="newPost">
      <div class="drop-modal-wrapper">
        <u-label style="font-size: 2em; line-height: 1.5em;">New Post</u-label>
        <u-label>{{this.error}}</u-label>
        <u-text-field placeholder="Title" v-model="newPostTitle"></u-text-field>
        <text-box
          class="shadowed slightly-responsive input-textbox"
          placeholder="Enter your post text here!"
          v-bind:editable="true"
          v-model="newPostContent"
        ></text-box>
        <div class="flex-row">
          <u-button @click.native="createPost()">Post</u-button>
          <u-button @click.native="newPost=false">Cancel</u-button>
        </div>
      </div>
    </u-modal>
    <u-card>
      <u-label style="font-size: 2.5em; line-height: 1.5em;">{{drop.title}}</u-label>
                      <text-box
          v-bind:value="drop.topic"
          v-model="drop.topic"
          v-bind:editable="false"
          ref="richtext"
      />
      <div class="flex-row">
        <u-button @click.native="goBack()">Go Back</u-button>
        <u-button @click.native="newPost = true">New Post</u-button>
        <u-button v-if="!userIsSubscribed" @click.native="handleSubscribe()">Subscribe</u-button>
        <u-button v-else @click.native="handleUnsubscribe()">Unsubscribe</u-button>
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
      if (this.newPostTitle == "" || this.newPostContent == "") {
        this.error = "Please add a title and content to your post.";
        return;
      }
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
            store.commit("iteratePostCount");
            this.newPost = false;
          }
        })
        .catch(() => {
          this.error = "error creating post";
        })
        .bind(this);
    },
    handleSubscribe() {
      this.axios
        .post(
          "/user/subscribe",
          {
            drop_id: this.drop.drop_id,
            drop_name: this.drop.title
          },
          { headers: { "Content-Type": "application/json" } }
        )
        .then(response => {
          if (response.status === 200) {
            store.commit("appendUserSubscription", this.drop);
          }
        });
    },
    handleUnsubscribe() {
      this.axios
        .post(
          "/user/unsubscribe",
          {
            drop_id: this.drop.drop_id
          },
          { headers: { "Content-Type": "application/json" } }
        )
        .then(response => {
          if (response.status === 200) {
            store.commit("removeUserSubscription", this.drop);
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
    },
    userIsSubscribed() {
      var result = false; // forEach takes a function.. haha, so you can't return THIS function from that scope
      store.state.userSubscriptions.forEach(val => {
        if (val.drop_id === this.drop.drop_id) {
          result = true;
          return;
        }
      });
      return result;
    }
  }
};
</script>

<style>
.drop-modal-wrapper {
  width: 50vw;
  padding-left: 3vw;
  padding-right: 3vw;
  padding-top: 3vh;
  padding-bottom: 3vh;
}

.drop-modal-wrapper > * {
  box-sizing: border-box;
  width: inherit;
}

.input-textbox {
  height: 30vh;
  border-radius: 10px;
  padding: 0.5vh 0.5vw 0.5vh 0.5vw;
}
</style>
