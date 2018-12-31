package org.clchat.servlet;

import com.mysql.cj.xdevapi.JsonArray;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.clchat.util.PasswordSHA1Util;
import org.mysql.entity.Message;
import org.mysql.entity.User;
import org.mysql.entity.UserFriend;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "LoginServlet",urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = PasswordSHA1Util.generateSHA1(request.getParameter("password"));
        SqlSessionFactory sqlSessionFactory = MySQLDB.getSqlSession();
        SqlSession sqlSession;
        sqlSession = sqlSessionFactory.openSession();
        MySQLUserMapper sqlUtil;
        sqlUtil = sqlSession.getMapper(MySQLUserMapper.class);
        User user = sqlUtil.select_user(username);
        PrintWriter out = response.getWriter();
        if (user != null) {
            if (user.getUser_password().equals(password)) {
                JSONObject user_json = JSONObject.fromObject(user);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("user", user_json);

                List<UserFriend> user_add_friend = sqlUtil.getFriendAddUser(username);
                if(!user_add_friend.isEmpty()){
                    List<HashMap> uer_add_friend_list = getUserFriendData(user_add_friend);
                    jsonObject.put("user_add_friend",uer_add_friend_list);
                }

                List<UserFriend> friend_add_user = sqlUtil.getUserAddFriend(username);
                if(!user_add_friend.isEmpty()){
                    List<HashMap> friend_add_user_list = getUserFriendData(friend_add_user);
                    jsonObject.put("friend_add_user",friend_add_user_list);
                }

                //得到自己发给好友信息
                List<UserFriend> userFriendList = sqlUtil.getUserFriend(username);

                //得到用户所有的好友账户和备注
                if(!userFriendList.isEmpty()) {
                    List<HashMap> uer_friend_name = getUserFriendData(userFriendList);
                    jsonObject.put("friend_name", uer_friend_name);

                    List<Message> messageList = sqlUtil.getMessage(username);
                    if(!messageList.isEmpty()){
                        JSONObject friend_message = getMessage(messageList,userFriendList);
                        if(friend_message!=null)
                            jsonObject.put("friend_message",friend_message);
                    }
                }

                System.out.println(jsonObject);
                String user_ip = request.getParameter("user_ip");
                if(sqlUtil.selectUserIP(username)==null) {
                    sqlUtil.addUserIP(username, user_ip);
                }else {
                    sqlUtil.updateUserIP(user_ip,username);
                }
                out.print(jsonObject);
                sqlUtil.updateIsMessage(username);
                sqlSession.getMapper(UpdateUserDataMapper.class).updateUserState(1,username);
            } else {
                out.print("密码错误");
            }
        } else {
            out.println("账户不存在");
        }
        out.flush();
        out.close();
        sqlSession.commit();
        sqlSession.close();
    }


    private JSONObject getMessage(List<Message> messageList,List<UserFriend> userFriendList){
        JSONObject jsonObject = new JSONObject();
        List<String> stringList = new ArrayList<String>();
        for (UserFriend userFriend :userFriendList){
            List<Message> messageList1 = new ArrayList<Message>();
            for(Message message:messageList){
                if(message.getFriend_id().equals(userFriend.getFriend_id())){
                    messageList1.add(message);
                }
            }
            if(!messageList1.isEmpty()) {
                stringList.add(userFriend.getFriend_id());
                JSONArray jsonArray = JSONArray.fromObject(messageList1);
                jsonObject.put(userFriend.getFriend_id(),jsonArray);
            }
        }
        if(!jsonObject.isEmpty()) {
            jsonObject.put("message_friend_id",stringList);
            return jsonObject;
        }
        return null;
    }



    /**
     * 处理得到好友的名称和账户
     * @param userFriendList
     * @return
     */
    private List<HashMap> getUserFriendData(List<UserFriend> userFriendList){

        if(!userFriendList.isEmpty()){
            List<HashMap> hashMapList = new ArrayList<HashMap>();
            for(UserFriend userFriend : userFriendList){
                HashMap<String,String> hashMap = new HashMap<String, String>();
                hashMap.put("friend_id",userFriend.getFriend_id());
                hashMap.put("friend_name",userFriend.getFriend_name());
                hashMapList.add(hashMap);
            }

            return hashMapList;
        }
        return null;

    }



}
