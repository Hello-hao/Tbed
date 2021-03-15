package cn.hellohao.utils;

import org.omg.CORBA.Environment;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.sql.SQLException;
import java.util.UUID;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/11/29 14:32
 */
@Configuration
public class FirstRun implements InitializingBean {

    @Value("${spring.datasource.username}")
    private String jdbcusername;

    @Value("${spring.datasource.password}")
    private String jdbcpass;

    @Value("${spring.datasource.url}")
    private String jdbcurl;

    @Override
    public void afterPropertiesSet() throws Exception {
        RunSqlScript.USERNAME = jdbcusername;
        RunSqlScript.PASSWORD = jdbcpass;
        RunSqlScript.DBURL = jdbcurl;
        Print.Normal("正在校验数据库参数...");
        RunSqlScript.RunInsert(dynamic);
        RunSqlScript.RunInsert(compressed);

        String uid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        RunSqlScript.RunInsert("update user set uid='"+uid+"' where id = 1");

        Integer ret1 = RunSqlScript.RunSelectCount(sql1);
        if(ret1==0){
            Print.Normal("In execution...");
            RunSqlScript.RunInsert(sql2);
            ;

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
        Integer ret5 = RunSqlScript.RunSelectCount(judgeTable+"'config' and column_name = 'theme'");
        if(ret5==0){
            RunSqlScript.RunInsert(sql9);
            Print.Normal("Stage 7");
        }

        Integer ret6 = RunSqlScript.RunSelectCount(judgeTable+"'imgandalbum' and column_name = 'notes' and TABLE_SCHEMA='picturebed'");
        if(ret6==0){
            RunSqlScript.RunInsert(sql11);
            Print.Normal("Stage 8");
        }

        RunSqlScript.RunInsert(sql10);

        //RunSqlScript.RunInsert("alter table imgdata drop index index_md5key_url");
        RunSqlScript.RunInsert(inddx_md5key);
        RunSqlScript.RunInsert("UPDATE `keys` SET `Endpoint` = '0' WHERE `id` = 8");
        Print.Normal("Stage success");
    }

    //检查表是否存在，后边加'imgdata' and column_name = 'explains'  检查字段是否存在
    private String judgeTable = "select count(*) from information_schema.columns where table_name = ";
    //创建blacklist  2019-11-29
    private String sql1 = "select count(*) from information_schema.columns where table_name = 'uploadconfig' and column_name = 'blacklist'";
    private String sql2 = "alter table uploadconfig add blacklist varchar(500);";
    //创建imgandalbum和album  添加imgdata表字段explain 2019-12-20
    private String sql3 ="CREATE TABLE `imgandalbum`  (`imgname` varchar(5000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,`albumkey` varchar(5000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic";
    private String sql4 ="CREATE TABLE `album`  (`albumkey` varchar(5000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,`albumtitle` varchar(5000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,`createdate` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL, `password` varchar(5000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic";
    private String sql6 = "alter table imgdata add explains varchar(5000)";
    //添加album表字段userid
    private String sql7 = "alter table album add userid int(10)";
    //添加imgdata表字段md5key
    private String sql8 = "alter table imgdata add md5key varchar(5000)";
    private String sql9 = "ALTER TABLE config ADD theme int(4) DEFAULT '1' COMMENT '主题'  ";
//修改字段长度
    private String sql10 = "alter table config modify column `explain` varchar(1000),modify column links varchar(1000),modify column notice varchar(1000),modify column baidu varchar(1000)";

    //添加imgandalbum字段notes(添加画廊图片说明)
    private String sql11 = "alter table imgandalbum add notes varchar(1000) DEFAULT ' '";

    private String inddx_md5key = "ALTER TABLE imgdata ADD INDEX index_md5key_url ( md5key,imgurl)";
    //                             create index yarn_app_result_i4 on yarn_app_result (flow_exec_id(100), another_column(50));

    private String dynamic = "alter table imgdata row_format=dynamic";
    private String compressed = "alter table imgdata row_format=compressed";

    private void clears(){
        File file1 = new File(File.separator+"HellohaoData"+File.separator+"hellohaotempimg");
        File file2 = new File(File.separator+"HellohaoData"+File.separator+"hellohaotempwatermarimg");

        //判断目录有没有创建
        File file = new File(File.separator+"HellohaoData");
        if(!file.exists()){
            file.mkdirs();
            file1.mkdirs();
            file2.mkdirs();
        }else{
            if(!file1.exists()){
                file1.mkdirs();
            }else if(!file2.exists()){
                file2.mkdirs();
            }
        }
    }


}
