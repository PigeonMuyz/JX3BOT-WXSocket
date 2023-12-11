package io.github.pigeonmuyz;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.pigeonmuyz.entity.MessageType;
import io.github.pigeonmuyz.entity.UserType;
import io.github.pigeonmuyz.tools.MessageTool;
import io.github.pigeonmuyz.tools.HttpTool;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class SocketServer extends WebSocketServer {

    private String defaultServer = "飞龙在天";

    public SocketServer(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
    }

    public SocketServer(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("机器人链接服务器成功");

    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("WS服务器关闭");

    }

    /**
     * 消息处理
     * 不含开服监控推送
     */
    @Override
    public void onMessage(WebSocket conn, String message) {
        //处理指令
        try {
            String groupId;
            //读取消息
            JsonNode jsonNode = new ObjectMapper().readTree(message);
            String[] command = jsonNode.get("alt_message").asText().split(" ");
            System.out.println("原文字："+jsonNode.get("alt_message").asText());
            System.out.println("处理后的文字数组："+command.toString());
            MessageType messageType;
            UserType userType;
            //判断是否是群聊消息
            if (jsonNode.get("group_id").isEmpty() || jsonNode.get("group_id") == null){
                groupId = null;
                userType = new UserType(jsonNode.get("user_id").asText(),"private");
            }else {
                groupId = jsonNode.get("group_id").asText();
                userType = new UserType(groupId,"group");
            }
            // 判断指令是单行还是多行
            if (command.length > 1){
                messageType = MessageTool.multiCommand(command,jsonNode.get("user_id").asText(),groupId,defaultServer);
            }else{
                messageType = MessageTool.singleCommand(jsonNode.get("alt_message").asText(),jsonNode.get("user_id").asText(),groupId,defaultServer);
            }
            sendMessage(messageType,userType);

        } catch (JsonProcessingException e) {
            //读取不了消息
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        //WS服务错误处理

    }

    @Override
    public void onStart() {
        System.out.println("WS服务开启");
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);

    }

    /**
     * 上传文件
     * @param url 链接
     * @return 文件id
     */
    private static String getFileId(String url){
        try {
            JsonNode jn = new ObjectMapper().readTree(HttpTool.postData("http://pigeon-wechat:8000/",String.format("{\"action\":\"upload_file\",\"params\":{\"type\":\"url\",\"name\":\"1.png\",\"url\":\"%s\"}}",url)));
            return jn.get("data").get("file_id").asText();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 发送消息
     * @param messageType 消息类型
     * @param userType 用户类型
     */
    private static void sendMessage(MessageType messageType, UserType userType){
        try {
            switch (messageType.getType()){
                case "image":
                    String fileIdTemp = getFileId(messageType.getContent());
                    switch (userType.getType()){
                        case "group":
                            HttpTool.postData("http://pigeon-wechat:8000/",String.format("{\"action\":\"send_message\",\"params\":{\"detail_type\":\"group\",\"group_id\":\"%s\",\"message\":[{\"type\":\"image\",\"data\":{\"file_id\":\"%s\"}}]}}",userType.getId(),fileIdTemp));
                            break;
                        case "private":
                            HttpTool.postData("http://pigeon-wechat:8000/",String.format("{\"action\":\"send_message\",\"params\":{\"detail_type\":\"private\",\"user_id\":\"%s\",\"message\":[{\"type\":\"image\",\"data\":{\"file_id\":\"%s\"}}]}}",userType.getId(),fileIdTemp));
                            break;
                    }
                    break;
                case "text":
                    switch (userType.getType()){
                        case "group":
                            HttpTool.postData("http://pigeon-wechat:8000/",String.format("{\"action\":\"send_message\",\"params\":{\"detail_type\":\"group\",\"group_id\":\"%s\",\"message\":[{\"type\":\"text\",\"data\":{\"text\":\"%s\"}}]}}",userType.getId(),messageType.getContent()));
                            break;
                        case "private":
                            HttpTool.postData("http://pigeon-wechat:8000/",String.format("{\"action\":\"send_message\",\"params\":{\"detail_type\":\"private\",\"user_id\":\"%s\",\"message\":[{\"type\":\"text\",\"data\":{\"text\":\"%s\"}}]}}",userType.getId(),messageType.getContent()));
                            break;
                    }
                    break;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

