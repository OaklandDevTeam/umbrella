import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    page: "landing",
    user: {},
    drop: {}
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
    }
  },
  actions: {},
  modules: {},
});
