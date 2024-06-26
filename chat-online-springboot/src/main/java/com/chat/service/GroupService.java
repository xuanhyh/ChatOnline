package com.chat.service;

import com.chat.pojo.entity.Group;
import com.chat.pojo.vo.GroupSearchVO;
import com.chat.pojo.vo.UserSearchVO;

import java.util.List;

public interface GroupService {
    List<GroupSearchVO> searchGroup(String groupName);

    void createGroup(String groupName);

    List<UserSearchVO> getMembers(Long groupId);

    List<Group> getAllGroupByUserId(Long userId);

    int dissolveGroup(Long userId,Long groupId);

    Group getGroupInfoByGroupId(Long groupId);

    void joinGroup(Long userId, Long groupId);

    void exitGroup(Long userId, Long groupId);

    void changeCreator(Long groupId, Long userId, String username);

    //退出群
    void quitGroup(Long userId,Long groupId);

}
