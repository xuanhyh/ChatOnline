package com.chat.ws;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.chat.pojo.dto.SignalDTO;
import com.chat.common.utils.DecoderUtil;
import com.chat.common.utils.EncoderUtil;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 感谢wangxianlin
 *
 * */

@Slf4j
@Component
@ServerEndpoint(value = "/sip/{userId}/{toUserId}", encoders = {EncoderUtil.class}, decoders = {DecoderUtil.class})
public class WebSocketServer {

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 接收userId
     */
    private String userId = "";



    /**
     * 连接建立成功调用的方法，成功建立之后，将用户的userName 存储到redis
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        this.userId = userId;
        webSocketMap.put(userId, this);
        addOnlineCount();
        log.info("用户加入:{},当前在线人数为:{}", userId, getOnlineCount());
        webSocketMap.forEach((key, value) -> {
            System.out.println("Key: " + key + ", Value: " + value);
        });
    }


    /**
     * 收到客户端消息后调用的方法, 调用API接口 发送消息
     */
    @OnMessage
    public void onMessage(String message, @PathParam("userId") String userId) {
        log.info("用户消息:" + userId + ",报文:" + message);
        if (!("").equals(message)) {
            JSONObject jsonObject = JSON.parseObject(message);
            String type = jsonObject.getString("type");
            System.out.println("userId:"+userId+" targetId:"+jsonObject.getString("targetUid"));
            String targetUid = jsonObject.getString("targetUid");
            // offer
            if (Objects.equals(type, "offer")) {
                oneToOne(targetUid, new SignalDTO("offer", "rtc offer", 200, jsonObject));
                System.out.println("offer");
            }
            // 远程呼叫
            else if (Objects.equals(type, "call")) {
                oneToOne(targetUid, new SignalDTO("call", "远程呼叫", 200, jsonObject));
                System.out.println("call");
            }
            // 对方已接听
            else if (Objects.equals(type, "accept")) {
                oneToOne(targetUid, new SignalDTO("accept", "对方已接听", 200, jsonObject));
                System.out.println("accept");
            }
            // 对方拒绝通话
            else if (Objects.equals(type, "reject")) {
                oneToOne(targetUid, new SignalDTO("reject", "对方拒绝通话", 200, jsonObject));
                System.out.println("reject");
            }
            // candidate
            else if (Objects.equals(type, "candidate")) {
                oneToOne(targetUid, new SignalDTO("candidate", "ice candidate", 200, jsonObject));
                System.out.println("candidate");
                System.out.println("userId:"+userId+" targetId:"+jsonObject.getString("targetUid"));
            }
            // answer
            else if (Objects.equals(type, "answer")) {
                oneToOne(targetUid, new SignalDTO("answer", "rtc answer", 200, jsonObject));
                System.out.println("answer");
            }
        }
    }

    /**
     * 推送消息给个人
     *
     * @param to
     * @param data
     */
    public void oneToOne(String to, SignalDTO data) {
        System.out.println("targetId:"+to);
        Session session = webSocketMap.containsKey(to) ? webSocketMap.get(to).session : null;
        System.out.println("通过targetId找到的Session:"+session);
        if (session != null && session.isOpen()) {
            try {
                // 为了避免并发情况下造成异常
                synchronized (session) {
                    session.getBasicRemote().sendObject(data);
                }
            } catch (IOException e) {
                log.error("消息发送IO异常：[{}]", e.toString());
            } catch (EncodeException e) {
                log.error("消息发送Encode异常：[{}]", e.toString());
            }
        } else {
            log.warn("用户：[{}]-->不在线", to);
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            //从set中删除
            subOnlineCount();
        }
        log.info("用户退出:" + userId + ",当前在线人数为:" + getOnlineCount());
        webSocketMap.forEach((key, value) -> {
            System.out.println("Key: " + key + ", Value: " + value);
        });
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:" + this.userId + ",原因:" + error.getMessage());
        log.info(error.toString());
        error.printStackTrace();
    }

    /**
     * 获取在线人数
     */
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    /**
     * 在线人数+1
     */
    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    /**
     * 在线人数-1
     */
    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}
