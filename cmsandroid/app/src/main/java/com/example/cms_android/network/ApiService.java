package com.example.cms_android.network;

import com.example.cms_android.auth.LoginRequest;
import com.example.cms_android.auth.LoginResponse;
import com.example.cms_android.auth.RegisterResponse;
import com.example.cms_android.model.Resident;
import com.example.cms_android.response.base.ResidentResponse;
import com.example.cms_android.response.base.HouseholdResponse;
import com.example.cms_android.response.base.EducationResponse;
import com.example.cms_android.response.base.EmploymentResponse;
import com.example.cms_android.response.base.MedicalResponse;
import com.example.cms_android.response.base.SocialSecurityResponse;
import com.example.cms_android.response.base.VehicleResponse;
import com.example.cms_android.response.base.PropertyResponse;
import com.example.cms_android.model.Household;
import com.example.cms_android.model.Education;
import com.example.cms_android.model.Employment;
import com.example.cms_android.model.Medical;
import com.example.cms_android.model.SocialSecurity;
import com.example.cms_android.model.Vehicle;
import com.example.cms_android.model.Property;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class ApiService {
    private static final String BASE_URL = "http://10.0.2.2:9090/";
    private static final String API_BASE_URL = BASE_URL + "api/";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private OkHttpClient client;
    private Gson gson;
    private static String authToken;

    public ApiService() {
        // 创建OkHttpClient实例，设置超时时间
        client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        // 创建Gson实例，用于JSON序列化和反序列化
        gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();
    }

    /**
     * 设置认证Token
     */
    public static void setAuthToken(String token) {
        authToken = token;
    }

    /**
     * 获取认证Token
     */
    public static String getAuthToken() {
        return authToken;
    }

    /**
     * 用户登录
     */
    public LoginResponse login(String username, String password) throws Exception {
        LoginRequest loginRequest = new LoginRequest(username, password);
        String json = gson.toJson(loginRequest);
        RequestBody body = RequestBody.create(json, JSON);

        Request request = new Request.Builder()
                .url(API_BASE_URL + "auth/login")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("登录失败: " + response.code());
            }

            String responseData = response.body().string();
            return gson.fromJson(responseData, LoginResponse.class);
        }
    }

    /**
     * 用户注册
     */
    public RegisterResponse register(String username, String password, String idCard) throws Exception {
        // 构建注册请求体
        // 注意：角色被硬编码为"USER"，只允许普通用户注册
        // 管理员账户需要通过网页端
        RegisterRequest registerRequest = new RegisterRequest(username, password, password, idCard, "USER");
        String json = gson.toJson(registerRequest);
        RequestBody body = RequestBody.create(json, JSON);

        Request request = new Request.Builder()
                .url(API_BASE_URL + "auth/register")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("注册失败: " + response.code());
            }

            String responseData = response.body().string();
            return gson.fromJson(responseData, RegisterResponse.class);
        }
    }

    /**
     * 重置密码
     */
    public LoginResponse resetPassword(String username, String idCard, String newPassword) throws Exception {
        // 构建重置密码请求体
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest(username, idCard, newPassword, newPassword,
                "");
        String json = gson.toJson(resetPasswordRequest);
        RequestBody body = RequestBody.create(json, JSON);

        Request request = new Request.Builder()
                .url(API_BASE_URL + "auth/reset-password")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("重置密码失败: " + response.code());
            }

            String responseData = response.body().string();
            return gson.fromJson(responseData, LoginResponse.class);
        }
    }

    /**
     * 获取居民列表
     */
    public ResidentResponse getResidents(int page, int size) throws Exception {
        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "resident/list?page=" + page + "&size=" + size);

        // 添加认证头
        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("获取居民列表失败: " + response.code());
            }

            String responseData = response.body().string();
            // 添加调试日志，打印服务器返回的原始JSON数据

            return gson.fromJson(responseData, ResidentResponse.class);
        }
    }

    /**
     * 注册请求模型
     */
    private static class RegisterRequest {
        private String username;
        private String password;
        private String confirmPassword;
        private String idCard;
        private String role;

        public RegisterRequest(String username, String password, String confirmPassword, String idCard, String role) {
            this.username = username;
            this.password = password;
            this.confirmPassword = confirmPassword;
            this.idCard = idCard;
            this.role = role;
        }

        // Getters and setters (required for Gson serialization)
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getConfirmPassword() {
            return confirmPassword;
        }

        public void setConfirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }

    /**
     * 重置密码请求模型
     */
    private static class ResetPasswordRequest {
        private String username;
        private String idCard;
        private String token;
        private String newPassword;
        private String confirmPassword;

        public ResetPasswordRequest(String username, String idCard, String newPassword, String confirmPassword,
                String token) {
            this.username = username;
            this.idCard = idCard;
            this.newPassword = newPassword;
            this.confirmPassword = confirmPassword;
            this.token = token;
        }

        // Getters and setters (required for Gson serialization)
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }

        public String getConfirmPassword() {
            return confirmPassword;
        }

        public void setConfirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
        }
    }

    /**
     * 添加居民
     */
    public boolean addResident(Resident resident) throws Exception {
        String json = gson.toJson(resident);
        RequestBody body = RequestBody.create(json, JSON);

        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "resident/add")
                .post(body);

        // 添加认证头
        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }

    /**
     * 更新居民信息
     */
    public boolean updateResident(Resident resident) throws Exception {
        String json = gson.toJson(resident);
        RequestBody body = RequestBody.create(json, JSON);

        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "resident/update")
                .put(body);

        // 添加认证头
        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }

    /**
     * 删除居民
     */
    public boolean deleteResident(Long id) throws Exception {
        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "resident/delete/" + id)
                .delete();

        // 添加认证头
        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }

    /**
     * 根据ID获取居民详情
     */
    public Resident getResidentById(Long id) throws Exception {
        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "resident/" + id);

        // 添加认证头
        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("获取居民详情失败: " + response.code());
            }

            String responseData = response.body().string();
            // 添加调试日志，打印服务器返回的原始JSON数据

            // 先解析响应为Result对象
            com.google.gson.JsonObject jsonObject = gson.fromJson(responseData, com.google.gson.JsonObject.class);
            // 获取data字段
            com.google.gson.JsonElement dataElement = jsonObject.get("data");
            // 解析data字段为Resident对象
            return gson.fromJson(dataElement, Resident.class);
        }
    }

    /**
     * 获取户籍信息列表
     */
    public HouseholdResponse getHouseholdList(int page, int size) throws Exception {
        return getHouseholds(page, size);
    }

    /**
     * 获取户籍信息列表
     */
    public HouseholdResponse getHouseholds(int page, int size) throws Exception {
        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "household/list?page=" + page + "&size=" + size);

        // 添加认证头
        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("获取户籍列表失败: " + response.code());
            }

            String responseData = response.body().string();
            return gson.fromJson(responseData, HouseholdResponse.class);
        }
    }

    /**
     * 添加户籍信息
     */
    public boolean addHousehold(Household household) throws Exception {
        String json = gson.toJson(household);
        RequestBody body = RequestBody.create(json, JSON);

        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "household/add")
                .post(body);

        // 添加认证头
        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }

    /**
     * 获取教育信息列表
     */
    public EducationResponse getEducationList(int page, int size) throws Exception {
        return getEducations(page, size);
    }

    /**
     * 获取教育信息列表
     */
    public EducationResponse getEducations(int page, int size) throws Exception {
        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "education/list?page=" + page + "&size=" + size);

        // 添加认证头
        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("获取教育列表失败: " + response.code());
            }

            String responseData = response.body().string();
            return gson.fromJson(responseData, EducationResponse.class);
        }
    }

    /**
     * 获取就业信息列表
     */
    public EmploymentResponse getEmploymentList(int page, int size) throws Exception {
        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "employment/list?page=" + page + "&size=" + size);

        // 添加认证头
        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("获取就业列表失败: " + response.code());
            }

            String responseData = response.body().string();
            // 添加调试日志，打印服务器返回的原始JSON数据

            return gson.fromJson(responseData, EmploymentResponse.class);
        }
    }

    /**
     * 获取就业信息列表
     */
    public EmploymentResponse getEmployments(int page, int size) throws Exception {
        return getEmploymentList(page, size);
    }

    /**
     * 获取医疗信息列表
     */
    public MedicalResponse getMedicalList(int page, int size) throws Exception {
        return getMedicals(page, size);
    }

    /**
     * 获取医疗信息列表
     */
    public MedicalResponse getMedicals(int page, int size) throws Exception {
        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "medical/list?page=" + page + "&size=" + size);

        // 添加认证头
        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("获取医疗列表失败: " + response.code());
            }

            String responseData = response.body().string();
            // 添加调试日志，打印完整的医疗列表响应

            return gson.fromJson(responseData, MedicalResponse.class);
        }
    }

    /**
     * 获取社保信息列表
     */
    public SocialSecurityResponse getSocialSecurityList(int page, int size) throws Exception {
        return getSocialSecurities(page, size);
    }

    /**
     * 获取社保信息列表
     */
    public SocialSecurityResponse getSocialSecurities(int page, int size) throws Exception {
        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "social-security/list?page=" + page + "&size=" + size);

        // 添加认证头
        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("获取社保列表失败: " + response.code());
            }

            String responseData = response.body().string();
            return gson.fromJson(responseData, SocialSecurityResponse.class);
        }
    }

    /**
     * 获取车辆信息列表
     */
    public VehicleResponse getVehicleList(int page, int size) throws Exception {
        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "vehicle/list?page=" + page + "&size=" + size);

        // 添加认证头
        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("获取车辆列表失败: " + response.code());
            }

            String responseData = response.body().string();
            return gson.fromJson(responseData, VehicleResponse.class);
        }
    }

    /**
     * 获取车辆信息列表
     */
    public VehicleResponse getVehicles(int page, int size) throws Exception {
        return getVehicleList(page, size);
    }

    /**
     * 获取房产信息列表
     */
    public PropertyResponse getPropertyList(int page, int size) throws Exception {
        return getProperties(page, size);
    }

    /**
     * 获取房产信息列表
     */
    public PropertyResponse getProperties(int page, int size) throws Exception {
        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "property/list?page=" + page + "&size=" + size);

        // 添加认证头
        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("获取房产列表失败: " + response.code());
            }

            String responseData = response.body().string();
            return gson.fromJson(responseData, PropertyResponse.class);
        }
    }

    /**
     * 根据ID获取社保详情
     */
    public SocialSecurity getSocialSecurityById(Long id) throws Exception {
        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "social-security/" + id);

        // 添加认证头
        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("获取社保详情失败: " + response.code());
            }

            String responseData = response.body().string();
            // 先解析响应为Result对象
            com.google.gson.JsonObject jsonObject = gson.fromJson(responseData, com.google.gson.JsonObject.class);
            // 获取data字段
            com.google.gson.JsonElement dataElement = jsonObject.get("data");
            // 解析data字段为SocialSecurity对象
            return gson.fromJson(dataElement, SocialSecurity.class);
        }
    }

    /**
     * 更新社保信息
     */
    public boolean updateSocialSecurity(SocialSecurity socialSecurity) throws Exception {
        String json = gson.toJson(socialSecurity);
        RequestBody body = RequestBody.create(json, JSON);

        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "social-security/update")
                .put(body);

        // 添加认证头
        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();
        
        try (Response response = client.newCall(request).execute()) {
            if (response.body() != null) {
                String responseBody = response.body().string();
                // 如果响应不成功，抛出带有详细信息的异常
                if (!response.isSuccessful()) {
                    throw new Exception("HTTP " + response.code() + ": " + response.message() + " - " + responseBody);
                }
                return response.isSuccessful();
            } else {
                return response.isSuccessful();
            }
        }
    }

    /**
     * 删除社保信息
     */
    public boolean deleteSocialSecurity(Long id) throws Exception {
        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "social-security/delete/" + id)
                .delete();

        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }

    /**
     * 添加社保信息
     */
    public boolean addSocialSecurity(SocialSecurity socialSecurity) throws Exception {
        String json = gson.toJson(socialSecurity);
        RequestBody body = RequestBody.create(json, JSON);

        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "social-security/add")
                .post(body);

        // 添加认证头
        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }

    /**
     * 根据ID获取车辆详情
     */
    public Vehicle getVehicleById(Long id) throws Exception {
        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "vehicle/" + id);

        // 添加认证头
        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("获取车辆详情失败: " + response.code());
            }

            String responseData = response.body().string();
            // 先解析响应为Result对象
            com.google.gson.JsonObject jsonObject = gson.fromJson(responseData, com.google.gson.JsonObject.class);
            // 获取data字段
            com.google.gson.JsonElement dataElement = jsonObject.get("data");
            // 解析data字段为Vehicle对象
            return gson.fromJson(dataElement, Vehicle.class);
        }
    }

    /**
     * 更新车辆信息
     */
    public boolean updateVehicle(Vehicle vehicle) throws Exception {
        String json = gson.toJson(vehicle);
        RequestBody body = RequestBody.create(json, JSON);

        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "vehicle/update")
                .put(body);

        // 添加认证头
        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }

    /**
     * 添加车辆信息
     */
    public boolean addVehicle(Vehicle vehicle) throws Exception {
        String json = gson.toJson(vehicle);
        RequestBody body = RequestBody.create(json, JSON);

        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "vehicle/add")
                .post(body);

        // 添加认证头
        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            if (response.body() != null) {
                String responseBody = response.body().string();
                // 如果响应不成功，抛出带有详细信息的异常
                if (!response.isSuccessful()) {
                    throw new Exception("HTTP " + response.code() + ": " + response.message() + " - " + responseBody);
                }
                return response.isSuccessful();
            } else {
                return response.isSuccessful();
            }
        }
    }

    /**
     * 删除车辆信息
     */
    public boolean deleteVehicle(Long id) throws Exception {
        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "vehicle/delete/" + id)
                .delete();

        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }

    /**
     * 根据ID获取房产详情
     */
    public Property getPropertyById(Long id) throws Exception {
        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "property/" + id);

        // 添加认证头
        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("获取房产详情失败: " + response.code());
            }

            String responseData = response.body().string();
            // 先解析响应为Result对象
            com.google.gson.JsonObject jsonObject = gson.fromJson(responseData, com.google.gson.JsonObject.class);
            // 获取data字段
            com.google.gson.JsonElement dataElement = jsonObject.get("data");
            // 解析data字段为Property对象
            return gson.fromJson(dataElement, Property.class);
        }
    }

    /**
     * 添加房产信息
     */
    public boolean addProperty(Property property) throws Exception {
        String json = gson.toJson(property);
        RequestBody body = RequestBody.create(json, JSON);

        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "property/add")
                .post(body);

        // 添加认证头
        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }

    /**
     * 删除房产信息
     */
    public boolean deleteProperty(Long id) throws Exception {
        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "property/delete/" + id)
                .delete();

        // 添加认证头
        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }

    /**
     * 更新房产信息
     */
    public boolean updateProperty(Property property) throws Exception {
        String json = gson.toJson(property);
        RequestBody body = RequestBody.create(json, JSON);

        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "property/update")
                .put(body);

        // 添加认证头
        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }

    /**
     * 根据ID获取教育详情
     */
    public Education getEducationById(Long id) throws Exception {
        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "education/" + id);

        // 添加认证头
        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("获取教育详情失败: " + response.code());
            }

            String responseData = response.body().string();
            // 添加调试日志，打印服务器返回的原始JSON数据

            // 先解析响应为Result对象
            com.google.gson.JsonObject jsonObject = gson.fromJson(responseData, com.google.gson.JsonObject.class);
            // 获取data字段
            com.google.gson.JsonElement dataElement = jsonObject.get("data");
            // 解析data字段为Education对象
            return gson.fromJson(dataElement, Education.class);
        }
    }

    /**
     * 添加教育信息
     */
    public boolean addEducation(Education education) throws Exception {
        String json = gson.toJson(education);
        RequestBody body = RequestBody.create(json, JSON);

        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "education/add")
                .post(body);

        // 添加认证头
        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }

    /**
     * 更新教育信息
     */
    public boolean updateEducation(Education education) throws Exception {
        String json = gson.toJson(education);
        RequestBody body = RequestBody.create(json, JSON);

        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "education/update")
                .put(body);

        // 添加认证头
        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return true;
            } else {
                // 记录错误信息用于调试
                String errorMessage = "HTTP " + response.code() + ": " + response.message();
                if (response.body() != null) {
                    errorMessage += " - " + response.body().string();
                }
                throw new Exception(errorMessage);
            }
        }
    }

    /**
     * 删除教育信息
     */
    public boolean deleteEducation(Long id) throws Exception {
        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "education/delete/" + id)
                .delete();

        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }

    /**
     * 根据ID获取就业详情
     */
    public Employment getEmploymentById(Long id) throws Exception {
        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "employment/" + id);

        // 添加认证头
        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("获取就业详情失败: " + response.code());
            }

            String responseData = response.body().string();
            // 先解析响应为Result对象
            com.google.gson.JsonObject jsonObject = gson.fromJson(responseData, com.google.gson.JsonObject.class);
            // 获取data字段
            com.google.gson.JsonElement dataElement = jsonObject.get("data");
            // 解析data字段为Employment对象
            return gson.fromJson(dataElement, Employment.class);
        }
    }

    /**
     * 添加就业信息
     */
    public boolean addEmployment(Employment employment) throws Exception {
        String json = gson.toJson(employment);
        RequestBody body = RequestBody.create(json, JSON);

        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "employment/add")
                .post(body);

        // 添加认证头
        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }

    /**
     * 更新就业信息
     */
    public boolean updateEmployment(Employment employment) throws Exception {
        String json = gson.toJson(employment);
        RequestBody body = RequestBody.create(json, JSON);

        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "employment/update")
                .put(body);

        // 添加认证头
        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }

    /**
     * 删除就业信息
     */
    public boolean deleteEmployment(Long id) throws Exception {
        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "employment/delete/" + id)
                .delete();

        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }

    /**
     * 根据ID获取医疗详情
     */
    public Medical getMedicalById(Long id) throws Exception {
        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "medical/" + id);

        // 添加认证头
        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("获取医疗详情失败: " + response.code());
            }

            String responseData = response.body().string();
            // 打印完整的服务器响应

            // 先解析响应为Result对象
            com.google.gson.JsonObject jsonObject = gson.fromJson(responseData, com.google.gson.JsonObject.class);
            // 获取data字段
            com.google.gson.JsonElement dataElement = jsonObject.get("data");
            // 打印data字段内容

            // 解析data字段为Medical对象
            return gson.fromJson(dataElement, Medical.class);
        }
    }

    /**
     * 更新医疗信息
     */
    public boolean updateMedical(Medical medical) throws Exception {
        String json = gson.toJson(medical);
        RequestBody body = RequestBody.create(json, JSON);

        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "medical/update")
                .put(body);

        // 添加认证头
        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }

    /**
     * 添加医疗信息
     */
    public boolean addMedical(Medical medical) throws Exception {
        String json = gson.toJson(medical);
        RequestBody body = RequestBody.create(json, JSON);

        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "medical/add")
                .post(body);

        // 添加认证头
        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }

    /**
     * 删除医疗信息
     */
    public boolean deleteMedical(Long id) throws Exception {
        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "medical/delete/" + id)
                .delete();

        // 添加认证头
        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }

    /**
     * 根据ID获取户籍详情
     */
    public Household getHouseholdById(Long id) throws Exception {
        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "household/" + id);

        // 添加认证头
        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("获取户籍详情失败: " + response.code());
            }

            String responseData = response.body().string();
            // 先解析响应为Result对象
            com.google.gson.JsonObject jsonObject = gson.fromJson(responseData, com.google.gson.JsonObject.class);
            // 获取data字段
            com.google.gson.JsonElement dataElement = jsonObject.get("data");
            // 解析data字段为Household对象
            return gson.fromJson(dataElement, Household.class);
        }
    }

    /**
     * 更新户籍信息
     */
    public boolean updateHousehold(Household household) throws Exception {
        String json = gson.toJson(household);
        RequestBody body = RequestBody.create(json, JSON);

        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "household/update")
                .put(body);

        // 添加认证头
        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }

    /**
     * 删除户籍信息
     */
    public boolean deleteHousehold(Long id) throws Exception {
        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + "household/delete/" + id)
                .delete();

        // 添加认证头
        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }

    /**
     * LocalDate类型适配器，处理"yyyy-MM-dd"格式
     */
    private static class LocalDateTypeAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        @Override
        public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.format(formatter));
        }

        @Override
        public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            String dateString = json.getAsString().trim();
            try {
                // 处理 "yyyy-MM-dd" 格式
                if (dateString.length() == 10) {
                    return LocalDate.parse(dateString, formatter);
                }
                // 处理 "yyyy-MM-dd HH:mm:ss" 格式（从服务器来的数据可能包含时间）
                return LocalDate.parse(dateString.substring(0, 10), formatter);
            } catch (Exception e) {
                throw new JsonParseException("Failed parsing '" + dateString + "' as LocalDate", e);
            }
        }
    }

    /**
     * LocalDateTime类型适配器，处理"yyyy-MM-dd HH:mm:ss"格式
     */
    private static class LocalDateTimeTypeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        @Override
        public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.format(formatter));
        }

        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            String dateString = json.getAsString().trim();
            try {
                return LocalDateTime.parse(dateString, formatter);
            } catch (Exception e) {
                throw new JsonParseException("Failed parsing '" + dateString + "' as LocalDateTime", e);
            }
        }
    }

    /**
     * 自定义日期类型适配器，处理多种日期格式
     */
    private static class DateTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {
        private final String[] dateFormats = {
                "yyyy-MM-dd HH:mm:ss",
                "yyyy-MM-dd'T'HH:mm:ss",
                "yyyy-MM-dd'T'HH:mm:ss.SSS",
                "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
                "yyyy-MM-dd"
        };

        @Override
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            return new JsonPrimitive(formatter.format(src));
        }

        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            String dateString = json.getAsString().trim();

            for (String format : dateFormats) {
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat(format);
                    formatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                    formatter.setLenient(false);
                    return formatter.parse(dateString);
                } catch (ParseException e) {
                    // 尝试下一种格式
                }
            }

            throw new JsonParseException("Failed parsing '" + dateString + "' as Date. Expected formats: yyyy-MM-dd HH:mm:ss, yyyy-MM-dd'T'HH:mm:ss, yyyy-MM-dd'T'HH:mm:ss.SSS, yyyy-MM-dd'T'HH:mm:ss.SSSZ, yyyy-MM-dd");
        }
    }
}