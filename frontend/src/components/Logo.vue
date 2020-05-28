<template>
    <div class="u-logo-bg shadowed mildly-responsive">
      <img src="../assets/umbrella-logo.png" class="u-logo"/>
    </div>
</template>

<script>
  export default {
    name: "Logo"
  }
</script>

<style>

.u-logo-bg{
  background-color: var(--component-primary);
  border-radius: 100%;
  width: var(--logo-size);
  user-select: none;
  margin: 1vh;

  padding: 1vh;
}

.u-logo{
  width: 100%;
  user-select: none;
  display: inline-block;
}
</style>

<style src="../assets/common-u-styles.css">
</style>