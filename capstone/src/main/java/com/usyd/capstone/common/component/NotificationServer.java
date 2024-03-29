package com.usyd.capstone.common.component;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.usyd.capstone.entity.Notification;
import com.usyd.capstone.mapper.MessageMapper;
import com.usyd.capstone.mapper.NotificationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author websocket服务
 */


@ServerEndpoint(value = "/notification/{userId}")
@Component
public class NotificationServer {


    /**
     * 用来解决webSocket中无法注入mapper
     */
    private static ApplicationContext applicationContext;


    public static void setApplicationContext(ApplicationContext applicationContext) {
        NotificationServer.applicationContext = applicationContext;
    }

    private static final Logger log = LoggerFactory.getLogger(NotificationServer.class);
    /**
     * 记录当前在线连接数
     */
    public static final Map<Integer, Session> sessionMap = new ConcurrentHashMap<>();


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Integer userId) {
        sessionMap.put(userId, session);
    }
    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session, @PathParam("userId") Integer userId) {
        sessionMap.remove(userId);
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("userId") Integer userId) {

        log.info("服务端收到用户userEmail={}的消息:{}", userId, message);
        JSONObject obj = JSONUtil.parseObj(message);

        // 收到消息的反馈
        Integer notificationID = obj.getInt("notificationId");
        Integer isRead = obj.getInt("isRead");

        if (isRead == 1){
              // 更新数据库消息状态
                NotificationMapper notificationMapper = applicationContext.getBean(NotificationMapper.class);
                Notification notification = notificationMapper.selectById(notificationID);
                notification.setIsRead(1);
                notificationMapper.updateById(notification);
        }


    }
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }
    /**
     * 服务端发送消息给客户端
     */
    public static void sendMessage(String message, Integer userId) {

        try {
            // 如果用户处于在线状态就进行推送
            if (sessionMap.get(userId)!=null){
                sessionMap.get(userId).getBasicRemote().sendText(message);


            }
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }

}

