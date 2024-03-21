package io.github.pigeonmuyz.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.pigeonmuyz.Main;
import io.github.pigeonmuyz.helper.WeChatHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class SocketClient extends WebSocketClient {
    private static final Logger log = LogManager.getLogger(WebSocketClient.class);
    public SocketClient(String serverUri) throws URISyntaxException {
        super(new URI(serverUri));
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        log.info("JX3API WS连接成功");
    }

    @Override
    public void onMessage(String message) {
        log.debug("收到API消息：\n"+message);
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(message);
            String resultMessage = "";
            if (jsonNode.get("action") != null){
                switch (jsonNode.get("action").toString()){
                    case "2001":
                        if (jsonNode.get("data").get("server").asText() == "飞龙在天"){
                            if (jsonNode.get("data").get("status").asInt() == 0){
                                resultMessage = "开始维护辣！";
                            }else{
                                resultMessage = "开服辣！快冲啊！";
                            }
                        }
                        break;
                    case "2002":
                        resultMessage = String.format(
                                "【%s】\\n" +
                                "标题：%s\\n" +
                                "链接：%s\\n" +
                                "日期：%s"
                                ,jsonNode.get("data").get("type").asText()
                                ,jsonNode.get("data").get("title").asText()
                                ,jsonNode.get("data").get("url").asText()
                                ,jsonNode.get("data").get("date").asText());
                        break;
                    case "2003":
                        resultMessage = String.format(
                                "有版本更新！！！\\n" +
                                "旧版本：%s\\n" +
                                "新版本：%s\\n" +
                                "补丁大小：%s"
                                ,jsonNode.get("data").get("old_version").asText()
                                ,jsonNode.get("data").get("new_version").asText()
                                ,jsonNode.get("data").get("package_size").asText());
                        break;
                    case "2004":
                        resultMessage = String.format(
                                "来自 %s吧 的%s" +
                                        "标题：%s" +
                                        "链接：%s" +
                                        "日期：%s"
                                ,jsonNode.get("data").get("name").asText()
                                ,jsonNode.get("data").get("subclass").asText()
                                ,jsonNode.get("data").get("title").asText()
                                ,jsonNode.get("data").get("url").asText()
                                ,jsonNode.get("data").get("date").asText());
                        break;
                    case "2006":
                        resultMessage = String.format(
                                "云从播报！！！\\n" +
                                "即将开始的事件：%s\\n" +
                                "地点：%s"
                                ,jsonNode.get("data").get("desc").asText()
                                ,jsonNode.get("data").get("site").asText());
                        break;
                }
            }
            if (resultMessage != ""){
                final String finalResultMessage = resultMessage;
                Main.personal.stream().filter(messObject -> messObject.getIsActive() && messObject.isGroup())
                        .forEach(messObject -> {
                            WeChatHelper.sendMessage(messObject.getWechatID(),messObject.isGroup(),"text",finalResultMessage);
                        });
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
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

