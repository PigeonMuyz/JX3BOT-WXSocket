package io.github.pigeonmuyz.entity;

import java.util.UUID;

public class MessageBottle {
    public MessageBottle(String content, String wechatId) {
        this.uuid = UUID.randomUUID().toString()+Math.random();
        this.content = content;
        this.wechatId = wechatId;
        this.isActive = true;
    }

    /**
     * ID
     */
    String uuid;
    /**
     * 内容
     */
    String content;
    /**
     * 微信号
     */
    String wechatId;
    /**
     * 是否激活
     */
    Boolean isActive;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWechatId() {
        return wechatId;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
