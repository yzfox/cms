<template>
  <div class="login-container">
    <div class="aurora-bg">
      <div class="aurora aurora-1"></div>
      <div class="aurora aurora-2"></div>
      <div class="aurora aurora-3"></div>
      <div class="aurora aurora-4"></div>
    </div>
    <div class="grid-overlay"></div>
    <div class="particles">
      <div class="particle" v-for="i in 20" :key="i" :style="getParticleStyle(i)"></div>
    </div>
    
    <div class="left-panel">
      <div class="brand-content">
        <div class="logo-wrapper fade-in" style="animation-delay: 0.1s">
          <svg class="logo-icon" viewBox="0 0 100 100" fill="none">
            <circle cx="50" cy="50" r="45" stroke="currentColor" stroke-width="2" fill="none" opacity="0.3"/>
            <circle cx="50" cy="32" r="11" fill="currentColor"/>
            <path d="M50 46 C37 46 28 55 28 68 L28 73 C28 76 30 78 33 78 L67 78 C70 78 72 76 72 73 L72 68 C72 55 63 46 50 46 Z" fill="currentColor"/>
            <circle cx="32" cy="40" r="9" fill="currentColor" opacity="0.6"/>
            <path d="M32 51 C22 51 15 58 15 68 L15 71 C15 73 16 74 18 74 L32 74 L32 68 C3261 35 55 40 51 C37 51 35 51 32 51 Z" fill="currentColor" opacity="0.6"/>
            <circle cx="68" cy="40" r="9" fill="currentColor" opacity="0.6"/>
            <path d="M68 51 C78 51 85 58 85 68 L85 71 C85 73 84 74 82 74 L68 74 L68 68 C68 61 65 55 60 51 C63 51 65 51 68 51 Z" fill="currentColor" opacity="0.6"/>
          </svg>
        </div>
        <h1 class="system-title gradient-text fade-in" style="animation-delay: 0.2s">人口普查管理系统</h1>
        <p class="system-subtitle fade-in" style="animation-delay: 0.3s">Population Census Management System</p>
        <div class="features fade-in" style="animation-delay: 0.4s">
          <div class="feature-item">
            <div class="feature-icon-wrapper">
              <svg class="feature-icon" viewBox="0 0 24 24" fill="currentColor">
                <path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/>
              </svg>
            </div>
            <span>精准统计</span>
          </div>
          <div class="feature-item">
            <div class="feature-icon-wrapper">
              <svg class="feature-icon" viewBox="0 0 24 24" fill="currentColor">
                <path d="M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zM9 17H7v-7h2v7zm4 0h-2V7h2v10zm4 0h-2v-4h2v4z"/>
              </svg>
            </div>
            <span>数据分析</span>
          </div>
          <div class="feature-item">
            <div class="feature-icon-wrapper">
              <svg class="feature-icon" viewBox="0 0 24 24" fill="currentColor">
                <path d="M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-5 14H7v-2h7v2zm3-4H7v-2h10v2zm0-4H7V7h10v2z"/>
              </svg>
            </div>
            <span>数据管理</span>
          </div>
        </div>
      </div>
    </div>
    
    <div class="right-panel">
      <div class="form-container fade-in-up">
        <div class="form-tabs" v-if="currentForm !== 'reset-password'">
          <div 
            class="tab-item" 
            :class="{ 'active': currentForm === 'login' }"
            @click="switchForm('login')"
          >
            <span class="tab-text">登录</span>
            <div class="tab-indicator"></div>
          </div>
          <div 
            class="tab-item" 
            :class="{ 'active': currentForm === 'register' }"
            @click="switchForm('register')"
          >
            <span class="tab-text">注册</span>
            <div class="tab-indicator"></div>
          </div>
        </div>
        
        <div class="form-header" v-if="currentForm === 'reset-password'">
          <h2>重置密码</h2>
          <p>请设置您的新密码</p>
        </div>
        
        <el-form 
          v-if="currentForm === 'login'"
          ref="loginForm" 
          :model="loginForm" 
          :rules="loginRules" 
          class="login-form"
          @keyup.enter.native="handleLogin"
        >
          <el-form-item prop="username">
            <div class="input-wrapper" :class="{ 'focused': inputFocused.username, 'has-value': loginForm.username }">
              <div class="input-icon">
                <svg viewBox="0 0 24 24" fill="currentColor">
                  <path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/>
                </svg>
              </div>
              <input
                v-model="loginForm.username"
                type="text"
                class="custom-input"
                placeholder="请输入用户名"
                @focus="inputFocused.username = true"
                @blur="inputFocused.username = false"
              />
            </div>
          </el-form-item>
          
          <el-form-item prop="password">
            <div class="input-wrapper" :class="{ 'focused': inputFocused.password, 'has-value': loginForm.password }">
              <div class="input-icon">
                <svg viewBox="0 0 24 24" fill="currentColor">
                  <path d="M18 8h-1V6c0-2.76-2.24-5-5-5S7 3.24 7 6v2H6c-1.1 0-2 .9-2 2v10c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V10c0-1.1-.9-2-2-2zm-6 9c-1.1 0-2-.9-2-2s.9-2 2-2 2 .9 2 2-.9 2-2 2zm3.1-9H8.9V6c0-1.71 1.39-3.1 3.1-3.1 1.71 0 3.1 1.39 3.1 3.1v2z"/>
                </svg>
              </div>
              <input
                v-model="loginForm.password"
                :type="showPassword ? 'text' : 'password'"
                class="custom-input"
                placeholder="请输入密码"
                @focus="inputFocused.password = true"
                @blur="inputFocused.password = false"
              />
              <button 
                type="button" 
                class="toggle-password"
                @click="showPassword = !showPassword"
              >
                <svg v-if="!showPassword" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M12 4.5C7 4.5 2.73 7.61 1 12c1.73 4.39 6 7.5 11 7.5s9.27-3.11 11-7.5c-1.73-4.39-6-7.5-11-7.5zM12 17c-2.76 0-5-2.24-5-5s2.24-5 5-5 5 2.24 5 5-2.24 5-5 5zm0-8c-1.66 0-3 1.34-3 3s1.34 3 3 3 3-1.34 3-3-1.34-3-3-3z"/>
                </svg>
                <svg v-else viewBox="0 0 24 24" fill="currentColor">
                  <path d="M12 7c2.76 0 5 2.24 5 5 0 .65-.13 1.26-.36 1.83l2.92 2.92c1.51-1.26 2.7-2.89 3.43-4.75-1.73-4.39-6-7.5-11-7.5-1.4 0-2.74.25-3.98.7l2.16 2.16C10.74 7.13 11.35 7 12 7zm-6.93-.01l2.17 2.17C5.69 9.92 4.5 11.86 4.5 14c0 2.76 2.24 5 5 5 1.86 0 3.48-1.02 4.34-2.52l2.17 2.17C14.42 20.24 12.38 21 10.5 21c-4.97 0-9-4.03-9-9 0-2.13.74-4.09 1.97-5.61l1.6-.4zm8.62 8.62l-3.77-3.77c.11-.01.22-.02.33-.02 1.66 0 3 1.34 3 3 0 .11-.01.22-.02.33l.46.46z"/>
                </svg>
              </button>
            </div>
          </el-form-item>
          
          <el-form-item prop="role">
            <div class="role-selector">
              <label class="role-label">选择角色</label>
              <div class="role-options">
                <div 
                  class="role-option" 
                  :class="{ 'active': loginForm.role === 'ADMIN' }"
                  @click="loginForm.role = 'ADMIN'"
                >
                  <div class="radio-dot"></div>
                  <span>管理员</span>
                </div>
                <div 
                  class="role-option" 
                  :class="{ 'active': loginForm.role === 'USER' }"
                  @click="loginForm.role = 'USER'"
                >
                  <div class="radio-dot"></div>
                  <span>普通用户</span>
                </div>
              </div>
            </div>
          </el-form-item>
          
          <div class="form-options">
            <el-checkbox v-model="rememberMe">记住我</el-checkbox>
            <span class="forgot-link" @click="switchForm('forgot-password')">忘记密码？</span>
          </div>
          
          <el-form-item>
            <button 
              type="button"
              class="submit-btn" 
              :disabled="loading"
              @click="handleLogin"
            >
              <span v-if="!loading">登录</span>
              <span v-else class="loading-text">
                <span class="spinner"></span>
                登录中...
              </span>
            </button>
          </el-form-item>
        </el-form>
        
        <el-form 
          v-else-if="currentForm === 'register'"
          ref="registerForm" 
          :model="registerForm" 
          :rules="registerRules" 
          class="login-form"
          @keyup.enter.native="handleRegister"
        >
          <el-form-item prop="username">
            <div class="input-wrapper" :class="{ 'focused': inputFocused.registerUsername, 'has-value': registerForm.username }">
              <div class="input-icon">
                <svg viewBox="0 0 24 24" fill="currentColor">
                  <path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/>
                </svg>
              </div>
              <input
                v-model="registerForm.username"
                type="text"
                class="custom-input"
                placeholder="请输入用户名"
                @focus="inputFocused.registerUsername = true"
                @blur="inputFocused.registerUsername = false"
              />
            </div>
          </el-form-item>
          
          <el-form-item prop="idCard">
            <div class="input-wrapper" :class="{ 'focused': inputFocused.idCard, 'has-value': registerForm.idCard }">
              <div class="input-icon">
                <svg viewBox="0 0 24 24" fill="currentColor">
                  <path d="M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm0 16H5V5h14v14zm-7-2h5v-2h-5v2zm0-4h5v-2h-5v2zm-3-4h8V7H9v2z"/>
                </svg>
              </div>
              <input
                v-model="registerForm.idCard"
                type="text"
                class="custom-input"
                placeholder="请输入身份证号"
                @focus="inputFocused.idCard = true"
                @blur="inputFocused.idCard = false"
              />
            </div>
          </el-form-item>
          
          <el-form-item prop="password">
            <div class="input-wrapper" :class="{ 'focused': inputFocused.registerPassword, 'has-value': registerForm.password }">
              <div class="input-icon">
                <svg viewBox="0 0 24 24" fill="currentColor">
                  <path d="M18 8h-1V6c0-2.76-2.24-5-5-5S7 3.24 7 6v2H6c-1.1 0-2 .9-2 2v10c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V10c0-1.1-.9-2-2-2zm-6 9c-1.1 0-2-.9-2-2s.9-2 2-2 2 .9 2 2-.9 2-2 2zm3.1-9H8.9V6c0-1.71 1.39-3.1 3.1-3.1 1.71 0 3.1 1.39 3.1 3.1v2z"/>
                </svg>
              </div>
              <input
                v-model="registerForm.password"
                :type="showRegisterPassword ? 'text' : 'password'"
                class="custom-input"
                placeholder="请输入密码"
                @focus="inputFocused.registerPassword = true"
                @blur="inputFocused.registerPassword = false"
              />
              <button 
                type="button" 
                class="toggle-password"
                @click="showRegisterPassword = !showRegisterPassword"
              >
                <svg v-if="!showRegisterPassword" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M12 4.5C7 4.5 2.73 7.61 1 12c1.73 4.39 6 7.5 11 7.5s9.27-3.11 11-7.5c-1.73-4.39-6-7.5-11-7.5zM12 17c-2.76 0-5-2.24-5-5s2.24-5 5-5 5 2.24 5 5-2.24 5-5 5zm0-8c-1.66 0-3 1.34-3 3s1.34 3 3 3 3-1.34 3-3-1.34-3-3-3z"/>
                </svg>
                <svg v-else viewBox="0 0 24 24" fill="currentColor">
                  <path d="M12 7c2.76 0 5 2.24 5 5 0 .65-.13 1.26-.36 1.83l2.92 2.92c1.51-1.26 2.7-2.89 3.43-4.75-1.73-4.39-6-7.5-11-7.5-1.4 0-2.74.25-3.98.7l2.16 2.16C10.74 7.13 11.35 7 12 7zm-6.93-.01l2.17 2.17C5.69 9.92 4.5 11.86 4.5 14c0 2.76 2.24 5 5 5 1.86 0 3.48-1.02 4.34-2.52l2.17 2.17C14.42 20.24 12.38 21 10.5 21c-4.97 0-9-4.03-9-9 0-2.13.74-4.09 1.97-5.61l1.6-.4zm8.62 8.62l-3.77-3.77c.11-.01.22-.02.33-.02 1.66 0 3 1.34 3 3 0 .11-.01.22-.02.33l.46.46z"/>
                </svg>
              </button>
            </div>
          </el-form-item>
          
          <el-form-item prop="confirmPassword">
            <div class="input-wrapper" :class="{ 'focused': inputFocused.confirmPassword, 'has-value': registerForm.confirmPassword }">
              <div class="input-icon">
                <svg viewBox="0 0 24 24" fill="currentColor">
                  <path d="M18 8h-1V6c0-2.76-2.24-5-5-5S7 3.24 7 6v2H6c-1.1 0-2 .9-2 2v10c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V10c0-1.1-.9-2-2-2zm-6 9c-1.1 0-2-.9-2-2s.9-2 2-2 2 .9 2 2-.9 2-2 2zm3.1-9H8.9V6c0-1.71 1.39-3.1 3.1-3.1 1.71 0 3.1 1.39 3.1 3.1v2z"/>
                </svg>
              </div>
              <input
                v-model="registerForm.confirmPassword"
                :type="showRegisterPassword ? 'text' : 'password'"
                class="custom-input"
                placeholder="请确认密码"
                @focus="inputFocused.confirmPassword = true"
                @blur="inputFocused.confirmPassword = false"
              />
            </div>
          </el-form-item>
          
          <el-form-item prop="role">
            <div class="role-selector">
              <label class="role-label">选择角色</label>
              <div class="role-options">
                <div 
                  class="role-option" 
                  :class="{ 'active': registerForm.role === 'ADMIN' }"
                  @click="registerForm.role = 'ADMIN'"
                >
                  <div class="radio-dot"></div>
                  <span>管理员</span>
                </div>
                <div 
                  class="role-option" 
                  :class="{ 'active': registerForm.role === 'USER' }"
                  @click="registerForm.role = 'USER'"
                >
                  <div class="radio-dot"></div>
                  <span>普通用户</span>
                </div>
              </div>
            </div>
          </el-form-item>
          
          <el-form-item>
            <button 
              type="button"
              class="submit-btn" 
              :disabled="loading"
              @click="handleRegister"
            >
              <span v-if="!loading">注册</span>
              <span v-else class="loading-text">
                <span class="spinner"></span>
                注册中...
              </span>
            </button>
          </el-form-item>
        </el-form>
        
        <el-form 
          v-else-if="currentForm === 'forgot-password'"
          ref="forgotPasswordForm" 
          :model="forgotPasswordForm" 
          :rules="forgotPasswordRules" 
          class="login-form"
          @keyup.enter.native="handleForgotPassword"
        >
          <div class="form-header">
            <h2>忘记密码</h2>
            <p>请输入您的身份信息进行验证</p>
          </div>
          
          <el-form-item prop="username">
            <div class="input-wrapper" :class="{ 'focused': inputFocused.forgotUsername, 'has-value': forgotPasswordForm.username }">
              <div class="input-icon">
                <svg viewBox="0 0 24 24" fill="currentColor">
                  <path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/>
                </svg>
              </div>
              <input
                v-model="forgotPasswordForm.username"
                type="text"
                class="custom-input"
                placeholder="请输入用户名"
                @focus="inputFocused.forgotUsername = true"
                @blur="inputFocused.forgotUsername = false"
              />
            </div>
          </el-form-item>
          
          <el-form-item prop="idCard">
            <div class="input-wrapper" :class="{ 'focused': inputFocused.forgotIdCard, 'has-value': forgotPasswordForm.idCard }">
              <div class="input-icon">
                <svg viewBox="0 0 24 24" fill="currentColor">
                  <path d="M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm0 16H5V5h14v14zm-7-2h5v-2h-5v2zm0-4h5v-2h-5v2zm-3-4h8V7H9v2z"/>
                </svg>
              </div>
              <input
                v-model="forgotPasswordForm.idCard"
                type="text"
                class="custom-input"
                placeholder="请输入身份证号"
                @focus="inputFocused.forgotIdCard = true"
                @blur="inputFocused.forgotIdCard = false"
              />
            </div>
          </el-form-item>
          
          <el-form-item>
            <button 
              type="button"
              class="submit-btn" 
              :disabled="loading"
              @click="handleForgotPassword"
            >
              <span v-if="!loading">验证身份</span>
              <span v-else class="loading-text">
                <span class="spinner"></span>
                验证中...
              </span>
            </button>
          </el-form-item>
          
          <div class="back-to-login">
            <span @click="switchForm('login')">返回登录</span>
          </div>
        </el-form>
        
        <el-form 
          v-else-if="currentForm === 'reset-password'"
          ref="resetPasswordForm" 
          :model="resetPasswordForm" 
          :rules="resetPasswordRules" 
          class="login-form"
          @keyup.enter.native="handleResetPassword"
        >
          <el-form-item prop="newPassword">
            <div class="input-wrapper" :class="{ 'focused': inputFocused.newPassword, 'has-value': resetPasswordForm.newPassword }">
              <div class="input-icon">
                <svg viewBox="0 0 24 24" fill="currentColor">
                  <path d="M18 8h-1V6c0-2.76-2.24-5-5-5S7 3.24 7 6v2H6c-1.1 0-2 .9-2 2v10c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V10c0-1.1-.9-2-2-2zm-6 9c-1.1 0-2-.9-2-2s.9-2 2-2 2 .9 2 2-.9 2-2 2zm3.1-9H8.9V6c0-1.71 1.39-3.1 3.1-3.1 1.71 0 3.1 1.39 3.1 3.1v2z"/>
                </svg>
              </div>
              <input
                v-model="resetPasswordForm.newPassword"
                :type="showResetPassword ? 'text' : 'password'"
                class="custom-input"
                placeholder="请输入新密码"
                @focus="inputFocused.newPassword = true"
                @blur="inputFocused.newPassword = false"
              />
              <button 
                type="button" 
                class="toggle-password"
                @click="showResetPassword = !showResetPassword"
              >
                <svg v-if="!showResetPassword" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M12 4.5C7 4.5 2.73 7.61 1 12c1.73 4.39 6 7.5 11 7.5s9.27-3.11 11-7.5c-1.73-4.39-6-7.5-11-7.5zM12 17c-2.76 0-5-2.24-5-5s2.24-5 5-5 5 2.24 5 5-2.24 5-5 5zm0-8c-1.66 0-3 1.34-3 3s1.34 3 3 3 3-1.34 3-3-1.34-3-3-3z"/>
                </svg>
                <svg v-else viewBox="0 0 24 24" fill="currentColor">
                  <path d="M12 7c2.76 0 5 2.24 5 5 0 .65-.13 1.26-.36 1.83l2.92 2.92c1.51-1.26 2.7-2.89 3.43-4.75-1.73-4.39-6-7.5-11-7.5-1.4 0-2.74.25-3.98.7l2.16 2.16C10.74 7.13 11.35 7 12 7zm-6.93-.01l2.17 2.17C5.69 9.92 4.5 11.86 4.5 14c0 2.76 2.24 5 5 5 1.86 0 3.48-1.02 4.34-2.52l2.17 2.17C14.42 20.24 12.38 21 10.5 21c-4.97 0-9-4.03-9-9 0-2.13.74-4.09 1.97-5.61l1.6-.4zm8.62 8.62l-3.77-3.77c.11-.01.22-.02.33-.02 1.66 0 3 1.34 3 3 0 .11-.01.22-.02.33l.46.46z"/>
                </svg>
              </button>
            </div>
          </el-form-item>
          
          <el-form-item prop="confirmPassword">
            <div class="input-wrapper" :class="{ 'focused': inputFocused.confirmNewPassword, 'has-value': resetPasswordForm.confirmPassword }">
              <div class="input-icon">
                <svg viewBox="0 0 24 24" fill="currentColor">
                  <path d="M18 8h-1V6c0-2.76-2.24-5-5-5S7 3.24 7 6v2H6c-1.1 0-2 .9-2 2v10c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V10c0-1.1-.9-2-2-2zm-6 9c-1.1 0-2-.9-2-2s.9-2 2-2 2 .9 2 2-.9 2-2 2zm3.1-9H8.9V6c0-1.71 1.39-3.1 3.1-3.1 1.71 0 3.1 1.39 3.1 3.1v2z"/>
                </svg>
              </div>
              <input
                v-model="resetPasswordForm.confirmPassword"
                :type="showResetPassword ? 'text' : 'password'"
                class="custom-input"
                placeholder="请确认新密码"
                @focus="inputFocused.confirmNewPassword = true"
                @blur="inputFocused.confirmNewPassword = false"
              />
            </div>
          </el-form-item>
          
          <el-form-item>
            <button 
              type="button"
              class="submit-btn" 
              :disabled="loading"
              @click="handleResetPassword"
            >
              <span v-if="!loading">重置密码</span>
              <span v-else class="loading-text">
                <span class="spinner"></span>
                重置中...
              </span>
            </button>
          </el-form-item>
          
          <div class="back-to-login">
            <span @click="switchForm('login')">返回登录</span>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import { login, register, forgotPassword, resetPassword } from '@/api/auth'

export default {
  name: 'Login',
  data() {
    return {
      currentForm: 'login',
      
      loginForm: {
        username: '',
        password: '',
        role: 'ADMIN'
      },
      
      registerForm: {
        username: '',
        idCard: '',
        password: '',
        confirmPassword: '',
        role: ''
      },
      
      forgotPasswordForm: {
        username: '',
        idCard: ''
      },
      
      resetPasswordForm: {
        username: '',
        idCard: '',
        newPassword: '',
        confirmPassword: '',
        token: ''
      },
      
      loginRules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur'}
        ],
        role: [
          {required: true, message: '请选择权限角色', trigger: 'blur'}
        ]
      },
      
      registerRules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'}
        ],
        idCard: [
          {required: true, message: '请输入身份证号', trigger: 'blur'},
          {pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, message: '请输入有效的身份证号', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur'}
        ],
        confirmPassword: [
          {required: true, message: '请确认密码', trigger: 'blur'},
          {validator: (rule, value, callback) => {
            if (value !== this.registerForm.password) {
              callback(new Error('两次输入的密码不一致'))
            } else {
              callback()
            }
          }, trigger: 'blur'}
        ],
        role: [
          {required: true, message: '请选择权限角色', trigger: 'blur'}
        ]
      },
      
      forgotPasswordRules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'}
        ],
        idCard: [
          {required: true, message: '请输入身份证号', trigger: 'blur'},
          {pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, message: '请输入有效的身份证号', trigger: 'blur'}
        ]
      },
      
      resetPasswordRules: {
        newPassword: [
          {required: true, message: '请输入新密码', trigger: 'blur'},
          {min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur'}
        ],
        confirmPassword: [
          {required: true, message: '请确认新密码', trigger: 'blur'},
          {validator: (rule, value, callback) => {
            if (value !== this.resetPasswordForm.newPassword) {
              callback(new Error('两次输入的密码不一致'))
            } else {
              callback()
            }
          }, trigger: 'blur'}
        ]
      },
      
      loading: false,
      rememberMe: false,
      
      inputFocused: {
        username: false,
        password: false,
        registerUsername: false,
        idCard: false,
        registerPassword: false,
        confirmPassword: false,
        forgotUsername: false,
        forgotIdCard: false,
        newPassword: false,
        confirmNewPassword: false
      },
      
      showPassword: false,
      showRegisterPassword: false,
      showResetPassword: false
    }
  },

  mounted() {
    const token = this.$route.query.token
    if (token) {
      this.currentForm = 'reset-password'
      this.resetPasswordForm.token = token
    }
  },
  
  methods: {
    getParticleStyle(index) {
      const random = (min, max) => Math.random() * (max - min) + min
      const size = random(2, 6)
      return {
        left: `${random(0, 100)}%`,
        top: `${random(0, 100)}%`,
        width: `${size}px`,
        height: `${size}px`,
        animationDelay: `${random(0, 5)}s`,
        animationDuration: `${random(10, 20)}s`
      }
    },
    
    switchForm(formName) {
      this.currentForm = formName
    },
    
    fillDemoAccount() {
      this.loginForm.username = 'admin'
      this.loginForm.password = '123456'
      this.$message.success('已填入演示账号信息')
    },
    
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true

          if (this.rememberMe) {
            localStorage.setItem('rememberedUsername', this.loginForm.username)
          } else {
            localStorage.removeItem('rememberedUsername')
          }

          login(this.loginForm)
            .then(response => {
              if (response.code === 200) {
                localStorage.setItem('token', response.data.token)
                localStorage.setItem('userInfo', JSON.stringify(response.data.user))
                this.$message.success('登录成功，欢迎使用人口普查管理系统！')
                setTimeout(() => {
                  this.$router.push('/')
                }, 800)
              } else {
                this.$message.error(response.message || '登录失败')
              }
            })
            .catch(error => {
              console.error('登录失败:', error)
              let errorMessage = '登录失败，请检查用户名和密码'
              if (error.response) {
                if (error.response.status === 403) {
                  errorMessage = '后端服务不可用，请确保后端服务已启动 (localhost:9090)'
                } else if (error.response.status === 404) {
                  errorMessage = '登录接口不存在，请检查后端路由配置'
                } else if (error.response.status === 500) {
                  errorMessage = '后端服务错误，请查看服务器日志'
                } else {
                  errorMessage = (error.response.data && error.response.data.message) || error.message || errorMessage
                }
              } else if (error.request) {
                errorMessage = '无法连接到服务器，请检查后端服务是否启动'
              } else {
                errorMessage = error.message || errorMessage
              }
              this.$message.error(errorMessage)
            })
            .finally(() => {
              this.loading = false
            })
        }
      })
    },
    
    handleRegister() {
      this.$refs.registerForm.validate(valid => {
        if (valid) {
          this.loading = true

          register(this.registerForm)
            .then(response => {
              if (response.code === 200) {
                this.$message.success('注册成功，请登录')
                this.currentForm = 'login'
                this.registerForm = {
                  username: '',
                  idCard: '',
                  password: '',
                  confirmPassword: '',
                  role: ''
                }
              } else {
                this.$message.error(response.message || '注册失败')
              }
            })
            .catch(error => {
              console.error('注册失败:', error)
              const errorMessage = error.message || '注册失败，请稍后重试'
              this.$message.error(errorMessage)
            })
            .finally(() => {
              this.loading = false
            })
        }
      })
    },
    
    handleForgotPassword() {
      this.$refs.forgotPasswordForm.validate(valid => {
        if (valid) {
          this.loading = true

          forgotPassword(this.forgotPasswordForm)
            .then(response => {
              if (response.code === 200) {
                this.$message.success('身份验证成功，请设置新密码')
                this.currentForm = 'reset-password'
                this.resetPasswordForm.username = this.forgotPasswordForm.username
                this.resetPasswordForm.idCard = this.forgotPasswordForm.idCard
              } else {
                this.$message.error(response.message || '身份验证失败')
              }
            })
            .catch(error => {
              console.error('身份验证失败:', error)
              const errorMessage = error.message || '身份验证失败，请稍后重试'
              this.$message.error(errorMessage)
            })
            .finally(() => {
              this.loading = false
            })
        }
      })
    },
    
    handleResetPassword() {
      this.$refs.resetPasswordForm.validate(valid => {
        if (valid) {
          this.loading = true

          resetPassword({
            username: this.resetPasswordForm.username,
            idCard: this.resetPasswordForm.idCard,
            token: this.resetPasswordForm.token,
            newPassword: this.resetPasswordForm.newPassword,
            confirmPassword: this.resetPasswordForm.confirmPassword
          })
            .then(response => {
              if (response.code === 200) {
                this.$message.success('密码重置成功，请登录')
                this.currentForm = 'login'
                this.resetPasswordForm = {
                  newPassword: '',
                  confirmPassword: '',
                  token: ''
                }
              } else {
                this.$message.error(response.message || '密码重置失败')
              }
            })
            .catch(error => {
              console.error('密码重置失败:', error)
              const errorMessage = error.message || '密码重置失败，请稍后重试'
              this.$message.error(errorMessage)
            })
            .finally(() => {
              this.loading = false
            })
        }
      })
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  min-height: 100vh;
  font-family: 'PingFang SC', 'Microsoft YaHei', 'Helvetica Neue', Arial, sans-serif;
  position: relative;
  overflow: hidden;
}

.aurora-bg {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
  overflow: hidden;
  opacity: 0.7;
}

.aurora {
  position: absolute;
  width: 50%;
  height: 50%;
  border-radius: 50%;
  filter: blur(100px);
  opacity: 0.4;
  animation: aurora-move 25s ease-in-out infinite;
}

.aurora-1 {
  background: linear-gradient(135deg, #38bdf8 0%, #60a5fa 100%);
  top: -10%;
  left: -5%;
  animation-delay: 0s;
}

.aurora-2 {
  background: linear-gradient(135deg, #60a5fa 0%, #38bdf8 100%);
  top: 30%;
  right: -15%;
  animation-delay: -7s;
}

.aurora-3 {
  background: linear-gradient(135deg, #22d3ee 0%, #38bdf8 100%);
  bottom: -15%;
  left: 25%;
  animation-delay: -14s;
}

.aurora-4 {
  background: linear-gradient(135deg, #38bdf8 0%, #22d3ee 100%);
  top: 15%;
  left: 45%;
  animation-delay: -20s;
}

@keyframes aurora-move {
  0%, 100% {
    transform: translate(0, 0) scale(1);
  }
  25% {
    transform: translate(8%, 8%) scale(1.05);
  }
  50% {
    transform: translate(-3%, 12%) scale(0.98);
  }
  75% {
    transform: translate(-10%, 4%) scale(1.02);
  }
}

.grid-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: 
    linear-gradient(rgba(255, 255, 255, 0.03) 1px, transparent 1px),
    linear-gradient(90deg, rgba(255, 255, 255, 0.03) 1px, transparent 1px);
  background-size: 50px 50px;
  z-index: 1;
  pointer-events: none;
}

.particles {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 2;
  pointer-events: none;
}

.particle {
  position: absolute;
  background: rgba(255, 255, 255, 0.6);
  border-radius: 50%;
  animation: particle-float linear infinite;
}

@keyframes particle-float {
  0% {
    transform: translateY(0) translateX(0);
    opacity: 0;
  }
  10% {
    opacity: 1;
  }
  90% {
    opacity: 1;
  }
  100% {
    transform: translateY(-100vh) translateX(20px);
    opacity: 0;
  }
}

.fade-in {
  opacity: 0;
  animation: fadeIn 0.8s ease-out forwards;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.fade-in-up {
  opacity: 0;
  animation: fadeInUp 0.6s ease-out forwards;
  animation-delay: 0.3s;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.gradient-text {
  background: linear-gradient(135deg, #ffffff 0%, #bfdbfe 30%, #ffffff 60%, #93c5fd 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  background-size: 300% 300%;
  animation: gradient-shift 8s ease infinite;
}

@keyframes gradient-shift {
  0%, 100% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
}

.left-panel {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  z-index: 10;
  overflow: hidden;
  background: linear-gradient(135deg, #38bdf8 0%, #0ea5e9 50%, #38bdf8 100%);
}

.left-panel::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: radial-gradient(ellipse at center, transparent 0%, rgba(0, 0, 0, 0.3) 100%);
  z-index: -1;
}

.brand-content {
  position: relative;
  z-index: 1;
  text-align: center;
  color: white;
  padding: 40px;
}

.logo-wrapper {
  margin-bottom: 30px;
}

.logo-icon {
  width: 120px;
  height: 120px;
  color: white;
  filter: drop-shadow(0 10px 30px rgba(0, 0, 0, 0.4));
  animation: float 3s ease-in-out infinite;
}

@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}

.system-title {
  font-size: 42px;
  font-weight: 700;
  margin: 0 0 15px;
  text-shadow: 0 4px 20px rgba(0, 0, 0, 0.5), 0 2px 4px rgba(0, 0, 0, 0.3);
  letter-spacing: 2px;
  color: #ffffff;
}

.system-subtitle {
  font-size: 18px;
  color: rgba(255, 255, 255, 0.95);
  margin: 0 0 50px;
  letter-spacing: 1px;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
}

.features {
  display: flex;
  gap: 40px;
  justify-content: center;
  margin-top: 40px;
}

.feature-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  transition: transform 0.3s ease;
}

.feature-item:hover {
  transform: translateY(-5px);
}

.feature-icon-wrapper {
  width: 70px;
  height: 70px;
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.25);
  backdrop-filter: blur(10px);
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  border: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
}

.feature-item:hover .feature-icon-wrapper {
  background: rgba(255, 255, 255, 0.35);
  transform: scale(1.1);
  box-shadow: 0 12px 35px rgba(0, 0, 0, 0.2);
}

.feature-icon {
  width: 32px;
  height: 32px;
  color: #ffffff;
}

.feature-item span {
  font-size: 15px;
  color: #ffffff;
  font-weight: 600;
  text-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
}

.right-panel {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  z-index: 10;
  padding: 40px;
  background: linear-gradient(180deg, #f8fafc 0%, #f1f5f9 100%);
}

.form-container {
  width: 100%;
  max-width: 440px;
  background: #ffffff;
  border-radius: 28px;
  padding: 48px 44px;
  box-shadow: 
    0 25px 60px rgba(0, 0, 0, 0.08),
    0 10px 25px rgba(0, 0, 0, 0.04),
    0 0 0 1px rgba(0, 0, 0, 0.02);
}

.form-tabs {
  display: flex;
  margin-bottom: 36px;
  background: #f1f5f9;
  border-radius: 14px;
  padding: 5px;
  position: relative;
}

.tab-item {
  flex: 1;
  padding: 14px 24px;
  text-align: center;
  font-size: 15px;
  font-weight: 600;
  color: #64748b;
  cursor: pointer;
  border-radius: 10px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}

.tab-text {
  position: relative;
  z-index: 2;
}

.tab-indicator {
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%) scaleX(0);
  width: 30px;
  height: 3px;
  background: linear-gradient(135deg, #38bdf8 0%, #60a5fa 100%);
  border-radius: 2px;
  transition: transform 0.3s ease;
}

.tab-item:hover {
  color: #38bdf8;
}

.tab-item.active {
  background: white;
  color: #38bdf8;
  box-shadow: 0 2px 10px rgba(56, 189, 248, 0.15);
}

.tab-item.active .tab-indicator {
  transform: translateX(-50%) scaleX(1);
}

.form-header {
  text-align: center;
  margin-bottom: 28px;
}

.form-header h2 {
  font-size: 24px;
  color: #1e293b;
  margin: 0 0 8px;
  font-weight: 700;
}

.form-header p {
  font-size: 14px;
  color: #64748b;
  margin: 0;
}

.login-form {
  margin-top: 0;
}

.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  background: #f8fafc;
  border: 2px solid #e2e8f0;
  border-radius: 14px;
  padding: 15px 18px;
  transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
  margin-bottom: 6px;
}

.input-wrapper:hover {
  background: #ffffff;
  border-color: #cbd5e1;
}

.input-wrapper.focused {
  background: #ffffff;
  border-color: #38bdf8;
  box-shadow: 0 0 0 4px rgba(56, 189, 248, 0.08);
}

.input-icon {
  display: flex;
  align-items: center;
  margin-right: 12px;
  color: #94a3b8;
  transition: all 0.25s ease;
}

.input-icon svg {
  width: 20px;
  height: 20px;
}

.input-wrapper.focused .input-icon {
  color: #38bdf8;
}

.custom-input {
  flex: 1;
  border: none;
  outline: none;
  background: transparent;
  font-size: 15px;
  color: #1e293b;
}

.custom-input::placeholder {
  color: #94a3b8;
}

.toggle-password {
  position: absolute;
  right: 15px;
  background: none;
  border: none;
  cursor: pointer;
  padding: 0;
  margin: 0;
  width: 24px;
  height: 24px;
  color: #94a3b8;
  transition: all 0.25s ease;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.toggle-password:hover {
  color: #38bdf8;
  background: rgba(56, 189, 248, 0.08);
}

.toggle-password svg {
  width: 20px;
  height: 20px;
}

.role-selector {
  width: 100%;
}

.role-label {
  display: block;
  font-size: 14px;
  color: #475569;
  margin-bottom: 10px;
  font-weight: 600;
}

.role-options {
  display: flex;
  gap: 14px;
}

.role-option {
  flex: 1;
  display: flex;
  align-items: center;
  padding: 14px 18px;
  background: #f8fafc;
  border: 2px solid #e2e8f0;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
}

.role-option:hover {
  background: #ffffff;
  border-color: #38bdf8;
}

.role-option.active {
  background: linear-gradient(135deg, #38bdf8 0%, #60a5fa 100%);
  border-color: transparent;
  color: white;
  box-shadow: 0 6px 20px rgba(56, 189, 248, 0.3);
}

.radio-dot {
  width: 18px;
  height: 18px;
  border: 2px solid #cbd5e1;
  border-radius: 50%;
  margin-right: 10px;
  position: relative;
  transition: all 0.25s ease;
}

.role-option.active .radio-dot {
  border-color: white;
}

.role-option.active .radio-dot::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%) scale(1);
  width: 8px;
  height: 8px;
  background: white;
  border-radius: 50%;
  animation: dot-appear 0.25s ease;
}

@keyframes dot-appear {
  from {
    transform: translate(-50%, -50%) scale(0);
  }
  to {
    transform: translate(-50%, -50%) scale(1);
  }
}

.role-option span {
  font-size: 14px;
  font-weight: 600;
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 18px 0;
}

.form-options :deep(.el-checkbox__label) {
  color: #475569;
  font-size: 14px;
}

.form-options :deep(.el-checkbox__input.is-checked .el-checkbox__inner) {
  background-color: #667eea;
  border-color: #667eea;
}

.forgot-link {
  color: #38bdf8;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.25s ease;
  position: relative;
}

.forgot-link::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 0;
  width: 0;
  height: 2px;
  background: linear-gradient(135deg, #38bdf8 0%, #60a5fa 100%);
  transition: width 0.25s ease;
}

.forgot-link:hover {
  color: #60a5fa;
}

.forgot-link:hover::after {
  width: 100%;
}

.submit-btn {
  width: 100%;
  padding: 16px;
  background: linear-gradient(135deg, #38bdf8 0%, #60a5fa 100%);
  border: none;
  border-radius: 14px;
  color: white;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
  margin-top: 8px;
  position: relative;
  overflow: hidden;
}

.submit-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: left 0.5s ease;
}

.submit-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 10px 30px rgba(56, 189, 248, 0.35);
}

.submit-btn:hover:not(:disabled)::before {
  left: 100%;
}

.submit-btn:active:not(:disabled) {
  transform: translateY(0);
}

.submit-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.loading-text {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.spinner {
  width: 16px;
  height: 16px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.back-to-login {
  text-align: center;
  margin-top: 18px;
}

.back-to-login span {
  color: #38bdf8;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.25s ease;
  position: relative;
}

.back-to-login span::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 0;
  width: 0;
  height: 2px;
  background: linear-gradient(135deg, #38bdf8 0%, #60a5fa 100%);
  transition: width 0.25s ease;
}

.back-to-login span:hover {
  color: #60a5fa;
}

.back-to-login span:hover::after {
  width: 100%;
}

@media (max-width: 968px) {
  .login-container {
    flex-direction: column;
  }
  
  .left-panel {
    padding: 60px 40px;
  }
  
  .system-title {
    font-size: 32px;
  }
  
  .features {
    gap: 30px;
  }
  
  .right-panel {
    padding: 30px 20px;
  }
  
  .form-container {
    padding: 40px 30px;
  }
}

@media (max-width: 480px) {
  .left-panel {
    padding: 40px 20px;
  }
  
  .logo-icon {
    width: 80px;
    height: 80px;
  }
  
  .system-title {
    font-size: 28px;
  }
  
  .features {
    flex-direction: column;
    gap: 20px;
  }
  
  .form-container {
    padding: 30px 25px;
  }
  
  .role-options {
    flex-direction: column;
    gap: 10px;
  }
}
</style>
