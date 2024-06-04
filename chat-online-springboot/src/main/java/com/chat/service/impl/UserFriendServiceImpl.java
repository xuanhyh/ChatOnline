package com.chat.service.impl;

import com.chat.common.constant.MessageConstant;
import com.chat.common.context.BaseContext;
import com.chat.common.exception.BaseException;
import com.chat.common.exception.FriendExistedException;
import com.chat.mapper.FriendRequestMapper;
import com.chat.mapper.UserFriendMapper;
import com.chat.pojo.dto.FriendGroupChangeDTO;
import com.chat.pojo.dto.FriendRequestDTO;
import com.chat.pojo.entity.FriendRequest;
import com.chat.pojo.entity.UserGrouping;
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
        Long toUserId = friendRequestDTO.getToUserId();
        Long friendId = friendRequestMapper.checkFriendExisted(fromUserId, toUserId);
        if(fromUserId == toUserId || friendId != null){
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
    public void createANewGrouping(String groupingName){
        Long userId = BaseContext.getCurrentId();
        String checkExisted = userFriendMapper.searchUserGrouping(userId, groupingName);
        if(checkExisted != null){
            throw new BaseException("分组已存在");
        }
        userFriendMapper.createNewGroup(userId, groupingName);
    }
    @Override
    public void respongToFriendRequest(Long fromUserId, String status){
        // status分为
        // PENDING 好友请求中
        // ACCEPT 接受好友请求
        // REJECT 拒绝好友请求
        // 当前用户是toUser，接受请求方
        Long toUserId = BaseContext.getCurrentId();
        // 更新当前用户对好友请求的操作
        friendRequestMapper.updateFriendRequestStatus(toUserId, fromUserId, status);

        if(status.equals("ACCEPT")) {
            // 添加进好友表
            friendRequestMapper.addFriends(toUserId, fromUserId);
            friendRequestMapper.addFriendsReverse(toUserId, fromUserId);
        }
    }

    @Override
    public void changeFriendGroup(FriendGroupChangeDTO friendGroupChangeDTO){
        // 当前用户是toUser
        Long userId = BaseContext.getCurrentId();
        Long friendId = friendGroupChangeDTO.getFriendId();
        String groupingName = friendGroupChangeDTO.getGroupName();
        // 获取当前好友分组
        String nowGroupName = userFriendMapper.searchFriendGroup(friendId, userId);
        if(nowGroupName != null && nowGroupName.equals(groupingName)){
            throw new BaseException("好友已在当前分组");
        }
        // 修改好友分组
        userFriendMapper.changeFriendGrouping(userId, friendId, groupingName);
    }
}
