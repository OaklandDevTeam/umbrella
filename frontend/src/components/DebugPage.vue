<template>
    <div>
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