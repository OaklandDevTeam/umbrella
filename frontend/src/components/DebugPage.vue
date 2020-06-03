<template>
    <div>
        <h1>This is the debug page</h1>
        <div class="textbox-wrapper">
            <u-text-box></u-text-box>
        </div>
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

<style>
    .textbox-wrapper {
        width: 50%;
        height: 20vh;
        margin: auto;
    }

</style>