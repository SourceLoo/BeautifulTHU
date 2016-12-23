const common_props = ['curr', 'data', 'is_tuanwei', 'is_xiaoban', 'is_zongban', 'is_main'];
const display_status = ['待审核', '待分类', '待解决', '解决中', '申请重分类', '申请延期', '已解决', '无效']
var handle_res = function(data) {
    return data;
    //return JSON.parse(data);
}
var handle_req = function(data) {
    return data;
    //return JSON.stringify(data);
}
const introduction = {
    name: 'introduction',
    template: '#introduction',
};
const info = {
    name: 'info',
    template: '#info',
    props: ['update_info'].concat(common_props),
    methods: {
        info_modify: function() {
            this.curr.info_modify = !this.curr.info_modify;
            this.curr.info_backup = JSON.parse(JSON.stringify(this.data.info));
        },
        info_discard: function() {
            this.data.info = this.curr.info_backup;
            this.curr.info_modify = !this.curr.info_modify;
        },
        info_save: function() {
            this.$http.post('/info/set/' + localStorage.token, this.data.contact[index].uname).then(res => {});
            this.curr.info_modify = !this.curr.info_modify;
        },
    }
};

const questions = {
    name: 'questions',
    template: '#questions',
    props: ['update_questions'].concat(common_props),
    data: function() {
        return {
            options: this.data.display_name[1],
            selected_leader: '',
            selected_ones: [],
            selected_ddl: '',
            selected_opinion: '',

            response_modify: -1,
            response_text: '',

            reclassify_reason: '',
            delay_days: 0,
            delay_reason: '',

            filter_common: false,
            filter_statistics: '',
        };
    },
    //components: { Multiselect },
    methods: {
        question_modify: function(index) {
            if (this.curr.question_modify != -1) {
                this.data.questions[this.curr.question_modify] = this.curr.question_backup;
            }
            this.curr.question_modify = index;
            this.curr.question_backup = JSON.parse(JSON.stringify(this.data.questions[index]));
        },
        question_discard: function(index) {
            this.data.questions[index] = this.curr.question_backup;
            this.curr.question_modify = -1;
        },
        question_status: function(status) {
            return display_status[status];
        },
        question_for_main: function(status) {
            return (status==0 && this.is_tuanwei) || (status==1 && (this.is_xiaoban || this.is_zongban));
        },
        question_for_related: function(status) {
            return ([2,3,4,5,6].indexOf(status) != -1);
        },
        question_is_show: function(status, is_common, role) {
            return (this.curr.question_filter == -1 || this.curr.question_filter == status) && (!this.filter_common || is_common) && (this.filter_statistics == '' || role.indexOf(this.filter_statistics) != -1);
        },
        question_filter: function(status) {
            if (this.curr.question_filter == status) {
                this.curr.question_filter = -1;
            } else {
                this.curr.question_filter = status;
            }
        },
        question_filter_used: function(status) {
            return this.curr.question_filter == status ? 'active' : '';
        },
        _update_questions: res => {
            res.data = handle_res(res.data);
            if (res.data.success === false) {
                alert(handle_res(res.data).msg);
                return false;
            } else {
                this.$router.app.update_questions().then(function(result) {
                    if (resullt) {
                        this.curr.question_modify = -1;
                    }
                });
            }
        },
        question_statistics: function(role) {
            if (this.filter_statistics != role) {
                this.filter_statistics = role;
            } else {
                this.filter_statistics = '';

            }
        },
        question_common: function(id, is_common) {
            var temp = {
                question_id: id,
            };
            var path = '';
            if (is_common) {
                path = '/qa/add/';
            } else {
                path = '/qa/del/';
            }
            this.$http.post('/qa/add/' + localStorage.token, handle_req(temp)).then(res => {
                res.data = handle_res(res.data);
                if (res.data.success === false) {
                    alert(res.data.msg);
                    return false;
                } else {
                    this.$router.app.update_questions();
                }
            });
        },
        question_common_top: function(id, is_common_top) {
            var temp = {
                question_id: id,
            };
            var path = '';
            if (is_common_top) {
                path = '/qa/top/';
            } else {
                path = '/qa/notop/';
            }
            this.$http.post(path + localStorage.token, handle_req(temp)).then(res => {
                res.data = handle_res(res.data);
                if (res.data.success === false) {
                    alert(res.data.msg);
                    return false;
                } else {
                    this.$router.app.update_questions();
                }
            });
        },
        question_reclassify: function(id, agree) {
            var temp = {
                question_id: id,
                agree: agree,
            };
            this.$http.post('/questions/main/reclassify/' + localStorage.token, handle_req(temp)).then(this._update_questions);
        },
        question_delay: function(id, agree) {
            var temp = {
                question_id: id,
                agree: agree,
            };
            this.$http.post('/questions/main/delay/' + localStorage.token, handle_req(temp)).then(this._update_questions);
        },
        question_classify: function(id) {
            var temp = {
                question_id: id,
                leader_role: this.selected_leader,
                other_roles: this.selected_ones,
                deadline: this.selected_ddl,
                opinion: this.selected_opinion,
            };
            this.$http.post('/questions/main/classify/' + localStorage.token, handle_req(temp)).then(this._update_questions);
        },
        question_forward: function(id, role) {
            var temp = {
                question_id: id,
                role: role,
            };
            this.$http.post('/questions/main/forward/' + localStorage.token, handle_req(temp)).then(this._update_questions);
        },
        question_reject: function(id) {
            var temp = {
                question_id: id,
            };
            this.$http.post('/questions/main/reject/' + localStorage.token, handle_req(temp)).then(this._update_questions);
        },
        question_related_modify: function(id, response) {
            if (this.response_modify == -1) {
                this.response_modify = id;
            } else {
                var temp = {
                    question_id: id,
                    response_id: response.id,
                    response_content: response.content,
                };
                this.$http.post('/questions/related/modify_response/' + localStorage.token, handle_req(temp)).then(res => {
                    res.data = handle_res(res.data);
                    if (res.data.success === false) {
                        alert(res.data.msg);
                        return false;
                    } else {
                        this.$router.app.update_questions().then(function(result) {
                            if (resullt) {
                                this.curr.response_modify = -1;
                            }
                        });
                    }
                });
            }
        },
        question_related_response: function(id) {
            var temp = {
                question_id: id,
                response_content: this.response_text
            };
            this.$http.post('/questions/related/response/' + localStorage.token, handle_req(temp)).then(this._update_questions);
        },
        question_related_reclassify: function(id) {
            var temp = {
                question_id: id,
                reclassify_reason: this.reclassify_reason,
            };
            this.$http.post('/questions/related/reclassify/' + localStorage.token, handle_req(temp)).then(this._update_questions);
        },
        question_related_delay: function(id) {
            var temp = {
                question_id: id,
                delay_reason: this.delay_reason,
                delay_days: this.delay_days,
            };
            this.$http.post('/questions/related/delay/' + localStorage.token, handle_req(temp)).then(this._update_questions);
        },
    }
};
const contact = {
    name: 'contact',
    template: '#contact',
    props: [].concat(common_props),
    methods: {
        contact_modify: function(index) {
            this.curr.contact_modify = index;
            this.curr.contact_backup = JSON.parse(JSON.stringify(this.data.contact[index]));
        },
        contact_new: function() {
            //TODO: validation
            this.curr.contact_modify = this.data.contact.push({
                is_new: true
            }) - 1;
            this.curr.contact_backup = {
                is_new: true
            };
        },
        contact_delete: function(index) {
            this.data.contact.splice(index, 1);
            this.$http.post('/contact/del/' + localStorage.token, this.data.contact[index].uname).then(res => {});
        },
        contact_discard: function(index) {
            this.data.contact[index] = this.curr.contact_backup;
            this.curr.contact_modify = -1;

        },
        contact_save: function(index) {
            this.$http.post('/contact/set/' + localStorage.token, this.data.contact[index]).then(res => {});
            this.curr.contact_modify = -1;
        },
    }
};
const error = {
    name: 'error',
    template: '#error',
};
Vue.prototype.$http = axios;
var router = new VueRouter({
    routes: [{
        path: '/',
        component: introduction,
        beforeEnter: function(to, from, next) {
            if (this.app) {
                this.app.set_curr_view('introduction');
            }
            next();
        }
    }, {
        path: '/introduction',
        component: introduction,
        beforeEnter: function(to, from, next) {
            this.app.set_curr_view('introduction');
            next();
        }
    }, {
        path: '/info',
        component: info,
        beforeEnter: function(to, from, next) {
            this.app.update_info().then(function(result) {
                next(result);
            });
        }
    }, {
        path: '/questions',
        component: questions,
        beforeEnter: function(to, from, next) {
            this.app.update_questions().then(function(result) {
            });
            if (this.app.is_xiaoban || this.app.is_zongban) {
                this.app.$http.post('/statistics/get/' + localStorage.token).then(res => {
                    res.data = handle_res(res.data);
                    if (res.data.success === false) {
                        alert(res.data.msg);
                    } else {
                        this.data.statistics = res.data;
                    }
                })
            }
            next();
        },
    }, {
        path: '/contact',
        component: contact,
        beforeEnter: function(to, from, next) {
            this.app.update_contact().then(function(result) {
                next(result);
            });
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
        if (localStorage.token != undefined && localStorage.token != '') {
            this.curr.is_login = true;
            this.curr.role = localStorage.getItem('role');
        } else {
            localStorage.setItem('token', '')
            localStorage.setItem('role', '')
            this.curr.role = '';
            this.curr.is_login = false;
        }
        this.$http.post('/init/get_displayname').then(res => {
            //console.log('get_displayname: ', res);
            this.data.display_name = handle_res(res.data); //JSON.parse(res.data);
        });
    },
    ready: function() {
        window.setInterval(function() {
            this.curr.time = Math.trunc((new Date()).getTime() / 1000);
        }, 1000);
    },
    router: router,
    component: {
        introduction: introduction,
        info: info,
        questions: questions,
        contact: contact,
    },
    data: {
        curr: {
            view: 'introduction',
            time: Math.trunc((new Date()).getTime() / 1000),
            contact_modify: -1,
            info_modify: false,
            question_modify: -1,
            question_filter: -1,
            is_login: false,
            role: '',
            uname: '',
            passwd: '',
        },

        data: {
            display_name: [],
            info: [],
            contact: [],
            questions: [],
            statistics: [],
        },
    },
    computed: {
        is_tuanwei: function() {
            return this.curr.role == 'tuanwei';
        },
        is_xiaoban: function() {
            return this.curr.role == 'xiaoban';
        },
        is_zongban: function() {
            return this.curr.role == 'zongban';
        },
        is_main: function() {
            return this.is_tuanwei || this.is_xiaoban || this.is_zongban;
        },
    },
    methods: {
        set_curr_view: function(view) {
            this.curr.view = view;
        },
        login: function() {
            //TODO: md5 passwd?
            var temp = {
                uname: this.curr.uname,
                passwd: this.curr.passwd
            };
            this.$http.post('/auth/login', handle_req(temp)).then(res => {
                res.data = handle_res(res.data);
                if (res.data.success === false) {
                    alert(res.data.msg);
                } else {
                    localStorage.setItem('token', res.data.token);
                    localStorage.setItem('role', res.data.role);
                    this.curr.role = res.data.role;
                    this.curr.is_login = true;
                    alert('登录成功');
                }
                this.curr.uname = '';
                this.curr.passwd = '';
            });
        },
        logout: function() {
            this.$http.post('/auth/logout/' + localStorage.token).then(res => {
                res.data = handle_res(res.data);
                if (res.data.success === false) {
                    alert(res.data.msg);
                } else {
                    localStorage.removeItem('token');
                    localStorage.removeItem('role');
                    this.curr.role = '';
                    this.curr.is_login = false;
                    alert('注销成功');
                    router.push('/');
                }
            });
        },
        update_info: function() {
            return this.$http.post('/info/get/' + localStorage.token).then(res => {
                res.data = handle_res(res.data);
                if (res.data.success === false) {
                    alert(res.data.msg);
                    return false;
                } else {
                    this.data.info = res.data;
                    this.set_curr_view('info');
                    return true;
                }
            })
        },
        update_contact: function() {
            return this.$http.post('/contact/get/').then(res => {
                res.data = handle_res(res.data);
                if (res.data.success === false) {
                    alert(res.data.msg);
                    return false;
                } else {
                    this.data.contact = res.data;
                    this.set_curr_view('contact');
                    return true;
                }
            })
        },
        update_questions: function() {
            return this.$http.post('/questions/get_all/' + localStorage.token).then(res => {
                res.data = handle_res(res.data);
                if (res.data.success === false) {
                    alert(res.data.msg);
                    return false;
                } else {
                    var _role = this.curr.role;
                    //TODO: remove after combined.
                    this.data.questions = res.data.filter(function(question) {
                        return _role == 'tuanwei' || _role == 'xiaoban' || question.resp_role.indexOf(_role) != -1;
                    });
                    this.set_curr_view('questions');
                    return true;
                }
            })
        }
    },
});
