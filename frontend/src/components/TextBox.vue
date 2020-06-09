<!--Lots of help from here https://tiptap.scrumpy.io/docs/-->
<template>
    <div class="text-box-wrapper shadowed">
        <editor-menu-bar :editor="textBoxEditor" v-slot="{commands, isActive}">
            <div class="text-box-menubar">
                <button style="font-weight: bolder" class="slightly-responsive textbox-menubar-button" :class="{'is-active': isActive.bold() }" @click="commands.bold">B</button>
                <button style="font-style: italic" class="slightly-responsive textbox-menubar-button" :class="{'is-active': isActive.italic() }" @click="commands.italic">I</button>
                <button style="text-decoration: underline" class="slightly-responsive textbox-menubar-button" :class="{'is-active': isActive.underline() }" @click="commands.underline">U</button>
            </div>
        </editor-menu-bar>
        <editor-content class="editor-content" :editor="textBoxEditor"/>
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
        props: ["value", "placeholder"],
        components: {
            EditorContent,
            EditorMenuBar
        },
        data() {
            return {
                textBoxEditor: null,
            }
        },
        mounted() {
            this.textBoxEditor = new Editor({
                    content: this.validatePlaceholder(),
                    extensions: [
                        new Bold(),
                        new Italic(),
                        new Underline()
                    ],
                    onUpdate: ({getHTML}) => {
                        this.handleInput(getHTML())
                    }
                })
        },
        beforeDestroy() {
            this.textBoxEditor.destroy()
        },
        methods: {
            handleInput(val) {
                if(val) {
                    this.$emit("input", val)
                }
            },
            validatePlaceholder() {
                if(this.placeholder) {
                    return `<p>${this.placeholder}</p>`
                } else {
                    return "<p>Type here!</p>"
                }
            }
        }
    }
</script>

<style scoped>
    * {
        box-sizing: border-box;
    }
    .text-box-wrapper {
        height: inherit;
        width: inherit;
        border-radius: 10px;
        font-size: 1.5em;
        font-family: roboto;
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

    .editor-content {
        outline: 0;
    }
</style>