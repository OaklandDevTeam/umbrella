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
    appendUserSubscription(state, dropId, dropname) {
      state.userSubscriptions.push({
        dropid: dropId,
        dropName: dropname
      })
    }
  },
  actions: {},
  modules: {},
});
