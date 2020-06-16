<!--Lots of help from here https://tiptap.scrumpy.io/docs/-->
<template>
    <div class="text-box-wrapper">
        <editor-menu-bar :editor="textBoxEditor" v-slot="{commands, isActive}">
            <div class="text-box-menubar" v-if="editable">
                <button style="font-weight: bolder" class="slightly-responsive textbox-menubar-button" :class="{'is-active': isActive.bold() }" @click="commands.bold">B</button>
                <button style="font-style: italic" class="slightly-responsive textbox-menubar-button" :class="{'is-active': isActive.italic() }" @click="commands.italic">I</button>
                <button style="text-decoration: underline" class="slightly-responsive textbox-menubar-button" :class="{'is-active': isActive.underline() }" @click="commands.underline">U</button>
            </div>
        </editor-menu-bar>
        <editor-content :editor="textBoxEditor"/>
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
        props: {
            value: String,
            placeholder: String,
            editable: Boolean,
        },
        components: {
            EditorContent,
            EditorMenuBar
        },
        data() {
            return {
                textBoxEditor: null
            }
        },
        mounted() {
            this.textBoxEditor = new Editor({
                    content: this.createContent(),
                    editable: this.editable,
                    extensions: [
                        new Bold(),
                        new Italic(),
                        new Underline()
                    ],
                    onUpdate: ({getJSON}) => {
                        if(this.editable) {
                            // Any input from the textbox is returned as the underlying JSON tokens.
                            // This allows us to reuse tiptap to both edit and render rich text.
                            // This will also allow us to easily facilitate topic/post/comment editing
                            this.handleInput(getJSON())
                        }
                    }
                })
        },
        beforeDestroy() {
            this.textBoxEditor.destroy()
        },
        methods: {
            handleInput(val) {
                var valObjectAsString = JSON.stringify(val)
                if(val) {
                    this.$emit("input", valObjectAsString)
                }
            },
            createHTMLPlaceholder(placeholderText) {
                return `<p>${placeholderText}</p>`
            },
            createContent() {
                if(this.value) {
                    return JSON.parse(this.value); // return a JSON object as content
                } else if(this.placeholder){
                    return `<p>${this.placeholder}</p>`
                }
                return `<p>Type here!</p>`
            },
            toggleEditable() {
                this.editable = !this.editable
                this.textBoxEditor.options.editable = !this.textBoxEditor.options.editable
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