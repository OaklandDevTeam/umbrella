<template>
  <div style="width: 100%;">
    <u-card>
      <div class="flex-row">
        <div class="flex-column">
          <u-label style="font-size: 2em; line-height: 1.5em;">{{post.title}}</u-label>
          <u-label>author: {{post.author}}</u-label>
          <text-box
            v-bind:value="post.bodyText"
            v-model="post.bodyText"
            v-bind:editable="editing"
            ref="richtext"
          />
          <u-flat-button @click.native="active=!active">
            <pre v-if="!active">read more</pre>
            <pre v-if="active">less</pre>
          </u-flat-button>

          <div
            class="flex-row"
            style="align-self: baseline; margin-left: -2.5%; margin-bottom: -5%;"
          >
            <u-flat-button
              v-if="user.username == post.author"
              @click.native="toggleEdit()"
            >{{editing ? "Done" : "Edit"}}</u-flat-button>
          </div>
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
    return { active: false, editing: false };
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

.postBody {
  overflow-wrap: anywhere;
}
</style>
