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

@WebServlet(name = "AddFriendServlet",urlPatterns = "/addFriend")
public class AddFriendServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user_id = request.getParameter("user_id");
        String friend_id = request.getParameter("friend_id");
        SqlSessionFactory sqlSessionFactory = MySQLDB.getSqlSession();
        SqlSession sqlSession;
        sqlSession = sqlSessionFactory.openSession();
        MySQLUserMapper sqlUtil = sqlSession.getMapper(MySQLUserMapper.class);
        if(sqlUtil.getFriendUser(user_id,friend_id)==0&&sqlUtil.getFriendUser(friend_id,user_id)==0){
            sqlUtil.addUserFriend(user_id,friend_id,0);
        }else if(sqlUtil.getFriendUser(user_id,friend_id)==1&&sqlUtil.getFriendUser(friend_id,user_id)==0){
            sqlUtil.addUserFriend(user_id,friend_id,1);
            sqlUtil.updateUserFriend(friend_id,user_id);
        }
    }
}
