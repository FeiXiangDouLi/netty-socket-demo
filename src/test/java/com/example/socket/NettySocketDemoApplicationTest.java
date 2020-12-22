/**
 * @author honglixiang
 * @date 2020年12月21日
 */
package com.example.socket;

import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.corundumstudio.socketio.SocketIOServer;

/**
 * @author 洪笠翔
 *
 */
@SpringBootTest
public class NettySocketDemoApplicationTest {

    @Autowired
    private SocketIOServer socketIOServer;

    @Test
    public void loadContext() throws URISyntaxException, InterruptedException {

    }
}
