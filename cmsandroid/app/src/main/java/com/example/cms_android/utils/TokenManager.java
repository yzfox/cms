package com.example.cms_android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.cms_android.auth.User;
import com.google.gson.Gson;

public class TokenManager {
    private static final String PREF_NAME = "cms_prefs";
    private static final String KEY_TOKEN = "auth_token";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USER_INFO = "user_info";
    
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    
    public TokenManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();
    }
    
    public void saveToken(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }
    
    public void saveUsername(String username) {
        editor.putString(KEY_USERNAME, username);
        editor.apply();
    }
    
    public void saveUserInfo(User user) {
        String userJson = gson.toJson(user);
        editor.putString(KEY_USER_INFO, userJson);
        editor.apply();
    }
    
    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }
    
    public String getUsername() {
        // 先从直接存储的用户名获取
        String username = sharedPreferences.getString(KEY_USERNAME, null);
        if (username != null) {
            return username;
        }
        
        // 如果没有直接存储的用户名，尝试从完整用户信息中获取
        User user = getUserInfo();
        if (user != null && user.getUsername() != null) {
            return user.getUsername();
        }
        
        // 都没有则返回默认值
        return "管理员";
    }
    
    public User getUserInfo() {
        String userJson = sharedPreferences.getString(KEY_USER_INFO, null);
        return userJson != null ? gson.fromJson(userJson, User.class) : null;
    }
    
    public void clearToken() {
        editor.remove(KEY_TOKEN);
        editor.remove(KEY_USERNAME);
        editor.remove(KEY_USER_INFO);
        editor.apply();
    }
    
    public boolean isLoggedIn() {
        return getToken() != null;
    }
}