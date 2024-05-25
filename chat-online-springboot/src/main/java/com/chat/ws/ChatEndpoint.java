package com.chat.ws;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.chat.common.context.BaseContext;
import com.chat.common.utils.MessageUtil;
import com.chat.config.SpringContextHolder;
import com.chat.mapper.GroupMemberMapper;
import com.chat.mapper.MessageMapper;
import com.chat.pojo.dto.GroupMessageDTO;
import com.chat.pojo.dto.MessageDTO;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@ServerEndpoint("/api/chat/{id}")//指定了 WebSocket 服务器端点的路径为 "/api/chat"，Endpoint是一个java对象，代表websocket链接的一端，握手时创建
public class ChatEndpoint {

    private static final Map<Long, Session> onlineUsers = new ConcurrentHashMap<>();
    //用于存储在线用户，其中键是用户的 ID，值是用户的 WebSocket 会话 Session。ConcurrentHashMap 线程安全

    private MessageMapper messageMapper;

    private GroupMemberMapper groupMemberMapper;

    /*
    * WebSocket 端点类不是由 Spring 容器直接管理的，
    * 而是由 WebSocket 容器（例如 Tomcat、Jetty 等）管理的。
    * 这通常会导致 Spring 无法自动注入依赖项。
    * */

    /**
     * 建立
     * @param session
     */
    @OnOpen//OnOpen客户端与服务器端建立连接时触发。在客户端与服务器端建立连接时，将当前用户的 ID 和 WebSocket 会话保存到 onlineUsers 中，并向所有在线用户广播当前在线用户列表。
    public void onOpen(@PathParam("id") Long id, Session session){
        log.info("OnOpen时id为：{}",id);
        // 1. 将session进行保存
        if(id!=null&&session!=null)
        {
            onlineUsers.put(id, session);
            // 2.广播消息：将已登录的用户id推送给登录的用户
            String message = MessageUtil.getMessage(true, null, getFriends());
            broadcastAllUsers(message);
        }
        else{
            log.info("id或session为空");
        }
    }
    private Set getFriends(){
        return onlineUsers.keySet();
    }
    //获取当前在线用户的 ID 集合

    private void broadcastAllUsers(String message){

        // 遍历map集合
        Set<Map.Entry<Long, Session>> entries = onlineUsers.entrySet();
        for(Map.Entry<Long, Session> entry : entries){
            Session session = entry.getValue();
            try {
                // 发送消息到客户端
                session.getBasicRemote().sendText(message);
                // 服务器端使用 Session 对象来管理与每个客户端的连接，Session 对象包含连接的相关信息，例如连接的 ID、远程地址等
                // 发送消息由RemoteEndpoint完成，其实例是session维护的，session.getBasicRemote()获取发送消息的实例（同步），session.getAsyncRemote()异步
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnMessage//OnMessage客户端发送消息时触发。在接收到客户端的消息后，解析消息中的 JSON 数据，并根据消息中的目标用户 ID 找到对应的 WebSocket 会话，然后将消息发送给目标用户。
    public void onMessage(String message){
        log.info("当前进程的id为"+BaseContext.getCurrentId());
        MessageDTO msg = new MessageDTO();
        GroupMessageDTO groupmsg = new GroupMessageDTO();
        Boolean isGroupMessage = false;

        //尝试解析为私聊消息
        msg = JSON.parseObject(message, MessageDTO.class);
        if(msg.getToId()!=null)
        {
            // 如果解析成功，标志不是群聊消息，是私聊消息
            isGroupMessage = false;
        }
        else {
            // 如果解析失败，解析为群聊消息
            isGroupMessage = true;
            groupmsg = JSON.parseObject(message, GroupMessageDTO.class);
        }

        log.info("是否是群聊消息：{}", isGroupMessage);
        if (!isGroupMessage) {
            //是私聊消息
            Long toId = msg.getToId();
            String message_sendTo = msg.getMessage(); // 获取消息内容
            String name = msg.getName(); // 获取消息发送者名称
            String time = msg.getTime(); // 获取消息发送时间
            Long fromId = msg.getFromId(); // 获取消息发送者的ID

            Session session = onlineUsers.get(toId);
            //        String msg_Json = MessageUtil.getMessage(false, fromId, message_sendTo);
            String msg_Json = MessageUtil.getMessage(false,false, fromId, toId, null,message_sendTo, name, time);
            try {
                if (session != null) {
                    session.getBasicRemote().sendText(msg_Json);
                } else {
                    log.info("id为{}的用户不在线", toId);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (messageMapper == null) {
                    messageMapper = SpringContextHolder.getApplicationContext().getBean(MessageMapper.class);
                }
                messageMapper.insertPrivateMessageTextMessage(msg);
                log.info("插入私聊文本消息：{}", msg);
            }
        } else {
            //是群聊消息
            Long toGroupId = groupmsg.getToGroupId();
            String message_sendTo = groupmsg.getMessage(); // 获取消息内容
            String name = groupmsg.getName(); // 获取消息发送者名称
            String time = groupmsg.getTime(); // 获取消息发送时间
            Long fromId = groupmsg.getFromId(); // 获取消息发送者的ID

            // 下面将消息发给所有群聊内在线成员
            if (groupMemberMapper == null) {
                groupMemberMapper = SpringContextHolder.getApplicationContext().getBean(GroupMemberMapper.class);
            }
            List<Long> memberIds = groupMemberMapper.getMembersByGroupId(toGroupId);
            List<Session> sessions = new ArrayList<>();
            for (Long id : memberIds) {
                if(id!=fromId){ //不要再发给自己
                    Session session = onlineUsers.get(id);
                    if (session != null) {
                        sessions.add(session);
                    }
                }
            }

            String msg_Json = MessageUtil.getMessage(false, true, fromId, null, toGroupId, message_sendTo, name, time);
            try {
                for (Session session : sessions) { // 发给所有在线好友
                    session.getBasicRemote().sendText(msg_Json);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // 将消息插入数据库
                if (messageMapper == null) {
                    messageMapper = SpringContextHolder.getApplicationContext().getBean(MessageMapper.class);
                }
                messageMapper.insertGroupMessageTextMessage(groupmsg);
                log.info("插入群聊文本消息：{}", groupmsg);
                messageMapper.updateUnreadMessageNum(toGroupId);
                log.info("群{}所有成员未读消息数加一",toGroupId);
            }

        }
    }

    @OnClose//OnClose在客户端与服务器端断开连接时触发。将断开连接的用户从 onlineUsers 中移除，并向所有在线用户广播当前在线用户列表。
    public void onClose(@PathParam("id") Long id,Session session){
        log.info(id+"为id的用户断开");
        if(onlineUsers.containsKey(id))
        {
            onlineUsers.remove(id);
        }
        else{
            log.info("id为"+id+"的用户本来没连接但发生断开");
        }
        String message = MessageUtil.getMessage(true, null, getFriends());
        broadcastAllUsers(message);
    }
}

/*
* 消息推送常见方式
* 轮询：浏览器以指定的时间间隔向服务器发出HTTP请求，服务器实时返回数据给浏览器，这样总会有延迟
* 长轮询：异步，浏览器发出ajax请求，服务器端接收到请求后，会阻塞请求直到有数据或者超时才返回。轮询1间隔可以设置的比轮询长，服务器压力小
* SSE服务器发送事件：打开服务器到客户端的单项通道，发text/event-stream的数据流，变更时实时传输
* websocket:就是一种全双工协议，客户端先用HTTP请求切换为websocket（Connection:Upgrade,Upgrade:websocket）（握手），响应后101切换为websocket
* */
