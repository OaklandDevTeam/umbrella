<template>
  <div style="width: 100%;">
    <u-card>
      <div class="flex-row">
        <div class="flex-column">
          <u-label style="font-size: 2em; line-height: 1.5em;">{{post.title}}</u-label>
          <u-label>author: {{post.author}}</u-label>

          <div class="postBody" v-bind:class="{closed: !active}" v-html="post.bodyText"></div>

          <u-flat-button @click.native="active=!active">
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
.closed {
  max-height: 3em;
  overflow: hidden;
}

.postBody {
  overflow-wrap: anywhere;
}
</style>
