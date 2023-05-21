package br.com.unitins.websocket;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/notifications")
@ApplicationScoped
public class NotificationWebSocket {
    Map<String, Session> sessions = new ConcurrentHashMap<>();

    public void sendNotification(String message) {
        sessions.values().forEach(session -> {
            session.getAsyncRemote().sendText(message);
        });
    }
}
