import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    page: "landing",
    user: {},
    drop: {},
    userSubscriptions: [],
  },
  mutations: {
    setPage(state, page) {
      state.page = page;
    },
    setUser(state, user) {
      state.user = user;
    },
    setDrop(state, drop) {
      state.drop = drop;
    },
    iteratePostCount(state) {
      state.user.post_count++;
    },
    setUserSubscriptions(state, dataArray) {
      state.userSubscriptions = dataArray;
    },
    appendUserSubscription(state, drop) {
      state.userSubscriptions.push({
        drop_id: drop.drop_id,
        drop_name: drop.title
      })
    },
    removeUserSubscription(state, drop) {
      var idx
      idx = state.userSubscriptions.findIndex(val => val.drop_id == drop.drop_id);
      state.userSubscriptions.splice(idx, 1)
    }
  },
  actions: {},
  modules: {}
});
