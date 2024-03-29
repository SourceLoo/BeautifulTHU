const common_props = ['curr', 'data', 'is_xiaoban', 'is_zongban', 'is_main'];
const display_status = ['待审核', '待分类', '待解决', '解决中', '申请重分类', '申请延期', '已解决', '无效']
var handle_res = function(data) {
    //return data;
    return JSON.parse(data);
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
            this.$http.post('/info/set/' + localStorage.token, handle_req(this.data.info)).then(res => {
                if (res.success === false) {
                    alert(res.msg);
                    return false;
                } else {
                    this.curr.info_modify = !this.curr.info_modify;
                }
            });
        },
    }
};

const questions = {
    name: 'questions',
    template: '#questions',
    props: [].concat(common_props),
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
            return (status == 0 && this.is_xiaoban) || (status == 1 && this.is_zongban);
        },
        question_for_related: function(status) {
            return ([2, 3, 4, 5, 6].indexOf(status) != -1);
        },
        question_is_show: function(status, is_common, role) {
            return (this.curr.question_filter == -1 || this.curr.question_filter == status) && (!this.filter_common || is_common) && (this.filter_statistics == '' || role.indexOf(this.filter_statistics) != -1);
        },
        question_is_urgent: function(question) {
            return ([3,6,7].indexOf(question.status) == -1) && (question.resp_role.indexOf(this.curr.role) != -1)
        },
        _seconds: function(date) {
        return ((date - this.curr.time) % 60).toString() + '秒';
        },
        _minutes: function(date) {
        return (Math.trunc((date - this.curr.time) / 60) % 60).toString() + '分';
        },
        _hours: function(date) {
        return (Math.trunc((date - this.curr.time) / 60 / 60) % 24).toString() + '小时';
        },
        _days: function(date) {
        return (Math.trunc((date - this.curr.time) / 60 / 60 / 24)).toString() + '天';
        },
        question_count_down: function(question) {
            var ddl;
            if ([0].indexOf(question.status) != -1) {
                ddl = new Date(question.created_time);
                ddl.setHours(ddl.getHours() - 8 + 2);
            }
            if ([1,4,5].indexOf(question.status) != -1) {
                ddl = new Date(question.timestamp1);
                ddl.setHours(ddl.getHours() - 8 + 2);
            }
            if (question.status == 2) {
                ddl = new Date(question.deadline);
                ddl.setHours(ddl.getHours() - 8);
            }
            if (ddl < new Date())
                return "已过期";
            ddl = Math.trunc(ddl.getTime() / 1000);
            //console.log(ddl);
            //console.log(this.curr.time);
            return this._days(ddl) + this._hours(ddl) + this._minutes(ddl) + this._seconds(ddl);
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
        _update_questions: function(res) {
            res = handle_res(res);
            if (res.success === false) {
                alert(handle_res(res).msg);
                return false;
            } else {
                this.response_modify = -1;
                this.response_text = '';

                this.selected_leader = '';
                this.selected_ones = [];
                this.selected_ddl = '';
                this.selected_opinion = '';

                this.reclassify_reason = '';
                this.delay_days = 0;
                this.delay_reason = '';
                this.$router.app.update_questions();
                alert("操作成功");
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
            if (!is_common) {
                path = '/qa/add/';
            } else {
                path = '/qa/del/';
            }
            this.$http.post(path + localStorage.token, handle_req(temp)).then(this._update_questions);
        },
        question_common_top: function(id, is_common_top) {
            var temp = {
                question_id: id,
            };
            var path = '';
            if (!is_common_top) {
                path = '/qa/top/';
            } else {
                path = '/qa/notop/';
            }
            this.$http.post(path + localStorage.token, handle_req(temp)).then(this._update_questions);
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
        question_response: function(id) {
            var temp = {
                question_id: id,
                response_content: this.response_text
            };
            this.$http.post('/questions/main/response/' + localStorage.token, handle_req(temp)).then(this._update_questions);
        },
        question_classify: function(id) {
            var index = this.selected_ones.indexOf(this.selected_leader);
            if (index != -1)
                this.selected_ones.splice(index, 1);
            var temp = {
                question_id: id,
                leader_role: this.selected_leader,
                other_roles: this.selected_ones.join(','),
                deadline: this.selected_ddl,
                opinion: this.selected_opinion,
            };
            console.log(temp);
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
            var temp = {
                question_id: id,
                response_id: response.response_id,
                response_content: response.response_content,
            };
            this.$http.post('/questions/related/modify_response/' + localStorage.token, handle_req(temp)).then(this._update_questions);
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
        },
        contact_delete: function(index) {
            var temp = {uname: this.data.contact[index].uname};
            this.$http.post('/contact/del/' + localStorage.token, handle_req(temp)).then(res => {
                if (res.success === false) {
                    alert(res.msg);
                    return false;
                } else {
                    this.data.contact.splice(index, 1);
                }
            });
        },
        contact_discard: function(index) {
            if (this.data.contact[index].is_new) {
                this.data.contact.splice(index, 1);
            } else {
                this.data.contact[index] = this.curr.contact_backup;
            }
            this.curr.contact_modify = -1;
        },
        contact_save: function(index) {
            this.data.contact[index].is_new = false;
            this.$http.post('/contact/set/' + localStorage.token, handle_req(this.data.contact[index])).then(res => {
                if (res.success === false) {
                    alert(res.msg);
                    return false;
                } else {
                    this.curr.contact_modify = -1;
                }
            });
        },
    }
};
const error = {
    name: 'error',
    template: '#error',
};
window.setInterval(function() {
    app.curr.time = Math.trunc((new Date()).getTime() / 1000);
}, 1000);
//Vue.prototype.$http = axios;
Vue.prototype.$http = $;
var router = new VueRouter({
    routes: [{
        path: '/school/manage_console',
        component: introduction,
        beforeEnter: function(to, from, next) {
            if (this.app) {
                this.app.set_curr_view('introduction');
            }
            next();
        }
    }, {
        path: '/school/manage_console/introduction',
        component: introduction,
        beforeEnter: function(to, from, next) {
            this.app.set_curr_view('introduction');
            next();
        }
    }, {
        path: '/school/manage_console/info',
        component: info,
        beforeEnter: function(to, from, next) {
            this.app.update_info().then(function(result) {
                next(result);
            });
        }
    }, {
        path: '/school/manage_console/questions',
        component: questions,
        beforeEnter: function(to, from, next) {
            this.app.update_questions().then(function(result) {});
            if (this.app.is_xiaoban || this.app.is_zongban) {
                this.app.$http.post('/statistics/get/' + localStorage.token).then(res => {
                    res = handle_res(res);
                    if (res.success === false) {
                        alert(res.msg);
                    } else {
                        this.app.data.statistics = res;
                    }
                })
            }
            next();
        },
    }, {
        path: '/school/manage_console/contact',
        component: contact,
        beforeEnter: function(to, from, next) {
            this.app.update_contact().then(function(result) {
                next(result);
            });
        },
    }, {
        path: '/school/manage_console/*',
        component: error
    }],
    mode: 'history',
});

var app = new Vue({
    el: '#app',
    //delimiters: ['${', '}'],
    created: function() {
        if (localStorage.token != undefined && localStorage.token != '') {
            this.$http.post('/auth/login/' + localStorage.token).then(res => {
                res = handle_res(res);
                if (res.success === false) {
                    //alert(res.msg);
                    localStorage.setItem('token', '')
                    localStorage.setItem('role', '')
                    this.curr.role = '';
                    this.curr.is_login = false;
                } else {
                    this.curr.is_login = true;
                    this.curr.role = localStorage.getItem('role');
                }
            });
        } else {
            localStorage.setItem('token', '')
            localStorage.setItem('role', '')
            this.curr.role = '';
            this.curr.is_login = false;
        }
        this.$http.post('/init/get_displayname').then(res => {
            console.log('get_displayname: ', res);
            this.data.display_name = handle_res(res);
        });
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
        is_xiaoban: function() {
            return this.curr.role == 'xiaoban';
        },
        is_zongban: function() {
            return this.curr.role == 'zongban';
        },
        is_main: function() {
            return this.is_xiaoban || this.is_zongban;
        },
    },
    methods: {
        set_curr_view: function(view) {
            this.curr.view = view;
        },
        login: function() {
            var temp = {
                uname: this.curr.uname,
                passwd: this.curr.passwd
            };
            this.$http.post('/auth/login', handle_req(temp)).then(res => {
                console.log('login', res);
                res = handle_res(res);
                if (res.success === false) {
                    alert(res.msg);
                } else {
                    localStorage.setItem('token', res.token);
                    localStorage.setItem('role', res.role);
                    this.curr.role = res.role;
                    this.curr.is_login = true;
                    alert('登录成功');
                }
                this.curr.uname = '';
                this.curr.passwd = '';
            });
        },
        logout: function() {
            this.$http.post('/auth/logout/' + localStorage.token).then(res => {
                res = handle_res(res);
                if (res.success === false) {
                    alert(res.msg);
                } else {
                    localStorage.setItem('token', '')
                    localStorage.setItem('role', '')
                    this.curr.role = '';
                    this.curr.is_login = false;
                    alert('注销成功');
                    router.push('/school/manage_console');
                }
            });
        },
        update_info: function() {
            return this.$http.post('/info/get/' + localStorage.token).then(res => {
                res = handle_res(res);
                if (res.success === false) {
                    alert(res.msg);
                    return false;
                } else {
                    this.data.info = res;
                    this.set_curr_view('info');
                    return true;
                }
            })
        },
        update_contact: function() {
            return this.$http.post('/contact/get/' + localStorage.token).then(res => {
                res = handle_res(res);
                if (res.success === false) {
                    alert(res.msg);
                    return false;
                } else {
                    this.data.contact = res;
                    this.set_curr_view('contact');
                    return true;
                }
            })
        },
        update_questions: function() {
            // TODO: split pages
            var temp = {
                start: 0,
                number: 100,
            }
            return this.$http.post('/questions/get_all/' + localStorage.token, handle_req(temp)).then(res => {
                res = handle_res(res);
                if (res.success === false) {
                    alert(res.msg);
                    return false;
                } else {
                    var _role = this.curr.role;
                    //TODO: remove after combined.
                    this.data.questions = res.filter(function(question) {
                        return _role == 'xiaoban' || question.resp_role.indexOf(_role) != -1;
                    });
                    this.set_curr_view('questions');
                    this.curr.question_modify = -1;
                    return true;
                }
            })
        }
    },
});
