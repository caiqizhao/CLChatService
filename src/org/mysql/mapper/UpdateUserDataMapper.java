package org.mysql.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface  UpdateUserDataMapper {

    /**
     * 更改网名
     * @param user_name
     * @param user_id
     */
    @Update("update user set user_name=#{user_name} where user_id=#{user_id}")
    void updateUserName(@Param("user_name")String user_name, @Param("user_id")String user_id);


    /**
     * 更改性别资料
     * @param user_sex
     * @param user_id
     */
    @Update("update user set user_sex=#{user_sex} where user_id=#{user_id}")
    void updateUserSex(@Param("user_sex")String user_sex,@Param("user_id")String user_id);

    /**
     * 更改密码
     * @param user_password
     * @param user_id
     */
    @Update("update user set user_password=#{user_password} where user_id=#{user_id}")
    void updateUserPassword(@Param("user_password")String user_password,@Param("user_id")String user_id);


    /**
     * 更新个性签名
     * @param user_introduce
     * @param user_id
     */
    @Update("update user set user_introduce=#{user_introduce} where user_id=#{user_id}")
    void updateUserIntroduce(@Param("user_introduce") String user_introduce, @Param("user_id") String user_id);

    /**
     * 更新用户的登陆状态
     * @param user_state
     * @param user_id
     */
    @Update("update user set user_state=#{user_state} where user_id=#{user_id}")
    void updateUserState(@Param("user_state")int user_state,@Param("user_id")String user_id);
}
