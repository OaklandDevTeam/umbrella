<template>
  <video class="v-bg" autoplay muted loop>
    <source :src="src" type="video/mp4" />
  </video>
</template>

<script>
export default {
  name: "VideoBG",
  prop: ["src"],
  data() {
    return {
      src: ""
    };
  }
};
</script>

<style>
.v-bg {
  min-width: 100%;
  min-height: 100%;
  overflow-y: hidden;
  position: fixed;
  right: 0;
  bottom: 0;
  z-index: -1;
}
</style>