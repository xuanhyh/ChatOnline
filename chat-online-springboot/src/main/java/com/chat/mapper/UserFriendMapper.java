package com.chat.mapper;

import com.chat.pojo.entity.UserFriend;
import com.chat.pojo.entity.UserGrouping;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserFriendMapper {

    void delete(Long userId, Long friendId);

    void update(UserFriend userFriend);
    // 新建好友分组
    void createNewGroup(Long userId, String groupingName);
    // 修改好友分组
    void changeFriendGrouping(Long userId, Long friendId, String groupingName);
    // 查找好友分组 fromUserId为好友id
    String searchFriendGroup(Long fromUserId, Long toUserId);

    String searchUserGrouping(Long userId, String groupingName);
}
