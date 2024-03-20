package io.github.pigeonmuyz.websocket;

import io.github.pigeonmuyz.tools.ManualTimer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class SocketClient extends WebSocketClient {
    private static final Logger log = LogManager.getLogger(WebSocketClient.class);
    public SocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        log.info("JX3API WS连接成功");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("收到消息: " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {

        // 嘗試重連邏輯
        reconnect();
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("發生錯誤: " + ex.getMessage());
    }

    // 重連方法
    public void reconnect() {
        // 這裡可以添加重連條件和重連次數限制
        try {
            // 等待一段時間後重連
            Thread.sleep(5000);
            System.out.println("正在重連...");
            super.reconnect();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) throws Exception {
//        AutoReconnectWebSocketClient client = new AutoReconnectWebSocketClient(new URI("ws://localhost:8887"));
//        client.connect();
//    }
}

