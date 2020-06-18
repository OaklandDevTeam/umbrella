<template>
  <div style="width: 100%;">
    <u-modal v-if="active">
      <u-label style="font-size: 2em; line-height: 1.5em;">{{post.title}}</u-label>
      <u-label>author: {{post.author}}</u-label>
      <text-box
        v-bind:value="post.bodyText"
        v-model="post.bodyText"
        v-bind:editable="editing"
        ref="richtext"
      />
      <u-flat-button
        v-if="user.username == post.author"
        @click.native="toggleEdit()"
      >{{editing ? "Done" : "Edit"}}</u-flat-button>
      <u-flat-button @click.native="active=false">close</u-flat-button>

      <u-text-field
        style="width: 55%"
        ref="commentText"
        v-model="commentText"
        placeholder="Say Something!"
      ></u-text-field>
      <u-button @click.native="postComment()">Post</u-button>
      <div class="comment-div">
        <u-label
          v-for="comment in comments.comments.slice().reverse()"
          v-bind:key="comment.idString"
          class="comment"
        >{{comment.bodyText}}</u-label>
      </div>
      <u-label style="margin: none; font-size: 0.65em;">∨</u-label>
    </u-modal>
    <u-card>
      <div class="flex-row">
        <div class="flex-column">
          <u-label style="font-size: 2em; line-height: 1.5em;">{{post.title}}</u-label>
          <u-label>author: {{post.author}}</u-label>
          <u-flat-button @click.native="openModal()">read</u-flat-button>
        </div>
      </div>
    </u-card>
  </div>
</template>

<script>
import store from "../store";

export default {
  name: "PostCard",
  data() {
    return { active: false, editing: false, comments: {}, commentText: "" };
  },
  props: {
    post: Object
  },
  methods: {
    toggleEdit() {
      if (this.editing) {
        this.axios.put(
          "/posts/modify",
          {
            post_id: this.post.idString,
            body: this.post.bodyText
          },
          { headers: { "Content-Type": "application/json" } }
        );
      }

      this.editing = !this.editing;
    },

    loadComments() {
      this.axios
        .put(
          "/comments/list",
          {
            postId: this.post.idString
          },
          { headers: { "Content-Type": "application/json" } }
        )
        .then(response => {
          this.comments = response.data;
        });
    },
    openModal() {
      this.active = true;
      this.loadComments();
    },
    postComment() {
      this.axios.post(
        "/comments/create",
        {
          postId: this.post.idString,
          bodyText: this.commentText
        },
        { headers: { "Content-Type": "application/json" } }
      );
      if (this.commentText != "") {
        this.comments.comments.push({ bodyText: this.commentText });
      }
      this.commentText = "";
      this.$refs.commentText.value = "";
    }
  },
  computed: {
    user() {
      return store.state.user;
    }
  }
};
</script>

<style>
.closed {
  max-height: 3em;
  overflow: hidden;
}

.comment {
  margin: 10px;
}

.comment-div {
  max-height: 25vh;
  overflow-y: auto;
}
::-webkit-scrollbar {
  display: none;
}

.postBody {
  overflow-wrap: anywhere;
}
</style>
