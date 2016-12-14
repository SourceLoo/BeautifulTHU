const introduction = {
    name: 'introduction',
    template: '#introduction',
};
const questions = {
    name: 'questions',
    template: '#questions',
};
const qa = {
    name: 'qa',
    template: '#qa',
};
const contact = {
    name: 'contact',
    template: '#contact',
};
const statistics = {
    name: 'statistics',
    template: '#statistics',
};
const error = {
    name: 'error',
    template: '#error',
};
var router = new VueRouter({
    routes: [{
        path: '/',
        component: introduction,
    }, {
        path: '/introduction',
        component: introduction,
    }, {
        path: '/questions',
        component: questions,
        beforeEnter: function(to, from, next) {
            //TODO: main or related.
            if (this.app.questions.length != 0) {
                this.app.set_current_view('questions');
                next();
            } else {
                this.app.$http.post('/questions/main/get_all/' + localStorage.token).then(function(res) {
                    //console.log('/questions/main/get_all', res);
                    if (res.data.success === false) {
                        alert(res.data.msg);
                        next(false);
                    } else {
                        this.questions = res.data;
                        this.set_current_view('questions');
                        next();
                    }
                })
            }
        }
    }, {
        path: '/qa',
        component: qa,
        beforeEnter: function(to, from, next) {
            if (this.app.questions.length != 0) {
                this.app.set_current_view('qa');
                next();
            } else {
                this.app.$http.post('/questions/main/get_all/' + localStorage.token).then(function(res) {
                    if (res.data.success === false) {
                        alert(res.data.msg);
                        next(false);
                    } else {
                        this.questions = res.data;
                        this.set_current_view('questions');
                        next();
                    }
                })
            }
        },
    }, {
        path: '/contact',
        component: contact,
        beforeEnter: function(to, from, next) {
            if (this.app.contact != []) {
                this.app.set_current_view('contact');
                next();
            } else {
                this.app.$http.post('/contact/get/' + localStorage.token).then(function(res) {
                    if (res.data.success === false) {
                        alert(res.data.msg);
                        next(false);
                    } else {
                        this.contact = res.data;
                        this.set_current_view('contact');
                        next();
                    }
                })
            }
        },
    }, {
        path: '/statistics',
        component: statistics,
        beforeEnter: function(to, from, next) {
            if (this.app.statistics != []) {
                this.app.set_current_view('statistics');
                next();
            } else {
                this.app.$http.post('/statistics/get/' + localStorage.token).then(function(res) {
                    if (res.data.success === false) {
                        alert(res.data.msg);
                        next(false);
                    } else {
                        this.statistics = res.data;
                        this.set_current_view('statistics');
                        next();
                    }
                })
            }
        },
    }, {
        path: '*',
        component: error
    }],
    mode: 'history',
});

var app = new Vue({
    el: '#app',
    //delimiters: ['${', '}'],
    created: function() {
        if (localStorage.getItem('token')) {
            this.is_login = true;
            this.uname = localStorage.getItem('uname');
        } else {
            localStorage.setItem('token', '')
            this.is_login = false;
        }
        this.$http.post('/init/get_displayname').then(function(res) {
            //console.log('get_displayname: ', res);
            this.display_name = res.data;
        });
    },
    router: router,
    data: {
        current_view: 'introduction',
        current_modify: -1,

        is_login: false,
        uname: '',
        passwd: '',

        display_name: [],
        contact: [],
        questions: [],
        statistics: [],
    },
    methods: {
        set_current_view: function(view) {
            this.current_view = view;
        },
        login: function() {
            //TODO: md5 passwd?
            this.$http.post('/auth/login', {
                uname: this.uname,
                passwd: this.passwd
            }).then(function(res) {
                if (res.data.success === false) {
                    alert(res.data.msg);
                } else {
                    localStorage.setItem('token', res.data);
                    localStorage.setItem('uname', this.uname);
                    this.is_login = true;
                    alert('登录成功');
                }
            });
        },
        logout: function() {
            this.$http.post('/auth/logout').then(function(res) {
                if (res.data.success === false) {
                    alert(res.data.msg);
                } else {
                    localStorage.removeItem('token');
                    localStorage.removeItem('user');
                    this.is_login = false;
                    this.uname = '';
                    this.passwd = '';
                    alert('注销成功');
                    router.push('/');
                }
            });
        },
    },
});
