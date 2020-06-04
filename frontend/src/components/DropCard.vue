<template>
  <div style="width: 100%;">
    <u-card>
      <div class="flex-row">
        <div class="flex-column">
          <u-label style="font-size: 2em;">{{drop.title}}</u-label>
          <span style="word-wrap: break-word;" v-html="drop.topic"></span>
        </div>
        <!-- <div class="flex-column" style="margin-left: 75%;"> -->
        <!-- <u-button>Subscribe</u-button> -->
        <!-- </div> -->
      </div>
      <div class="flex-row" style="align-self: baseline; margin-left: -2.5%; margin-bottom: -5%;">
        <u-flat-button @click.native="open()">open</u-flat-button>
      </div>
    </u-card>
  </div>
</template>

<script>
import store from "../store";

export default {
  name: "DropCard",
  data() {
    return {};
  },
  props: {
    drop: Object
  },
  methods: {
    open() {
      store.commit("setDrop", this.drop);
      store.commit("setPage", "drop");
    }
  }
};
</script>

<style>
</style>
