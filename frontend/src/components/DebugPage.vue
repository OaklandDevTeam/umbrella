<template>
    <div>
        <h1>This is the debug page</h1>
        <u-text-box></u-text-box>
    </div>
</template>

<script>
    //import store from "../store";

    export default {
        name: "DebugPage",
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

<style></style>