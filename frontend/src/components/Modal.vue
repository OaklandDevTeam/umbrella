<template>
  <div class="u-modal-bg">
    <div class="flex-column" style="height: 100vh;">
      <u-card style="padding: 1vh;">
        <slot></slot>
      </u-card>
    </div>
  </div>
</template>

<script>
export default {
  name: "Modal"
};
</script>

<style>
.u-modal-bg {
  position: fixed;
  z-index: 10;
  width: 100%;
  height: 100vh;
  top: 0;
  left: 0;
  background-color: rgba(0, 0, 0, 0.5);
}
</style>

<style src="../assets/common-u-styles.css"></styles>