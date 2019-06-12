

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
        $('.error').addClass('alert alert-danger').html("用户名格式不正确");
    }else if(val==2){
        $('.error').addClass('alert alert-danger').html("无效的电子邮件");
    }else if(val==3){
        $('.error').addClass('alert alert-danger').html("密码输入不一致");
    }else if(val==5){
        $('.error').addClass('alert alert-danger').html("邮箱不能为空");
    }else{
        $('.error').addClass('alert alert-danger').html("密码不能为空");
    }

    $('input[type="password"]').val('');
    setTimeout(function () {
        $('#loginModal .modal-dialog').removeClass('shake');
    }, 1000);
}

//验证邮箱
function ismail() {
    var registerusername = $("#registerusername").val();
    var mail = $("#registeremail").val();
    var registerpassword = $("#registerpassword").val();
    var registerpassword_confirmation = $("#registerpassword_confirmation").val();

    var ze2 = /^(\d|[a-zA-Z])+$/;
    if (ze2.test(registerusername) == false || registerusername.length>10) {
        shakeModal(1);
        return false;
    }
    if (mail == "") {
        shakeModal(5);
        return false;
    }
    var ze = /^\w+@\w+(\.[a-zA-Z]{2,3}){1,2}$/;
    if (ze.test(mail) == false) {
        shakeModal(2);
        return false;
    }

    if (registerpassword != "" && registerpassword != null) {
        if (registerpassword == registerpassword_confirmation) {
            zhuce(mail, registerusername, registerpassword);
            return true;
        } else {
            shakeModal(3);
            return false;
        }
    }else{
        shakeModal(4);
        return false;
    }
}



   