package com.chat.service.impl;

import com.chat.mapper.MessageMapper;
import com.chat.mapper.UserMapper;
import com.chat.pojo.entity.Message;
import com.chat.pojo.entity.User;
import com.chat.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

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
