package cn.hellohao.utils;

import cn.hellohao.pojo.Config;
import cn.hellohao.pojo.EmailConfig;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

public class SendEmail {

    public static MimeMessage Emails(EmailConfig emailConfig) {
        Properties p = new Properties();
        p.setProperty("mail.smtp.auth", "true");
        p.setProperty("mail.smtp.host", emailConfig.getEmailurl());
        p.setProperty("mail.smtp.port", emailConfig.getPort());
        p.setProperty("mail.smtp.socketFactory.port", emailConfig.getPort());
        p.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        //p.setProperty("mail.smtp.socketFactory.class", "SSL_FACTORY");

        Session session = Session.getInstance(p, new Authenticator() {
            // 设置认证账户信息
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailConfig.getEmails(), emailConfig.getEmailkey());
            }
        });
        session.setDebug(true);
        MimeMessage message = new MimeMessage(session);
        return message;
    }

    public static Integer sendEmail(MimeMessage message, String username, String Url, String email, EmailConfig emailConfig, Config config) {
        String webname=config.getWebname();
        String domain = config.getDomain();
        String texts = "<h3>你正在注册【"+webname+"】，点击下方链接进行激活：</h3><br /><a href='"+domain+"/user/activation.do?activation=" + Url + "&username=" + username + "'>"+domain+"/user/activation.do?activation=" + Url + "&username=" + username + "</a>";
        String body = "<!DOCTYPE html>\n" +
                "<html>\n" +
                " <head>\n" +
                "  <style type=\"text/css\">*{margin:0;padding:0;box-sizing:border-box}a{color:inherit;text-decoration:none;background-color:transparent}li{list-style:none}.clearfix:after{content:'';display:table;clear:both}body{font-size:14px;color:#494949;overflow:auto}.wrap{position:relative;min-height:580px}.wrap-bg circle,.wrap-bg rect{stroke-width:0;-ms-transform:rotate(30deg) scale(1.1);transform:rotate(30deg) scale(1.1);-ms-transform-origin:center;transform-origin:center}.main{position:absolute;top:50%;left:50%;z-index:2;width:970px;background:#effbff;-ms-transform:translate(-50%,-50%);transform:translate(-50%,-50%);box-shadow:0 0 50px rgba(0,0,0,.1)}.header-tabs{width:100%;height:55px;padding:0 30px;background:linear-gradient(45deg,#4c75e9,#2c7ce3);overflow:hidden}.header-tabs .tab{float:left;height:40px;width:200px;padding:0 18px;margin-top:15px;border-radius:10px 10px 0 0;background:#fbfdff;line-height:40px;color:#113c73}.header-tabs .tab:after{content:'';display:table;clear:both}.header-tabs .tab span{float:left}.header-tabs .tab i{position:relative;float:right;width:11px;height:11px;margin-top:14px;cursor:pointer;-ms-transform:rotate(45deg);transform:rotate(45deg)}.header-tabs .tab i:before{content:'';position:absolute;top:5px;left:0;display:block;width:100%;height:1px;background:#113c73}.header-tabs .tab i:after{content:'';position:absolute;left:5px;top:0;display:block;width:1px;height:100%;background:#113c73}.header-tabs .tab-add{position:relative;float:left;width:28px;height:28px;margin:20px 0 0 15px;border-radius:50%;background:#a9cdf7}.header-tabs .tab-add:before{content:'';position:absolute;top:13px;left:9px;display:block;width:10px;height:2px;background:#113c73}.header-tabs .tab-add:after{content:'';position:absolute;left:13px;top:9px;display:block;width:2px;height:10px;background:#113c73}.header-tabs .tabs-tool{float:right;height:100%}.header-tabs .tabs-tool a{position:relative;float:left;width:14px;height:100%;margin:0 15px}.header-tabs .tabs-tool .btn-min:before{content:'';position:absolute;top:27px;left:0;width:100%;height:1px;background:#113c73}.header-tabs .tabs-tool .btn-max:before{content:'';position:absolute;top:20px;left:0;width:14px;height:14px;border:1px solid #113c73;box-sizing:border-box}.header-tabs .tabs-tool .btn-close{width:15px;-ms-transform:rotate(45deg);transform:rotate(45deg)}.header-tabs .tabs-tool .btn-close:before{content:'';position:absolute;top:27px;left:0;display:block;width:15px;height:1px;background:#113c73}.header-tabs .tabs-tool .btn-close:after{content:'';position:absolute;left:7px;top:20px;display:block;width:1px;height:15px;background:#113c73}.header-url{width:100%;height:54px;padding:10px 20px 10px 15px;background:#f2f2f2}.header-url a{float:left;height:100%;margin:0 9px;overflow:hidden}.header-url a svg{margin-top:9px}.header-url a .arrow{fill:none;stroke:#494949;stroke-width:2px;stroke-linecap:round;stroke-linejoin:round}.header-url .btn-next .arrow{-ms-transform-origin:center;transform-origin:center;-ms-transform:rotate(180deg);transform:rotate(180deg);stroke:#d4d5d6}.header-url .btn-refresh path{fill:none;stroke:#494949;stroke-width:2px;stroke-linecap:round;stroke-linejoin:round;-ms-transform-origin:center;transform-origin:center;-ms-transform:rotate(40deg);transform:rotate(40deg)}.header-url .btn-refresh polyline{fill:#494949;-ms-transform-origin:center;transform-origin:center;-ms-transform:rotate(40deg);transform:rotate(40deg)}.header-url input[type=text]{height:100%;width:820px;margin-left:6px;padding:0 1em;border:1px solid #a9a9a9;border-radius:4px;background:#fff}.main-content{height:470px;border:1px solid #c4dce5;background:#f4fcff}.main-content h5{padding:110px 0;font-weight:400;text-align:center}.main-content h5 span{position:relative;display:inline-block;font-size:60px;color:#2c7ce3}.main-content h5 span:before{content:'';position:absolute;top:48px;left:-36px;display:block;width:14px;height:14px;border-radius:50%;background:linear-gradient(45deg,#ded9ff,#2c7ce3);opacity:.2}.main-content h5 span:after{content:'';position:absolute;top:32px;left:-80px;display:block;width:20px;height:20px;border-radius:50%;background:linear-gradient(45deg,#ded9ff,#2c7ce3);opacity:.2}.main-content h5 span i:before{content:'';position:absolute;top:32px;right:-72px;display:block;width:14px;height:14px;border-radius:50%;background:linear-gradient(45deg,#ded9ff,#2c7ce3);opacity:.2}.main-content h5 span i:after{content:'';position:absolute;top:48px;right:-40px;display:block;width:20px;height:20px;border-radius:50%;background:linear-gradient(45deg,#ded9ff,#2c7ce3);opacity:.2}.main-content p{font-size:26px;text-align:center}</style>\n" +
                " </head>\n" +
                " <body>\n" +
                "  <div class=\"wrap\">\n" +
                "   <div class=\"main\">\n" +
                "    <header>\n" +
                "     <div class=\"header-tabs clearfix\">\n" +
                "      <a href=\"javascript: void(0);\" class=\"tab\"><span>账号激活</span> <i></i> </a>\n" +
                "      <a href=\"javascript: void(0);\" class=\"tab-add\"></a>\n" +
                "      <div class=\"tabs-tool\">\n" +
                "       <a href=\"javascript: void(0);\" class=\"btn-min\"></a> \n" +
                "       <a href=\"javascript: void(0);\" class=\"btn-max\"></a> \n" +
                "       <a href=\"javascript: void(0);\" class=\"btn-close\"></a>\n" +
                "      </div>\n" +
                "     </div>\n" +
                "    </header>\n" +
                "    <div class=\"main-content\">\n" +
                "     <h5><span>您正在注册"+webname+"<i></i></span></h5>\n" +
                "     <p>点击 <a href='"+domain+"/user/activation.do?activation=" + Url + "&username=" + username + "'  ><u>&nbsp;激活链接&nbsp;</u></a> 进行账号激活</p>\n" +
                "    </div>\n" +
                "   </div>\n" +
                "  </div>\n" +
                "  <script async=\"\" src=\"//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js\"></script> \n" +
                "  <!-- yedomains --> \n" +
                "  <ins class=\"adsbygoogle\" style=\"display:block\" data-ad-client=\"ca-pub-2629805781865389\" data-ad-slot=\"1129638672\" data-ad-format=\"auto\" data-full-width-responsive=\"true\"></ins> \n" +
                "  <script>\n" +
                "(adsbygoogle = window.adsbygoogle || []).push({});\n" +
                "</script>  \n" +
                " </body>\n" +
                "</html>";
        try {
            // 发件人
            message.setFrom(new InternetAddress(emailConfig.getEmails(), emailConfig.getEmailname(), "UTF-8"));
            // 收件人和抄送人
            message.setRecipients(Message.RecipientType.TO, email);
            message.setSubject(emailConfig.getEmailname()+"账号激活");//标题
            message.setContent(texts, "text/html;charset=UTF-8");//内容
            message.setSentDate(new Date());
            message.saveChanges();
            Transport.send(message);
            return 1;
        } catch (MessagingException e) {
            e.printStackTrace();
            return 0;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return 0;
        }
    }


}
