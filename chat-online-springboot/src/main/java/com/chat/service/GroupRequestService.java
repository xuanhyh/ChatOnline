package com.chat.service;

import com.chat.pojo.dto.GroupRequestDTO;
import com.chat.pojo.vo.GroupRequestVO;
import com.chat.pojo.vo.UserGroupRequestVO;

import java.util.List;

public interface GroupRequestService {
    List<GroupRequestVO> getJoinRequest(Long userId);

    void handleGroupRequest(GroupRequestDTO groupRequestDTO);

    List<UserGroupRequestVO> getByFromUserId(Long userId);

}
