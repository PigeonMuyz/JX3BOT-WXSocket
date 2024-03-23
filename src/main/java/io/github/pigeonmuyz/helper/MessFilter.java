package io.github.pigeonmuyz.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 消息过滤&处理器
 */
public class MessFilter {
    private static final Logger log = LogManager.getLogger(MessFilter.class);
    public static Map<String, String> languageFilter;

    /**
     * 初始化语言过滤器
     * @return 正常被处理的文本
     */
    public static void initLanguageFilter(){
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
        tempMap.put("烟花","烟花");
        tempMap.put("fireworks","烟花");
        tempMap.put("煙花","烟花");
        tempMap.put("花火","烟花");
        //endregion

        //region 掉落
        tempMap.put("掉落","掉落");
        tempMap.put("stat","掉落");
        //endregion

        //region 测试
        tempMap.put("test","test");
        tempMap.put("测试","test");
        tempMap.put("測試","test");
        //endregion

        //region 魔盒
        tempMap.put("搜索","魔盒");
        tempMap.put("JX3BOX","魔盒");
        tempMap.put("魔盒","魔盒");
        //endregion

        //region 魔盒
        tempMap.put("日志","日志");
        tempMap.put("updatelog","日志");
        tempMap.put("日誌","日志");
        //endregion

        //region 绑定
        tempMap.put("绑定","绑定");
        tempMap.put("bind","绑定");
        tempMap.put("日誌","日志");
        //endregion
        log.info("语言过滤器初始化成功");
        languageFilter =  tempMap;
    }

    /**
     * 语义识别器
     * @param input 接收到的文本
     * @return 处理好的数组&未能被处理的数组
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
                log.debug("语言筛选完毕"+new String[]{languageFilter.getOrDefault(matcher.group(2),matcher.group(2)), matcher.group(1)});
                return new String[]{matcher.group(2), matcher.group(1)};
            }
        }
        log.debug("原始数据："+input);
        return new String[]{input};
    }
}
