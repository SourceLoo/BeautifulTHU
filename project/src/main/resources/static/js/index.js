const introduction = {
    template: '#introduction',
};
const questions = {
    template: '#questions',
};
const qa = {
    template: '#qa',
};
const contact = {
    template: '#contact',
};
const statistics = {
    template: '#statistics',
};
const error = {
    template: '#error',
};
var router = new VueRouter({
    routes:
    [
      { path: '/', component: introduction },
      { path: '/introduction', component: introduction },
      { path: '/questions', component: questions },
      { path: '/qa', component: qa },
      { path: '/contact', component: contact },
      { path: '/statistics', component: statistics },
      { path: '*', component: error },
    ],
    mode: 'history',
});

var app = new Vue({
    el: '#app',
    //delimiters: ['${', '}'],
    router: router,
    data: {
        current_view: 'introduction',
        is_login: false,
    },
    components: {
    },
    methods: {
        set_current_view: function(view) {
            this.current_view = view;
        }
    },
});
