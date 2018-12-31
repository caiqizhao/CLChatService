package org.clchat.servlet;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.clchat.util.PasswordSHA1Util;
import org.mysql.mapper.MySQLUserMapper;
import org.mysql.mapper.UpdateUserDataMapper;
import org.mysql.util.MySQLDB;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "UpdateUserDataServlet",urlPatterns = "/update_user_data")
public class UpdateUserDataServlet extends HttpServlet {
    private HttpServletRequest request;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.request = request;
        SqlSessionFactory sqlSessionFactory = MySQLDB.getSqlSession();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UpdateUserDataMapper sqlUtil;
        sqlUtil = sqlSession.getMapper(UpdateUserDataMapper.class);
        PrintWriter out = response.getWriter();
        switch (Integer.parseInt(request.getParameter("code"))){
            case 1:
                updateUserName(sqlUtil);
                out.print(1);
                break;
            case 2:
                updateUserSex(sqlUtil);
                out.print(1);
                break;
            case 3:
                updateUserPassword(sqlUtil);
                out.print(1);
                break;
            case 4:
                updateUserIntroduce(sqlUtil);
                out.print(1);
                break;
        }
        if (out!=null) {
            out.flush();
            out.close();
        }
        sqlSession.commit();
        sqlSession.close();
    }

    private void updateUserName(UpdateUserDataMapper sql){
        String user_name = request.getParameter("user_name");
        String user_id =request.getParameter("user_id");
        sql.updateUserName(user_name,user_id);


    }

    private void updateUserSex(UpdateUserDataMapper sql){
        String user_sex = request.getParameter("user_sex");
        String user_id =request.getParameter("user_id");
        sql.updateUserSex(user_sex,user_id);
    }

    private void updateUserPassword(UpdateUserDataMapper sql){
        String user_password = PasswordSHA1Util.generateSHA1(request.getParameter("user_password"));
        String user_id =request.getParameter("user_id");
        sql.updateUserPassword(user_password,user_id);
    }

    private void updateUserIntroduce(UpdateUserDataMapper sql){
        String user_introduce = request.getParameter("user_introduce");
        String user_id = request.getParameter("user_id");
        sql.updateUserIntroduce(user_introduce,user_id);

    }

}
