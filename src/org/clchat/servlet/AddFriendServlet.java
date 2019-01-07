package org.clchat.servlet;

import net.sf.json.JSONObject;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mysql.entity.User;
import org.mysql.entity.UserFriend;
import org.mysql.mapper.MySQLUserMapper;
import org.mysql.util.MySQLDB;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "AddFriendServlet",urlPatterns = "/addFriend")
public class AddFriendServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user_id = request.getParameter("user_id");
        String friend_id = request.getParameter("friend_id");
        String code = request.getParameter("code");
        SqlSessionFactory sqlSessionFactory = MySQLDB.getSqlSession();
        SqlSession sqlSession;
        sqlSession = sqlSessionFactory.openSession();
        MySQLUserMapper sqlUtil = sqlSession.getMapper(MySQLUserMapper.class);
        PrintWriter out = response.getWriter();
        User user = null;
        System.out.println(user_id +"  "+friend_id);
        switch (Integer.parseInt(code)) {
            case 0:
                sqlUtil.deleteUserFriend(friend_id,user_id);
                break;
            case 1:
                if(sqlUtil.getFriendUser(user_id,friend_id)==0)
                    sqlUtil.addUserFriend(user_id, friend_id, 0);
                break;
            case 2:
                sqlUtil.addUserFriend(user_id, friend_id, 1);
                sqlUtil.updateUserFriend(friend_id, user_id);
                break;
            case 3:
                user = sqlUtil.select_user(friend_id);
                if (user != null) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("friend_id", user.getUser_id());
                    jsonObject.put("friend_name", user.getUser_name());
                    out.print(jsonObject);
                }else {
                    out.print("用户不存在");
                }
                break;
        }
        out.flush();
        out.close();
        sqlSession.commit();
        sqlSession.close();
    }
}
