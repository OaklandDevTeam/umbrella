<template>
  <div>
    <landing-page v-if="state=='landing'"></landing-page>
  </div>
</template>

<script>

export default {

  name: "App",
  data() {
    return{
      state: "landing",
    }
  },
  methods: {
   
    }
};

</script>

<style>
</style>


