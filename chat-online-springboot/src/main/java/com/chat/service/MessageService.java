package com.chat.service;

import com.chat.pojo.entity.Message;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.annotations.Select;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface MessageService {

    public Page<Message> getPrivateMessagePages(Long senderId, Long receiverId, int page, int size);

    List<Message> selectPrivateMessage(Long senderId, Long receiverId);

    Long selectUnreadPrivateMessageNum(Long userId, Long friendId);

    void updateReadPrivateMessageStatus(Long userId, Long friendId);

    List<Message> selectGroupMessage(Long groupId);

    Long selectUnreadGroupMessageNum(Long groupId, Long userId);

    void updateReadGroupMessageNum(Long groupId, Long userId);

    ResponseEntity<Resource> downloadPrivateMessage(Long userId, Long friendId);
}
