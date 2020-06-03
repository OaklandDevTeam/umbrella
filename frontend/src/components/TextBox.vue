<!--Lots of help from here https://tiptap.scrumpy.io/docs/-->
<template>
    <div class="text-box">
        <editor-menu-bar :editor="textBoxEditor" v-slot="{commands, isActive}">
            <div class="text-box-menubar">
                <button :class="{'is-active': isActive.bold() }" @click="commands.bold">B</button>
                <button :class="{'is-active': isActive.italic() }" @click="commands.italic">I</button>
                <button :class="{'is-active': isActive.underline() }" @click="commands.underline">U</button>
            </div>
        </editor-menu-bar>
        <editor-content class="editorBox" :editor="textBoxEditor"/>
    </div>
</template>

<script>
    import {
        Editor,
        EditorContent,
        EditorMenuBar
    } from 'tiptap'
    import {
        Bold, Italic, Underline, Image, Link, Blockquote, Code, CodeBlock
    } from 'tiptap-extensions'

    export default {
        name: "TextBox",
        components: {
            EditorContent,
            EditorMenuBar
        },
        data() {
            return {
                textBoxEditor: new Editor({
                    content: '<p>Content here!</p>',
                    extensions: [
                        new Bold(),
                        new Italic(),
                        new Underline,
                        new Image(),
                        new Link(),
                        new Blockquote(),
                        new Code(),
                        new CodeBlock()
                    ]
                }),
            }
        },
        beforeDestroy() {
            this.textBoxEditor.destroy()
        },
        computed: {
            textContent: function () {
                return this.textBoxEditor.content
            }
        }
    }
</script>

<style scoped>
    .text-box {
        border: solid #a0a0a0 2px;
    }
    .editorBox {
        height: 15vh;
    }
</style>