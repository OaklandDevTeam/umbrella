<template>
  <div style="width: 100%;">
    <u-card>
      <div class="flex-row">
        <div class="flex-column">
          <u-label style="font-size: 2em; line-height: 1.5em;">{{post.title}}</u-label>
          <u-label>author: {{post.author}}</u-label>
          <button v-on:click="toggleEdit()">Edit</button>
          <text-box
            v-bind:value="post.bodyText"
            v-bind:editable="false"
            ref="richtext"
            />
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
  methods: {
    toggleEdit() {
      this.$refs.richtext.toggleEditable()
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
