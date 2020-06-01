<template>
  <div class="shadowed u-card slightly-responsive">
    <slot></slot>
  </div>
</template>

<script>
export default {
  name: "Card"
};
</script>

<style>
.u-card {
  background-color: var(--card-bg-color);
  border-radius: 2vh;
  padding: 5%;
  margin: 1%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
</style>

<style src="../assets/common-u-styles.css"></styles>