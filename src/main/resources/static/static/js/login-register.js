

/*
 *
 * login-register modal
 * Autor: Creative Tim
 * Web-autor: creative.tim
 * Web script: #
 * 
 */
function showRegisterForm() {
    $('.loginBox').fadeOut('fast', function () {
        $('.registerBox').fadeIn('fast');
        $('.login-footer').fadeOut('fast', function () {
            $('.register-footer').fadeIn('fast');
        });
        $('.modal-title').html('系统注册');
        $("#biaoti").text("注册账号");
    });
    $('.error').removeClass('alert alert-danger').html('');

}

function showLoginForm() {
    $('#loginModal .registerBox').fadeOut('fast', function () {
        $('.loginBox').fadeIn('fast');
        $('.register-footer').fadeOut('fast', function () {
            $('.login-footer').fadeIn('fast');
        });

        //$('.modal-title').html('登 录');
        $("#biaoti").text("登 录");
    });
    $('.error').removeClass('alert alert-danger').html('');
}

function showLoginForm2() {
    $('#loginModal .registerBox').fadeOut('fast', function () {
        $('.loginBox').fadeIn('fast');
        $('.register-footer').fadeOut('fast', function () {
            $('.login-footer').fadeIn('fast');
        });

        //$('.modal-title').html('登 录');
    });
    $('.error').removeClass('alert alert-danger').html('');
}

function openLoginModal() {
    showLoginForm();
    setTimeout(function () {
        $('#loginModal').modal('show');
    }, 230);
}

function openRegisterModal() {
    showRegisterForm();
    setTimeout(function () {
        $('#loginModal').modal('show');
    }, 230);

}


function shakeModal(val) {

    $('#loginModal .modal-dialog').addClass('shake');
    if(val==1){
        $('#zcts').text('用户名格式不正确');
        //$('.error').addClass('alert alert-danger').html("用户名格式不正确");
    }else if(val==2){
        $('#zcts').text('无效的电子邮件');
        //$('.error').addClass('alert alert-danger').html("无效的电子邮件");
    }else if(val==3){
        $('#zcts').text('密码输入不一致');
        //$('.error').addClass('alert alert-danger').html("密码输入不一致");
    }else if(val==4){
        $('#zcts').text('密码不能为空');
    }else if(val==5){
        $('#zcts').text('邮箱不能为空');
        //$('.error').addClass('alert alert-danger').html("邮箱不能为空");
    }else if(val==6){
        $('#zcts').text('未通过验证码校验');
    }else{
        $('#zcts').text('');
    }

    $('input[type="password"]').val('');
    setTimeout(function () {
        $('#loginModal .modal-dialog').removeClass('shake');
    }, 1000);
}
//窗口抖动
function  dd() {
    var len = 4, //晃动的距离，单位像素
        c = 16, //晃动次数，4次一圈
        step = 0, //计数器
        img = $("#modal-3"),
        off = img.offset();
    this.step = 0;
    timer = setInterval(function () {
        var set = pos();
        img.offset({ top: off.top + set.y * len, left: off.left + set.x * len });
        if (step++ >= c) {
            img.offset({ top: off.top, left: off.left }); //抖动结束回归原位
            clearInterval(timer);

        }
        // console.log(step)
    }, 45);
}
function pos() {
    this.step = this.step ? this.step : 0;
    this.step = this.step == 4 ? 0 : this.step;
    var set = {
        0: { x: 0, y: -1 },
        1: { x: -1, y: 0 },
        2: { x: 0, y: 1 },
        3: { x: 1, y: 0 }
    }
    return set[this.step++];
}

//验证邮箱
function ismail() {
    $("#userzc").css('display','none');
    $("#zctishi").css('display','block');

    var registerusername = $("#registerusername").val();
    var mail = $("#registeremail").val();
    var registerpassword = $("#registerpassword").val();
    var registerpassword_confirmation = $("#registerpassword_confirmation").val();

    var ze2 = /^(\d|[a-zA-Z])+$/;
    if (ze2.test(registerusername) == false || registerusername.length>10) {
        shakeModal(1);
        $("#userzc").css('display','block');
        $("#zctishi").css('display','none');
        return false;
    }
    if (mail == "") {
        shakeModal(5);
        $("#userzc").css('display','block');
        $("#zctishi").css('display','none');
        return false;
    }
    var ze = /^\w+@\w+(\.[a-zA-Z]{2,3}){1,2}$/;
    if (ze.test(mail) == false) {
        shakeModal(2);
        $("#userzc").css('display','block');
        $("#zctishi").css('display','none');
        return false;
    }

    if (registerpassword != "" && registerpassword != null) {
        if (registerpassword == registerpassword_confirmation) {
            zhuce(mail, registerusername, registerpassword);
            return true;
        } else {
            shakeModal(3);
            $("#userzc").css('display','block');
            $("#zctishi").css('display','none');
            return false;
        }
    }else{
        shakeModal(4);
        $("#userzc").css('display','block');
        $("#zctishi").css('display','none');
        return false;
    }
}



   