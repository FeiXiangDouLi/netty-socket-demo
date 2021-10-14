package com.example.socket.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author honglixiang
 */
@org.springframework.context.annotation.Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "socketio")
public class SocketIoConfig {

    private String host;

    private Integer port;

}
