<template>
  <div class="u-checkbox-container">
    <div class="u-checkbox-label"><slot></slot></div>
    <div type="checkbox" @click="handleInput()" id="c-box" class="u-checkbox responsive shadowed">
      <img class="u-check" :hidden="!checked" id="c-mark" src = "../assets/check.svg">
    </div>
  </div>
</template>

<script>
  export default {
    name: "Checkbox",
    data () {
      return{
        checked: false
      }
    },
    methods: {
      handleInput() {
        this.checked = ! this.checked;
        this.$emit('input', this.checked);
      }
    }
  }

</script>

<style>
  .u-checkbox{
    background-color: var(--component-primary);
    border-radius: 5px;
    width: 1em;
    height: 1em;
    display: inline-block;
    user-select: none;
    cursor: pointer;
  }

  .u-check{
    max-width: 0.9em;
    max-height: 0.9em;
    user-select: none;
  }

  .u-checkbox-label{
    display: inline;
    font-size: 1em;
    font-family: var(--font);
    text-align: center;
    line-height: 0.25em;
    margin-right: 0.25em;
    user-select: none;
  }

  .u-checkbox-container{
    align-items: center;
    margin: 0.75vh;
    text-align: center;
  }
</style>

<style src="../assets/common-u-styles.css"></styles>