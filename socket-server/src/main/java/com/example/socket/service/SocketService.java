package com.example.socket.service;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;

public interface SocketService {

    void onConnect(SocketIOClient socketIOClient);

    void disConnect(SocketIOClient socketIOClient);

    void pushMsg(SocketIOClient socketIOClient, AckRequest ackRequest, String strData);
}
