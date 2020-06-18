<template>
    <div class="user-card">
        <span>I AM THE FUTURE</span>
        <ul class="subscription-list">
            <li v-for="drop in subscribedDrops" v-bind:key="drop.dropName">
                <u-flat-button @click.native="handleOpenDrop(drop.dropid)" >{{drop.dropName}}</u-flat-button>
            </li>
        </ul>
    </div>
</template>

<script>
import store from "../store"
export default {
    name: "FavoriteDropsSidebarComponent",
    data() {
        return {
            subscriptions: {}
        }
    },
    methods: {
        loadUserSubscriptions() {
            this.axios.get('/user/subscribed').then(response => {
                store.commit("setUserSubscriptions", response.data)
            })
        },
        handleOpenDrop(dropId) {
            this.axios.get(`/drops/${dropId}`).then(response => {
                this.openDrop(response.data)
            })
        },
        openDrop(dropObject) {
            store.commit("setDrop", dropObject)
            store.commit("setPage", "drop")
        }
    },
    mounted: function() {
            this.loadUserSubscriptions()
    },
    computed: {
        subscribedDrops() {
            return store.state.userSubscriptions;
        }
    }
}
</script>

<style scoped>
    .subscription-list {

    }
</style>