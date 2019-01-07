package org.clchat.servlet;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mysql.mapper.MySQLUserMapper;
import org.mysql.util.MySQLDB;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "UpdateMessageStateServlet",urlPatterns = "/updateMessageState")
public class UpdateMessageStateServlet extends HttpServlet {
    SqlSessionFactory sqlSessionFactory;
    SqlSession sqlSession;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        sqlSessionFactory = MySQLDB.getSqlSession();
        sqlSession = sqlSessionFactory.openSession();
        MySQLUserMapper sqlUtil = sqlSession.getMapper(MySQLUserMapper.class);
        String friend_id = request.getParameter("friend_id");
        String user_id = request.getParameter("user_id");
        sqlUtil.updateMessageSate(user_id,friend_id);
        sqlSession.commit();
        sqlSession.close();

    }
}
