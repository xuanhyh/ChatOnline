package com.chat.mapper;

import com.chat.pojo.entity.FriendGroup;
import com.chat.pojo.entity.UserFriend;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserFriendMapper {

    void delete(Long userId, Long friendId);

    void update(UserFriend userFriend);
    // 新建好友分组
    void createNewGroup(Long userId, String groupName);
    // 添加好友分组
    void addFriendToGroup(FriendGroup friendGroup);
    // 从好友分组中删除
    void deleteFriendFromGroup(FriendGroup friendGroup);
    // 查找好友分组 fromUserId为好友id
    String searchFriendGroup(Long fromUserId, Long toUserId);
}
