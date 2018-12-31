package org.mysql.mapper;

import org.apache.ibatis.annotations.*;
import org.mysql.entity.Message;
import org.mysql.entity.User;
import org.mysql.entity.UserFriend;
import org.mysql.entity.UserIP;

import java.util.List;

public interface MySQLUserMapper {

    /**
     * 获得用户信息
     * @param user_id
     * @return
     */
    @Select("select * from user where user_id=#{user_id}")
    User select_user(@Param("user_id") String user_id);


    /**
     * 获取有好友关系的用户
     * @param user_id
     * @return
     */
    @Select("select * from user_friend where user_id=#{user_id} and is_friend=1 ")
    List<UserFriend> getUserFriend(@Param("user_id") String user_id);



    /**
     * 获得待确定好友关系的用户(用户添加朋友)
     * @param user_id
     * @return
     */
    @Select("select * from user_friend where user_id=#{user_id} and is_friend=0 ")
    List<UserFriend> getUserAddFriend(@Param("user_id") String user_id);


    /**
     * 获得待确定好友关系的用户(朋友添加用户)
     * @param user_id
     * @return
     */
    @Select("select * from user_friend where friend_id=#{user_id} and is_friend=0 ")
    List<UserFriend> getFriendAddUser(@Param("user_id") String user_id);


    /**
     * 得到用户跟他人是否存在待确定关系
     * @param user_id
     * @param friend_id
     * @return
     */
    @Select("select count(*) from user_friend where friend_id=#{user_id} and user_id=#{friend_id} )")
    int getFriendUser(@Param("user_id") String user_id,@Param("friend_id") String friend_id);


    /**
     * 添加好友
     * @param user_id
     * @param friend_id
     * @param is_friend
     */
    @Insert("insert into user_friend(user_id,friend_id,friend_name,is_friend)" +
            "value(#{user_id},#{friend_id},#{friend_id},#{is_friend})")
    void addUserFriend(@Param("user_id")String user_id,@Param("friend_id")String friend_id,@Param("is_friend") int is_friend);


    /**
     * 更新好友关系关系
     * @param user_id
     * @param friend_id
     */
    @Update("update uer_friend set is_friend=1 where user_id=#{user_id} and friend_id=#{friend_id}")
    void updateUserFriend(@Param("user_id")String user_id,@Param("friend_id")String friend_id);




    /**
     * 注册用户
     * @param user_id
     * @param user_password
     */
    @Insert("insert into user(user_id,user_name,user_password,user_state) " +
            "value(#{user_id},#{user_id},#{user_password},0)")
    void addUser(@Param("user_id") String user_id,@Param("user_password") String user_password);



    /**
     * 登陆成功后将所有未读消息全部转换为已读
     * @param user_id
     */
    @Update("update message set message_state=1 where user_id=#{user_id}")
    void updateIsMessage(@Param("user_id")String user_id);

    /**
     * 获取聊天记录
     * @param user_id
     * @return
     */
    @Select("select * from message where user_id=#{user_id}")
    List<Message> getMessage(@Param("user_id") String user_id);

//    /**
//     * 用户如果接收信息后修改消息的状态
//     * @param message_no
//     */
//    @Update("update message set message_state=1 where message_no=#{message_no}")
//    void updateIsMessage(@Param("message_no")int message_no);

    /**
     * 发送消息保存在数据库中
     * @param user_id
     * @param friend_id
     * @param message
     * @param message_state
     * @param put_id
     * @param time
     */
    @Insert("insert into message(user_id,friend_id,message,message_state,put_id,time)" +
            "value(#{user_id},#{friend_id},#{message},#{message_state},#{put_id},#{time}")
    void addMessage(@Param("user_id")String user_id,@Param("friend_id")String friend_id,
                    @Param("message")String message,@Param("message_state")int message_state,
                    @Param("put_id")int put_id, @Param("time") String time);


    /**
     * 登陆时对用户状态表进行插入
     * @param user_id
     * @param user_ip
     */
    @Insert("insert into is_user(user_id,user_ip) " +
            "value(#{user_id},#{user_ip})")
    void addUserIP(@Param("user_id")String user_id,@Param("user_ip")String user_ip);


    /**
     * 查看当前好友是否在线
     * @param user_id
     * @return
     */
    @Select("select * from is_user where user_id=#{user_id}")
    UserIP selectUserIP(@Param("user_id")String user_id);


    /**
     * 更新ip地址
     * @param user_ip
     * @param user_id
     */
    @Update("update is_user set user_ip=#{user_ip} where user_id=#{user_id}")
    void updateUserIP(@Param("user_ip")String user_ip,@Param("user_id")String user_id);


    @Delete("delete from is_user where user_id=#{user_id}")
    void deleteUserIP(@Param("user_id")String user_id);


}
