package com.chat.mapper;

import com.chat.pojo.dto.GroupMessageDTO;
import com.chat.pojo.dto.MessageDTO;
import com.chat.pojo.entity.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface MessageMapper {
    @Insert("insert into message (chat_flag, content_flag, text_message, picture_message, video_message, sender_id, send_time, receiver_id, receive_group_id, received) " +
            "values (0, 0, #{message}, null, null, #{fromId}, #{time}, #{toId}, null, false)")
    void insertPrivateMessageTextMessage(MessageDTO message);

    List<Message> selectPrivateMessageTextMessage(Long senderId, Long receiverId);

    @Select("select count(*) " +
            "from message " +
            "where sender_id=#{senderId} and receiver_id=#{receiverId} and received=false")
    Long selectUnreadPrivateMessageNum(Long senderId, Long receiverId);

    @Update("update message "+
            "set received = true "+
            "where sender_id = #{senderId} and receiver_id = #{receiverId} and received = false")
    void updateReadPrivateMessageStatus(Long senderId, Long receiverId);



    @Insert("insert into message (chat_flag, content_flag, text_message, picture_message, video_message, sender_id, send_time, receiver_id, receive_group_id, received) " +
            "values (1, 0, #{message}, null, null, #{fromId}, #{time}, null, #{toGroupId}, false)")
    void insertGroupMessageTextMessage(GroupMessageDTO groupMessage);

    List<Message> selectGroupMessageTextMessage(Long groupId);

    @Select("select unread_num " +
            "from group_unread_num " +
            "where group_id=#{groupId} and user_id=#{userId}")
    Long selectUnreadGroupMessageNum(Long groupId, Long userId);

    @Update("update group_unread_num "+
            "set unread_num = 0 "+
            "where group_id = #{groupId} and user_id=#{userId}")
    void updateReadGroupMessageNum(Long groupId, Long userId);

    @Update("update group_unread_num "+
            "set unread_num = unread_num + 1 "+
            "where group_id = #{groupId}")
    void updateUnreadMessageNum(Long groupId);

    @Insert("insert group_unread_num(group_id,user_id,unread_num) values(#{groupId},#{userId},0)")
    void addNewItem(Long groupId, Long userId);// 在用户加入群后应该使用这个新建记录
}
