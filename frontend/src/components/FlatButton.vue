<template>
  <div class="u-flat-button responsive">
    <p class="u-flat-button-label">
      <slot></slot>
    </p>
  </div>
</template>

<script>
export default {
  name: "FlatButton"
};
</script>

<style>
.u-flat-button {
  padding: 0.25vh 1vw 0.25vh 1vw;
  background-color: transparent;
  border-radius: 5px;
  display: inline-block;
  margin: 0.5vh;
  cursor: pointer;
}

.u-flat-button:active {
  background-color: var(--component-primary);
  box-shadow: 1px 1px 1px #a0a0a0;
}

.u-flat-button-label {
  line-height: 0;
  text-align: justify;
  font-family: var(--font);
  user-select: none;
}
</style>

<style src="../assets/common-u-styles.css"></styles>