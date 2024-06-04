package com.chat.service.impl;

import com.chat.common.constant.MemberPermissionConstant;
import com.chat.mapper.GroupMemberMapper;
import com.chat.mapper.GroupRequestMapper;
import com.chat.pojo.dto.GroupRequestDTO;
import com.chat.pojo.entity.GroupMember;
import com.chat.pojo.entity.GroupRequest;
import com.chat.pojo.vo.GroupRequestVO;
import com.chat.pojo.vo.UserGroupRequestVO;
import com.chat.service.GroupRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class GroupRequestServiceImpl implements GroupRequestService {
    @Autowired
    GroupRequestMapper groupRequestMapper;

    @Autowired
    GroupMemberMapper groupMemberMapper;

    @Override
    public List<GroupRequestVO> getJoinRequest(Long userId) {
        // 在group_request表中查询group_creator_id为userId并且status为PENDING的记录
        List<GroupRequest> list1 = groupRequestMapper.getJoinRequest(userId);
        List<GroupRequestVO> list2 = new ArrayList<>();
        for (GroupRequest groupRequest : list1) {
            GroupRequestVO groupRequestVO = new GroupRequestVO();
            BeanUtils.copyProperties(groupRequest, groupRequestVO);
            list2.add(groupRequestVO);
            // VO中只包含申请人的id和申请的群的id
        }
        return list2;
    }

    @Override
    public void handleGroupRequest(GroupRequestDTO groupRequestDTO) {
        // 如果是ACCEPT，就先更新group_request表中的记录
        // 然后在group_member表中添加申请人
        // 如果是REJECT，就只更新group_request表中的记录

        groupRequestMapper.update(groupRequestDTO);
        if(groupRequestDTO.getStatus().equals("ACCEPT")){
            GroupRequest groupRequest = groupRequestMapper.getById(groupRequestDTO.getGroupRequestId());
            // 获取申请人的id、申请加入的群的id
            Long fromUserId = groupRequest.getFromUserId();
            Long groupId = groupRequest.getGroupId();
            // 在group_member表中添加申请人
            GroupMember groupMember = new GroupMember();
            groupMember.setGroupId(groupId);
            groupMember.setMemberId(fromUserId);
            groupMember.setMemberPermission(MemberPermissionConstant.ORDINARY_MEMBERS);
            log.info("即将在group_member表中插入记录：{}", groupMember);
            groupMemberMapper.insert(groupMember);
            log.info("插入成功！");
        }
    }

    @Override
    public List<UserGroupRequestVO> getByFromUserId(Long userId) {
        List<GroupRequest> list1 = groupRequestMapper.getByFromUserId(userId);
        List<UserGroupRequestVO> list2 = new ArrayList<>();
        for (GroupRequest groupRequest : list1) {
            UserGroupRequestVO userGroupRequestVO = new UserGroupRequestVO();
            BeanUtils.copyProperties(groupRequest, userGroupRequestVO);
            list2.add(userGroupRequestVO);
        }
        return list2;
    }
}
