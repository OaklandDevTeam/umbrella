<template>
  <div style="width: 100%;">
    <u-card>
      <div class="flex-row">
        <div class="flex-column">
          <u-label style="font-size: 2em; line-height: 1.5em;">{{post.title}}</u-label>
          <u-label>author: {{post.author}}</u-label>
          <p
            v-if="post.bodyText.length <= 100 || active"
            style="word-wrap: break-word;"
          >{{post.bodyText}}</p>
          <p
            v-if="post.bodyText.length > 100 && !active"
            style="word-wrap: break-word;"
          >{{post.bodyText.substring(0, 100)}}...</p>

          <u-flat-button @click.native="active=!active" v-if="post.bodyText.length > 100">
            <pre v-if="!active">read more</pre>
            <pre v-if="active">less</pre>
          </u-flat-button>
        </div>
      </div>
    </u-card>
  </div>
</template>

<script>
// import store from "../store";

export default {
  name: "PostCard",
  data() {
    return { active: false };
  },
  props: {
    post: Object
  },
  methods: {}
};
</script>

<style>
</style>
