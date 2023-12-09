package io.github.pigeonmuyz.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.pigeonmuyz.entity.MessageType;

import java.text.SimpleDateFormat;

public class MessageTool {

    static ObjectMapper mapper = new ObjectMapper();

    /**
     * 时间格式
     */
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    static JsonNode rootNode;
    static JsonNode dataNode;
    static MessageType mt;
static String temp;
    public static MessageType singleCommand(String command, String userID, String channelID, String server) {
        try {
            switch(command){
                //region 日常
                case "日常":
                    
                    rootNode = mapper.readTree(HttpTool.getData("http://pigeon-server-developer:25555/api/daily?server="+server));
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            dataNode = rootNode.path("data");
                            temp = "秘境日常：" + dataNode.get("war").asText() + "\n"
                                    + "公共日常：" + dataNode.get("team").get(0).asText() + "\n"
                                    + "PVP日常\n"
                                    + "矿车：跨服•烂柯山\n"
                                    + "战场：" + dataNode.get("battle").asText() + "\n"
                                    + "PVX日常\n"
                                    + (dataNode.get("draw").asText().isEmpty() || dataNode.get("draw").asText().equals("null") ? "美人图：无\n" : "美人图：" + dataNode.get("draw").asText() + "\n")
                                    + "门派事件：" + dataNode.get("school").asText() + "\n"
                                    + String.format("福源宠物：%s;%s;%s\n", dataNode.get("luck").get(0).asText(), dataNode.get("luck").get(1).asText(), dataNode.get("luck").get(2).asText())
                                    + "PVE周常\n"
                                    + "五人秘境：" + dataNode.get("team").get(1).asText() + "\n"
                                    + "十人秘境：" + dataNode.get("team").get(2).asText() + "\n"
                                    + "今天是" + dataNode.get("date").asText() + " 星期" + dataNode.get("week").asText();
                            mt = new MessageType("text",temp);
                            break;
                        default:
                            mt = new MessageType("text","服务器响应异常，请联系管理或者核对参数后再次重试");
                            break;
                    }
                    break;
                //endregion
                //region 金价
                case "金价":
                    
                    rootNode = mapper.readTree(HttpTool.getData("http://pigeon-server-developer:25555/image/api/trade/demon?server="+server));
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            dataNode = rootNode.path("data");
                            mt = new MessageType("image",dataNode.get("url").asText());
                            break;
                        default:
                            mt = new MessageType("text","服务器响应异常，请联系管理或者核对参数后再次重试");
                            break;
                    }
                    break;
                //endregion
                //region 花价
                case "花价":
                    
                    rootNode = mapper.readTree(HttpTool.getData("http://pigeon-server-developer:25555/image/api/home/flower?server="+server));
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            dataNode = rootNode.path("data");
                            mt = new MessageType("image",dataNode.get("url").asText());
                            break;
                        default:
                            mt = new MessageType("text","服务器响应异常，请联系管理或者核对参数后再次重试");
                            break;
                    }
                    break;
                //endregion
                //region 团队招募
                case "招募":
                case "团队招募":
                    
                    rootNode = mapper.readTree(HttpTool.getData("http://pigeon-server-developer:25555/api/teamactivity?server="+server));
                    System.out.println(rootNode.toString());
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            dataNode = rootNode.path("data");
                            mt = new MessageType("image",dataNode.get("url").asText());
                            break;
                        default:
                            mt = new MessageType("text","服务器响应异常，请联系管理或者核对参数后再次重试");
                            break;
                    }
                    break;
                //endregion
                //region 楚天行侠
                case "楚天行侠":
                case "楚天社":
                case "行侠":
                    
                    rootNode = mapper.readTree(HttpTool.getData("http://pigeon-server-developer:25555/api/celebrities"));
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            dataNode = rootNode.path("data");
                            temp = "现在正在进行：" + dataNode.get("event").get(0).get("desc").asText() + "\n"
                                    + String.format("地点：%s · %s\n", dataNode.get("event").get(0).get("map_name").asText(), dataNode.get("event").get(0).get("site").asText())
                                    + "开始时间：" + dataNode.get("event").get(0).get("time").asText() + "\n"
                                    + "下一次将要进行：" + dataNode.get("event").get(1).get("desc").asText() + "\n"
                                    + String.format("地点：%s · %s\n", dataNode.get("event").get(1).get("map_name").asText(), dataNode.get("event").get(1).get("site").asText())
                                    + "开始时间：" + dataNode.get("event").get(1).get("time").asText() + "\n"
                                    + "之后将要进行：" + dataNode.get("event").get(2).get("desc").asText() + "\n"
                                    + String.format("地点：%s · %s\n", dataNode.get("event").get(2).get("map_name").asText(), dataNode.get("event").get(2).get("site").asText())
                                    + "开始时间：" + dataNode.get("event").get(2).get("time").asText();
                            mt = new MessageType("text",temp);
                            break;
                        default:
                            mt = new MessageType("text","服务器响应异常，请联系管理或者核对参数后再次重试");
                            break;
                    }
                    break;
                //endregion
                //region 百战
                case "百战":
                    
                    rootNode = mapper.readTree(HttpTool.getData("http://pigeon-server-developer:25555/image/api/active/monster"));
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            dataNode = rootNode.path("data");
                            mt = new MessageType("image",dataNode.get("url").asText());
                            break;
                        default:
                            mt = new MessageType("text","服务器响应异常，请联系管理或者核对参数后再次重试");
                            break;
                    }
                    break;
                //endregion
                //TODO: 临时抽调
                //region 更新日志
                case "日志":
                case "更新日志":
                    String temp = "剑三鸽鸽Re 1.0 （开发版本号：8）\n"
                            + "1. 修复了部分指令反馈错误的问题！！\n"
                            + "2. 优化了部分指令反馈排版问题！！\n"
                            + "3. 增加了魔盒文章查询！（仅支持工具，相关副本和职业攻略帖子）\n"
                            + "4. 终于增加了交易行价格！！！（仅支持工具，相关副本和职业攻略帖子）\n"
                            + "5. I人拯救计划（第二次重构版的机器人）正在进行中！可以密聊作者获取体验资格\n"
                            + "6. I人拯救计划将附带独一无二的iPhone通知推送！";
                    mt = new MessageType("text",temp);

                    break;
                //endregion
                //TODO: 临时借用
                //region 帮助
                case "帮助":
                case "功能":
                case "指令":
                    temp = "通用类\n"
                            + "赞助指令：赞助\n"
                            + "最新公告：公告\n"
                            + "查询日常：日常\n"
                            + "绑定服务器：绑定 [服务器(必选)]\n"
                            + "外观价格：外观 [物品名称|物品别名(必选)]\n"
                            + "角色装备：查询 [服务器(可选)] [玩家名字(必选)]\n"
                            + "查询金价：金价 [服务器(可选)]\n"
                            + "查询奇遇：奇遇 [服务器(可选)] [玩家名字(必选)]\n"
                            + "查询物价：交易行 [服务器(可选)] [物品名字(必选)]\n"
                            + "查询魔盒：魔盒 [关键字（必选）]\n"
                            + "订阅监控：全部订阅[首次订阅将会立即生效]\n"
                            + "团队招募：招募|团队招募 [服务器名（可选）] [副本名(可选，例：西津渡)]\n"
                            + "查询日志：[日志|更新日志|Version]\n"
                            + "PVE类\n"
                            + "百战指令：百战\n"
                            + "查宏指令：宏 [心法名（可以使用别称！！！）]\n"
                            + "PVP类\n"
                            + "JJC战绩：JJC｜战绩 [玩家名(必选)] [模式，例如22｜33｜55(必选)] [服务器(可选)]\n"
                            + "战争沙盘：沙盘 [服务器(可选)]\n"
                            + "PVX类\n"
                            + "查询花价：花价 \n"
                            + "成就进度：成就 [服务器(机器人绑定过服务器的不需要输入)] [玩家名] [成就名或成就系列(例：沈剑心)]\n"
                            + "楚天行侠：行侠|楚天社|楚天行侠\n"
                            + "宠物游历：游历 [地图(必选)]\n"
                            + "剑三鸽鸽 Remake 1.0";
                    mt = new MessageType("text",temp);
                    break;
                //endregion
                //region 服务器开服查询
                case "开服":
                    rootNode = mapper.readTree(HttpTool.getData("http://pigeon-server-developer:25555/api/serverCheck?server="+server));
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            if (rootNode.get("data").get("status").asInt() >0){
                                temp = server+" 当前状态：开启";
                            }else{
                                temp = server+" 当前状态：维护";
                            }
                            mt = new MessageType("text",temp);
                        break;
                        default:
                            mt = new MessageType("text","服务器响应异常，请联系管理或者核对参数后再次重试");
                            break;
                    }
                    break;
                //endregion
                //region 公告
                case "公告":
                    
                    rootNode = mapper.readTree(HttpTool.getData("http://pigeon-server-developer:25555/image/api/web/news/announce"));
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            dataNode = rootNode.path("data");
                            mt = new MessageType("image",dataNode.get("url").asText());
                            break;
                        default:
                            mt = new MessageType("text","服务器响应异常，请联系管理或者核对参数后再次重试");
                            break;
                    }
                    break;
                //endregion
                //region 骚话
                case "骚话":
                        JsonNode rootNode = mapper.readTree(HttpTool.getData("http://pigeon-server-developer:25555/api/saohua"));
                        JsonNode dataNode = rootNode.path("data");
                        //简单的随机判断
                        if (Math.random()>0.5){
                            temp = "就你小子想要骚话是吧";
                        }else{
                            temp = dataNode.get("text").asText();
                        }
                    mt = new MessageType("text",temp);
                    break;
                    //endregion
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return mt;
    }

    public static MessageType multiCommand(String[] command,String userID,String guildID,String server) {
        try{
            switch (command[0]){
                //region 绑定服务器
                case "绑定":
                    if (guildID != null){
                        JsonNode jn = mapper.readTree(HttpTool.getData("http://pigeon-server-developer:25555/user/get?GroupID="+guildID));
                        if (jn.get("code").asInt() == 200){
                            //群组已经绑定过了，走频道更新流程
                            HttpTool.getData("http://pigeon-server-developer:25555/user/update?GroupID="+guildID+"&server="+command[1]);
                            mt = new MessageType("text","绑定更新成功");
                        }else{
                            HttpTool.getData("http://pigeon-server-developer:25555/user/add?GroupID="+guildID+"&server="+command[1]);
                            mt = new MessageType("text","绑定成功");
                        }
                    }

                    rootNode = mapper.readTree(HttpTool.getData("http://pigeon-server-developer:25555/user/get?WXID="+userID));
                    if (rootNode.get("code").asInt() == 200 && rootNode.get("data").get(0).get("WXID") != null){
                        if (guildID == null && rootNode.get("data").get(0).get("server") != null){
                            //用户已经绑定过了，走用户更新流程
                            HttpTool.getData("http://pigeon-server-developer:25555/user/update?WXID="+userID+"&server="+command[1]);
                            mt = new MessageType("text","绑定更新成功");
                        }
                    }else{
                        //用户未绑定，走用户绑定流程
                        HttpTool.getData("http://pigeon-server-developer:25555/user/add?WXID="+userID+"&server="+command[1]);
                        mt = new MessageType("text","绑定成功");
                    }
                    break;
                //endregion
                //region 功能开关
                case "开启":
                case "关闭":
                    boolean status;
                    if(command[0].equals("开启")){
                        status = true;
                    }else{
                        status = false;
                    }
                    switch (command[1]){
                        case "版本更新":
                            if (guildID != null){
                                HttpTool.getData("http://pigeon-server-developer:25555/user/update?GroupID="+guildID+"&VersionUpdate="+status);
                            }else{
                                HttpTool.getData("http://pigeon-server-developer:25555/user/update?WXID="+userID+"&VersionUpdate="+status);
                            }
                            if (status){
                                temp = command[1]+"已开启！";
                            }else{
                                temp = command[1]+"已关闭！";
                            }
                            mt = new MessageType("text",temp);
                            break;
                        case "开服监控":
                            if (guildID != null){
                                HttpTool.getData("http://pigeon-server-developer:25555/user/update?GroupID="+guildID+"&ServerStatus="+status);
                            }else{
                                HttpTool.getData("http://pigeon-server-developer:25555/user/update?WXID="+userID+"&ServerStatus="+status);
                            }
                            if (status){
                                temp = command[1]+"已开启！";
                            }else{
                                temp = command[1]+"已关闭！";
                            }
                            mt = new MessageType("text",temp);
;
                            break;
                        case "新闻监控":
                            if (guildID != null){
                                HttpTool.getData("http://pigeon-server-developer:25555/user/update?GroupID="+guildID+"&News="+status);
                            }else{
                                HttpTool.getData("http://pigeon-server-developer:25555/user/update?WXID="+userID+"&News="+status);
                            }
                            if (status){
                                temp = command[1]+"已开启！";
                            }else{
                                temp = command[1]+"已关闭！";
                            }
                            mt = new MessageType("text",temp);
;
                            break;
                        case "818监控":
                            if (guildID != null){
                                HttpTool.getData("http://pigeon-server-developer:25555/user/update?GroupID="+guildID+"&forumPost="+status);
                            }else{
                                HttpTool.getData("http://pigeon-server-developer:25555/user/update?WXID="+userID+"&forumPost="+status);
                            }
                            if (status){
                                temp = command[1]+"已开启！";
                            }else{
                                temp = command[1]+"已关闭！";
                            }
                            mt = new MessageType("text",temp);
;
                            break;
                        case "先锋测试":
                            if (guildID != null){
                                HttpTool.getData("http://pigeon-server-developer:25555/user/update?GroupID="+guildID+"&bossRefresh="+status);
                            }else{
                                HttpTool.getData("http://pigeon-server-developer:25555/user/update?WXID="+userID+"&bossRefresh="+status);
                            }
                            if (status){
                                temp = command[1]+"已开启！";
                            }else{
                                temp = command[1]+"已关闭！";
                            }
                            mt = new MessageType("text",temp);
;
                            break;
//                        case "云从事件":
//                            if (guildID != null){
//                                HttpTool.getData("http://pigeon-server-developer:25555/user/update?GroupID="+guildID+"&bossRefresh="+status);
//                            }else{
//                                HttpTool.getData("http://pigeon-server-developer:25555/user/update?WXID="+userID+"&bossRefresh="+status);
//                            }
//                            break;
//                        case "关隘预告":
//                            if (guildID != null){
//                                HttpTool.getData("http://pigeon-server-developer:25555/user/update?GroupID="+guildID+"&bossRefresh="+status);
//                            }else{
//                                HttpTool.getData("http://pigeon-server-developer:25555/user/update?WXID="+userID+"&bossRefresh="+status);
//                            }
                        case "手机通知":
                            if (guildID != null){
                                //这里是群聊，群聊绝对不要绑定！
                            }else{
                                rootNode = mapper.readTree(HttpTool.getData("http://pigeon-server-developer:25555/user/get?WXID="+userID));
                                HttpTool.getData("http://pigeon-server-developer:25555/user/update?WXID="+userID+"&BarkNotify="+status);
                                if (rootNode.get("code").asInt() == 200 && rootNode.get("data").get(0).get("barkKey") != null){
                                    HttpTool.getData("http://pigeon-server-developer:25555/user/update?WXID="+userID+"&barkNotify="+status);
                                    rootNode = mapper.readTree(HttpTool.getData("http://pigeon-server-developer:25555/user/get?WXID="+userID));
                                    if (rootNode.get("data").get(0).get("barkNotify").asBoolean()){
                                        temp = "Bark推送已经开启";
                                    }else{
                                        temp = "Bark推送已经关闭";

                                    }
                                }else{
                                    temp = "请先绑定BarkKey";
                                }
                            }
                            mt = new MessageType("text",temp);
                            
                            break;
                    }
                    break;
                //endregion
                //region 状态查询
                case "当前状态":
                case "功能状态":
                case "服务":
                    if (guildID != null){
                        rootNode = mapper.readTree(HttpTool.getData("http://pigeon-server-developer:25555/user/get?GroupID="+guildID));
                        if (rootNode.get("data").isEmpty() || rootNode.get(0).get("GroupID") == null) {
                            temp = "当前频道未绑定服务器并开启任何功能";
                        }else{
                            temp = "当前频道功能开启状态：";
                            if (rootNode.get(0).get("VersionUpdate").asBoolean()){
                                temp = "版本更新：开启";
                            }else{
                                temp = "版本更新：关闭";
                            }
                            if (rootNode.get(0).get("ServerStatus").asBoolean()){
                                temp = "开服监控：开启";
                            }else{
                                temp = "开服监控：关闭";
                            }
                            if (rootNode.get(0).get("News").asBoolean()){
                                temp = "新闻监控：开启";
                            }else{
                                temp = "新闻监控：关闭";
                            }
                            if (rootNode.get(0).get("forumPost").asBoolean()){
                                temp = "818监控：开启";
                            }else{
                                temp = "818监控：关闭";
                            }
                            if (rootNode.get(0).get("bossRefresh").asBoolean()){
                                temp = "先锋测试：开启";
                            }else{
                                temp = "先锋测试：关闭";
                            }
                        }
                    }else{
                        rootNode = mapper.readTree(HttpTool.getData("http://pigeon-server-developer:25555/user/get?WXID="+userID));
                        if (rootNode.get("data").isEmpty() || rootNode.get(0).get("WXID") == null) {
                            temp = "当前用户未绑定服务器，并开启任何功能";
                        }else{
                            temp = "当前用户功能开启状态：";
                            if (rootNode.get(0).get("VersionUpdate").asBoolean()){
                                temp = "版本更新：开启";
                            }else{
                                temp = "版本更新：关闭";
                            }
                            if (rootNode.get(0).get("ServerStatus").asBoolean()){
                                temp = "开服监控：开启";
                            }else{
                                temp = "开服监控：关闭";
                            }
                            if (rootNode.get(0).get("News").asBoolean()){
                                temp = "新闻监控：开启";
                            }else{
                                temp = "新闻监控：关闭";
                            }
                            if (rootNode.get(0).get("forumPost").asBoolean()){
                                temp = "818监控：开启";
                            }else{
                                temp = "818监控：关闭";
                            }
                            if (rootNode.get(0).get("bossRefresh").asBoolean()){
                                temp = "先锋测试：开启";
                            }else{
                                temp = "先锋测试：关闭";
                            }
                            if (rootNode.get(0).get("barkNotify").asBoolean()){
                                temp = "手机推送：开启";
                            }else{
                                temp = "手机推送：关闭";
                            }
                        }
                    }
                    mt = new MessageType("text",temp);
                    break;
                //endregion
                //region 外观
                case "外观":
                case "万宝楼":
                    
                    rootNode = mapper.readTree(HttpTool.getData("http://pigeon-server-developer:25555/image/api/trade/record?name="+command[1]));
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            dataNode = rootNode.path("data");
                            mt = new MessageType("image",dataNode.get("url").asText());
                            break;
                        default:
                            mt = new MessageType("text","服务器响应异常，请联系管理或者核对参数后再次重试");
                            break;
                    }
                    break;
                //endregion
                //region Bark绑定
                case "Bark":
                case "消息推送密钥":
                case "消息推送":
                    rootNode = mapper.readTree(HttpTool.getData("http://pigeon-server-developer:25555/user/get?WXID="+userID));
                    if (rootNode.get("code").asInt() == 200 && rootNode.get("data").get(0).get("WXID") != null){
                        if (guildID == null && rootNode.get("data").get(0).get("barkKey") != null){
                            //用户已经绑定过了，走用户更新流程
                            HttpTool.getData("http://pigeon-server-developer:25555/user/update?WXID="+userID+"&barkKey="+command[1]+"&barkNotify=true");
                            temp = "更改成功！将开启推送！";
                        }
                    }else{
                        //用户未绑定，走用户绑定流程
                        HttpTool.getData("http://pigeon-server-developer:25555/user/add?WXID="+userID+"&barkKey="+command[1]);
                        HttpTool.getData("http://pigeon-server-developer:25555/user/update?WXID="+userID+"&barkNotify=true");
                        temp = "绑定成功！默认将开启推送！";
                    }
                    mt = new MessageType("text",temp);
                    break;
                //endregion
                //region 奇遇
                case "奇遇":
                    
                    if (command.length>=3){
                        rootNode = mapper.readTree(HttpTool.getData("http://pigeon-server-developer:25555/image/api/luck/adventure?server="+command[1]+"&name="+command[2]+"&filter=1"));
                    }else{
                        rootNode = mapper.readTree(HttpTool.getData("http://pigeon-server-developer:25555/image/api/luck/adventure?server="+server+"&name="+command[1]+"&filter=1"));
                    }
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            dataNode = rootNode.path("data");
                            mt = new MessageType("image",dataNode.get("url").asText());
                            break;
                        default:
                            mt = new MessageType("text","服务器响应异常，请联系管理或者核对参数后再次重试");
                            break;
                    }
                break;
                //endregion
                //region JJC战绩
                case "名剑":
                case "战绩":
                case "JJC":
                    
                    if (command.length >= 4){
                        rootNode = mapper.readTree(HttpTool.getData("http://pigeon-server-developer:25555/image/api/match/recent?server="+command[1]+"&name="+command[2]+"&robot=剑三咕咕"+"&mode="+command[3]));
                    }else {
                        rootNode = mapper.readTree(HttpTool.getData("http://pigeon-server-developer:25555/image/api/match/recent?server="+server+"&name="+command[1]+"&mode="+command[2]));
                    }
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            dataNode = rootNode.path("data");
                            mt = new MessageType("image",dataNode.get("url").asText());
                            break;
                        default:
                            mt = new MessageType("text","服务器响应异常，请联系管理或者核对参数后再次重试");
                            break;
                    }
                    break;
                //endregion
                //region 金价
                case "金价":
                    
                    rootNode = mapper.readTree(HttpTool.getData("http://pigeon-server-developer:25555/image/api/trade/demon?server="+command[1]));
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            dataNode = rootNode.path("data");
                            mt = new MessageType("image",dataNode.get("url").asText());
                            break;
                        default:
                            mt = new MessageType("text","服务器响应异常，请联系管理或者核对参数后再次重试");
                            break;
                    }
                    break;
                //endregion
                //region 团队招募
                case "招募":
                case "团队招募":
                    
                    if (command.length>=3){
                        rootNode = mapper.readTree(HttpTool.getData("http://pigeon-server-developer:25555/image/api/member/recruit?server="+command[1]+"&keyword="+command[2]));
                    }else{
                        rootNode = mapper.readTree(HttpTool.getData("http://pigeon-server-developer:25555/image/api/member/recruit?server="+command[1]));
                        if (rootNode.get("code").asInt() != 200){
                            rootNode = mapper.readTree(HttpTool.getData("http://pigeon-server-developer:25555/image/api/member/recruit?server="+server+"&keyword="+command[1]));
                        }
                    }
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            dataNode = rootNode.path("data");
                            mt = new MessageType("image",dataNode.get("url").asText());
                            break;
                        default:
                            mt = new MessageType("text","服务器响应异常，请联系管理或者核对参数后再次重试");
                            break;
                    }
                    break;
                //endregion
                //region 装备查询
                case "查询":
                    
                    if (command.length>=3){
                        rootNode = mapper.readTree(HttpTool.getData("http://pigeon-server-developer:25555/image/api/role/attribute?server="+command[1]+"&name="+command[2]));
                    }else{
                        rootNode = mapper.readTree(HttpTool.getData("http://pigeon-server-developer:25555/image/api/role/attribute?server="+server+"&name="+command[1]));
                    }
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            dataNode = rootNode.path("data");
                            mt = new MessageType("image",dataNode.get("url").asText());
                            break;
                        default:
                            mt = new MessageType("text","服务器响应异常，请联系管理或者核对参数后再次重试");
                            break;
                    }
                    break;
                //endregion
                //region 魔盒文章索引
                case "魔盒":
                case "搜索":
                case "文章":
                case "魔盒文章":
                    
                    rootNode = mapper.readTree(HttpTool.getData("http://pigeon-server-developer:25555/jx3box/search?keyword="+command[1]));
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            dataNode = rootNode.path("data");

                            if (rootNode.get("data").size() > 0 || !rootNode.get("data").isEmpty()) {
                                StringBuilder aa = new StringBuilder("以下是关于" + command[1] + "的魔盒搜索结果：\n");

                                for (int i = 0; i < rootNode.get("data").size(); i++) {
                                    aa.append("").append(rootNode.get("data").get(i).get("postTitle").asText());
                                    aa.append("\n").append(rootNode.get("data").get(i).get("url").asText()+"\n");
                                }
                                mt = new MessageType("text",aa.toString());
                            }else{
                                mt = new MessageType("text","没有在魔盒找到结果，请更换关键词");
                            }
;
                            break;
                        default:
                            mt = new MessageType("text","服务器响应异常，请联系管理或者核对参数后再次重试");
                            break;
                    }
                    break;
                //endregion
                //region 副本记录
                case "副本击杀":
                case "副本进度":
                case "击杀记录":
                case "进度":
                    
                    if (command.length>=3){
                        rootNode = mapper.readTree(HttpTool.getData("http://pigeon-server-developer:25555/image/api/role/teamCdList?server="+command[1]+"&name="+command[2]));
                    }else{
                        rootNode = mapper.readTree(HttpTool.getData("http://pigeon-server-developer:25555/image/api/role/teamCdList?server="+server+"&name="+command[1]));
                    }
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            dataNode = rootNode.path("data");
                            mt = new MessageType("image",dataNode.get("url").asText());
                            break;
                        default:
                            temp = "服务器响应异常，请联系管理或者核对参数后再次重试";
                            break;
                    }
                    break;
                //endregion
                //region 服务器开服查询
                case "开服":
                    
                    rootNode = mapper.readTree(HttpTool.getData("http://pigeon-server-developer:25555/api/serverCheck?server="+command[1]));
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            if (rootNode.get("data").get("status").asInt() >0){
                                temp = server+" 当前状态：开启";
                            }else{
                                temp = server+" 当前状态：维护";
                            }
                            mt = new MessageType("text",temp);

                            break;
                        default:
                            mt = new MessageType("text","服务器响应异常，请联系管理或者核对参数后再次重试");
                            break;
                    }
                    break;
                //endregion
                //region 烟花
                case "烟花":
                    
                    if (command.length>=3){
                        rootNode = mapper.readTree(HttpTool.getData("http://pigeon-server-developer:25555/image/api/watch/record?server="+command[1]+"&name="+command[2]));
                    }else{
                        rootNode = mapper.readTree(HttpTool.getData("http://pigeon-server-developer:25555/image/api/watch/record?server="+server+"&name="+command[1]));
                    }
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            dataNode = rootNode.path("data");
                            mt = new MessageType("image",dataNode.get("url").asText());
                            break;
                        default:
                            mt = new MessageType("text","服务器响应异常，请联系管理或者核对参数后再次重试");
                            break;
                    }
                    break;
                //endregion
                //region 沙盘
                case "沙盘":
                    
                    rootNode = mapper.readTree(HttpTool.getData("http://pigeon-server-developer:25555/image/api/server/sand?server="+command[1]+"&desc=我只是个平凡的鸽鸽罢了"));
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            dataNode = rootNode.path("data");
                            mt = new MessageType("image",dataNode.get("url").asText());
                            break;
                        default:
                            mt = new MessageType("text","服务器响应异常，请联系管理或者核对参数后再次重试");
                    }
                    break;
                //endregion
                //region 成就相关实现
                case "成就":
                    if (command.length >= 4){
                        rootNode = mapper.readTree(HttpTool.getData("http://api.muyz.xyz:25555/image/api/role/achievement?server="+command[1]+"&role="+command[2]+"&name="+command[3]));
                    }else {
                        rootNode = mapper.readTree(HttpTool.getData("http://api.muyz.xyz:25555/image/api/role/achievement?server="+server+"&role="+command[1]+"&name="+command[2]));
                    }
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            dataNode = rootNode.path("data");
                            mt = new MessageType("image",dataNode.get("url").asText());
                            break;
                        default:
                            mt = new MessageType("text","服务器响应异常，请联系管理或者核对参数后再次重试");
                            break;
                    }
                    break;
                //endregion
                //region 宏
                case "宏":
                    rootNode = mapper.readTree(HttpTool.getData("http://pigeon-server-developer:25555/api/macros/jx3api?kungfu="+command[1]));
                    switch (rootNode.get("code").asInt()){
                        case 200:
                            dataNode = rootNode.path("data");
                            mt = new MessageType("text",dataNode.get("context").asText());
                            break;
                        default:
                            mt = new MessageType("text","服务器响应异常，请联系管理或者核对参数后再次重试");
                            break;
                    }
                    break;
                //endregion

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return mt;
    }
}