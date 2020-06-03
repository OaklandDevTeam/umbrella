<!--Lots of help from here https://tiptap.scrumpy.io/docs/-->
<template>
    <div class="text-box">
        <editor-menu-bar :editor="textBoxEditor" v-slot="{commands, isActive}">
            <div class="text-box-menubar">
                <button style="font-weight: bolder" class="slightly-responsive textbox-menubar-button" :class="{'is-active': isActive.bold() }" @click="commands.bold">B</button>
                <button style="font-style: italic" class="slightly-responsive textbox-menubar-button" :class="{'is-active': isActive.italic() }" @click="commands.italic">I</button>
                <button style="text-decoration: underline" class="slightly-responsive textbox-menubar-button" :class="{'is-active': isActive.underline() }" @click="commands.underline">U</button>
            </div>
        </editor-menu-bar>
        <editor-content @input="handleInput" :editor="textBoxEditor"/>
    </div>
</template>

<script>
    import {
        Editor,
        EditorContent,
        EditorMenuBar
    } from 'tiptap'
    import {
        Bold, Italic, Underline
    } from 'tiptap-extensions'

    export default {
        name: "TextBox",
        prop: ['placeholder'],
        components: {
            EditorContent,
            EditorMenuBar
        },
        data() {
            return {
                textBoxEditor: new Editor({
                    content: '<p>Type here !</p>',
                    extensions: [
                        new Bold(),
                        new Italic(),
                        new Underline()
                    ]
                }),
            }
        },
        beforeDestroy() {
            this.textBoxEditor.destroy()
        },
        methods: {
            handleInput() {
                // todo textBoxEditor.value() might be the wrong method to call, the docs aren't strait-forward.
                this.$emit('input', this.textBoxEditor.value())
            }
        }
    }
</script>

<style scoped>
    * {
        box-sizing: border-box;
    }
    .text-box {
        height: inherit;
        width: inherit;
        padding: 5px;
        margin: 0;
    }
    .text-box-menubar {
        width: 100%;
        height: 3vh;
        margin: 0;
    }
    .textbox-menubar-button {
        height: inherit;
        font-weight: bold;
        background-color: transparent;
        border: none;
    }
</style>