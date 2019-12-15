package cn.hellohao.utils;

import org.omg.CORBA.Environment;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

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
        Integer ret1 = RunSqlScript.RunSelectCount(sql1);
        if(ret1==0){
            Print.Normal("In execution...");
            RunSqlScript.RunInsert(sql2);

        }else{
            if(ret1>0){
                Print.Normal("Good! No action required!");
            }else{
                Print.Normal("Mysql 报了一个错");
            }
        }
    }

    //创建blacklist  2019-11-29
    private String sql1 = "select count(*) from information_schema.columns where table_name = 'uploadconfig' and column_name = 'blacklist'";
    private String sql2 = "alter table uploadconfig add blacklist varchar(500);";

}
