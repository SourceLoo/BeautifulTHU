$(function(){
    var counter = 0;
    // 每页展示4个
    var num = 4;
    var page = 0;
    // dropload

    var k = $('.weui_panel').dropload({
        scrollArea : window,
        autoLoad : true,
        domDown : {//上拉
            domClass   : 'dropload-down',
            domRefresh : '<div class="weui-tabbar__label dropload-refresh f15 " style="text-align: center"><i class="icon icon-20"></i>上拉加载更多</div>',
            domLoad    : '<div class="weui-tabbar__label dropload-load f15" style="text-align: center"><span class="weui-loading"></span>正在加载中...</div>',
            domNoData  : '<div class="weui-tabbar__label dropload-noData" style="text-align: center">没有更多数据了</div>'
        },
        domUp : {//下拉
            domClass   : 'dropload-up',
            domRefresh : '<div class="weui-tabbar__label dropload-refresh" style="text-align: center"><i class="icon icon-114"></i>上拉加载更多</div>',
            domUpdate  : '<div class="weui-tabbar__label dropload-load f15" style="text-align: center"><i class="icon icon-20"></i>释放更新...</div>',
            domLoad    : '<div class="weui-tabbar__label dropload-load f15" style="text-align: center"><span class="weui-loading"></span>正在加载中...</div>'
        },
//        loadUpFn : function(me) {//刷新
//            counter = 0
//            pageStart = 0
//            pageEnd = 0
//            page = 0
//            type = $('#type').val()
//            var is_common = false;
//            if (type == 'qa') {
//                is_common = true
//            }
//            $.ajax({
//                type: 'GET',
//                url: '/student/home/question/all/',
//                dataType: 'json',
//                data: {
//                    page_size : num,
//                    page_num : page,
//                    state_condition : $('#status').val(),
//                    depart_condition : $('#dept').val(),
//                    order_type : $('#order').val(),
//                    keywords : $('#searchInput').val(),
//                    isCommon : is_common
//                },
//                success: function(data){
//                    var result = '';
//                    page += 1
//                    // alert("success1")
//                    for(var i = 0; i < data.question_list.length; i++){
//                        var q = data.question_list[i]
//                        var liked
//                        if (q.liked == "1") {
//                            liked = "已赞"
//                        } else {
//                            liked = "赞"
//                        }
//                        var dots = ""
//                        if (q.unread == 1) {
//                            dots = '<span class="weui-badge weui-badge_dot" style="position: absolute;top: 0;right: -6px;"></span>'
//                        }
//                        result += '<div id="question" class="weui-media-box weui-media-box_text" question_id="' + q.question_id + '">' +
//                                  '<h4 class="weui-media-box__title">' + q.question_title + '</h4>' +
//                                  '<p class="weui-media-box__desc">' + q.question_content + '</p>' +
//                                  '<ul class="weui-media-box__info">' +
//                                  '<li class="weui-media-box__info__meta">' + q.question_location + '</li>' +
//                                  '</ul>' +
//                                  '<ul class="weui-media-box__info">' +
//                                  '<li class="weui-media-box__info__meta weui-media-box__info__meta_extra"><a href="javascript:" onclick="clicklike(this)" id="like" question_id="'+q.question_id+'" state='+q.liked+'>'+liked+' </a>' +
//                                  '<li class="weui-media-box__info__meta weui-media-box__info__meta_extra" id="likenum">点赞数: ' + q.like_num + '</li>' +
//                                  '<li class="weui-media-box__info__meta weui-media-box__info__meta_extra"><a href="/student/home/question/?question_id='+q.question_id+'">'+dots+'详情</a></li>' +
//                                  '</ul>' +
//                                  '</div>'
//                    }
//                    // 为了测试，延迟1秒加载
//                    // alert("success")
//                    setTimeout(function(){
//                        $('.weui_panel_bd').html(result);
//                        // 每次数据加载完，必须重置
//                        me.resetload();
//                        // 重置索引值，重新拼接more.json数据
//                        counter = 0;
//                        // 解锁
//                        me.unlock();
//                        me.noData(false);
//                    },1000);
//                },
//                error: function(xhr, type){
//                    // alert('Ajax error!');
//                    // 即使加载出错，也得重置
//                    me.resetload();
//                }
//            });
//        },
        loadDownFn : function(me){//加载更多
            type = $('#type').val()
            var is_common = false;
            if (type == 'qa') {
                is_common = true
            }
            $.ajax({
                type: 'GET',
                url: '/student/home/question/all/',
                dataType: 'json',
                data: {
                    page_size : num,
                    page_num : page,
                    state_condition : $('#status').val(),
                    depart_condition : $('#dept').val(),
                    order_type : $('#order').val(),
                    keywords : $('#searchInput').val(),
                    isCommon : is_common
                },
                success: function(data) {
                    counter++;
                    page++;
                    var result = ''
                    for(var i = 0; i < data.question_list.length; i++) {
                        q = data.question_list[i]
                        var liked
                        if (q.liked == "1") {
                            liked = "<i class='fa fa-thumbs-up' style='color:#990099'></i>"
                        } else {
                            liked = "<i class='fa fa-thumbs-up' style='color:#FFB3FF'></i>"
                        }
                        var dots = ""
                        if (q.unread == 1) {
                            dots = '<span class="weui-badge weui-badge_dot" style="position: absolute;top: 25px;right: 20px;"></span>'
                        }

                        result += '<div id="question" class="weui-media-box weui-media-box_text" question_id="' + q.question_id + '">' +
                        '<h1 class="title weui-media-box__title ">' + q.question_title + '</h4>' +
                        '<p class="content weui-media-box__desc ">' + q.question_content + '</p>' +
                        '<ul class="others weui-media-box__info ">' +
                        '<li class="weui-media-box__info__meta"><a href="javascript:" onclick="clicklike(this)" id="like" question_id="'+q.question_id+'" state='+q.liked+'>'+liked+' </a></li>' +
                        '<li class="weui-media-box__info__meta" id="likenum">'+q.like_num+'</li>' +
                        '<li class="weui-media-box__info__meta weui-media-box__info__meta_extra"><a href="/student/home/question/?question_id='+q.question_id+'" style="color: #990099">'+dots+'详情</a></li>' +
                        '<li class="weui-media-box__info__meta weui-media-box__info__meta_extra">' + q.question_location + '</li>' +
                        '</ul>' +
                        '</div>'
                    }
                    // alert(result)
                    if(data.question_list.length == 0) {
                            // 锁定
                            me.lock();
                            // 无数据
                            me.noData();
                    }
                    // 为了测试，延迟1秒加载
                    $('.weui_panel_bd').append(result);
                    // 每次数据加载完，必须重置
                    me.resetload();
                },
                error: function(xhr, type){
                    alert('Ajax error!');
                    // 即使加载出错，也得重置
                    me.resetload();
                }
            });
        }
    });

//    $('#searchInput').on('change', function(){
//        page = 0
//        k.opts.loadUpFn(k)
//    });

//    $('select').on('change', function(){
//        page = 0
//        k.opts.loadUpFn(k)
//    });
});
