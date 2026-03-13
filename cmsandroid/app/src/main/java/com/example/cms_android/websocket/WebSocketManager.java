package com.example.cms_android.websocket;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.cms_android.websocket.DataChangeMessage;
import com.google.gson.Gson;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * WebSocket管理类
 * 用于管理Android客户端与后端WebSocket服务器的连接
 */
public class WebSocketManager {
    private static final String TAG = "WebSocketManager";
    private static final String BASE_URL = "ws://10.0.2.2:9090"; // Android模拟器访问本地服务器的地址
    
    private WebSocketClient webSocketClient;
    private String token;
    private boolean isConnected = false;
    private boolean isConnecting = false; // 正在连接标志
    private WebSocketListener listener;
    private DataChangeListener dataChangeListener;
    private Gson gson = new Gson();
    private Handler reconnectHandler = new Handler(Looper.getMainLooper());
    private Runnable reconnectRunnable;
    private int reconnectAttempts = 0;
    private static final int MAX_RECONNECT_ATTEMPTS = 5;
    private static final long RECONNECT_INTERVAL = 5000; // 5秒
    
    // 单例模式
    private static WebSocketManager instance;
    
    public static synchronized WebSocketManager getInstance() {
        if (instance == null) {
            instance = new WebSocketManager();
        }
        return instance;
    }
    
    private WebSocketManager() {
    }
    
    /**
     * 连接到WebSocket服务器
     * @param token JWT认证令牌
     */
    public void connect(String token) {
        this.token = token;
        
        if (isConnected || isConnecting) {
            Log.d(TAG, "WebSocket已连接或正在连接");
            return;
        }
        
        isConnecting = true;
        
        try {
            URI uri = new URI(BASE_URL + "/ws");
            webSocketClient = new WebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake handshake) {
                    Log.d(TAG, "WebSocket连接已打开");
                    isConnected = true;
                    isConnecting = false;
                    reconnectAttempts = 0; // 重置重连次数
                    if (listener != null) {
                        listener.onConnected();
                    }
                }
                
                @Override
                public void onMessage(String message) {
                    Log.d(TAG, "收到WebSocket消息: " + message);
                    
                    // 尝试解析为DataChangeMessage
                    try {
                        DataChangeMessage dataChangeMessage = gson.fromJson(message, DataChangeMessage.class);
                        if (dataChangeMessage != null && dataChangeMessage.getEntityType() != null) {
                            // 如果是数据变更消息，通知数据变更监听器
                            if (dataChangeListener != null) {
                                dataChangeListener.onDataChange(dataChangeMessage);
                            }
                        } else {
                            // 如果不是数据变更消息，通知普通消息监听器
                            if (listener != null) {
                                listener.onMessage(message);
                            }
                        }
                    } catch (Exception e) {
                        // 如果解析失败，当作普通消息处理
                        Log.d(TAG, "消息不是DataChangeMessage格式，当作普通消息处理");
                        if (listener != null) {
                            listener.onMessage(message);
                        }
                    }
                }
                
                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.d(TAG, "WebSocket连接已关闭: " + reason);
                    isConnected = false;
                    isConnecting = false;
                    if (listener != null) {
                        listener.onDisconnected();
                    }
                    
                    // 如果不是主动断开连接，尝试重连
                    if (!remote) {
                        attemptReconnect();
                    }
                }
                
                @Override
                public void onError(Exception ex) {
                    Log.e(TAG, "WebSocket错误: ", ex);
                    isConnecting = false;
                    if (listener != null) {
                        listener.onError(ex);
                    }
                    
                    // 出错时尝试重连
                    attemptReconnect();
                }
            };
            
            // 添加认证头
            if (token != null && !token.isEmpty()) {
                webSocketClient.addHeader("Authorization", "Bearer " + token);
            }
            
            webSocketClient.connect();
        } catch (URISyntaxException e) {
            Log.e(TAG, "WebSocket URI语法错误", e);
            isConnecting = false;
        }
    }
    
    /**
     * 尝试重连
     */
    private void attemptReconnect() {
        if (reconnectAttempts < MAX_RECONNECT_ATTEMPTS) {
            reconnectAttempts++;
            Log.d(TAG, "尝试重连 WebSocket，第 " + reconnectAttempts + " 次");
            
            reconnectRunnable = new Runnable() {
                @Override
                public void run() {
                    if (token != null && !token.isEmpty()) {
                        connect(token);
                    }
                }
            };
            
            reconnectHandler.postDelayed(reconnectRunnable, RECONNECT_INTERVAL);
        } else {
            Log.d(TAG, "达到最大重连次数，停止重连");
        }
    }
    
    /**
     * 断开WebSocket连接
     */
    public void disconnect() {
        if (webSocketClient != null) {
            webSocketClient.close();
        }
        isConnected = false;
        isConnecting = false;
        
        // 取消重连任务
        if (reconnectRunnable != null) {
            reconnectHandler.removeCallbacks(reconnectRunnable);
        }
        
        // 重置重连次数
        reconnectAttempts = 0;
    }
    
    /**
     * 发送消息
     * @param message 消息内容
     */
    public void sendMessage(String message) {
        if (webSocketClient != null && isConnected) {
            webSocketClient.send(message);
        } else {
            Log.w(TAG, "WebSocket未连接，无法发送消息");
        }
    }
    
    /**
     * 检查连接状态
     * @return 是否已连接
     */
    public boolean isConnected() {
        return isConnected;
    }
    
    /**
     * 设置WebSocket事件监听器
     * @param listener 监听器
     */
    public void setListener(WebSocketListener listener) {
        this.listener = listener;
    }
    
    /**
     * WebSocket事件监听器接口
     */
    public interface WebSocketListener {
        void onConnected();
        void onDisconnected();
        void onMessage(String message);
        void onError(Exception error);
    }
    
    /**
     * 数据变更监听器接口
     */
    public interface DataChangeListener {
        void onDataChange(DataChangeMessage message);
    }
    
    /**
     * 设置数据变更监听器
     * @param listener 监听器
     */
    public void setDataChangeListener(DataChangeListener listener) {
        this.dataChangeListener = listener;
    }
}