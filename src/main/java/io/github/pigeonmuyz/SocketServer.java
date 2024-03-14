package io.github.pigeonmuyz;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    
    //语言过滤器
    Map<String, String> languageFilter = initLanguageFilter();

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
            String groupId = "";
            //读取消息
            JsonNode jsonNode = new ObjectMapper().readTree(message);
            String[] command = jsonNode.get("alt_message").asText().split(" ");
            if(command.length == 1){
                command = processString(command[0]);
            }
            MessageType messageType;
            UserType userType = null;
            if(languageFilter.get(command[0]) == null){
                
            }else{
                command[0] = languageFilter.get(command[0]);
            }
            //判断是否是群聊消息
            switch(jsonNode.get("detail_type").asText()){
                case "private":
                    groupId = null;
                    userType = new UserType(jsonNode.get("user_id").asText(),"private");
                    break;
                case "group":
                    groupId = jsonNode.get("group_id").asText();
                    userType = new UserType(groupId,"group");
                    break;
            }
            // 判断指令是单行还是多行
            if (command.length > 1){
                messageType = MessageTool.multiCommand(command,jsonNode.get("user_id").asText(),groupId,defaultServer);
                sendMessage(messageType,userType);
                messageType.setType("null");
            }else{
                if(languageFilter.get(command[0]) == null) {
                    messageType = MessageTool.singleCommand(command[0],jsonNode.get("user_id").asText(),groupId,defaultServer);
                }else{
                    messageType = MessageTool.singleCommand(languageFilter.get(command[0]),jsonNode.get("user_id").asText(),groupId,defaultServer);
                }
                sendMessage(messageType,userType);
                messageType.setType("null");
            }
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
                    System.out.println("ImageID: "+fileIdTemp);
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

    /**
     * 语义识别器
     */
    public static String[] processString(String input) {
        String[] patterns = {
            "我想看看(.*)的(.{2})$",
            "看看(.*)的(.{2})$",
            "给我(.*)的(.{2})$",
            "给我看看(.*)的(.{2})$",
            "(.*)的(.{2})$",
            "我想看看(.*)的(.{4})$",
            "看看(.*)的(.{4})$",
            "给我(.*)的(.{4})$",
            "给我看看(.*)的(.{4})$",
            "(.*)的(.{4})$",
            "我想看看(.*)的(.*)",
            "看看(.*)的(.*)",
            "给我(.*)的(.*)",
            "给我看看(.*)的(.*)",
            "(.*)的(.*)"
        };
        for (String patternStr : patterns) {
            Pattern pattern = Pattern.compile(patternStr);
            Matcher matcher = pattern.matcher(input);
            if (matcher.find()) {
                return new String[]{matcher.group(2), matcher.group(1)};
            }
        }
        return new String[]{input};
    }
    
    /**
     * 初始化语言过滤器
     */
    Map<String, String> initLanguageFilter(){
        Map<String, String> tempMap = new HashMap<String, String>();
        //region 日常
        tempMap.put("日常","日常");     
        tempMap.put("日常","日常");     
        tempMap.put("daily","日常"); 
        tempMap.put("dailys","日常"); 
        //endregion    

        //region 装备
        tempMap.put("装备","装备");     
        tempMap.put("裝備","装备");     
        tempMap.put("equip","装备");     
        tempMap.put("equips","装备");     
        //endregion
        
        //region 帮助
        tempMap.put("帮助","帮助");     
        tempMap.put("幫助","帮助");     
        tempMap.put("help","帮助"); 
        tempMap.put("helps","帮助"); 
        //endregion

        //region 招募
        tempMap.put("招募","招募");     
        tempMap.put("團隊招募","招募");     
        tempMap.put("团队招募","招募");
        tempMap.put("招募","招募"); 
        tempMap.put("teamactivity","招募"); 
        //endregion

        //region 奇遇
        tempMap.put("奇遇","奇遇");     
        tempMap.put("奇遇","奇遇");     
        tempMap.put("adventure","奇遇"); 
        //endregion

        //region 宏
        tempMap.put("宏","宏");     
        tempMap.put("宏","宏");     
        tempMap.put("macro","宏"); 
        //endregion

        //region 战绩
        tempMap.put("战绩","战绩");     
        tempMap.put("JJC","战绩");     
        tempMap.put("戰績","战绩");     
        //endregion

        //region 金价
        tempMap.put("金价","金价");     
        tempMap.put("金價","金价");     
        tempMap.put("gold","金价"); 
        //endregion

        //region 外观
        tempMap.put("外观","外观");     
        tempMap.put("外觀","外观");     
        tempMap.put("fashions","外观"); 
        //endregion

        //region 楚天社
        tempMap.put("行侠","楚天行侠");     
        tempMap.put("行俠","楚天行侠");     
        tempMap.put("楚天行俠","楚天行侠");     
        tempMap.put("楚天行侠","楚天行侠");     
        tempMap.put("chutian","楚天行侠"); 
        //endregion

        //region 战争沙盘
        tempMap.put("沙盘","沙盘");     
        tempMap.put("沙盤","沙盘");     
        tempMap.put("sandbox","沙盘"); 
        //endregion

        //region 成就
        tempMap.put("成就","成就");     
        tempMap.put("成就","成就");     
        tempMap.put("achievement","成就"); 
        //endregion

        //region 副本进度
        tempMap.put("进度","进度");     
        tempMap.put("進度","进度");     
        tempMap.put("progress","进度"); 
        //endregion

        //region 副本进度
        tempMap.put("百战","百战");     
        tempMap.put("百戰","百战");     
        tempMap.put("monster","百战"); 
        //endregion

        //region 公告
        tempMap.put("公告","公告");
        tempMap.put("公告","公告");
        tempMap.put("更新日志","公告");
        tempMap.put("更新日志","公告");
        tempMap.put("announce","公告");
        //endregion

        //region 开服
        tempMap.put("开服","开服");
        tempMap.put("開服","开服");
        tempMap.put("serverstatus","开服");
        //endregion

        //region 烟花
        tempMap.put(".a.烟花","烟花");
//        tempMap.put(".a.烟花","烟花");
//        tempMap.put(".a.烟花","烟花");
        //endregion
        return tempMap;
    }
}
