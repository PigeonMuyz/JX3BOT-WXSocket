package io.github.pigeonmuyz.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.pigeonmuyz.Main;
import io.github.pigeonmuyz.tools.HttpTool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class WeChatHelper {

    private final static Logger log = LogManager.getLogger(WeChatHelper.class);

    /**
     * 上传文件
     * @param url 链接
     * @return 文件id
     */
    private static String getFileId(String url){
        try {
            JsonNode jn = new ObjectMapper().readTree(HttpTool.postData("http://pigeon-wechat:8000/",String.format("{\"action\":\"upload_file\",\"params\":{\"type\":\"url\",\"name\":\"1.png\",\"url\":\"%s\"}}",url)));
            log.debug("图片上传成功");
            return jn.get("data").get("file_id").asText();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 发送消息
     * @param wechatId
     * @param isGroup
     * @param messageType
     * @param message
     */
    public static void sendMessage(String wechatId, Boolean isGroup, String messageType, String message){
        log.debug(wechatId,isGroup,messageType,message);
        String wechatRollbackUrl = Main.configProperties.getProperty("config.wechatRollbackUrl");
        try {
            switch (messageType){
                case "image":
                    String fileIdTemp = getFileId(message);
                    log.debug("ImageID: "+fileIdTemp);
                    if (isGroup){
                        HttpTool.postData(wechatRollbackUrl,String.format("{\"action\":\"send_message\",\"params\":{\"detail_type\":\"group\",\"group_id\":\"%s\",\"message\":[{\"type\":\"image\",\"data\":{\"file_id\":\"%s\"}}]}}",wechatId,fileIdTemp));
                    }else{
                        HttpTool.postData(wechatRollbackUrl,String.format("{\"action\":\"send_message\",\"params\":{\"detail_type\":\"private\",\"user_id\":\"%s\",\"message\":[{\"type\":\"image\",\"data\":{\"file_id\":\"%s\"}}]}}",wechatId,fileIdTemp));
                    }
                    break;
                case "text":
                    if (isGroup){
                        HttpTool.postData(wechatRollbackUrl,String.format("{\"action\":\"send_message\",\"params\":{\"detail_type\":\"group\",\"group_id\":\"%s\",\"message\":[{\"type\":\"text\",\"data\":{\"text\":\"%s\"}}]}}",wechatId,message));
                    }else{
                        HttpTool.postData(wechatRollbackUrl,String.format("{\"action\":\"send_message\",\"params\":{\"detail_type\":\"private\",\"user_id\":\"%s\",\"message\":[{\"type\":\"text\",\"data\":{\"text\":\"%s\"}}]}}",wechatId,message));
                    }
                    break;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
