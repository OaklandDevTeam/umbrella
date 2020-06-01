<template>
  <div></div>
</template>

<script>
// import store from "../store";

export default {
  name: "FrontPage",
  data() {
    return {};
  },
  methods: {
    setState(e) {
      this.state = e;
    }
  }
};
</script>

<style>
.flex-row {
  display: flex;
  flex-direction: row;
  justify-content: space-evenly;
  margin: 1%;
  padding: 0;
}

.flex-column.unspaced {
  justify-content: left;
}

.flex-column {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  margin: 0;
  padding: 0;
}

.flex-row.unspaced {
  justify-content: flex-start;
}

.stuff-container {
  overflow: hidden;
  width: min-content;

  transition: all 5.5s ease-in-out;
}
</style>
