package com.usyd.capstone.common.component;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.usyd.capstone.entity.MessageDB;
import com.usyd.capstone.mapper.MessageMapper;
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
        JSONObject obj = JSONUtil.parseObj(message);

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

