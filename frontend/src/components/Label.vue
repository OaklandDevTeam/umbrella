<template>
    <pre class="u-label">
      <slot></slot>
    </pre>
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
    line-height: 1em;
    margin: 0;
  }
</style>

<style src="../assets/common-u-styles.css"></styles>