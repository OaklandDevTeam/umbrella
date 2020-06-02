<template>
  <div style="height: 10px;">
    <u-card>
      <div class="flex-row">
        <div class="flex-column">
          <u-label>{{drop.drop_title}}</u-label>
          <p
            style="word-wrap: break-word;"
          >Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud</p>
        </div>
        <div cless="flex-column">
          <u-flat-button>Subscribe</u-flat-button>
        </div>
      </div>
    </u-card>
  </div>
</template>

<script>
export default {
  name: "DropCard",
  props: {
    drop: Object
  }
};
</script>

<style>
</style>
