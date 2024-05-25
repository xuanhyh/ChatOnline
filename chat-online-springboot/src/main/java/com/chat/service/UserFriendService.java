package com.chat.service;

import com.chat.pojo.dto.FriendGroupChangeDTO;
import com.chat.pojo.dto.FriendRequestDTO;
import com.chat.pojo.entity.FriendRequest;
import com.chat.pojo.entity.User;

import java.util.List;

public interface UserFriendService {
    void sendFriendRequest(FriendRequestDTO friendRequestDTO);

    List<FriendRequest> getFriendRequest();

    void respongToFriendRequest(Long requestId, String status);

    void changeFriendGroup(FriendGroupChangeDTO friendGroupChangeDTO);
}
