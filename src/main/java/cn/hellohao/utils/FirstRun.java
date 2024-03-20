package cn.hellohao.utils;

import cn.hellohao.config.GlobalConstant;
import cn.hutool.core.io.FileUtil;import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Properties;
import java.util.UUID;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/11/29 14:32
 */
@Configuration
public class FirstRun implements InitializingBean {
    private static Logger logger = LoggerFactory.getLogger(FirstRun.class);
    @Value("${spring.datasource.username}")
    private String jdbcusername;

    @Value("${spring.datasource.password}")
    private String jdbcpass;

    @Value("${spring.datasource.url}")
    private String jdbcurl;

    @Value("${DockerWebHost}")
    private String DockerWebHost;


    @Override
    public void afterPropertiesSet() {
        if(!DockerWebHost.contains("null")){
            contentToTxt("/hellohaotbed/webapps/hellohao/config.json", "{\"serverHost\": \""+DockerWebHost+"\"}");
        }
        isWindows();
        RunSqlScript.USERNAME = jdbcusername;
        RunSqlScript.PASSWORD = jdbcpass;
        RunSqlScript.DBURL = jdbcurl;
        logger.info("正在校验数据库参数...");
        RunSqlScript.RunInsert(dynamic);
        RunSqlScript.RunInsert(compressed);

        String uid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        RunSqlScript.RunInsert("update user set uid='"+uid+"' where id = 1");

        Integer ret0 =
                RunSqlScript.RunSelectCount(judgeTable + "'keys' and column_name = 'Region'");
        if (ret0 == 0) {
            RunSqlScript.RunInsert(sql0);
            Print.Normal("Add keys.Region||RootPath");
        }

        Integer ret1 = RunSqlScript.RunSelectCount(sql1);
        if(ret1==0){
            Print.Normal("In execution...");
            RunSqlScript.RunInsert(sql2);
        }else{
            if(ret1>0){
                Print.Normal("Stage 1");
            }else{
                Print.Normal("Mysql 报了一个错");
            }
        }
        Integer imgandalbum = RunSqlScript.RunSelectCount(judgeTable+"'imgandalbum'");
        if(imgandalbum==0){
            RunSqlScript.RunInsert(sql3);
            Print.Normal("Stage 2");
        }
        Integer album = RunSqlScript.RunSelectCount(judgeTable+"'album'");
        if(album==0){
            RunSqlScript.RunInsert(sql4);
            Print.Normal("Stage 3");
        }
        Integer ret2 = RunSqlScript.RunSelectCount(judgeTable+"'imgdata' and column_name = 'explains'");
        if(ret2==0){
            RunSqlScript.RunInsert(sql6);
            Print.Normal("Stage 4");
        }
        Integer ret3 = RunSqlScript.RunSelectCount(judgeTable+"'album' and column_name = 'userid'");
        if(ret3==0){
            RunSqlScript.RunInsert(sql7);
            Print.Normal("Stage 5");
        }
        Integer ret4 = RunSqlScript.RunSelectCount(judgeTable+"'imgdata' and column_name = 'md5key'");
        if(ret4==0){
            RunSqlScript.RunInsert(sql8);
            Print.Normal("Stage 6");
        }

        Integer ret6 = RunSqlScript.RunSelectCount(judgeTable+" 'user' and column_name = 'token'");
        if(ret6==0){
            RunSqlScript.RunInsert(sql12);
            Print.Normal("Add user.token");
        }
        Integer isappclient = RunSqlScript.RunSelectCount(isTableName+"'appclient'");
        if(isappclient==0){
            Integer integer = RunSqlScript.RunInsert(createAppclient);
            RunSqlScript.RunInsert(instartAppclient);
            Print.Normal("Add table.appclient");
        }
        Integer isconfdata = RunSqlScript.RunSelectCount(isTableName+"'confdata'");
        if(isconfdata==0){
            RunSqlScript.RunInsert(createConfdata);
            RunSqlScript.RunInsert(instartConfdata);
            Print.Normal("Add table.confdata");
        }
        Integer ret7 =
                RunSqlScript.RunSelectCount(judgeTable + " 'keys' and column_name = 'SysTransmit'");
        if (ret7 == 0) {
            RunSqlScript.RunInsert(sql7);
            Print.Normal("Add keys.SysTransmit");
        }
        Integer ret8 =
                RunSqlScript.RunSelectCount(judgeTable + " 'imgdata' and column_name = 'brieflink'");
        if (ret8 == 0) {
            RunSqlScript.RunInsert(sql8);
            Print.Normal("Add imgdata.brieflink");
        }
        Integer ret9 =
                RunSqlScript.RunSelectCount(judgeTable + " 'imgdata' and column_name = 'shortlink'");
        if (ret9 == 0) {
            RunSqlScript.RunInsert(sql9);
            Print.Normal("Add imgdata.shortlink");
        }
        Integer ret10 =
                RunSqlScript.RunSelectCount(" select count(*) from tbed.keys where storageType='9'");
        if (ret10 == 0) {
            RunSqlScript.RunInsert(sql10);
            Print.Normal("Add Webdav Config");
        }
        RunSqlScript.RunInsert(index_imgdata);
        RunSqlScript.RunInsert("ALTER TABLE tbed.`user` MODIFY id int auto_increment;");
        Print.Normal("Stage success");

        clears();
    }

    //检查表是否存在，后边加'imgdata' and column_name = 'explains'  检查字段是否存在
    private String isTableName = "SELECT count(table_name) FROM information_schema.TABLES WHERE TABLE_SCHEMA='tbed' and table_name =";
    private String judgeTable = "select count(*) from information_schema.columns where TABLE_SCHEMA='tbed' and table_name = ";
    private String sql0 =
            "alter table `keys` add (Region varchar(255) DEFAULT null,RootPath varchar(255) DEFAULT '/');";
    private String sql1 = "select count(*) from information_schema.columns where table_name = 'uploadconfig' and column_name = 'blacklist'";
    private String sql2 = "alter table uploadconfig add blacklist varchar(500);";
    //创建imgandalbum和album 添加imgdata表字段explain 2019-12-20
    private String sql3 ="CREATE TABLE `imgandalbum`  (`imgname` varchar(5000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,`albumkey` varchar(5000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic";
    private String sql4 ="CREATE TABLE `album`  (`albumkey` varchar(5000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,`albumtitle` varchar(5000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,`createdate` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL, `password` varchar(5000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic";
    private String sql6 = "alter table imgdata add explains varchar(5000)";
    private String sql7 =
            "alter table `keys` add SysTransmit varchar(50) DEFAULT NULL ;";
    private String sql8 = "alter table `imgdata` add brieflink varchar(100) DEFAULT null ;";
    private String sql9 = "alter table `imgdata` add shortlink varchar(100) DEFAULT null ;";
    //  图片标识名字段
    private String sql10 = "INSERT INTO `tbed`.`keys`(`id`, `AccessKey`, `AccessSecret`, `Endpoint`, `Bucketname`, `RequestAddress`, `storageType`, `keyname`, `Region`, `RootPath`, `SysTransmit`) VALUES (null, '账号', '密码', '连接', '', '存储源请求域名', 9, 'WebDAV', NULL, '/', NULL);";

    private String sql12 = "alter table tbed.user add `token` varchar(255)";
    //创建客户端程序相关表
    private String createAppclient = "CREATE TABLE `appclient`  (`id` varchar(10) NOT NULL,`isuse` varchar(10) NOT NULL,`winpackurl` varchar(255) NULL DEFAULT NULL,`macpackurl` varchar(255) NULL DEFAULT NULL,`appname` varchar(20) NULL,`applogo` varchar(255) NULL,`appupdate` varchar(10) NOT NULL) ";
    private String instartAppclient = "INSERT INTO `appclient` VALUES ('app', 'on', NULL, NULL, 'Hellohao图像托管', 'https://hellohao.nos-eastchina1.126.net/TbedClient/app.png', '1.0.1');";
    //创建confdata表
    private String createConfdata = "CREATE TABLE tbed.confdata ( `key` varchar(100) NULL, jsondata LONGTEXT NULL )";
    private String instartConfdata =
      "INSERT INTO tbed.confdata VALUES ('config', '{\\\"sourcekey\\\":7,\\\"emails\\\":1,\\\"webname\\\":\\\"Hellohao图床\\\",\\\"explain\\\":\\\"Hellohao图像托管，是一家免费开源的图像托管，即时分享你的美好瞬间。\\\",\\\"links\\\":\\\"<p style=\\\\\\\"color:#7c7c88;\\\\\\\">© 2019 <a href=\\\\\\\"http://www.Hellohao.cn/\\\\\\\" target=\\\\\\\"_blank\\\\\\\" title=\\\\\\\"Hellohao\\\\\\\">Hellohao</a><span>  - All Rights Reserved</span> </p>\\\",\\\"notice\\\":\\\"也许...|这将是最好用的图床|为了更好的用户体验，建议您注册本站继续免费使用Hellohao图床。本站不得上传任何形式的非法图片，一旦发现，永久删除并禁封账户。情节严重者将相关资料交于相关部门处理。\\\",\\\"domain\\\":\\\"http://127.0.0.1:10088\\\",\\\"background1\\\":\\\"\\\",\\\"sett\\\":\\\"1\\\",\\\"webms\\\":\\\"Hellohao图像托管，是一家免费开源的图像托管，即时分享你的美好瞬间。\\\",\\\"webkeywords\\\":\\\"hellohao图床,图床,图片上传,开源图床,hellohao,图像托管，图片分享\\\",\\\"theme\\\":2,\\\"websubtitle\\\":\\\"这将是你用过最优秀的图像托管程序\\\",\\\"logo\\\":null,\\\"aboutinfo\\\":\\\"<img width=\\\\\\\"300px\\\\\\\" src=\\\\\\\"http://img.wwery.com/hellohao/rPscRYwz.png\\\\\\\">            <br />            <br />            <p>也许,这将是你用到最优秀的图像托管程序</p>            <p>本程序为Hellohao图象托管程序</p>            <br/>            <p style=\\\\\\\"color: #656565;\\\\\\\">作者：hellohao独立开发</p>            <p style=\\\\\\\"color: #656565;\\\\\\\">www.hellohao.cn</p>\\\"}');";

    private String index_imgdata = "ALTER TABLE imgdata ADD INDEX index_md5key_url ( id,md5key,imgname,imgurl,idname,imguid)";

    private String dynamic = "alter table imgdata row_format=dynamic";
    private String compressed = "alter table imgdata row_format=compressed";

    private void clears(){
        File file1 = new File(GlobalConstant.LOCPATH+File.separator+"hellohaotempimg");
        File file2 = new File(GlobalConstant.LOCPATH+File.separator+"hellohaotempwatermarimg");

        //判断目录有没有创建
        File file = new File(GlobalConstant.LOCPATH);
        if(!file.exists()){
            FileUtil.mkdir(file);
            FileUtil.mkdir(file1);
            FileUtil.mkdir(file2);

        }else{
            if(!file1.exists()){
                FileUtil.mkdir(file1);
            }else if(!file2.exists()){
                FileUtil.mkdir(file2);
            }
        }
    }


    public boolean isWindows() {
        System.out.println("当前系统类型:"+System.getProperties().getProperty("os.name").toUpperCase());
        if(System.getProperties().getProperty("os.name").toUpperCase().contains("MAC")){
            GlobalConstant.SYSTYPE = "MAC";
            Properties props=System.getProperties();
            GlobalConstant.LOCPATH = props.getProperty("user.home")+File.separator+".HellohaoData";
        }
        return System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1;
    }

    public static void contentToTxt(String filePath, String content) {
//        String str = new String(); //原有txt内容
        String s1 = new String();//内容更新
        try {
            File f = new File(filePath);
            if (f.exists()) {
                logger.info("文件存在");
            } else {
                logger.info("文件不存在,准备创建");
//                f.createNewFile();// 不存在则创建
                FileUtil.createTempFile(f);
            }
//            BufferedReader input = new BufferedReader(new FileReader(f));
//            while ((str = input.readLine()) != null) {
//                s1 += str + "\n";
//            }
//            input.close();
            s1 += content + "\n";
            BufferedWriter output = new BufferedWriter(new FileWriter(f));
            output.write(s1);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}
