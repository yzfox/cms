package com.example.cms_android.websocket;

import android.util.Log;

import com.example.cms_android.ListActivity.HouseholdListActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据变更事件处理类
 * 用于处理来自WebSocket的数据变更通知
 */
public class DataChangeHandler {
    private static final String TAG = "DataChangeHandler";
    private static DataChangeHandler instance;
    private Gson gson = new Gson();
    private List<DataChangeListener> listeners = new ArrayList<>();

    // 单例模式
    public static synchronized DataChangeHandler getInstance() {
        if (instance == null) {
            instance = new DataChangeHandler();
        }
        return instance;
    }

    private DataChangeHandler() {
    }

    /**
     * 处理WebSocket消息
     * @param message 消息内容
     */
    public void handleMessage(String message) {
        try {
            JsonObject jsonObject = gson.fromJson(message, JsonObject.class);
            
            // 检查是否是数据变更事件
            if (jsonObject.has("module") && jsonObject.has("operation")) {
                String module = jsonObject.get("module").getAsString();
                String operation = jsonObject.get("operation").getAsString();
                
                Log.d(TAG, "收到数据变更事件: module=" + module + ", operation=" + operation);
                
                // 通知所有监听器
                for (DataChangeListener listener : listeners) {
                    listener.onDataChanged(module, operation);
                }
            }
        } catch (JsonSyntaxException e) {
            Log.e(TAG, "解析WebSocket消息失败", e);
        }
    }

    /**
     * 注册数据变更监听器
     * @param listener 监听器
     */
    public void registerListener(DataChangeListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * 注销数据变更监听器
     * @param listener 监听器
     */
    public void unregisterListener(DataChangeListener listener) {
        listeners.remove(listener);
    }

    /**
     * 数据变更监听器接口
     */
    public interface DataChangeListener {
        /**
         * 当数据发生变更时调用
         * @param module 模块名称
         * @param operation 操作类型
         */
        void onDataChanged(String module, String operation);
    }
}