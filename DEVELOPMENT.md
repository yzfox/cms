# CMS系统开发文档

## 1. 项目概述

CMS（Content Management System）是一个完整的内容管理系统，包含后端服务、管理员网页端、普通用户网页端、普通用户微信小程序端和普通用户Android客户端五个主要组件。该系统旨在提供便捷的内容管理功能，支持多种客户端访问。其中，普通用户Android客户端正在开发中。

## 2. 系统架构

### 2.1 整体架构

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│                 │     │                 │     │                 │
│  管理员网页端    │────▶│   Spring Boot   │◀────│   普通用户网页端   │
│                 │     │    后端服务     │     │                 │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                              ▲
                              │
┌─────────────────┐     ┌────┴─────┐     ┌─────────────────┐
│                 │     │          │     │                 │
│  Android客户端  │────▶│          │◀────│  微信小程序客户端  │
│  (开发中)        │     │          │     │                 │
└─────────────────┘     └──────────┘     └─────────────────┘
                              │
                              ▼
                        ┌─────────┐
                        │  MySQL  │
                        └─────────┘
```

### 2.2 技术栈

| 组件 | 技术 | 版本 |
|------|------|------|
| 后端框架 | Spring Boot | 2.5.14 |
| 持久层 | MyBatis Plus | 3.4.2 |
| 数据库 | MySQL | 8.0 |
| 管理员网页端 | Vue 3 + Element Plus | - |
| 普通用户网页端 | Vue 3 + Element Plus | - |
| 微信小程序 | Vue + uni-app | - |
| Android客户端 | Java + Android SDK | - |
| 构建工具 | Maven (后端) + npm (前端) + Gradle (Android) | - |
| 版本控制 | Git | - |

## 3. 目录结构

### 3.1 根目录结构

```
CMS/
├── .idea/              # IDEA配置文件
├── .trae/              # Trae IDE配置
├── .vscode/            # VS Code配置
├── vue/                # 前端网页代码（管理员/普通用户）
├── cms-miniprogram/    # 微信小程序项目
├── cms-android/        # Android客户端项目（开发中）
├── src/                # 后端源代码
├── .gitignore          # Git忽略文件
├── CMS.iml             # IDEA项目文件
├── HELP.md             # 帮助文档
├── README.md           # 项目说明文档
└── pom.xml             # Maven配置文件
```

### 3.2 后端项目结构

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── example/
│   │           └── cms/
│   │               ├── CmsApplication.java    # 应用入口
│   │               ├── controller/             # 控制器层
│   │               ├── service/                # 业务逻辑层
│   │               ├── mapper/                 # 数据访问层
│   │               ├── entity/                 # 实体类
│   │               ├── dto/                    # 数据传输对象
│   │               ├── config/                 # 配置类
│   │               ├── security/               # 安全相关
│   │               └── utils/                  # 工具类
│   └── resources/
│       ├── application.properties             # 应用配置
│       ├── application-dev.properties         # 开发环境配置
│       ├── application-prod.properties        # 生产环境配置
│       └── mapper/                            # MyBatis映射文件
└── test/                                      # 测试代码
```

### 3.3 微信小程序结构

```
cms-miniprogram/
├── pages/              # 页面文件
├── components/         # 组件
├── utils/              # 工具函数
├── static/             # 静态资源
├── App.vue             # 应用入口
├── main.js             # 主入口文件
├── manifest.json       # 配置文件
├── pages.json          # 页面配置
└── package.json        # 依赖配置
```

### 3.4 Android项目结构（规划）

```
cms-android/
├── app/                # 主应用模块
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │       └── example/
│   │   │   │           └── cms_android/    # 包名
│   │   │   │               ├── activity/    # 活动类
│   │   │   │               ├── adapter/     # 适配器
│   │   │   │               ├── dao/         # 数据访问
│   │   │   │               ├── database/    # 数据库
│   │   │   │               ├── model/       # 模型类
│   │   │   │               ├── repository/  # 仓库层
│   │   │   │               └── utils/       # 工具类
│   │   │   ├── res/       # 资源文件
│   │   │   └── AndroidManifest.xml  # 应用清单
│   │   └── test/         # 测试代码
│   └── build.gradle.kts  # 模块构建配置
├── gradle/             # Gradle包装器
├── .gitignore          # Git忽略文件
├── build.gradle.kts    # 项目构建配置
├── gradle.properties   # Gradle属性
├── gradlew             # Gradle包装器脚本
└── settings.gradle.kts # 项目设置
```

## 4. 功能模块

### 4.1 后端服务

| 模块 | 功能描述 |
|------|----------|
| 用户管理 | 用户注册、登录、权限管理 |
| 居民管理 | 居民信息的增删改查 |
| 户籍管理 | 户籍信息的管理 |
| 教育管理 | 教育信息的管理 |
| 医疗管理 | 医疗信息的管理 |
| 社保管理 | 社保信息的管理 |
| 就业管理 | 就业信息的管理 |
| 车辆管理 | 车辆信息的管理 |
| 通知管理 | 系统通知的管理 |
| 统计分析 | 多维度数据统计和分析 |

### 4.2 管理员网页端

| 模块 | 功能描述 |
|------|----------|
| 登录管理 | 管理员登录、权限验证 |
| 仪表盘 | 系统概览、数据统计 |
| 用户管理 | 系统用户的增删改查、权限分配 |
| 居民管理 | 居民信息的增删改查、批量操作 |
| 户籍管理 | 户籍信息的管理、关联居民 |
| 教育管理 | 教育信息的管理 |
| 医疗管理 | 医疗信息的管理 |
| 社保管理 | 社保信息的管理 |
| 就业管理 | 就业信息的管理 |
| 车辆管理 | 车辆信息的管理 |
| 统计分析 | 多维度数据统计和可视化展示 |
| 系统设置 | 系统参数配置、日志管理 |

### 4.3 普通用户网页端

| 模块 | 功能描述 |
|------|----------|
| 登录注册 | 用户登录、注册、忘记密码 |
| 个人中心 | 个人信息管理、密码修改 |
| 居民管理 | 居民信息的查看、添加、编辑 |
| 户籍管理 | 户籍信息的查看、关联居民 |
| 教育管理 | 教育信息的查看、添加、编辑 |
| 医疗管理 | 医疗信息的查看、添加、编辑 |
| 社保管理 | 社保信息的查看、添加、编辑 |
| 就业管理 | 就业信息的查看、添加、编辑 |
| 车辆管理 | 车辆信息的查看、添加、编辑 |

### 4.4 微信小程序

| 模块 | 功能描述 |
|------|----------|
| 用户登录注册 | 微信授权登录、密码登录、注册 |
| 首页 | 系统概览、最新通知 |
| 居民管理 | 居民信息列表、详情、添加、编辑 |
| 户籍管理 | 户籍信息管理 |
| 教育管理 | 教育信息管理 |
| 医疗管理 | 医疗信息管理 |
| 社保管理 | 社保信息管理 |
| 就业管理 | 就业信息管理 |
| 车辆管理 | 车辆信息管理 |
| 个人中心 | 个人信息、设置 |

### 4.5 Android客户端（开发中）

| 模块 | 功能描述 |
|------|----------|
| 用户认证 | 登录、注册、忘记密码 |
| 居民管理 | 居民信息的增删改查 |
| 户籍管理 | 户籍信息管理 |
| 教育管理 | 教育信息管理 |
| 医疗管理 | 医疗信息管理 |
| 社保管理 | 社保信息管理 |
| 就业管理 | 就业信息管理 |
| 车辆管理 | 车辆信息管理 |
| 数据同步 | 本地数据与服务器同步 |
| 离线功能 | 支持离线操作，在线后同步 |

## 5. API接口设计

### 5.1 接口规范

- 基础URL：`http://localhost:8080/api`
- 请求方式：GET/POST/PUT/DELETE
- 数据格式：JSON
- 认证方式：JWT Token

### 5.2 主要接口示例

#### 用户登录
- URL: `/auth/login`
- 方法: POST
- 请求体: `{"username": "admin", "password": "123456"}`
- 响应: `{"code": 200, "message": "登录成功", "data": {"token": "xxx", "user": {}}}`

#### 获取居民列表
- URL: `/resident/list`
- 方法: GET
- 请求参数: `page=1&size=10&name=张三`
- 响应: `{"code": 200, "message": "查询成功", "data": {"total": 100, "records": [{}]}}`

## 6. 添加Android项目步骤

### 6.1 准备工作

1. 安装Android Studio
2. 配置Java JDK 8+
3. 确保已安装Git

### 6.2 创建Android项目

1. 在CMS根目录下创建`cms-android`文件夹
2. 使用Android Studio打开该文件夹，选择"New Project"
3. 选择"Empty Activity"模板
4. 配置项目信息：
   - Name: CMSAndroid
   - Package name: com.example.cms_android
   - Save location: d:\Developer Tool\IDEA\JAVAEE\CMS\cms-android
   - Language: Java
   - Minimum SDK: API 21 (Android 5.0)
5. 点击"Finish"创建项目

### 6.3 项目配置

1. 配置依赖项：
   - 添加网络请求库（如OkHttp、Retrofit）
   - 添加JSON解析库（如Gson、Jackson）
   - 添加数据库支持（如Room）
   - 添加图片加载库（如Glide）

2. 配置权限：
   - 在AndroidManifest.xml中添加必要权限
   - 网络权限：`INTERNET`
   - 存储权限：`READ_EXTERNAL_STORAGE`, `WRITE_EXTERNAL_STORAGE`
   - 定位权限（如需）：`ACCESS_FINE_LOCATION`

3. 配置API地址：
   - 创建配置类管理API地址
   - 支持多环境切换（开发、测试、生产）

### 6.4 实现核心功能

1. 用户认证模块
   - 登录界面
   - 注册界面
   - 忘记密码界面
   - JWT Token管理

2. 数据管理模块
   - 居民信息管理
   - 户籍信息管理
   - 教育信息管理
   - 医疗信息管理
   - 社保信息管理
   - 就业信息管理
   - 车辆信息管理

3. 数据同步模块
   - 本地数据缓存
   - 服务器数据同步
   - 离线操作支持

### 6.5 构建与测试

1. 构建APK：
   - Debug版本：`./gradlew assembleDebug`
   - Release版本：`./gradlew assembleRelease`

2. 测试：
   - 单元测试
   - 集成测试
   - UI测试
   - 真机测试

## 7. 开发流程与规范

### 7.1 版本控制规范

- 主分支：`main`（稳定版本）
- 开发分支：`develop`（日常开发）
- 特性分支：`feature/xxx`（新功能开发）
- 修复分支：`fix/xxx`（bug修复）
- 发布分支：`release/xxx`（版本发布）

### 7.2 代码规范

1. Java代码规范：
   - 遵循Google Java Style Guide
   - 使用Lombok简化代码
   - 合理使用注释

2. 命名规范：
   - 类名：驼峰命名，首字母大写
   - 方法名：驼峰命名，首字母小写
   - 变量名：驼峰命名，首字母小写
   - 常量名：全大写，下划线分隔

3. 代码提交规范：
   - 提交信息清晰明了
   - 每个提交只包含一个功能或修复
   - 使用语义化提交信息（如feat: 添加登录功能）

### 7.3 开发工具

- 后端开发：IntelliJ IDEA
- 前端网页开发：VS Code / HBuilderX
- 微信小程序开发：HBuilderX + uni-app插件
- Android开发：Android Studio
- 数据库管理：Navicat
- API测试：Apifox

## 8. 部署与发布

### 8.1 后端部署

1. 构建Jar包：`mvn clean package`
2. 部署到服务器：
   - 使用Docker容器化部署
   - 或直接运行：`java -jar CMS-0.0.1-SNAPSHOT.jar`
3. 配置Nginx反向代理（可选）

### 8.2 前端网页部署

1. 构建生产版本：
   ```bash
   cd vue
   npm install
   npm run build
   ```
2. 部署到服务器：
   - 将`vue/dist/`目录下的文件上传到Web服务器（如Nginx、Apache）
   - 配置Nginx反向代理，将API请求转发到后端服务
   
### 8.3 微信小程序发布

1. 编译打包：在HBuilderX中选择"发行"->"微信小程序"->"发行"
2. 登录微信公众平台，上传代码
3. 提交审核
4. 审核通过后发布

### 8.4 Android应用发布

1. 生成签名文件
2. 构建Release版本APK
3. 上传到应用商店（如华为应用市场、小米应用商店等）
4. 或通过企业分发平台发布

## 9. 维护与升级

### 9.1 日志管理

- 后端使用SLF4J + Logback记录日志
- 日志级别：DEBUG、INFO、WARN、ERROR
- 日志文件按日期滚动

### 9.2 监控与告警

- 可集成Spring Boot Actuator监控应用状态
- 配置Prometheus + Grafana进行监控
- 设置告警规则，及时发现问题

### 9.3 安全更新

- 定期更新依赖库，修复安全漏洞
- 定期检查并更新JWT密钥
- 加强服务器安全配置

## 10. 参考文档

- [Spring Boot官方文档](https://spring.io/projects/spring-boot)
- [MyBatis Plus官方文档](https://baomidou.com/)
- [uni-app官方文档](https://uniapp.dcloud.io/)
- [Android开发官方文档](https://developer.android.com/docs)
- [Git官方文档](https://git-scm.com/doc)

## 11. 联系方式

- 项目负责人：涂家乐
- 技术支持：涂家乐
- 邮箱：2131969030@qq.com
- 文档更新日期：2025-12-15

---

**文档版本：1.0**

**最后更新：2025-12-15**