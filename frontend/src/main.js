import Vue from "vue";
import App from "./App.vue";
import store from "./store";
import axios from 'axios';
import VueAxios from 'vue-axios';
import Logo from "./components/Logo.vue";
import Button from "./components/Button.vue";
import FlatButton from "./components/FlatButton.vue";
import TextField from "./components/TextField.vue";
import Card from "./components/Card.vue";
import CheckBox from "./components/CheckBox.vue";
import Label from "./components/Label.vue";
import VideoBG from "vue-videobg";
import LandingPage from "./components/LandingPage.vue";
import FrontPage from "./components/FrontPage.vue";
import Sidebar from "./components/Sidebar.vue";
import UserCard from "./components/UserCard.vue";
import DropCard from "./components/DropCard.vue";
import TextBox from "./components/TextBox";
import DebugPage from "./components/DebugPage";
import DropPage from "./components/DropPage.vue";
import PostCard from "./components/PostCard.vue";
import Modal from "./components/Modal.vue";

Vue.component('u-logo', Logo);
Vue.component('u-button', Button);
Vue.component('u-flat-button', FlatButton);
Vue.component('u-text-field', TextField);
Vue.component('u-card', Card);
Vue.component('u-check-box', CheckBox);
Vue.component('u-label', Label);
Vue.component('u-video-bg', VideoBG);
Vue.component('u-sidebar', Sidebar);
Vue.component('u-user-card', UserCard);
Vue.component('u-drop-card', DropCard);
Vue.component('u-text-box', TextBox)
Vue.component('landing-page', LandingPage);
Vue.component('front-page', FrontPage);
Vue.component('debug-page', DebugPage)
Vue.component('u-post-card', PostCard);
Vue.component('u-modal', Modal);

Vue.component('landing-page', LandingPage);
Vue.component('front-page', FrontPage);
Vue.component('drop-page', DropPage);

Vue.use(VueAxios, axios);

Vue.config.productionTip = false;

new Vue({
  store,
  render: h => h(App)
}).$mount("#app");