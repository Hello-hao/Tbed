package cn.hellohao.utils;

import cn.hellohao.pojo.Config;
import cn.hellohao.pojo.EmailConfig;
import cn.hellohao.pojo.Msg;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import io.github.biezhi.ome.OhMyEmail;
import org.springframework.core.io.ClassPathResource;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

public class NewSendEmail {

    public static Integer sendEmail(EmailConfig emailConfig, String username, String uid, String toEmail,  Config config) {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.debug", "false");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.debug",  "false");
        props.put("mail.smtp.timeout", "20000");
        props.put("mail.smtp.port", emailConfig.getPort());//465  25
        props.put("mail.smtp.host", emailConfig.getEmailurl());
        // 配置一次即可，可以配置为静态方法
//        OhMyEmail.config(OhMyEmail.SMTP_QQ(false), "xxxx@qq.com", "your@password");
        OhMyEmail.config(props, emailConfig.getEmails(), emailConfig.getEmailkey());

        String webname=config.getWebname();
        String domain = config.getDomain();
        try {
            //生成模板
            PebbleEngine engine = new PebbleEngine.Builder().build();
            ClassPathResource classPathResource = new ClassPathResource("emailTemplate/emailRegister.html");
            PebbleTemplate compiledTemplate = engine.getTemplate(classPathResource.getPath());
            Map<String, Object> context = new HashMap<>();
            context.put("username", username);
            context.put("webname", webname);
            context.put("url", domain+"/user/activation?activation="+uid+"&username=" + username );
            Writer writer = new StringWriter();
            compiledTemplate.evaluate(writer, context);
            String output = writer.toString();
            OhMyEmail.subject(webname+"账号激活")
                    .from(webname)
                    .to(toEmail)
                    .html(output)
                    .send();
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static Msg sendTestEmail(EmailConfig emailConfig, String toEmail) {
        Msg msg = new Msg();
        Properties props = new Properties();
        try {
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.debug", "false");
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.debug",  "false");
            props.put("mail.smtp.timeout", "20000");
            props.put("mail.smtp.port", emailConfig.getPort());//465  25
            props.put("mail.smtp.host", emailConfig.getEmailurl());
            OhMyEmail.config(props, emailConfig.getEmails(), emailConfig.getEmailkey());
            String webname="Hellohao图像托管程序";
            OhMyEmail.subject("Hellohao图像托管程序邮箱配置测试")
                    .from(webname)
                    .to(toEmail)
                    .html("<p>这是一条测试邮件，当您收到此邮件证明测试成功了</p>")
                    .send();
            msg.setInfo("发送邮件指令已执行，请自行前往收信箱或垃圾箱查看是否收到测试邮件");
            return msg;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            msg.setCode("110500");
            msg.setInfo(e.getMessage());
            return msg;
        }
    }


    public static Integer sendEmailFindPass(EmailConfig emailConfig,String username, String uid, String toEmail,  Config config) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.debug", "false");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.debug",  "false");
        props.put("mail.smtp.timeout", "20000");
        props.put("mail.smtp.port", emailConfig.getPort());//465  25
        props.put("mail.smtp.host", emailConfig.getEmailurl());
        // 配置一次即可，可以配置为静态方法
//        OhMyEmail.config(OhMyEmail.SMTP_QQ(false), "xxxx@qq.com", "your@password");
        OhMyEmail.config(props, emailConfig.getEmails(), emailConfig.getEmailkey());
        String webname=config.getWebname();
        String domain = config.getDomain();
        String new_pass = UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,10);
        try {
            //生成模板
            PebbleEngine engine = new PebbleEngine.Builder().build();
            ClassPathResource classPathResource = new ClassPathResource("emailTemplate/emailFindPass.html");
            PebbleTemplate compiledTemplate = engine.getTemplate(classPathResource.getPath());
            Map<String, Object> context = new HashMap<>();
            context.put("username", username);
            context.put("webname", webname);
            context.put("new_pass", new_pass);
            context.put("url",domain+"/user/retrieve?activation=" + uid+"&cip="+ HexUtil.encodeHexStr(new_pass, CharsetUtil.CHARSET_UTF_8));
            Writer writer = new StringWriter();
            compiledTemplate.evaluate(writer, context);
            String output = writer.toString();
            OhMyEmail.subject(webname+"密码重置")
                    .from(webname)
                    .to(toEmail)
                    .html(output)
                    .send();
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }



}
