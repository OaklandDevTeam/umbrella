<template>
    <editor-content :editor="textBoxEditor"/>
</template>

<script>
    import {
        Editor,
        EditorContent
    } from 'tiptap'

    export default {
        name: "TextBox",
        components: {
            EditorContent
        },
        data() {
            return {
                textBoxEditor: new Editor({
                    content: '<p>Content here!</p>'
                })
            }
        },
        beforeDestroy() {
            this.textBoxEditor.destroy()
        }
    }
</script>

<style scoped>

</style>