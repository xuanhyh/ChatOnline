package com.chat.service.impl;

import com.chat.common.constant.MemberPermissionConstant;
import com.chat.common.context.BaseContext;
import com.chat.mapper.GroupMapper;
import com.chat.mapper.GroupMemberMapper;
import com.chat.mapper.UserMapper;
import com.chat.pojo.entity.Group;
import com.chat.pojo.entity.GroupMember;
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


}
