package com.example.socket.service.impl;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.example.socket.model.SocketMsgData;
import com.example.socket.service.SocketService;
import com.example.socket.util.JSONUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SocketServiceImpl implements SocketService {

    @Autowired
    private SocketIOServer socketIOServer;

    // key : 用户唯一标识 ， UUID : socket sessionId
    private Map<String, UUID> socketIOClientMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void start() {
        socketIOServer.start();
    }

    @PreDestroy
    public void stop() {
        socketIOServer.stop();
    }

    @OnConnect
    @Override
    public void onConnect(SocketIOClient socketIOClient) {
        log.info("【onConnect】SessionId: {}", socketIOClient.getSessionId());
        log.info("【onConnect】AllRooms: {}", socketIOClient.getAllRooms());
        log.info("【onConnect】RemoteAddress: {}", socketIOClient.getRemoteAddress());

        Map<String, List<String>> urlParams = socketIOClient.getHandshakeData().getUrlParams();
        log.info("【onConnect】HandshakeData: {}", urlParams);
        // log.info("【onConnect】RemoteAddress: {}", socketIOClient.getRemoteAddress());
        // log.info("【onConnect】Transport: {}", socketIOClient.getTransport());
        String user = getUser(socketIOClient);
        if (user != null) {
            socketIOClientMap.put(user, socketIOClient.getSessionId());
        }

        // 发送上线通知
    }

    @OnDisconnect
    @Override
    public void disConnect(SocketIOClient socketIOClient) {
        log.info("【disConnect】SessionId: {}", socketIOClient.getSessionId());
        log.info("【disConnect】AllRooms: {}", socketIOClient.getAllRooms());
        String user = getUser(socketIOClient);
        if (user != null) {
            socketIOClientMap.remove(user);
        } else {
            System.err.println("【服务端】不明客户端！！！");
        }

        // 发送下线通知

    }

    @OnEvent("push_event")
    @Override
    public void pushMsg(SocketIOClient socketIOClient, AckRequest ackRequest, String strData) {
        SocketMsgData data = JSONUtils.parseIgnoreErrors(strData, SocketMsgData.class);
        log.info("【pushMsg】isAckRequested : {}", ackRequest.isAckRequested());
        log.info("【pushMsg】SocketMsgData : {}", strData);

        socketIOClient.sendEvent("push_event", "【服务端】我收到了");
        String user = getUser(socketIOClient);
        if (user != null) {
            UUID uuid = socketIOClientMap.get(data.getTargetUserName());
            if (uuid != null) {
                SocketIOClient client = socketIOServer.getClient(uuid);
                client.sendEvent("push_event", data);
                socketIOClient.sendEvent("push_event", "【服务端】我发送给" + data.getTargetUserName() + "了！");
            } else {
                socketIOClient.sendEvent("push_event", "【服务端】" + data.getTargetUserName() + "不在线！");
            }
        } else {
            System.err.println("【服务端】不明客户端！！！");
        }

        // socketIOServer.getRoomOperations("room1").sendEvent(name, data);
        // socketIOServer.getNamespace("namespace1").getBroadcastOperations().sendEvent(name, data);
        // socketIOServer.addMultiTypeEventListener(eventName, listener, eventClass);
    }

    private String getUser(SocketIOClient socketIOClient) {
        List<String> list = socketIOClient.getHandshakeData().getUrlParams().get("user");
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
