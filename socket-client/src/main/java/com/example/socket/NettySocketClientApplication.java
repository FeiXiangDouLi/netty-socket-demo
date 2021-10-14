package com.example.socket;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.example.socket.config.SocketIoConfig;
import com.example.socket.constant.MessageEvent;
import com.example.socket.constant.MessageType;
import com.example.socket.model.SocketMsgData;
import com.example.socket.util.JSONUtils;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author honglixiang
 */
@SpringBootApplication
public class NettySocketClientApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(NettySocketClientApplication.class, args);

		SocketIoConfig config = run.getBean(SocketIoConfig.class);

		// 服务端socket.io连接通信地址
		String url = "http://127.0.0.1:9091";
		try {
			IO.Options options = new IO.Options();
			options.transports = new String[]{"websocket"};
			options.reconnectionAttempts = 2;
			// 失败重连的时间间隔
			options.reconnectionDelay = 1000;
			// 连接超时时间(ms)
			options.timeout = 500;
			// userId: 唯一标识 传给服务端存储
			final Socket socket = IO.socket(url + "?userId=2", options);

//            socket.on(Socket.EVENT_CONNECT, args1 -> socket.send("hello..."));

			// 自定义事件`connected` -> 接收服务端成功连接消息
			socket.on(MessageEvent.CONNECTED, new Emitter.Listener() {
				@Override
				public void call(Object... args) {
					System.err.println(args[0]);
				}
			});

			// 自定义事件`push_data_event` -> 接收服务端消息
			socket.on(MessageEvent.BACK_MESSAGE, new Emitter.Listener() {
				@Override
				public void call(Object... args) {
					System.err.println(args[0]);
				}
			});

			socket.connect();

			while (true) {
				Thread.sleep(30000);

				SocketMsgData socketMsgData = new SocketMsgData();
				socketMsgData.setContent(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
				socketMsgData.setSourceUserName("1");
				socketMsgData.setTargetUserName("2");
				socketMsgData.setType(MessageType.WORD);

				socket.emit(MessageEvent.PUSH_MESSAGE, JSONUtils.toJSONString(socketMsgData));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
