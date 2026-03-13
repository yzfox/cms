package com.example.cms_android.websocket;

import com.google.gson.annotations.SerializedName;

/**
 * 数据变更消息类
 * 用于解析WebSocket接收到的数据变更通知
 */
public class DataChangeMessage {
    @SerializedName("entityType")
    private String entityType;
    
    @SerializedName("action")
    private String action;
    
    @SerializedName("id")
    private Long id;
    
    @SerializedName("userId")
    private Long userId;
    
    @SerializedName("timestamp")
    private Long timestamp;
    
    // Constructors
    public DataChangeMessage() {
    }
    
    public DataChangeMessage(String entityType, String action, Long id, Long userId, Long timestamp) {
        this.entityType = entityType;
        this.action = action;
        this.id = id;
        this.userId = userId;
        this.timestamp = timestamp;
    }
    
    // Getters and Setters
    public String getEntityType() {
        return entityType;
    }
    
    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        this.action = action;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
    
    @Override
    public String toString() {
        return "DataChangeMessage{" +
                "entityType='" + entityType + '\'' +
                ", action='" + action + '\'' +
                ", id=" + id +
                ", userId=" + userId +
                ", timestamp=" + timestamp +
                '}';
    }
}