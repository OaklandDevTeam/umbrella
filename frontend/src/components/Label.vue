<template>
    <p class="u-label">
      <slot></slot>
    </p>
</template>

<script>
  export default {
    name: "Label"
  }
</script>

<style>
  .u-label{
    font-size: 1em;
    font-family: var(--font);
    text-align: center;
    line-height: 0.25em;
    margin: 0.75vh;
  }
</style>

<style src="../assets/common-u-styles.css"></styles>