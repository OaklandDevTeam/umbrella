<template>
  <div class="u-button shadowed responsive">
    <pre class="u-button-label"><slot></slot></pre>
  </div>
</template>

<script>
export default {
  name: "Button"
};
</script>

<style>
.u-button {
  padding: 0.25vh 1em;
  background-color: var(--component-primary);
  border-radius: 25vh;
  display: inline-block;
  margin: 1vh;
  cursor: pointer;
  width: max-content;
}

.u-button-label {
  line-height: 0;
  text-align: justify;
  font-size: 1em;
  font-family: var(--font);
  user-select: none;
}
</style>

<style src="../assets/common-u-styles.css"></styles>