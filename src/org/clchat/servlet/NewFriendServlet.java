package org.clchat.servlet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@WebServlet(name = "NewFriendServlet",urlPatterns = "/newFriend")
public class NewFriendServlet extends HttpServlet {
    SqlSessionFactory sqlSessionFactory;
    SqlSession sqlSession;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        sqlSessionFactory = MySQLDB.getSqlSession();
        sqlSession = sqlSessionFactory.openSession();
        MySQLUserMapper sqlUtil = sqlSession.getMapper(MySQLUserMapper.class);
        String user_id = request.getParameter("user_id");
        PrintWriter out = response.getWriter();
        JSONObject jsonObject = new JSONObject();
        List<UserFriend> user_add_friend = sqlUtil.getUserAddFriend(user_id);
        if(user_add_friend!=null) {
            if (!user_add_friend.isEmpty()) {
                List<HashMap> uer_add_friend_list = LoginServlet.getUserFriendData(user_add_friend);
                jsonObject.put("user_add_friend", uer_add_friend_list);
            }
        }

        List<UserFriend> friend_add_user = sqlUtil.getFriendAddUser(user_id);
        if(friend_add_user!=null) {
            if (!friend_add_user.isEmpty()) {
                List<HashMap> friend_add_user_list = new ArrayList<HashMap>();
                for (UserFriend userFriend : friend_add_user) {
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("friend_id", userFriend.getUser_id());
                    hashMap.put("friend_name", sqlUtil.select_user(userFriend.getUser_id()).getUser_name());
                    friend_add_user_list.add(hashMap);
                }
                jsonObject.put("friend_add_user", friend_add_user_list);
            }
        }
        //得到自己发给好友信息
        List<UserFriend> userFriendList = sqlUtil.getUserFriend(user_id);
        if(userFriendList!=null) {
            //得到用户所有的好友账户和备注
            if (!userFriendList.isEmpty()) {
                List<HashMap> uer_friend_name = LoginServlet.getUserFriendData(userFriendList);
                jsonObject.put("friend_name", uer_friend_name);
            }
        }
        out.print(jsonObject);
        out.flush();
        out.close();
        sqlSession.close();
    }


}
