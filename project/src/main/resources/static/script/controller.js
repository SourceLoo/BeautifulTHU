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
            domRefresh : '<div class="dropload-refresh f15 "><i class="icon icon-20"></i>上拉加载更多</div>',
            domLoad    : '<div class="dropload-load f15"><span class="weui-loading"></span>正在加载中...</div>',
            domNoData  : '<div class="dropload-noData">没有更多数据了</div>'
        },
        domUp : {//下拉
            domClass   : 'dropload-up',
            domRefresh : '<div class="dropload-refresh"><i class="icon icon-114"></i>上拉加载更多</div>',
            domUpdate  : '<div class="dropload-load f15"><i class="icon icon-20"></i>释放更新...</div>',
            domLoad    : '<div class="dropload-load f15"><span class="weui-loading"></span>正在加载中...</div>'
        },
        loadUpFn : function(me) {//刷新
            counter = 0
            pageStart = 0
            pageEnd = 0
            page = 0
            type = $('#type').val()
            var is_common = false;
            if (type == 'qa') {
                is_common = true
            }
            $.ajax({
                type: 'GET',
                url: '/student/question/all/',
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
                success: function(data){
                    var result = '';
                    page += 1
                    // alert("success1")
                    for(var i = 0; i < data.question_list.length; i++){
                        var q = data.question_list[i]
                        var liked
                        if (q.liked == "1") {
                            liked = "已赞"
                        } else {
                            liked = "赞"
                        }
                        result += '<div id="question" class="weui-media-box weui-media-box_text" question_id="' + q.question_id + '">' +
                                  '<h4 class="weui-media-box__title">' + q.question_title + '</h4>' +
                                  '<p class="weui-media-box__desc">' + q.question_content + '</p>' +
                                  '<ul class="weui-media-box__info">' +
                                  '<li class="weui-media-box__info__meta">' + q.question_location + '</li>' +
                                  '</ul>' +
                                  '<ul class="weui-media-box__info">' +
                                  '<li class="weui-media-box__info__meta weui-media-box__info__meta_extra"><a href="javascript:" onclick="clicklike(this)" id="like" question_id="'+q.question_id+'" state='+q.liked+'>'+liked+' </a>' +
                                  '<li class="weui-media-box__info__meta weui-media-box__info__meta_extra" id="likenum">点赞数: ' + q.like_num + '</li>' +
                                  '<li class="weui-media-box__info__meta weui-media-box__info__meta_extra"><a href="/student/question/?question_id='+q.question_id+'">点此查看详情</a></li>' +
                                  '</ul>' +
                                  '</div>'
                    }
                    // 为了测试，延迟1秒加载
                    // alert("success")
                    setTimeout(function(){
                        $('.weui_panel_bd').html(result);
                        // 每次数据加载完，必须重置
                        me.resetload();
                        // 重置索引值，重新拼接more.json数据
                        counter = 0;
                        // 解锁
                        me.unlock();
                        me.noData(false);
                    },1000);
                },
                error: function(xhr, type){
                    // alert('Ajax error!');
                    // 即使加载出错，也得重置
                    me.resetload();
                }
            });
        },
        loadDownFn : function(me){//加载更多
            type = $('#type').val()
            var is_common = false;
            if (type == 'qa') {
                is_common = true
            }
            $.ajax({
                type: 'GET',
                url: '/student/question/all/',
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
                            liked = "已赞"
                        } else {
                            liked = "赞"
                        }
                        result += '<div id="question" class="weui-media-box weui-media-box_text" question_id="' + q.question_id + '">' +
                                '<h4 class="weui-media-box__title">' + q.question_title + '</h4>' +
                                '<p class="weui-media-box__desc">' + q.question_content + '</p>' +
                                '<ul class="weui-media-box__info">' +
                                '<li class="weui-media-box__info__meta">' + q.question_location + '</li>' +
                                '</ul>' +
                                '<ul class="weui-media-box__info">' +
                                '<li class="weui-media-box__info__meta weui-media-box__info__meta_extra"><a href="javascript:" onclick="clicklike(this)" id="like" question_id="'+q.question_id+'" state='+q.liked+'>'+liked+' </a>' +
                                '<li class="weui-media-box__info__meta weui-media-box__info__meta_extra" id="likenum">点赞数: ' + q.like_num + '</li>' +
                                '<li class="weui-media-box__info__meta weui-media-box__info__meta_extra"><a href="/student/question/?question_id='+q.question_id+'">点此查看详情</a></li>' +
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

    $('#searchInput').on('change', function(){
        page = 0
        k.opts.loadUpFn(k)
    });

    $('select').on('change', function(){
        page = 0
        k.opts.loadUpFn(k)
    });
});