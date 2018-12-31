package org.mysql.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MySQLDB {
    private static String RESOURCE = "org/mysql/config/mysql-config.xml";

    private static SqlSessionFactory sqlSessionFactory;

    private static ThreadLocal<SqlSession> threadLocal = new ThreadLocal<SqlSession>();

    /**
     * 创建一个初始化SqlSessionFactory的方法
     */
    public static void initSqlSessionFactry() {
        try {
            InputStream is = Resources.getResourceAsStream(RESOURCE);

            sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取工厂对象的方法
     * @return
     */
    public static SqlSessionFactory getSqlSession() {
        return sqlSessionFactory;
    }


    /**
     * 关闭SqlSession的方法
     */
    public static void close(){
        SqlSession session = threadLocal.get();
        if(session != null) {
            session.close();
            threadLocal.set(null);
        }
    }
}
