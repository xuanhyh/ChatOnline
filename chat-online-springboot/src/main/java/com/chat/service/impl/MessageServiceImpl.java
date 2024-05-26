package com.chat.service.impl;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.chat.common.exception.BaseException;
import com.chat.mapper.MessageMapper;
import com.chat.mapper.UserMapper;
import com.chat.pojo.entity.Message;
import com.chat.pojo.entity.User;
import com.chat.service.MessageService;
import com.opencsv.CSVWriter;
import com.opencsv.ICSVWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageMapper messageMapper;

    @Autowired
    UserMapper userMapper;

    @Override
    public List<Message> selectPrivateMessage(Long senderId, Long receiverId){
        log.info("在Impl中获取私聊消息");
        // 获取数据库中 发 和 回 的消息
        List<Message> msgs_send = messageMapper.selectPrivateMessageTextMessage(senderId,receiverId);
        List<Message> msgs_receive = messageMapper.selectPrivateMessageTextMessage(receiverId,senderId);

        // 把两个合起来返回
        msgs_send.addAll(msgs_receive);
        if(msgs_send.isEmpty())
        {
            log.info("消息为空");
        }
        else{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            for (Message message : msgs_send) {
                message.setSendTimeString(message.getSendTime().format(formatter));
                System.out.println(message);
            }
        }
        return msgs_send;
    }

    @Override
    public Long selectUnreadPrivateMessageNum(Long userId, Long friendId){
        //查询对方发给本用户的未读消息数
        return messageMapper.selectUnreadPrivateMessageNum(friendId, userId);
    }

    @Override
    public void updateReadPrivateMessageStatus(Long userId, Long friendId){
        //设置私聊消息已读
        messageMapper.updateReadPrivateMessageStatus(friendId,userId);
    }

    @Override
    public ResponseEntity<Resource> downloadPrivateMessage(Long userId, Long friendId){
        List<Message> messages = selectPrivateMessage(userId, friendId);

        if(messages.isEmpty()){
            log.info("消息记录为空");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        // 根据发送时间排序
        messages = messages.stream()
                .sorted((m1, m2) -> m1.getSendTime().compareTo(m2.getSendTime()))
                .collect(Collectors.toList());
        StringWriter stringWriter = new StringWriter();

        try(CSVWriter csvWriter = new CSVWriter(stringWriter)){
            String[] header = {"messageID", "senderId", "receiverId", "text_message", "sendTime"};
            csvWriter.writeNext(header);

            for (Message message : messages) {
                String[] data = {message.getId().toString(),
                        message.getSenderId().toString(),
                        message.getReceiverId().toString(),
                        message.getTextMessage(),
                        message.getSendTime().toString()};
                csvWriter.writeNext(data);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        byte[] chatData = stringWriter.toString().getBytes(StandardCharsets.UTF_8);
        ByteArrayResource resource = new ByteArrayResource(chatData);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=chat_record.csv")
                .header(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @Override
    public List<Message> selectGroupMessage(Long groupId){
        log.info("在Impl中获取群聊消息");
        List<Message> msgs = messageMapper.selectGroupMessageTextMessage(groupId);

        if(msgs.isEmpty())
        {
            log.info("消息为空");
        }
        else{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            for (Message message : msgs) {
                message.setSendTimeString(message.getSendTime().format(formatter));
                //设置字符串日期
                System.out.println(message);

                //设置发送人昵称
                User user= userMapper.getByUserId(message.getSenderId());
                message.setSenderName(user.getName());
            }
        }

        return msgs;
    }

    @Override
    public Long selectUnreadGroupMessageNum(Long groupId, Long userId){  // 待改，每个人都是一样的未读消息数
        return messageMapper.selectUnreadGroupMessageNum(groupId,userId);
    }

    @Override
    public void updateReadGroupMessageNum(Long groupId, Long userId){  // 待改，每个人都是一样的未读状态
        //设置群聊消息已读
        messageMapper.updateReadGroupMessageNum(groupId,userId);
    }
}
