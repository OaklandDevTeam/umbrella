<template>
  <div style="width: 100%;">
    <u-card>
      <div class="flex-row">
        <div class="flex-column">
          <u-label style="font-size: 2em; line-height: 1.5em;">{{post.title}}</u-label>
          <u-label>{{post.author}}</u-label>
          <p style="word-wrap: break-word;">{{post.bodyText}}</p>
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
    return {};
  },
  props: {
    post: Object
  },
  methods: {}
};
</script>

<style>
</style>
