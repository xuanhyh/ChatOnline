package com.chat.mapper;

import com.chat.pojo.dto.UpdateImpressionDTO;
import com.chat.pojo.entity.UserImpression;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserImpressionMapper {
    @Select("select * from chat_online.user_impression where user_id = #{userId}")
    List<UserImpression> select(Long userId);

    @Insert("insert into user_impression (user_id, content_flag, impression_text, impression_picture, sender_id) " +
            "VALUES (#{userId}, #{contentFlag}, #{impressionText}, #{impressionPicture}, #{senderId})")
    void insert(UserImpression userImpression);



    @Delete("delete from user_impression where impression_id = #{impressionId}")
    void delete(Long impressionId);

    @Select("select * from user_impression where impression_id = #{impressionId}")
    UserImpression getByImpressionId(Long impressionId);


    void update(UpdateImpressionDTO updateImpressionDTO);
}
