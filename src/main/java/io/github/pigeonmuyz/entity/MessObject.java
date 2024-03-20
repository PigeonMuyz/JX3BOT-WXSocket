package io.github.pigeonmuyz.entity;

import java.util.Map;

/**
 * 消息对象
 */
public class MessObject {
    public MessObject() {
    }

    public MessObject(boolean isGroup, String wechatID, String wechatName, String bindServer, String master, Boolean isActive, Map<String, Boolean> serverStatus) {
        this.isGroup = isGroup;
        this.wechatID = wechatID;
        this.wechatName = wechatName;
        this.bindServer = bindServer;
        this.master = master;
        this.isActive = isActive;
        this.serverStatus = serverStatus;
    }

    /**
     * 是否为 群组
     */
    boolean isGroup;
    /**
     * 微信标识符
     */
    String wechatID;
    /**
     * 微信名称
     */
    String wechatName;
    /**
     * 绑定服务器
     */
    String bindServer;
    /**
     * 群主
     */
    String master;
    /**
     * 是否被允许使用
     */
    Boolean isActive;
    /**
     * 激活推送
     */
    Map<String, Boolean> serverStatus;

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public String getWechatID() {
        return wechatID;
    }

    public void setWechatID(String wechatID) {
        this.wechatID = wechatID;
    }

    public String getWechatName() {
        return wechatName;
    }

    public void setWechatName(String wechatName) {
        this.wechatName = wechatName;
    }

    public String getBindServer() {
        return bindServer;
    }

    public void setBindServer(String bindServer) {
        this.bindServer = bindServer;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Map<String, Boolean> getServerStatus() {
        return serverStatus;
    }

    public void setServerStatus(Map<String, Boolean> serverStatus) {
        this.serverStatus = serverStatus;
    }
}
