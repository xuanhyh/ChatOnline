package com.chat.mapper;

import com.chat.annotation.AutoFill;
import com.chat.common.enumeration.OperationType;
import com.chat.pojo.entity.FriendRequest;
import com.chat.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FriendRequestMapper {

    void insertFriendReuqest(FriendRequest friendRequest);

    // 查询当前用户的未处理所有好友请求
    List<FriendRequest> getUserById(Long toUserId);

    // 检查好友是否存在
    @Select("SELECT friend_id from user_friend where user_id = #{userId} and friend_id = #{friendId}")
    Long checkFriendExisted(Long userId, Long friendId);
    void updateFriendRequestStatus(Long toUserId, Long fromUserId, String status);

    // 查询用户请求是否存在
    List<FriendRequest> checkRequestExisted(Long fromUserId, Long toUserId);

    // 为双方添加好友
    void addFriends(Long fromUserId, Long toUserId);
    void addFriendsReverse(Long fromUserId, Long toUserId);

}
