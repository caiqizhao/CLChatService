package org.clchat.servlet;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.clchat.util.PasswordSHA1Util;
import org.mysql.entity.User;
import org.mysql.mapper.MySQLUserMapper;
import org.mysql.util.MySQLDB;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "RegisterServlet",urlPatterns = "/register")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        response.setContentType("text/plain;charset=UTF-8");
        String username = request.getParameter("username");
        String password = PasswordSHA1Util.generateSHA1(request.getParameter("password"));
        SqlSessionFactory sqlSessionFactory = MySQLDB.getSqlSession();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        MySQLUserMapper sqlUtil = sqlSession.getMapper(MySQLUserMapper.class);
        User user = sqlUtil.select_user(username);
        PrintWriter out = null;
        if (user != null){
            out = response.getWriter();
            out.print("用户名已被占用");
        }else {
            sqlUtil.addUser(username,password);
            out = response.getWriter();
            out.print("注册成功");
        }
        out.flush();
        out.close();
        sqlSession.commit();
        sqlSession.close();
    }
}
