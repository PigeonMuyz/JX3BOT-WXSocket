package io.github.pigeonmuyz.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.pigeonmuyz.Main;

public class MessageTool {

    static ObjectMapper mapper = new ObjectMapper();
    static JsonNode rootNode;
    static JsonNode dataNode;
    static String temp;
    public static String[] singleCommand(String command, String server) {
        try {
            switch(command){
                //region 日常
                case "日常":
                    rootNode = mapper.readTree(HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/api/data/daily?server="+server));
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            dataNode = rootNode.path("data");
                            temp =  "【PVE日常】\\n"
                                    + "秘境日常：" + dataNode.get("war").asText() + "\\n"
                                    + "公共日常：" + dataNode.get("team").get(0).asText() + "\\n"
                                    + "【PVP日常】\\n"
                                    + "矿车：跨服•烂柯山\\n"
                                    + "战场：" + dataNode.get("battle").asText() + "\\n"
                                    + "【PVX日常】\\n"
                                    + (dataNode.get("draw") != null  ? "美人图：" + dataNode.get("draw").asText() : "美人图：无")
                                    + "门派事件：" + dataNode.get("school").asText() + "\\n"
                                    + String.format("福源宠物：%s;%s;%s\\n", dataNode.get("luck").get(0).asText(), dataNode.get("luck").get(1).asText(), dataNode.get("luck").get(2).asText())
                                    + "【PVE周常】\\n"
                                    + "五人秘境：" + dataNode.get("team").get(1).asText() + "\\n"
                                    + "十人秘境：" + dataNode.get("team").get(2).asText() + "\\n"
                                    + "【今天是" + dataNode.get("date").asText() + " 星期" + dataNode.get("week").asText() +"】";
                            return new String[]{"text",temp};
                        default:
                            return new String[]{"text","数据异常，可能是因为渡渡鸟没人陪！"};
                    }
                //endregion
                //region 金价
                case "金价":
                    rootNode = mapper.readTree(HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/api/image/trade/demon?server="+server));
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            dataNode = rootNode.path("data");
                           return new String[]{"image",dataNode.get("url").asText()};
                        default:
                            return new String[]{"text","数据异常，可能是因为渡渡鸟没人陪！"};
                    }
                //endregion
                //region 花价
                case "花价":
                    rootNode = mapper.readTree(HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/api/image/home/flower?server="+server));
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            dataNode = rootNode.path("data");
                           return new String[]{"image",dataNode.get("url").asText()};
                        default:
                            return new String[]{"text","数据异常，可能是因为渡渡鸟没人陪！"};
                    }
                //endregion
                //region 团队招募
                case "招募":
                case "团队招募":
                    rootNode = mapper.readTree(HttpTool.getData("http://localhost:25555/api/image/member/recruit?server="+server));
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            dataNode = rootNode.path("data");
                           return new String[]{"image",dataNode.get("url").asText()};
                        default:
                            return new String[]{"text","数据异常，可能是因为渡渡鸟没人陪！"};
                    }
                //endregion
                //region 楚天行侠
                case "楚天行侠":
                case "楚天社":
                case "行侠":
                    rootNode = mapper.readTree(HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/api/data/celebrities"));
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            dataNode = rootNode.path("data");
                            temp = "现在正在进行：" + dataNode.get(0).get("desc").asText() + "\\n"
                                    + String.format("地点：%s · %s\\n", dataNode.get(0).get("map_name").asText(), dataNode.get(0).get("site").asText())
                                    + "开始时间：" + dataNode.get(0).get("time").asText() + "\\n"
                                    + "下一次将要进行：" + dataNode.get(1).get("desc").asText() + "\\n"
                                    + String.format("地点：%s · %s\\n", dataNode.get(1).get("map_name").asText(), dataNode.get(1).get("site").asText())
                                    + "开始时间：" + dataNode.get(1).get("time").asText() + "\\n"
                                    + "之后将要进行：" + dataNode.get(2).get("desc").asText() + "\\n"
                                    + String.format("地点：%s · %s\\n", dataNode.get(2).get("map_name").asText(), dataNode.get(2).get("site").asText())
                                    + "开始时间：" + dataNode.get(2).get("time").asText();
                            return new String[]{"text",temp};
                        default:
                            return new String[]{"text","数据异常，可能是因为渡渡鸟没人陪！"};
                    }
                //endregion
                //region 百战
                case "百战":
                    rootNode = mapper.readTree(HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/api/image/active/monster"));
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            dataNode = rootNode.path("data");
                           return new String[]{"image",dataNode.get("url").asText()};
                        default:
                            return new String[]{"text","数据异常，可能是因为渡渡鸟没人陪！"};
                    }
                //endregion
                //region 更新日志
                case "日志":
                case "更新日志":
                    String temp = "剑三鸽鸽Re 2.0 （开发版本号：咕咕咕）\\n"
                            + "1. 添加群组服务器绑定功能（未添加服务器则默认飞龙）\\n"
                            + "2. 添加激活功能！\\n"
                            + "3. 推送新功能\\n"
                            + "4. 更加香喷喷了！\\n";
                    return new String[]{"text",temp};
                //endregion
                //region 帮助
                case "帮助":
                case "功能":
                case "指令":
                    return new String[]{"image","https://s11.ax1x.com/2023/12/22/pi7MrKe.png"};
                //endregion
                //region 服务器开服查询
                case "开服":
                    rootNode = mapper.readTree(HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/api/data/serverCheck?server="+server));
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            if (rootNode.get("data").get("status").asInt() >0){
                                temp = server+" 当前状态：开启";
                            }else{
                                temp = server+" 当前状态：维护";
                            }
                            return new String[]{"text",temp};
                        default:
                            return new String[]{"text","数据异常，可能是因为渡渡鸟没人陪！"};
                    }
                //endregion
                //region 公告
                case "公告":
                    rootNode = mapper.readTree(HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/api/image/web/news/announce"));
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            dataNode = rootNode.path("data");
                           return new String[]{"image",dataNode.get("url").asText()};
                        default:
                            return new String[]{"text","数据异常，可能是因为渡渡鸟没人陪！"};
                    }
                //endregion
                //region 骚话
                case "骚话":
                    JsonNode rootNode = mapper.readTree(HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/api/data/saohua"));
                    JsonNode dataNode = rootNode.path("data");
                    temp = dataNode.get("text").asText();
                    return new String[]{"text",temp};
                //endregion
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new String[]{"",""};
    }

    public static String[] multiCommand(String[] command, String server) {
        try{
            switch (command[0]){
                //region 绑定服务器
//                case "绑定":
//                    if (guildID != null){
//                        JsonNode jn = mapper.readTree(HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/user/get?GroupID="+guildID));
//                        if (jn.get("code").asInt() == 200){
//                            if (!jn.get("data").isEmpty()){
//                                //群组已经绑定过了，走频道更新流程
//                                HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/user/update?GroupID="+guildID+"&server="+command[1]);
//                                mt = new MessageType("text","绑定更新成功");
//                            }else{
//                                HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/user/add?GroupID="+guildID+"&server="+command[1]);
//                            mt = new MessageType("text","绑定成功");
//                            }
//                        }else{
//                            HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/user/add?GroupID="+guildID+"&server="+command[1]);
//                            mt = new MessageType("text","绑定成功");
//                        }
//                    }
//
//                    rootNode = mapper.readTree(HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/user/get?WXID="+userID));
//                    if (rootNode.get("code").asInt() == 200 && !rootNode.get("data").isEmpty()){
//                        if (guildID == null && rootNode.get("data").get(0).get("server") != null){
//                            //用户已经绑定过了，走用户更新流程
//                            HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/user/update?WXID="+userID+"&server="+command[1]);
//                            mt = new MessageType("text","绑定更新成功");
//                        }else{
//                            //用户未绑定，走用户绑定流程
//                            HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/user/add?WXID="+userID+"&server="+command[1]);
//                            mt = new MessageType("text","绑定成功");
//                        }
//                    }else{
//                        //用户未绑定，走用户绑定流程
//                        HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/user/add?WXID="+userID+"&server="+command[1]);
//                        mt = new MessageType("text","绑定成功");
//                    }
//                    break;
                //endregion
                //region 功能开关
//                case "开启":
//                case "关闭":
//                    boolean status;
//                    if(command[0].equals("开启")){
//                        status = true;
//                    }else{
//                        status = false;
//                    }
//                    switch (command[1]){
//                        case "版本更新":
//                            if (guildID != null){
//                                HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/user/update?GroupID="+guildID+"&VersionUpdate="+status);
//                            }else{
//                                HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/user/update?WXID="+userID+"&VersionUpdate="+status);
//                            }
//                            if (status){
//                                temp = command[1]+"已开启！";
//                            }else{
//                                temp = command[1]+"已关闭！";
//                            }
//                            mt = new MessageType("text",temp);
//                            break;
//                        case "开服监控":
//                            if (guildID != null){
//                                HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/user/update?GroupID="+guildID+"&ServerStatus="+status);
//                            }else{
//                                HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/user/update?WXID="+userID+"&ServerStatus="+status);
//                            }
//                            if (status){
//                                temp = command[1]+"已开启！";
//                            }else{
//                                temp = command[1]+"已关闭！";
//                            }
//                            mt = new MessageType("text",temp);
//;
//                            break;
//                        case "新闻监控":
//                            if (guildID != null){
//                                HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/user/update?GroupID="+guildID+"&News="+status);
//                            }else{
//                                HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/user/update?WXID="+userID+"&News="+status);
//                            }
//                            if (status){
//                                temp = command[1]+"已开启！";
//                            }else{
//                                temp = command[1]+"已关闭！";
//                            }
//                            mt = new MessageType("text",temp);
//;
//                            break;
//                        case "818监控":
//                            if (guildID != null){
//                                HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/user/update?GroupID="+guildID+"&forumPost="+status);
//                            }else{
//                                HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/user/update?WXID="+userID+"&forumPost="+status);
//                            }
//                            if (status){
//                                temp = command[1]+"已开启！";
//                            }else{
//                                temp = command[1]+"已关闭！";
//                            }
//                            mt = new MessageType("text",temp);
//;
//                            break;
//                        case "先锋测试":
//                            if (guildID != null){
//                                HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/user/update?GroupID="+guildID+"&bossRefresh="+status);
//                            }else{
//                                HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/user/update?WXID="+userID+"&bossRefresh="+status);
//                            }
//                            if (status){
//                                temp = command[1]+"已开启！";
//                            }else{
//                                temp = command[1]+"已关闭！";
//                            }
//                            mt = new MessageType("text",temp);
//;
//                            break;
//                        case "云从事件":
//                            if (guildID != null){
//                                HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/user/update?GroupID="+guildID+"&bossRefresh="+status);
//                            }else{
//                                HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/user/update?WXID="+userID+"&bossRefresh="+status);
//                            }
//                            break;
//                        case "关隘预告":
//                            if (guildID != null){
//                                HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/user/update?GroupID="+guildID+"&bossRefresh="+status);
//                            }else{
//                                HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/user/update?WXID="+userID+"&bossRefresh="+status);
//                            }
//                        case "手机通知":
//                            if (guildID != null){
//                                //这里是群聊，群聊绝对不要绑定！
//                            }else{
//                                rootNode = mapper.readTree(HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/user/get?WXID="+userID));
//                                HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/user/update?WXID="+userID+"&BarkNotify="+status);
//                                if (rootNode.get("code").asInt() == 200 && rootNode.get("data").get(0).get("barkKey") != null){
//                                    HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/user/update?WXID="+userID+"&barkNotify="+status);
//                                    rootNode = mapper.readTree(HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/user/get?WXID="+userID));
//                                    if (rootNode.get("data").get(0).get("barkNotify").asBoolean()){
//                                        temp = "Bark推送已经开启";
//                                    }else{
//                                        temp = "Bark推送已经关闭";
//
//                                    }
//                                }else{
//                                    temp = "请先绑定BarkKey";
//                                }
//                            }
//                            mt = new MessageType("text",temp);
//
//                            break;
//                    }
//                    break;
                //endregion
                //region 状态查询
//                case "当前状态":
//                case "功能状态":
//                case "服务":
//                    if (guildID != null){
//                        rootNode = mapper.readTree(HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/user/get?GroupID="+guildID));
//                        if (rootNode.get("data").isEmpty() || rootNode.get(0).get("GroupID") == null) {
//                            temp = "当前频道未绑定服务器并开启任何功能";
//                        }else{
//                            temp = "当前频道功能开启状态：";
//                            if (rootNode.get(0).get("VersionUpdate").asBoolean()){
//                                temp = "版本更新：开启";
//                            }else{
//                                temp = "版本更新：关闭";
//                            }
//                            if (rootNode.get(0).get("ServerStatus").asBoolean()){
//                                temp = "开服监控：开启";
//                            }else{
//                                temp = "开服监控：关闭";
//                            }
//                            if (rootNode.get(0).get("News").asBoolean()){
//                                temp = "新闻监控：开启";
//                            }else{
//                                temp = "新闻监控：关闭";
//                            }
//                            if (rootNode.get(0).get("forumPost").asBoolean()){
//                                temp = "818监控：开启";
//                            }else{
//                                temp = "818监控：关闭";
//                            }
//                            if (rootNode.get(0).get("bossRefresh").asBoolean()){
//                                temp = "先锋测试：开启";
//                            }else{
//                                temp = "先锋测试：关闭";
//                            }
//                        }
//                    }else{
//                        rootNode = mapper.readTree(HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/user/get?WXID="+userID));
//                        if (rootNode.get("data").isEmpty() || rootNode.get(0).get("WXID") == null) {
//                            temp = "当前用户未绑定服务器，并开启任何功能";
//                        }else{
//                            temp = "当前用户功能开启状态：";
//                            if (rootNode.get(0).get("VersionUpdate").asBoolean()){
//                                temp = "版本更新：开启";
//                            }else{
//                                temp = "版本更新：关闭";
//                            }
//                            if (rootNode.get(0).get("ServerStatus").asBoolean()){
//                                temp = "开服监控：开启";
//                            }else{
//                                temp = "开服监控：关闭";
//                            }
//                            if (rootNode.get(0).get("News").asBoolean()){
//                                temp = "新闻监控：开启";
//                            }else{
//                                temp = "新闻监控：关闭";
//                            }
//                            if (rootNode.get(0).get("forumPost").asBoolean()){
//                                temp = "818监控：开启";
//                            }else{
//                                temp = "818监控：关闭";
//                            }
//                            if (rootNode.get(0).get("bossRefresh").asBoolean()){
//                                temp = "先锋测试：开启";
//                            }else{
//                                temp = "先锋测试：关闭";
//                            }
//                            if (rootNode.get(0).get("barkNotify").asBoolean()){
//                                temp = "手机推送：开启";
//                            }else{
//                                temp = "手机推送：关闭";
//                            }
//                        }
//                    }
//                    mt = new MessageType("text",temp);
//                    break;
                //endregion
                //region 外观
                case "外观":
                case "万宝楼":
                    rootNode = mapper.readTree(HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/api/image/trade/record?name="+command[1]));
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            dataNode = rootNode.path("data");
                           return new String[]{"image",dataNode.get("url").asText()};
                        default:
                            return new String[]{"text",rootNode.get("msg") != null ? rootNode.get("msg").asText() : rootNode.get("message") != null ? rootNode.get("message").asText() : "找渡渡鸟来修"};
                    }
                //endregion
                //region Bark绑定
//                case "Bark":
//                case "消息推送密钥":
//                case "消息推送":
//                    rootNode = mapper.readTree(HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/user/get?WXID="+userID));
//                    if (rootNode.get("code").asInt() == 200 && rootNode.get("data").get(0).get("WXID") != null){
//                        if (guildID == null && rootNode.get("data").get(0).get("barkKey") != null){
//                            //用户已经绑定过了，走用户更新流程
//                            HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/user/update?WXID="+userID+"&barkKey="+command[1]+"&barkNotify=true");
//                            temp = "更改成功！将开启推送！";
//                        }
//                    }else{
//                        //用户未绑定，走用户绑定流程
//                        HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/user/add?WXID="+userID+"&barkKey="+command[1]);
//                        HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/user/update?WXID="+userID+"&barkNotify=true");
//                        temp = "绑定成功！默认将开启推送！";
//                    }
//                    mt = new MessageType("text",temp);
//                    break;
                //endregion
                //region 奇遇
                case "奇遇":
                    if (command.length>=3){
                        rootNode = mapper.readTree(HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/api/image/luck/adventure?server="+command[1]+"&name="+command[2]+"&filter=1"));
                    }else{
                        rootNode = mapper.readTree(HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/api/image/luck/adventure?server="+server+"&name="+command[1]+"&filter=1"));
                    }
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            dataNode = rootNode.path("data");
                           return new String[]{"image",dataNode.get("url").asText()};
                        default:
                            return new String[]{"text",rootNode.get("msg") != null ? rootNode.get("msg").asText() : rootNode.get("message") != null ? rootNode.get("message").asText() : "找渡渡鸟来修"};
                    }
                //endregion
                //region JJC战绩
                case "名剑":
                case "战绩":
                case "JJC":

                    if (command.length >= 4){
                        rootNode = mapper.readTree(HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/api/image/match/recent?server="+command[1]+"&name="+command[2]+"&robot=剑三咕咕"+"&mode="+command[3]));
                    }else {
                        rootNode = mapper.readTree(HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/api/image/match/recent?server="+server+"&name="+command[1]+"&mode="+command[2]));
                    }
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            dataNode = rootNode.path("data");
                           return new String[]{"image",dataNode.get("url").asText()};
                        default:
                            return new String[]{"text",rootNode.get("msg") != null ? rootNode.get("msg").asText() : rootNode.get("message") != null ? rootNode.get("message").asText() : "找渡渡鸟来修"};
                    }
                //endregion
                //region 金价
                case "金价":
                    rootNode = mapper.readTree(HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/api/image/trade/demon?server="+command[1]));
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            dataNode = rootNode.path("data");
                           return new String[]{"image",dataNode.get("url").asText()};
                        default:
                            return new String[]{"text",rootNode.get("msg") != null ? rootNode.get("msg").asText() : rootNode.get("message") != null ? rootNode.get("message").asText() : "找渡渡鸟来修"};
                    }
                //endregion
                //region 团队招募
                case "招募":
                case "团队招募":
                    if (command.length>=3){
                        rootNode = mapper.readTree(HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/api/image/member/recruit?server="+command[1]+"&keyword="+command[2]));
                    }else{
                        rootNode = mapper.readTree(HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/api/image/member/recruit?server="+command[1]));
                        if (rootNode.get("code").asInt() != 200){
                            rootNode = mapper.readTree(HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/api/image/member/recruit?server="+server+"&keyword="+command[1]));
                        }
                    }
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            dataNode = rootNode.path("data");
                           return new String[]{"image",dataNode.get("url").asText()};
                        default:
                            return new String[]{"text",rootNode.get("msg") != null ? rootNode.get("msg").asText() : rootNode.get("message") != null ? rootNode.get("message").asText() : "找渡渡鸟来修"};
                    }
                //endregion
                //region 装备查询
                case "查询":
                case "装备":
                    if (command.length>=3){
                        rootNode = mapper.readTree(HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/api/image/role/attribute?server="+command[1]+"&name="+command[2]));
                    }else{
                        rootNode = mapper.readTree(HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/api/image/role/attribute?server="+server+"&name="+command[1]));
                    }
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            dataNode = rootNode.path("data");
                           return new String[]{"image",dataNode.get("url").asText()};
                        default:
                            return new String[]{"text",rootNode.get("msg") != null ? rootNode.get("msg").asText() : rootNode.get("message") != null ? rootNode.get("message").asText() : "找渡渡鸟来修"};
                    }
                //endregion
                //region 魔盒文章索引
                case "魔盒":
                case "搜索":
                case "文章":
                case "魔盒文章":
                    rootNode = mapper.readTree(HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/jx3box/search?keyword="+command[1]));
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            dataNode = rootNode.path("data");
                            if (rootNode.get("data") != null || !rootNode.get("data").isEmpty()) {
                                StringBuilder aa = new StringBuilder("以下是关于" + command[1] + "的魔盒搜索结果：\\n");

                                for (int i = 0; i < rootNode.get("data").size(); i++) {
                                    aa.append("").append(rootNode.get("data").get(i).get("postTitle").asText());
                                    aa.append("\\n").append(rootNode.get("data").get(i).get("url").asText()+"\\n");
                                }
                                return new String[]{"text",aa.toString()};
                            }else{
                                return new String[]{"text","没有在魔盒找到结果，请更换关键词"};
                            }
                        default:
                            return new String[]{"text","数据异常，可能是因为渡渡鸟没人陪！"};
                    }
                //endregion
                //region 副本记录
                case "副本击杀":
                case "副本进度":
                case "击杀记录":
                case "进度":
                    if (command.length>=3){
                        rootNode = mapper.readTree(HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/api/image/role/teamCdList?server="+command[1]+"&name="+command[2]));
                    }else{
                        rootNode = mapper.readTree(HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/api/image/role/teamCdList?server="+server+"&name="+command[1]));
                    }
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            dataNode = rootNode.path("data");
                           return new String[]{"image",dataNode.get("url").asText()};
                        default:
                            temp = "找渡渡鸟来修";
                    }
                //endregion
                //region 服务器开服查询
                case "开服":
                    rootNode = mapper.readTree(HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/api/data/serverCheck?server="+command[1]));
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            if (rootNode.get("data").get("status").asInt() >0){
                                temp = server+" 当前状态：开启";
                            }else{
                                temp = server+" 当前状态：维护";
                            }
                            return new String[]{"text",temp};
                        default:
                            return new String[]{"text",rootNode.get("msg") != null ? rootNode.get("msg").asText() : rootNode.get("message") != null ? rootNode.get("message").asText() : "找渡渡鸟来修"};
                    }
                //endregion
                //region 烟花
                case "烟花":
                    if (command.length>=3){
                        rootNode = mapper.readTree(HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/api/image/watch/record?server="+command[1]+"&name="+command[2]));
                    }else{
                        rootNode = mapper.readTree(HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/api/image/watch/record?server="+server+"&name="+command[1]));
                    }
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            dataNode = rootNode.path("data");
                           return new String[]{"image",dataNode.get("url").asText()};
                        default:
                            return new String[]{"text",rootNode.get("msg") != null ? rootNode.get("msg").asText() : rootNode.get("message") != null ? rootNode.get("message").asText() : "找渡渡鸟来修"};
                    }
                //endregion
                //region 沙盘
                case "沙盘":
                    rootNode = mapper.readTree(HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/api/image/server/sand?server="+command[1]+"&desc=我只是个平凡的鸽鸽罢了"));
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            dataNode = rootNode.path("data");
                           return new String[]{"image",dataNode.get("url").asText()};
                        default:
                           return new String[]{"text",rootNode.get("msg") != null ? rootNode.get("msg").asText() : rootNode.get("message") != null ? rootNode.get("message").asText() : "找渡渡鸟来修"};
                    }
                //endregion
                //region 成就相关实现
                case "成就":
                    if (command.length >= 4){
                        rootNode = mapper.readTree(HttpTool.getData("http://api.muyz.xyz:25555/api/image/role/achievement?server="+command[1]+"&role="+command[2]+"&name="+command[3]));
                    }else {
                        rootNode = mapper.readTree(HttpTool.getData("http://api.muyz.xyz:25555/api/image/role/achievement?server="+server+"&role="+command[1]+"&name="+command[2]));
                    }
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            dataNode = rootNode.path("data");
                           return new String[]{"image",dataNode.get("url").asText()};
                        default:
                            return new String[]{"text",rootNode.get("msg") != null ? rootNode.get("msg").asText() : rootNode.get("message") != null ? rootNode.get("message").asText() : "找渡渡鸟来修"};
                    }
                //endregion
                //region 宏
                // TODO：待修复
//                case "宏":
//                    rootNode = mapper.readTree(HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/api/data/macros/jx3api?kungfu="+command[1]));
//                    switch (rootNode.get("code").asInt()){
//                        case 200:
//                            dataNode = rootNode.path("data");
//                            mt = new MessageType("text",dataNode.get("context").asText().replace("\n","\\n").replace("\\r","\\r"));
//                            break;
//                        default:
//                            return new String[]{"text","数据异常，可能是因为渡渡鸟没人陪！"};
//                    }
//                    break;
                //endregion
                //region 掉落
                case "掉落":
                    if (command.length>=3){
                        rootNode = mapper.readTree(HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/api/image/valuables/statistical?server="+command[1]+"&name="+command[2]));
                    }else{
                        rootNode = mapper.readTree(HttpTool.getData(Main.configProperties.getProperty("config.serverUrl")+"/api/image/valuables/statistical?server="+server+"&name="+command[1]));
                    }
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            dataNode = rootNode.path("data");
                           return new String[]{"image",dataNode.get("url").asText()};
                        default:
                            return new String[]{"text",rootNode.get("msg") != null ? rootNode.get("msg").asText() : rootNode.get("message") != null ? rootNode.get("message").asText() : "找渡渡鸟来修"};
                    }
                //endregion
                //region 鸽子盘
                case "渡渡鸟":
                    switch (command[1]){
                        case "搜索":
                            if (command[2] != null) {
                                rootNode = mapper.readTree(HttpTool.postData(Main.configProperties.getProperty("config.alistUrl")+"/api/fs/search",String.format("{\"parent\":\"/\",\"keywords\":\"%s\",\"scope\":0,\"page\":1,\"per_page\":100,\"password\":\"\"}",command[2])));
                                if (rootNode.get("code").asInt() == 200){
                                    if (rootNode.get("data").get("total").asInt()>0){
                                        dataNode = rootNode.get("data").get("content");
                                        String tempMessage = "以下是搜索到的渡渡鸟珍藏：\\n";
                                        for (int i = 0; i < rootNode.get("data").get("total").asInt(); i++) {
                                            if (i == 10){
                                                tempMessage += "非特殊用户仅展示不超过10条";
                                                return new String[]{"text",tempMessage};
                                            }
                                            if (!dataNode.get(i).get("is_dir").asBoolean()){
                                                tempMessage += String.format("%s - 下载地址：%s\\n",dataNode.get(i).get("name"),Main.configProperties.getProperty("config.alistUrl")+"/d"+dataNode.get(i).get("parent")+"/"+dataNode.get(i).get("name"));
                                            }
                                        }
                                    }else{
                                        return new String[]{"text","没有找到相关数据！"};
                                    }
                                }
                            }else{
                                return new String[]{"text","请输入正确的搜索关键词阿喂！！！"};
                            }
                            break;
                    }
                    break;
                //endregion
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new String[]{"",""};
    }
}

