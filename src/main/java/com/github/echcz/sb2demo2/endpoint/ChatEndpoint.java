package com.github.echcz.sb2demo2.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@Slf4j
@ServerEndpoint("/chat")
public class ChatEndpoint implements Endpoint {
    private static ConcurrentMap<String, Session> sessionMap = new ConcurrentHashMap<>();

    @OnOpen
    @Override
    public void onOpen(Session session) {
        sessionMap.put(session.getId(), session);
        sendMsg(String.format("连接成功, 当前连接数: %d; 请设置您的昵称(格式:name=****):", sessionMap.size()), session);
        log.info("{}: 连接成功", session.getId());
    }

    @OnMessage
    @Override
    public void onMessage(String message, Session session) {
        log.info("{} 发送: {}", session.getId(), message);
        broadcastMsg(message, session.getId());
    }

    @OnClose
    @Override
    public void onClose(Session session) {
        sessionMap.remove(session.getId());
        log.info("{}: 连接关闭", session.getId());
    }

    @OnError
    @Override
    public void onError(Session session, Throwable error) {
        log.warn("{} 发生错误: {}", session.getId(), error.getMessage());
    }

    private void sendMsg(String msg, Session session) {
        try {
            session.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            log.warn("给 {} 推送消息时发生错误: {}", session.getId(), e.getMessage());
        }
    }

    private void broadcastMsg(String msg, String excluded) {
        for (Map.Entry<String, Session> entry : sessionMap.entrySet()) {
            if (!Objects.equals(excluded, entry.getKey())) {
                sendMsg(msg, entry.getValue());
            }
        }
    }

}
