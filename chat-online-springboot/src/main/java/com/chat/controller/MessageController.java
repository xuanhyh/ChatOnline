package com.chat.controller;

import com.chat.common.result.Result;
import com.chat.pojo.entity.Message;
import com.chat.service.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/message")
@CrossOrigin(origins = "http://localhost:8081") // 允许来自 http://localhost:8081 的跨域请求
@Slf4j
public class MessageController {

    @Autowired
    MessageService messageService;

    @GetMapping("/getPrivateMessages")
    public Result getPrivateMessages(Long senderId, Long receiverId){
        log.info("获取私聊消息：id{}的用户 和 id{}的用户",senderId,receiverId);
        List<Message> msgs = messageService.selectPrivateMessage(senderId,receiverId);
        return Result.success(msgs);
    }

    @GetMapping("/getPrivateMessageUnreadNum")
    public Result getPrivateMessageUnreadNum(Long userId, Long friendId){
        log.info("获取id{}的用户发给id{}的用户未读消息数",friendId,userId);
        Long num = messageService.selectUnreadPrivateMessageNum(userId,friendId);
        return Result.success(num);
    }

    @PatchMapping("/updateReadPrivateMessageStatus")
    public Result updateReadPrivateMessageStatus(Long userId, Long friendId){
        log.info("将id{}的用户发给id{}的用户未读消息设置为已读",friendId,userId);
        messageService.updateReadPrivateMessageStatus(userId,friendId);
        return Result.success();
    }

    @GetMapping("/downloadPrivateMessage")
    public ResponseEntity<Resource> downloadPrivateMessage(Long userId, Long friendId) throws IOException {
        log.info("下载id{}的用户与id{}的用户的聊天记录", userId, friendId);
        ResponseEntity<Resource> resource = messageService.downloadPrivateMessage(userId, friendId);
        return resource;
    }



    //----------------------Group
    @GetMapping("/getGroupMessages")
    public Result getGroupMessages(Long groupId){
        log.info("获取群聊消息：群id{}",groupId);
        List<Message> msgs = messageService.selectGroupMessage(groupId);
        return Result.success(msgs);
    }

    @GetMapping("/getGroupMessageUnreadNum")
    public Result getGroupMessageUnreadNum(Long groupId, Long userId){
        log.info("用户{}获取id{}的群未读消息数",userId,groupId);
        Long num = messageService.selectUnreadGroupMessageNum(groupId, userId);
        log.info("未读消息数为{}",num);
        return Result.success(num);
    }

    @PatchMapping("/updateReadGroupMessageNum")
    public Result updateReadGroupMessageNum(Long groupId, Long userId){
        log.info("将用户{}id为{}的群未读消息设置为已读",userId,groupId);
        messageService.updateReadGroupMessageNum(groupId,userId);
        return Result.success();
    }
}
