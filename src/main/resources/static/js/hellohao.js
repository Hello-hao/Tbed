/*! Hellohao -pro | (c) 2019 Hellohao Foundation, Inc. | Hellohao.cn/license */

//cookie登录
// var cookiepass = $.cookie('pass_hellohaobycookie');
// var cookiename = $.cookie('name_hellohaobycookie');
var Hellohao_UniqueUserKey = $.cookie('Hellohao_UniqueUserKey');
if(Hellohao_UniqueUserKey!=null && Hellohao_UniqueUserKey!=""){
    cookieLogin(Hellohao_UniqueUserKey)
}else{
    $.ajax({
        type: "POST",
        url: "/islogin",
        dataType: "json",
        success: function (data) {
            if(data.lgoinret==1){
                bodys();
            }else{

            }
        }
    });
}
function cookieLogin(Hellohao_UniqueUserKey){
    $.ajax({
        type: "POST",
        url: "/user/login_c",
        data: {Hellohao_UniqueUserKey: Hellohao_UniqueUserKey},
        dataType: "json",
        success: function (data) {
            $('#loginModal').modal('hide');
            if (data == 1) {
                $.cookie('name_hellohaobycookie', loginemail, { expires: 90});
                $.cookie('pass_hellohaobycookie', loginpassword, { expires: 90});
                bodys();
            } else {
                $.cookie('name_hellohaobycookie', null);
                $.cookie('pass_hellohaobycookie', null);
            }
        }
    });
}
function bodys() {
    var h = '<li class="active"><a class="menu-link" data-section="home" data-nav-title="home"\n' +
        '                    data-section-pos="0" data-section-posy="0">Home <i class="fa fa-fw fa-home"></i>\n' +
        '                </a></li>';
    var h1 = '<li><a class="menu-link" target="_blank" href="/admin/goadmin" id="usernames">控制面板&nbsp;<i class="fa fa-fw fa-list"></i></a></li>';
    var h2 = '<li><a class="menu-link" href="javascript:exit();"  onclick="">退出&nbsp;<span class="glyphicon glyphicon-log-out"></span></a></li>';
    $("#usersrc").html(h + h1 + h2);
}

//网站退出
function exit() {
    $.ajax({
        type: "POST",
        url: "/user/exit.do",
        dataType: "json",
        success: function (data) {
            $.cookie('name_hellohaobycookie', null);
            $.cookie('pass_hellohaobycookie', null);
            layer.msg('账号已退出', {icon: 1});
            window.location.reload();
        }
    });
}