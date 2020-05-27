<template>
  <input type="text" id="t-field" class="u-text-field shadowed mildly-responsive">
</template>

<script>
  export default {
    name: "TextField"
  }

  

</script>

<style>
  .u-text-field{
    border-width: 0;
    outline-width: 0;
    border-radius: 25vh;
    padding: 10px;
    font-size: 1.5em;
    margin: 0.75vh;
    /* The font family on this element doesn't like variables for some reason */
    font-family: roboto; 
    overflow: hidden;
    text-align: center;
  }
</style>

<style src="../assets/common-u-styles.css"></styles>