package com.chat.service.impl;

import com.chat.common.constant.MessageConstant;
import com.chat.common.context.BaseContext;
import com.chat.common.exception.BaseException;
import com.chat.common.exception.FriendExistedException;
import com.chat.mapper.FriendRequestMapper;
import com.chat.mapper.UserFriendMapper;
import com.chat.mapper.UserMapper;
import com.chat.pojo.dto.FriendGroupChangeDTO;
import com.chat.pojo.dto.FriendRequestDTO;
import com.chat.pojo.entity.FriendGroup;
import com.chat.pojo.entity.FriendRequest;
import com.chat.pojo.entity.User;
import com.chat.service.UserFriendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserFriendServiceImpl implements UserFriendService {
    @Autowired
    FriendRequestMapper friendRequestMapper;
    @Autowired
    UserFriendMapper userFriendMapper;

    @Override
    public void sendFriendRequest(FriendRequestDTO friendRequestDTO){
        // 发送添加好友请求
        Long fromUserId = BaseContext.getCurrentId();
        if(fromUserId == friendRequestDTO.getToUserId()){
            throw new FriendExistedException(MessageConstant.FRIENF_EXISTED);
        }
        FriendRequest friendRequest = new FriendRequest();
        BeanUtils.copyProperties(friendRequestDTO, friendRequest);
        friendRequest.setFromUserId(fromUserId);
        friendRequest.setStatus("PENDING");
        friendRequestMapper.insertFriendReuqest(friendRequest);
    }

    @Override
    public List<FriendRequest> getFriendRequest(){
        // 获取当前用户接收到的好友请求
        Long toUserId = BaseContext.getCurrentId();
        List<FriendRequest> list = friendRequestMapper.getUserById(toUserId);
        return list;
    }

    @Override
    public void respongToFriendRequest(Long fromUserId, String status){
        // 当前用户是toUser，接受请求方
        Long toUserId = BaseContext.getCurrentId();
        // 更新当前用户对好友请求的操作
        friendRequestMapper.updateFriendRequestStatus(toUserId, fromUserId, status);

        if(status.equals("ACCEPT")) {
            // 添加进好友表
            friendRequestMapper.addFriends(toUserId, fromUserId);
            friendRequestMapper.addFriendsReverse(toUserId, fromUserId);
            // 添加好友进入对方的默认分组
            FriendGroup friendGroup = new FriendGroup(toUserId, fromUserId, "default");
            userFriendMapper.addFriendToGroup(friendGroup);
            friendGroup.setUserId(fromUserId); friendGroup.setMemberId(toUserId);
            userFriendMapper.addFriendToGroup(friendGroup);
        }
    }

    @Override
    public void changeFriendGroup(FriendGroupChangeDTO friendGroupChangeDTO){
        // 当前用户是toUser
        Long userId = BaseContext.getCurrentId();
        Long memberId = friendGroupChangeDTO.getMemberId();
        String groupName = friendGroupChangeDTO.getGroupName();
        // 获取当前好友分组
        String nowGroupName = userFriendMapper.searchFriendGroup(memberId, userId);
        if(nowGroupName.equals(groupName)){
            throw new BaseException("好友已在当前分组");
        }
        // 更换好友分组
        FriendGroup friendGroup = new FriendGroup(userId, memberId, groupName);
        userFriendMapper.addFriendToGroup(friendGroup);
        friendGroup.setGroupName(nowGroupName);
        userFriendMapper.deleteFriendFromGroup(friendGroup);
    }
}
