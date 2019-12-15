package cn.hellohao.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;


final class RunSqlScript {

    public static  String DBDRIVER = "com.mysql.cj.jdbc.Driver";
    public static  String DBURL ;
    //现在使用的是mysql数据库，是直接连接的，所以此处必须有用户名和密码
    public static  String USERNAME ;
    public static  String PASSWORD ;

    public static Integer RunSelectCount(String sql) {
        Integer count = 0;
        //数据库连接对象
        Connection conn = null;
        //数据库操作对象
        Statement stmt = null;
        //1、加载驱动程序
        try {
            Class.forName(DBDRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //2、连接数据库
        //通过连接管理器连接数据库
        try {
            //在连接的时候直接输入用户名和密码才可以连接
            conn = DriverManager.getConnection(DBURL,USERNAME,PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //3、向数据库中插入一条数据
        //String sql = "select count(*) from information_schema.columns where table_name = 'config' and column_name = 'id'";
        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //4、执行语句
        try {
            //stmt.executeUpdate(sql);
            ResultSet resultSet = stmt.executeQuery(sql);
            if(resultSet.next()){
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        //5、关闭操作
        try {
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }


    //insert操作
    public static Integer RunInsert(String sql) {
        Integer count = 0;
        //数据库连接对象
        Connection conn = null;
        //数据库操作对象
        Statement stmt = null;
        //1、加载驱动程序
        try {
            Class.forName(DBDRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //2、连接数据库
        //通过连接管理器连接数据库
        try {
            //在连接的时候直接输入用户名和密码才可以连接
            conn = DriverManager.getConnection(DBURL,USERNAME,PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //4、执行语句
        try {
            count = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //5、关闭操作
        try {
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }


}