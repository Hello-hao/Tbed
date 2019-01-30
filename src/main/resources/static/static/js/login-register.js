/*
 *
 * login-register modal
 * Autor: Creative Tim
 * Web-autor: creative.tim
 * Web script: #
 * 
 */
function showRegisterForm(){
    $('.loginBox').fadeOut('fast',function(){
        $('.registerBox').fadeIn('fast');
        $('.login-footer').fadeOut('fast',function(){
            $('.register-footer').fadeIn('fast');
        });
        $('.modal-title').html('注 册');
    }); 
    $('.error').removeClass('alert alert-danger').html('');
       
}
function showLoginForm(){
    $('#loginModal .registerBox').fadeOut('fast',function(){
        $('.loginBox').fadeIn('fast');
        $('.register-footer').fadeOut('fast',function(){
            $('.login-footer').fadeIn('fast');    
        });
        
        //$('.modal-title').html('登 录');
    });       
     $('.error').removeClass('alert alert-danger').html(''); 
}

function showLoginForm2(){
    $('#loginModal .registerBox').fadeOut('fast',function(){
        $('.loginBox').fadeIn('fast');
        $('.register-footer').fadeOut('fast',function(){
            $('.login-footer').fadeIn('fast');
        });

        //$('.modal-title').html('登 录');
    });
    $('.error').removeClass('alert alert-danger').html('');
}

function openLoginModal(){
    showLoginForm();
    setTimeout(function(){
        $('#loginModal').modal('show');    
    }, 230);
    
}
function openRegisterModal(){
    showRegisterForm();
    setTimeout(function(){
        $('#loginModal').modal('show');    
    }, 230);
    
}


function shakeModal(){
	
    $('#loginModal .modal-dialog').addClass('shake');
             $('.error').addClass('alert alert-danger').html("无效的电子邮件/密码组合");
             $('input[type="password"]').val('');
             setTimeout( function(){ 
                $('#loginModal .modal-dialog').removeClass('shake'); 
    }, 1000 ); 
}

//验证邮箱
function ismail(){
	var registerusername = $("#registerusername").val();
   var mail=$("#registeremail").val();
   var registerpassword=$("#registerpassword").val();
   var registerpassword_confirmation=$("#registerpassword_confirmation").val();
  if(mail=="")
  {
	  shakeModal();
      return false;
  }
   var ze = /^\w+@\w+(\.[a-zA-Z]{2,3}){1,2}$/;
   console.log(registerpassword);
   console.log(registerpassword_confirmation);
   if(ze.test(mail)==false)
   {
	   shakeModal();
       return false;
   }
   if(registerpassword!=""&&registerpassword!=null){
   if(registerpassword==registerpassword_confirmation){
	   zhuce(mail,registerusername,registerpassword);
	   return true;  
   }else{
   shakeModal();
   return false;
   }
   }    
}



   