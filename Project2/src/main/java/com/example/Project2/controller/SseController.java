package com.example.Project2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class SseController {
    private static final ConcurrentHashMap<String, SseEmitter> clients = new ConcurrentHashMap<>();
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    @GetMapping("/subscribe")
    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        String clientId = generateClientId();
        clients.put(clientId, emitter);

        emitter.onCompletion(() -> clients.remove(clientId));
        emitter.onTimeout(() -> {
            clients.remove(clientId);
            System.err.println("SSE connection timed out for client: " + clientId);
        });
        emitter.onError((e) -> {
            clients.remove(clientId);
            System.err.println("SSE connection error for client: " + clientId + ", error: " + e.getMessage());
        });

        // 定期发送心跳消息
        executor.execute(() -> {
            try {
                while (clients.containsKey(clientId)) {
                    emitter.send(SseEmitter.event().name("heartbeat").data("ping"));
                    Thread.sleep(15000); // 每 15 秒发送一次心跳
                }
            } catch (Exception e) {
                clients.remove(clientId);
                System.err.println("Failed to send heartbeat: " + e.getMessage());
            }
        });

        return emitter;
    }


    public void sendUpdate(String clientId, String message) {
        SseEmitter emitter = clients.get(clientId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("task-update").data(message));
                System.out.println("Sse: Message sent to client: " + message);
            } catch (IOException e) {
                clients.remove(clientId);
                System.err.println("Sse: Failed to send message: " + e.getMessage());
            }
        } else {
            System.err.println("Sse: No emitter found for clientId: " + clientId);
        }
    }


    public String generateClientId() {
        return String.valueOf(System.currentTimeMillis());
    }
}
