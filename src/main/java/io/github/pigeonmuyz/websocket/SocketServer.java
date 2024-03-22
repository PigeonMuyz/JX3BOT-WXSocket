package io.github.pigeonmuyz.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.pigeonmuyz.Main;
import io.github.pigeonmuyz.entity.MessObject;
import io.github.pigeonmuyz.entity.MessageBottle;
import io.github.pigeonmuyz.helper.MessFilter;
import io.github.pigeonmuyz.helper.WeChatHelper;
import io.github.pigeonmuyz.tools.HttpTool;
import io.github.pigeonmuyz.tools.MessageTool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.github.pigeonmuyz.helper.MessFilter.languageFilter;
import static io.github.pigeonmuyz.helper.MessFilter.processString;

public class SocketServer extends WebSocketServer {

    private final static Logger log = LogManager.getLogger(SocketServer.class);

    public SocketServer(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
        log.debug("当前WSS监听端口："+port);
    }

    public SocketServer(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        log.info("机器人链接WS服务器成功");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        log.warn("WS服务器关闭");

    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        //处理指令
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(message);
            Boolean isGroup = false;
            String wechatId = "";
            if (jsonNode.get("detail_type") != null){
                if(jsonNode.get("detail_type").asText().equalsIgnoreCase("group")){
                    isGroup = true;
                    wechatId = jsonNode.get("group_id").asText();
                }else if (jsonNode.get("detail_type").asText().equalsIgnoreCase("private")){
                    wechatId = jsonNode.get("user_id").asText();
                }
            }
            final String finalWechatId = wechatId;
            final Boolean finalIsGroup = isGroup;
            // 当用户首次触发之后，且没有被记录之后
            if (!Main.personal.stream().anyMatch(messObject -> messObject.getWechatID().equalsIgnoreCase(finalWechatId))){
                String wechatName ="";
                JsonNode tempNode;
                if (isGroup){
                    tempNode = new ObjectMapper().readTree(HttpTool.postData(Main.configProperties.getProperty("config.wechatRollbackUrl"),String.format("{\"action\":\"get_group_info\",\"params\":{\"group_id\":\"%s\"}}", wechatId)));
                    if (tempNode.get("data") != null){
                        wechatName = tempNode.get("data").get("group_name") != null ? tempNode.get("data").get("group_name").asText() : "未知";
                    }
                }else{
                    tempNode = new ObjectMapper().readTree(HttpTool.postData(Main.configProperties.getProperty("config.wechatRollbackUrl"),String.format("{\"action\":\"get_user_info\",\"params\":{\"user_id\":\"%s\"}}", wechatId)));
                    if (tempNode.get("data") != null){
                        wechatName = tempNode.get("data").get("user_name") != null ? tempNode.get("data").get("user_name").asText() : "未知";
                    }
                }
                MessObject messObject = new MessObject(isGroup,wechatId,wechatName,"","",false);
                log.info(String.format("新增%s用户：%s",isGroup? "群":"私聊",wechatName));
                Main.personal.add(messObject);
            }
            // 触发并且被激活之后才可以安排
            if (Main.personal.stream().anyMatch(messObject -> messObject.getWechatID().equalsIgnoreCase(finalWechatId) && messObject.getIsActive())){
                log.debug("激活用户发送的消息: "+jsonNode.get("alt_message").asText());
                // 从这里开始处理消息并发送
                Main.personal.stream().filter(messObject -> messObject.getWechatID().equalsIgnoreCase(finalWechatId))
                        .forEach(messObject -> {
                            if (jsonNode.get("alt_message").asText().split(" ")[0].equals("绑定") && messObject.getMaster().equalsIgnoreCase(jsonNode.get("user_id").asText()) && jsonNode.get("alt_message").asText().split(" ").length > 1){
                                try {
                                    JsonNode temp = new ObjectMapper().readTree(HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/api/data/serverCheck?server="+jsonNode.get("alt_message").asText().split(" ")[1]));
                                    if (temp.get("data") != null && temp.get("data").get("server") != null){
                                        messObject.setBindServer(temp.get("data").get("server").asText());
                                        WeChatHelper.sendMessage(finalWechatId, finalIsGroup, "text", String.format("绑定%s成功",temp.get("data").get("server").asText()));
                                    }
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            /**
                             * 群组主人消息处理模块
                             */
                            if (messObject.getMaster().equalsIgnoreCase(jsonNode.get("user_id").asText()) || jsonNode.get("user_id").asText().equalsIgnoreCase("carol0774")){
                                if (messObject.isGroup()){
                                    switch (jsonNode.get("alt_message").asText()){
                                        case "开服状态":
                                            if (messObject.getServerStatus().get("2001") != null) {
                                                messObject.getServerStatus().put("2001", !messObject.getServerStatus().get("2001"));
                                            } else {
                                                messObject.getServerStatus().put("2001", true);
                                            }
                                            WeChatHelper.sendMessage(finalWechatId, finalIsGroup, "text", String.format("%s：%s推送成功",messObject.getServerStatus().get("2001") ? "开启":"关闭",jsonNode.get("alt_message").asText()));
                                            break;
                                        case "云从预告":
                                            if (messObject.getServerStatus().get("2006") != null) {
                                                messObject.getServerStatus().put("2006", !messObject.getServerStatus().get("2006"));
                                            } else {
                                                messObject.getServerStatus().put("2006", true);
                                            }
                                            WeChatHelper.sendMessage(finalWechatId, finalIsGroup, "text", String.format("%s：%s推送成功",messObject.getServerStatus().get("2006") ? "开启":"关闭",jsonNode.get("alt_message").asText()));
                                            break;
                                        case "八卦推送":
                                            if (messObject.getServerStatus().get("2004") != null) {
                                                messObject.getServerStatus().put("2004", !messObject.getServerStatus().get("2004"));
                                            } else {
                                                messObject.getServerStatus().put("2004", true);
                                            }
                                            WeChatHelper.sendMessage(finalWechatId, finalIsGroup, "text", String.format("%s：%s推送成功",messObject.getServerStatus().get("2004") ? "开启":"关闭",jsonNode.get("alt_message").asText()));
                                            break;
                                        case "版本更新":
                                            if (messObject.getServerStatus().get("2003") != null) {
                                                messObject.getServerStatus().put("2003", !messObject.getServerStatus().get("2003"));
                                            } else {
                                                messObject.getServerStatus().put("2003", true);
                                            }
                                            WeChatHelper.sendMessage(finalWechatId, finalIsGroup, "text", String.format("%s：%s推送成功",messObject.getServerStatus().get("2003") ? "开启":"关闭",jsonNode.get("alt_message").asText()));
                                            break;
                                        case "官方资讯":
                                            if (messObject.getServerStatus().get("2002") != null) {
                                                messObject.getServerStatus().put("2002", !messObject.getServerStatus().get("2002"));
                                            } else {
                                                messObject.getServerStatus().put("2002", true);
                                            }
                                            WeChatHelper.sendMessage(finalWechatId, finalIsGroup, "text", String.format("%s：%s推送成功",messObject.getServerStatus().get("2002") ? "开启":"关闭",jsonNode.get("alt_message").asText()));
                                            break;
                                        case "我全都要！！！":
                                            messObject.getServerStatus().put("2002",true);
                                            messObject.getServerStatus().put("2003",true);
                                            messObject.getServerStatus().put("2004",true);
                                            messObject.getServerStatus().put("2006",true);
                                            messObject.getServerStatus().put("2001",true);
                                            WeChatHelper.sendMessage(finalWechatId, finalIsGroup, "text", "所有消息推送开启成功！");
                                            break;
                                        case "推送状态":
                                            String tempMessage = String.format(
                                                    "群名：%s\\n" +
                                                    "群ID：%s\\n" +
                                                    "开服监控：%s\\n" +
                                                    "贴吧吃瓜：%s\\n" +
                                                    "版本更新：%s\\n" +
                                                    "官方资讯：%s\\n" +
                                                    "云从预告：%s"
                                                    ,messObject.getWechatName()
                                                    ,messObject.getWechatID()
                                                    ,messObject.getServerStatus().get("2001")? "开启":"关闭"
                                                    ,messObject.getServerStatus().get("2004")? "开启":"关闭"
                                                    ,messObject.getServerStatus().get("2003")? "开启":"关闭"
                                                    ,messObject.getServerStatus().get("2002")? "开启":"关闭"
                                                    ,messObject.getServerStatus().get("2006")? "开启":"关闭");
                                            WeChatHelper.sendMessage(finalWechatId, finalIsGroup, "text", tempMessage);

                                            break;
                                    }

                                }
                            }



                            //#region 消息处理模块
                            String[] command = jsonNode.get("alt_message").asText().split(" ");
                            //region 特殊消息处理模块
                            try{
                                switch (command[0]){
                                    //region 漂流瓶功能
                                    case "漂流瓶":
                                        switch (command[1]){
                                            case "丢出":
                                                if (command[2] != null){
                                                    if (command.length >3){
                                                        String temp = command[2];
                                                        for (int i = 0; i < command.length; i++) {
                                                            if (i>=3){
                                                                temp += command[i];
                                                            }
                                                        }
                                                        Main.messsageBottles.add(new MessageBottle(temp,jsonNode.get("user_id").asText()));
                                                    }else{
                                                        Main.messsageBottles.add(new MessageBottle(command[2],jsonNode.get("user_id").asText()));
                                                    }
                                                    WeChatHelper.sendMessage(finalWechatId,finalIsGroup,"text","匿名漂流瓶已经丢完了！");
                                                }else{
                                                    WeChatHelper.sendMessage(finalWechatId,finalIsGroup,"text","来都来了，写一句话再走吧！");
                                                }
                                                break;
                                        }
                                    //endregion
                                }
                                switch (jsonNode.get("alt_message").asText()){
                                    //region 漂流瓶功能
                                    case "随机漂流瓶":
                                        if (Main.messsageBottles.size() >= 1){
                                            MessageBottle mb = Main.messsageBottles.get(new Random().nextInt(Main.messsageBottles.size()));
                                            WeChatHelper.sendMessage(finalWechatId,finalIsGroup,"text",String.format("捡到一个匿名漂流瓶！！！\\n瓶子编号：%s...\\n瓶子内容：%s",mb.getUuid().substring(0,5),mb.getContent()));
                                            break;
                                        }else{
                                            WeChatHelper.sendMessage(finalWechatId,finalIsGroup,"text","还没有瓶子哦！快自己来丢一个吧！");

                                        }
                                        break;
                                    //endregion
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            //endregion
                            String[] result = {"default",""};
                            if (command.length == 1){
                                command = MessFilter.processString(jsonNode.get("alt_message").asText());
                            }
                            command[0] = languageFilter.get(command[0]) != null ? languageFilter.get(command[0]) : command[0];
                            if (command.length > 1){
                                result = MessageTool.multiCommand(command, messObject.getBindServer().isEmpty() ? "飞龙在天": messObject.getBindServer());
                            }else{
                                result = MessageTool.singleCommand(command[0], messObject.getBindServer().isEmpty() ? "飞龙在天": messObject.getBindServer());
                            }
                            if (!result[0].equals("default")){
                                WeChatHelper.sendMessage(finalWechatId, finalIsGroup, result[0], result[1]);
                            }
                            //#endregion
                        });
            } else if (Main.personal.stream().anyMatch(messObject -> messObject.getWechatID().equalsIgnoreCase(finalWechatId) && !messObject.getIsActive())){
                // 在列表中且未被激活
                Main.personal.stream()
                        .filter(messObject -> messObject.getWechatID().equalsIgnoreCase(finalWechatId))
                        .forEach(messObject -> {
                            if (jsonNode.get("alt_message").asText().equals(Main.configProperties.getProperty("config.activeMessage"))){
                                messObject.setMaster(jsonNode.get("user_id").asText());
                                messObject.setIsActive(true);
                                // 给予用户反馈
                                WeChatHelper.sendMessage(finalWechatId, finalIsGroup, "text", "尊敬的Master！恭喜你领养成功！");
                            }
                        });
            }
        } catch (Exception e) {
            log.error("WSS处理消息侧报错");
            e.printStackTrace();
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        //WS服务错误处理
    }

    @Override
    public void onStart() {
        log.info("WSS启动成功");
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);

    }

}
