package com.chat.service.impl;

import com.chat.common.constant.MemberPermissionConstant;
import com.chat.common.constant.MessageConstant;
import com.chat.common.context.BaseContext;
import com.chat.common.exception.*;
import com.chat.mapper.GroupMapper;
import com.chat.mapper.GroupMemberMapper;
import com.chat.mapper.GroupRequestMapper;
import com.chat.mapper.UserMapper;
import com.chat.pojo.entity.Group;
import com.chat.pojo.entity.GroupMember;
import com.chat.pojo.entity.GroupRequest;
import com.chat.pojo.entity.User;
import com.chat.pojo.vo.GroupSearchVO;
import com.chat.pojo.vo.UserSearchVO;
import com.chat.service.GroupService;
import com.chat.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class GroupServiceImpl implements GroupService {


    @Autowired
    GroupMapper groupMapper;

    @Autowired
    GroupMemberMapper groupMemberMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    GroupRequestMapper groupRequestMapper;

    @Override
    public List<GroupSearchVO> searchGroup(String groupName) {
        List<Group> groupList =  groupMapper.getByName(groupName);
        List<GroupSearchVO> groups = new ArrayList<>();
        for(Group g : groupList){
            GroupSearchVO groupSearchVO = new GroupSearchVO();
            BeanUtils.copyProperties(g, groupSearchVO);
            groups.add(groupSearchVO);
        }
        return groups;
    }

    @Override
    public void createGroup(String groupName) {
        Group newGroup = new Group();
        newGroup.setGroupName(groupName);
        groupMapper.insert(newGroup);
        // 在group表中插入之后获取群id
        Long groupId = newGroup.getGroupId();
        Long userId = BaseContext.getCurrentId();
        GroupMember groupMember = new GroupMember();
        groupMember.setGroupId(groupId);
        groupMember.setMemberId(userId);
        groupMember.setMemberPermission(MemberPermissionConstant.CREATOR);
        groupMemberMapper.insert(groupMember);

    }

    @Override
    public List<UserSearchVO> getMembers(Long groupId) {
        List<Long> userIdList = groupMemberMapper.getMembersByGroupId(groupId);
        List<User> userList = userMapper.getByIds(userIdList);
        List<UserSearchVO> list = new ArrayList<>();
        for(User user : userList){
            UserSearchVO userSearchVO = new UserSearchVO();
            BeanUtils.copyProperties(user ,userSearchVO);
            list.add(userSearchVO);
        }
        return list;
    }

    @Override
    public List<Group> getAllGroupByUserId(Long userId){
        List<Long> groupIds = groupMemberMapper.getAllGroupByUserId(userId);
        List<Group> groups= new ArrayList<>();
        for(Long id:groupIds){
            Group newGroup =  groupMapper.getById(id);
            if(newGroup!=null)
            {
                groups.add(newGroup);
            }
        }
        return groups;
    }

    @Override
    public int dissolveGroup(Long userId, Long groupId) {
        Integer memberPermission=groupMemberMapper.getMemberPermissionByGroupId(groupId,userId);
        if(memberPermission==null){//群里无该用户
            return 0;
        }else{
            if(memberPermission.equals(1)){//权限不足
                return -1;
            }else{
                groupMemberMapper.deleteGroupMemberById(groupId);
                groupMapper.deleteGroupById(groupId);
                return 1;
            }
        }

    }

    @Override
    public Group getGroupInfoByGroupId(Long groupId){
        return groupMapper.getById(groupId);
    }

    @Override
    public void joinGroup(Long userId, Long groupId) {
        // 首先查一查该用户是否已经在群里
        GroupMember groupMember = groupMemberMapper.checkUserInGroup(userId, groupId);
        if(groupMember!=null){
            // 说明已经在群里
            throw new UserInGroupException(MessageConstant.UserInGroup);
        }

        // 到这说明用户不在群里，就在group_request表中插入一条记录表示加群请求
        // 首先查一查群主是谁
        // status分为
        // PENDING 请求中
        // ACCEPT 接受请求
        // REJECT 拒绝请求
        Long creatorId = groupMemberMapper.getCreator(groupId);
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setGroupId(groupId);
        groupRequest.setGroupCreatorId(creatorId);
        groupRequest.setFromUserId(userId);
        groupRequest.setStatus("PENDING");
        groupRequestMapper.insert(groupRequest);
    }

    @Override
    public void exitGroup(Long userId, Long groupId) {
        // 首先查询这个人是否在群内以及在这个群内的权限是什么
        GroupMember groupMember = groupMemberMapper.checkUserInGroup(userId, groupId);
        if(groupMember == null){
            throw new UserNotInGroupException("群里不存在该用户！");
        }
        if(groupMember.getMemberPermission().equals(MemberPermissionConstant.CREATOR)){
            // 说明这个人是群主，不能退出群
            throw new UserIsCreatorException("当前用户为群主，不能退出群！");
        }
        groupMemberMapper.deleteMemberById(groupId, userId);

    }

    @Override
    public void changeCreator(Long groupId, Long userId, String username) {
        // 首先检查当前用户是不是群主
        GroupMember groupMember = groupMemberMapper.checkUserInGroup(userId, groupId);
        if(!groupMember.getMemberPermission().equals(MemberPermissionConstant.CREATOR)){
            // 说明这个人不是群主
            throw new UserIsNotCreatorException("当前用户不是群主！");
        }
        // 再检查username是否在群里
        User user = userMapper.getByUsername(username);
        if(user==null){
            throw new AccountNotFoundException("不存在用户名为"+username+"的用户！");
        }
        Long newCreatorId = user.getUserId();
        GroupMember newCreator = groupMemberMapper.checkUserInGroup(newCreatorId, groupId);
        if(newCreator==null){
            // 说明群里没有这个人
            throw new UserNotInGroupException("群里不存在用户名为"+username+"的用户！");
        }
        groupMemberMapper.updatePermission(groupId, userId, MemberPermissionConstant.ORDINARY_MEMBERS);
        groupMemberMapper.updatePermission(groupId, newCreatorId, MemberPermissionConstant.CREATOR);

    }


}
