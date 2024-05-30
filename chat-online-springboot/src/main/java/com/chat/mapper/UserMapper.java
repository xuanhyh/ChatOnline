package com.chat.mapper;

import com.chat.annotation.AutoFill;
import com.chat.common.enumeration.OperationType;
import com.chat.pojo.entity.EmailAndVerifyCode;
import com.chat.pojo.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {


    @Select("select * from user where username = #{username}")
    User getByUsername(String username);

    @Select("select * from user where user_id = #{userId}")
    User getByUserId(Long userId);

    List<User> getFriendById(Long id);

    @Select("select * from user where email = #{email}")
    User checkEmail(String email);

    @Select("select * from email_verification where email = #{email}")
    @Results({
            @Result(property = "email", column = "email"),
            @Result(property = "verifyCode", column = "verification_code")
    })
    EmailAndVerifyCode checkVerifyCode(String email);

    @Insert("""
        INSERT INTO email_verification (email, verification_code)
        VALUES (#{email}, #{verifyCode})
        ON DUPLICATE KEY UPDATE
        verification_code = VALUES(verification_code)""")
    void updateVerifyCode(String email,String verifyCode);

    @Insert("insert into user (user_id, name, username, password, sex, avatar_url, email, state, create_time, update_time) " +
            "values (#{userId}, #{name}, #{username}, #{password}, #{sex}, #{avatarUrl}, #{email}, #{state}, #{createTime}, #{updateTime})")
    @AutoFill(value = OperationType.INSERT)
    void insert(User user);


    @AutoFill(value = OperationType.UPDATE)
    void update(User user);


    List<User> getByIds(List<Long> userIdList);

    void insertDefaultGroup(Long userId);

    //为了好友分组新做的接口
    List<User> getFriendByIdWithGroupingId(Long id);
}
