package org.clchat.servlet;

import net.sf.json.JSONObject;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mysql.entity.UserIP;
import org.mysql.mapper.MySQLUserMapper;
import org.mysql.util.MySQLDB;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "GetFriendIPServlet",urlPatterns = "/getip")
public class GetFriendIPServlet extends HttpServlet {
    SqlSessionFactory sqlSessionFactory;
    SqlSession sqlSession;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        sqlSessionFactory = MySQLDB.getSqlSession();
        sqlSession = sqlSessionFactory.openSession();
        MySQLUserMapper sqlUtil;
        sqlUtil = sqlSession.getMapper(MySQLUserMapper.class);
        String friend_id = request.getParameter("friend_id");
        UserIP userIP = sqlUtil.selectUserIP(friend_id);
        PrintWriter out = response.getWriter();
        if(userIP == null){
            out.print(0);
        }else {
            out.print(userIP.getUser_ip());
        }
        out.flush();
        out.close();
        sqlSession.close();
    }
}
