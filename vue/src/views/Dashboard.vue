<template>
  <div class="dashboard">
    <div class="dashboard-content-wrapper">
      <div class="welcome-section">
        <div class="welcome-card glass-card glass-surface">
          <div class="welcome-header">
            <div class="welcome-icon animated-icon">
              <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M12 2L2 7L12 12L22 7L12 2Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                <path d="M2 17L12 22L22 17" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                <path d="M2 12L12 17L22 12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </div>
            <h1 class="welcome-title">
              <span class="shiny-text">欢迎使用人口普查管理系统</span>
            </h1>
            <p class="welcome-subtitle animated-fade-in" v-if="userInfo.role === 'ADMIN'">全面的人口数据管理与分析平台</p>
            <p class="welcome-subtitle animated-fade-in" v-else>便捷的个人数据管理平台</p>
            <div class="welcome-meta animated-fade-in-delay">
              <div class="current-time">
                <svg class="time-icon" viewBox="0 0 24 24" fill="none">
                  <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="2"/>
                  <path d="M12 6V12L16 14" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
                </svg>
                {{ currentTime }}
              </div>
              <div class="greeting">{{ greeting }}</div>
            </div>
          </div>
        </div>
      </div>
    
      <div class="stats-section" v-if="userInfo.role === 'ADMIN'">
        <h2 class="section-title animated-fade-in">
          <span class="title-icon animated-icon-small">
            <svg viewBox="0 0 24 24" fill="none">
              <path d="M16 8V16M12 11V16M8 14V16M6 20H18C19.1046 20 20 19.1046 20 18V6C20 4.89543 19.1046 4 18 4H6C4.89543 4 4 4.89543 4 6V18C4 19.1046 4.89543 20 6 20Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </span>
          <span class="gradient-text-title">数据概览</span>
        </h2>
        <div class="stats-grid">
          <div class="stat-card glass-card glass-surface" v-for="(stat, index) in adminStats" :key="index" :style="{ animationDelay: index * 0.1 + 's' }">
            <div class="stat-icon animated-bounce" :class="stat.bgClass">
              <el-icon :size="28"><component :is="stat.icon" /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-title">{{ stat.title }}</div>
              <div class="stat-value animated-count">{{ summaryData[stat.key] || 0 | numberFormat }}</div>
            </div>
            <div class="stat-decoration"></div>
            <div class="card-glow"></div>
          </div>
        </div>
      </div>
    
    
      <div class="modules-section">
        <h2 class="section-title animated-fade-in">
          <span class="title-icon animated-icon-small">
            <svg viewBox="0 0 24 24" fill="none">
              <path d="M4 6H10M4 12H16M4 18H20" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
          </span>
          <span class="gradient-text-title">功能模块</span>
        </h2>
        
        <div class="modules-grid">
          <div class="module-card glass-card glass-surface" v-for="(module, index) in modules" :key="index" :style="{ animationDelay: index * 0.15 + 's' }">
            <div class="module-header">
              <div class="module-icon-wrapper animated-bounce" :class="module.iconBg">
                <el-icon class="module-icon"><component :is="module.icon" /></el-icon>
              </div>
              <h3 class="module-title gradient-text-subtitle">{{ module.title }}</h3>
            </div>
            <p class="module-description">{{ module.description }}</p>
            <div class="module-actions">
              <button v-for="(action, idx) in module.actions" :key="idx"
                class="action-btn animated-btn" :class="action.type"
                @click="navigateTo(action.path)">
                <el-icon><component :is="action.icon" /></el-icon>
                <span>{{ action.text }}</span>
              </button>
            </div>
            <div class="card-glow"></div>
          </div>
        </div>
      </div>
    
      <div class="charts-section" v-if="userInfo.role === 'ADMIN'">
        <h2 class="section-title animated-fade-in">
          <span class="title-icon animated-icon-small">
            <svg viewBox="0 0 24 24" fill="none">
              <path d="M18 20V10M12 20V4M6 20V14" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </span>
          <span class="gradient-text-title">数据可视化</span>
        </h2>
        
        <div class="charts-grid">
          <div class="chart-card glass-card glass-surface">
            <div class="chart-header">
              <h3 class="gradient-text-subtitle">各地区人口统计</h3>
            </div>
            <div ref="populationByRegionChart" class="chart-container"></div>
            <div class="card-glow"></div>
          </div>
          
          <div class="chart-card glass-card glass-surface">
            <div class="chart-header">
              <h3 class="gradient-text-subtitle">年龄分布情况</h3>
            </div>
            <div ref="ageDistributionChart" class="chart-container"></div>
            <div class="card-glow"></div>
          </div>
        </div>
        
        <div class="chart-card glass-card glass-surface full-width">
          <div class="chart-header">
            <h3 class="gradient-text-subtitle">近5年人口增长趋势</h3>
          </div>
          <div ref="populationTrendChart" class="chart-container large"></div>
          <div class="card-glow"></div>
        </div>
        
        <div class="charts-grid">
          <div class="chart-card glass-card glass-surface">
            <div class="chart-header">
              <h3 class="gradient-text-subtitle">教育程度分布</h3>
            </div>
            <div ref="educationLevelChart" class="chart-container"></div>
            <div class="card-glow"></div>
          </div>
          
          <div class="chart-card glass-card glass-surface">
            <div class="chart-header">
              <h3 class="gradient-text-subtitle">医疗保险类型分布</h3>
            </div>
            <div ref="medicalInsuranceChart" class="chart-container"></div>
            <div class="card-glow"></div>
          </div>
        </div>
        
        <div class="charts-grid">
          <div class="chart-card glass-card glass-surface">
            <div class="chart-header">
              <h3 class="gradient-text-subtitle">就业情况统计</h3>
            </div>
            <div ref="employmentChart" class="chart-container"></div>
            <div class="card-glow"></div>
          </div>
          
          <div class="chart-card glass-card glass-surface">
            <div class="chart-header">
              <h3 class="gradient-text-subtitle">社保参保情况</h3>
            </div>
            <div ref="socialSecurityChart" class="chart-container"></div>
            <div class="card-glow"></div>
          </div>
        </div>
        
        <div class="charts-grid">
          <div class="chart-card glass-card glass-surface">
            <div class="chart-header">
              <h3 class="gradient-text-subtitle">房产类型分布</h3>
            </div>
            <div ref="propertyChart" class="chart-container"></div>
            <div class="card-glow"></div>
          </div>
          
          <div class="chart-card glass-card glass-surface">
            <div class="chart-header">
              <h3 class="gradient-text-subtitle">车辆品牌分布</h3>
            </div>
            <div ref="vehicleChart" class="chart-container"></div>
            <div class="card-glow"></div>
          </div>
        </div>
      </div>
    
      <div class="latest-data-section" v-if="userInfo.role === 'ADMIN'">
        <div class="data-card glass-card glass-surface">
          <div class="data-card-header">
            <h3 class="gradient-text-subtitle">最新数据</h3>
            <span class="update-time">数据更新时间：{{ summaryData.updateTime || '暂无数据' }}</span>
          </div>
          <div class="table-wrapper">
            <el-table :data="tableData" class="modern-table" stripe>
              <el-table-column prop="date" label="日期" width="120"></el-table-column>
              <el-table-column prop="name" label="姓名" width="120"></el-table-column>
              <el-table-column prop="type" label="类型" width="120"></el-table-column>
              <el-table-column prop="status" label="状态" width="100">
                <template #default="scope">
                  <el-tag v-if="scope && scope.row && scope.row.status" :type="scope.row.status === '已完成' ? 'success' : 'warning'">
                    {{ scope.row.status }}
                  </el-tag>
                  <span v-else>-</span>
                </template>
              </el-table-column>
              <el-table-column prop="content" label="内容"></el-table-column>
            </el-table>
          </div>
          <div class="card-glow"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { User, HomeFilled, OfficeBuilding, Medal, School, FirstAidKit, Briefcase, Ticket, House, DataAnalysis, Tools, Van } from '@element-plus/icons-vue'
import { 
  getPopulationByRegion,
  getAgeDistribution,
  getEducationLevel,
  getMedicalInsurance,
  getPopulationTrend,
  getSummary,
  getIndustryDistribution,
  getSocialSecurityCoverage,
  getPropertyTypeDistribution,
  getVehicleBrandDistribution,
  getEmploymentStatusDistribution,
  getLatestData
} from '@/api/statistics'

export default {
  name: 'Dashboard',
  components: {
    User,
    HomeFilled,
    OfficeBuilding,
    Medal,
    School,
    FirstAidKit,
    Briefcase,
    Ticket,
    House,
    DataAnalysis,
    Van
  },
  data() {
    return {
      currentTime: '',
      greeting: '祝您工作愉快，生活美满！',
      summaryData: {
        residentCount: 0,
        householdCount: 0,
        employmentCount: 0,
        socialSecurityCount: 0,
        educationCount: 0,
        medicalCount: 0,
        propertyCount: 0,
        vehicleCount: 0,
        updateTime: ''
      },
      userInfo: {
        username: '',
        role: 'USER'
      },
      tableData: [],
      adminStats: [
        { title: '总人口', key: 'residentCount', icon: 'User', bgClass: 'bg-blue' },
        { title: '户籍数', key: 'householdCount', icon: 'HomeFilled', bgClass: 'bg-green' },
        { title: '就业人数', key: 'employmentCount', icon: 'OfficeBuilding', bgClass: 'bg-yellow' },
        { title: '社保参保', key: 'socialSecurityCount', icon: 'Medal', bgClass: 'bg-red' },
        { title: '教育人数', key: 'educationCount', icon: 'School', bgClass: 'bg-purple' },
        { title: '医疗人数', key: 'medicalCount', icon: 'FirstAidKit', bgClass: 'bg-cyan' },
        { title: '房产数量', key: 'propertyCount', icon: 'House', bgClass: 'bg-orange' },
        { title: '车辆数量', key: 'vehicleCount', icon: 'Van', bgClass: 'bg-indigo' }
      ],
      modules: []
    }
  },
  created() {
    this.getUserInfo()
    this.initModules()
  },
  mounted() {
    this.updateCurrentTime()
    this.timeInterval = setInterval(this.updateCurrentTime, 1000) // 每秒更新时间
    
    // 根据用户角色决定是否初始化图表和加载数据
    if (this.userInfo.role === 'ADMIN') {
      this.initCharts() // 管理员初始化图表
      this.loadAllData() // 管理员加载所有数据
    } else {
      // 普通用户不初始化图表，避免DOM引用错误
      // 普通用户不加载统计数据，避免权限错误
      this.$message.success('登录成功，欢迎使用人口普查管理系统！')
    }
  },
  beforeDestroy() {
    // 清除定时器
    if (this.timeInterval) {
      clearInterval(this.timeInterval)
    }
    // 只有管理员才销毁图表实例，普通用户没有初始化图表
    if (this.userInfo.role === 'ADMIN') {
      this.destroyAllCharts()
    }
  },
  filters: {
    numberFormat(value) {
      if (!value) return '0'
      return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
    }
  },
  methods: {
    initModules() {
      const baseModules = [
        {
          title: '人口管理',
          description: '管理居民基本信息和户籍资料，包括人口统计、户籍变动等功能。',
          icon: 'User',
          iconBg: 'icon-blue',
          actions: [
            { text: '户籍管理', path: '/population/household', icon: 'HomeFilled', type: 'primary' },
            { text: '居民管理', path: '/population/resident', icon: 'User', type: 'success' }
          ]
        },
        {
          title: '社会管理',
          description: '管理居民教育信息和医疗健康数据，提供教育资源和医疗服务统计。',
          icon: 'School',
          iconBg: 'icon-purple',
          actions: [
            { text: '教育管理', path: '/social/education', icon: 'School', type: 'warning' },
            { text: '医疗管理', path: '/social/medical', icon: 'FirstAidKit', type: 'danger' }
          ]
        },
        {
          title: '就业社保',
          description: '管理居民就业信息和社会保险数据，提供就业趋势分析和社保统计。',
          icon: 'Briefcase',
          iconBg: 'icon-yellow',
          actions: [
            { text: '就业管理', path: '/employment-security/employment', icon: 'Briefcase', type: 'info' },
            { text: '社保管理', path: '/employment-security/social-security', icon: 'Medal', type: 'primary' }
          ]
        },
        {
          title: '财产管理',
          description: '管理居民房产和车辆信息，提供财产统计和分布分析功能。',
          icon: 'House',
          iconBg: 'icon-orange',
          actions: [
            { text: '房产管理', path: '/property/property-list', icon: 'House', type: 'success' },
            { text: '车辆管理', path: '/property/vehicle', icon: 'Van', type: 'warning' }
          ]
        }
      ]
      
      if (this.userInfo.role === 'ADMIN') {
        baseModules.push({
          title: '数据管理',
          description: '提供全面的数据统计和分析功能，包括人口趋势、年龄分布、教育医疗等多维度数据可视化。',
          icon: 'DataAnalysis',
          iconBg: 'icon-cyan',
          actions: [
            { text: '查看数据分析', path: '/data/detail', icon: 'DataAnalysis', type: 'primary' }
          ]
        })
      }
      
      this.modules = baseModules
    },
    
    updateCurrentTime() {
      const now = new Date()
      this.currentTime = now.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        weekday: 'long'
      })
    },
    
    loadSummaryData() {
      getSummary().then(response => {
        this.summaryData = response.data
      }).catch(error => {
        console.error('获取系统统计摘要失败:', error)
        this.$message.error('获取系统统计摘要失败')
      })
    },
    
    initCharts() {
      const chartTextStyle = {
        color: '#1a1a2e'
      }
      const chartAxisStyle = {
        color: 'rgba(0, 0, 0, 0.4)'
      }
      const chartSplitLineStyle = {
        color: 'rgba(0, 0, 0, 0.06)'
      }
      
      this.populationByRegionChart = echarts.init(this.$refs.populationByRegionChart)
      const populationByRegionOption = {
        backgroundColor: 'transparent',
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow',
            shadowStyle: {
              color: 'rgba(102, 126, 234, 0.1)'
            }
          },
          formatter: function (params) {
            return `
              <div style="padding: 8px 12px;">
                <div style="font-weight: bold; margin-bottom: 4px;">${params[0].name}</div>
                <div style="color: #667eea;">
                  <span style="display: inline-block; width: 10px; height: 10px; background: #667eea; border-radius: 50%; margin-right: 6px;"></span>
                  人口数量: <span style="font-weight: bold;">${params[0].value.toLocaleString()}</span> 人
                </div>
              </div>
            `
          },
          backgroundColor: 'rgba(255, 255, 255, 0.95)',
          borderColor: '#667eea',
          borderWidth: 1,
          borderRadius: 8,
          textStyle: {
            color: '#333'
          }
        },
        grid: {
          left: '1%',
          right: '8%',
          bottom: '3%',
          top: '5%',
          containLabel: true
        },
        xAxis: {
          type: 'value',
          boundaryGap: [0, 0.01],
          axisTick: { show: false },
          axisLine: { lineStyle: chartAxisStyle },
          splitLine: { lineStyle: chartSplitLineStyle },
          axisLabel: {
            formatter: '{value} 人',
            color: 'rgba(0, 0, 0, 0.6)'
          }
        },
        yAxis: {
          type: 'category',
          data: [],
          axisTick: { alignWithLabel: true, lineStyle: chartAxisStyle },
          axisLabel: {
            fontSize: 11,
            color: 'rgba(0, 0, 0, 0.7)',
            fontWeight: 500,
            align: 'right',
            padding: [0, 10, 0, 0],
            width: 120
          },
          axisLine: { lineStyle: chartAxisStyle }
        },
        series: [
          {
            name: '人口数量',
            type: 'bar',
            barWidth: '50%',
            data: [],
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                { offset: 0, color: '#667eea' },
                { offset: 1, color: '#764ba2' }
              ]),
              borderRadius: [0, 6, 6, 0]
            },
            emphasis: {
              itemStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                  { offset: 0, color: '#5a6fd8' },
                  { offset: 1, color: '#6a4190' }
                ]),
                shadowBlur: 10,
                shadowColor: 'rgba(102, 126, 234, 0.3)'
              }
            },
            label: {
              show: true,
              position: 'right',
              align: 'center',
              color: 'rgba(0, 0, 0, 0.8)',
              fontSize: 12,
              fontWeight: 'bold',
              formatter: '{c}',
              offset: [5, 0]
            },
            animationDelay: function (idx) {
              return idx * 50
            },
            animationEasing: 'elasticOut'
          }
        ],
        animationDuration: 1500,
        animationEasing: 'cubicOut'
      }
      this.populationByRegionChart.setOption(populationByRegionOption)
      
      // 添加点击事件
      this.populationByRegionChart.on('click', (params) => {
        this.$message({
          message: `您查看了 ${params.name} 的人口数据: ${params.value} 人`,
          type: 'success',
          duration: 2000
        })
      })
      
      this.ageDistributionChart = echarts.init(this.$refs.ageDistributionChart)
      const ageDistributionOption = {
        backgroundColor: 'transparent',
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c}人 ({d}%)',
          backgroundColor: 'rgba(255, 255, 255, 0.95)',
          borderColor: '#667eea',
          textStyle: { color: '#333' }
        },
        legend: {
          orient: 'vertical',
          right: 10,
          top: 'middle',
          textStyle: { color: 'rgba(0, 0, 0, 0.7)' }
        },
        series: [
          {
            name: '年龄分布',
            type: 'pie',
            radius: ['50%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: true,
              formatter: '{b}: {d}%',
              position: 'outside',
              color: 'rgba(0, 0, 0, 0.8)'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '14',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: true,
              lineStyle: { color: 'rgba(0, 0, 0, 0.3)' }
            },
            data: []
          }
        ]
      }
      this.ageDistributionChart.setOption(ageDistributionOption)
      
      // 添加点击事件
      this.ageDistributionChart.on('click', (params) => {
        this.$message.info(`您点击了${params.name}，人数为${params.value}人，占比${params.percent.toFixed(1)}%`)
      })
      
      this.educationLevelChart = echarts.init(this.$refs.educationLevelChart)
      const educationLevelOption = {
        backgroundColor: 'transparent',
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'shadow' },
          formatter: function (params) {
            let total = 0
            params.forEach(item => { total += item.value })
            let tooltip = params[0].name + '<br/>'
            params.forEach(item => {
              tooltip += item.marker + item.seriesName + ': ' + item.value + '人 (' + (item.value/total*100).toFixed(1) + '%)<br/>'
            })
            return tooltip
          },
          backgroundColor: 'rgba(255, 255, 255, 0.95)',
          borderColor: '#667eea',
          textStyle: { color: '#333' }
        },
        legend: {
          data: ['男性', '女性'],
          top: 10,
          textStyle: { color: 'rgba(0, 0, 0, 0.7)' }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'value',
          axisLine: { lineStyle: chartAxisStyle },
          splitLine: { lineStyle: chartSplitLineStyle },
          axisLabel: { color: 'rgba(0, 0, 0, 0.6)' }
        },
        yAxis: {
          type: 'category',
          data: [],
          axisLine: { lineStyle: chartAxisStyle },
          axisLabel: { color: 'rgba(0, 0, 0, 0.7)' }
        },
        series: [
          {
            name: '男性',
            type: 'bar',
            stack: 'total',
            label: { show: true, color: 'rgba(0, 0, 0, 0.8)' },
            emphasis: { focus: 'series' },
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                { offset: 0, color: '#667eea' },
                { offset: 1, color: '#764ba2' }
              ])
            },
            data: []
          },
          {
            name: '女性',
            type: 'bar',
            stack: 'total',
            label: { show: true, color: 'rgba(0, 0, 0, 0.8)' },
            emphasis: { focus: 'series' },
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                { offset: 0, color: '#f093fb' },
                { offset: 1, color: '#f5576c' }
              ])
            },
            data: []
          }
        ]
      }
      this.educationLevelChart.setOption(educationLevelOption)
      
      // 添加点击事件
      this.educationLevelChart.on('click', (params) => {
        this.$message.info(`您点击了${params.name}的${params.seriesName}，人数为${params.value}人`)
      })
      
      this.medicalInsuranceChart = echarts.init(this.$refs.medicalInsuranceChart)
      const medicalInsuranceOption = {
        backgroundColor: 'transparent',
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c}人 ({d}%)',
          backgroundColor: 'rgba(255, 255, 255, 0.95)',
          borderColor: '#667eea',
          textStyle: { color: '#333' }
        },
        legend: {
          orient: 'horizontal',
          bottom: 10,
          textStyle: { color: 'rgba(0, 0, 0, 0.7)' }
        },
        series: [
          {
            name: '保险类型',
            type: 'pie',
            radius: ['30%', '70%'],
            center: ['50%', '50%'],
            roseType: 'radius',
            itemStyle: {
              borderRadius: 8,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: true,
              formatter: '{b}: {d}%',
              color: 'rgba(0, 0, 0, 0.8)'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '14',
                fontWeight: 'bold'
              }
            },
            data: []
          }
        ]
      }
      this.medicalInsuranceChart.setOption(medicalInsuranceOption)
      
      // 添加点击事件
      this.medicalInsuranceChart.on('click', (params) => {
        this.$message.info(`您点击了${params.name}，人数为${params.value}人，占比${params.percent.toFixed(1)}%`)
      })
      
      this.populationTrendChart = echarts.init(this.$refs.populationTrendChart)
      const populationTrendOption = {
        backgroundColor: 'transparent',
        tooltip: {
          trigger: 'axis',
          formatter: function (params) {
            let tooltip = params[0].name + '<br/>'
            params.forEach(item => {
              tooltip += item.marker + item.seriesName + ': ' + item.value.toLocaleString() + '人<br/>'
            })
            return tooltip
          },
          backgroundColor: 'rgba(255, 255, 255, 0.95)',
          borderColor: '#667eea',
          textStyle: { color: '#333' }
        },
        legend: {
          data: ['总人口', '男性', '女性'],
          top: 10,
          textStyle: { color: 'rgba(0, 0, 0, 0.7)' }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: [],
          axisLine: { lineStyle: chartAxisStyle },
          axisLabel: { color: 'rgba(0, 0, 0, 0.6)' }
        },
        yAxis: {
          type: 'value',
          axisLine: { lineStyle: chartAxisStyle },
          splitLine: { lineStyle: chartSplitLineStyle },
          axisLabel: {
            formatter: function (value) { return value.toLocaleString() },
            color: 'rgba(0, 0, 0, 0.6)'
          }
        },
        series: [
          {
            name: '总人口',
            type: 'line',
            data: [],
            smooth: true,
            itemStyle: { color: '#667eea' },
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: 'rgba(102, 126, 234, 0.3)' },
                { offset: 1, color: 'rgba(102, 126, 234, 0.02)' }
              ])
            }
          },
          {
            name: '男性',
            type: 'line',
            data: [],
            smooth: true,
            itemStyle: { color: '#4facfe' },
            lineStyle: { type: 'dashed' }
          },
          {
            name: '女性',
            type: 'line',
            data: [],
            smooth: true,
            itemStyle: { color: '#f093fb' },
            lineStyle: { type: 'dashed' }
          }
        ]
      }
      this.populationTrendChart.setOption(populationTrendOption)
      
      // 添加点击事件
      this.populationTrendChart.on('click', (params) => {
        this.$message.info(`您点击了${params.name}的${params.seriesName}，人口数量为${params.value.toLocaleString()}人`)
      })
      
      this.employmentChart = echarts.init(this.$refs.employmentChart)
      const employmentOption = {
        backgroundColor: 'transparent',
        series: [
          {
            type: 'gauge',
            progress: { show: true, width: 18 },
            axisLine: {
              lineStyle: {
                width: 18,
                color: [
                  [0.3, '#11998e'],
                  [0.7, '#667eea'],
                  [1, '#f093fb']
                ]
              }
            },
            axisTick: { show: false },
            splitLine: {
              length: 15,
              lineStyle: { width: 2, color: 'rgba(0, 0, 0, 0.2)' }
            },
            axisLabel: {
              distance: 25,
              color: 'rgba(0, 0, 0, 0.5)',
              fontSize: 10
            },
            anchor: {
              show: true,
              showAbove: true,
              size: 18,
              itemStyle: { borderWidth: 10 }
            },
            title: { 
              show: true,
              offsetCenter: [0, '80%'],
              fontSize: 14,
              color: 'rgba(0, 0, 0, 0.7)'
            },
            detail: {
              valueAnimation: true,
              fontSize: 24,
              fontWeight: 'bold',
              color: 'rgba(0, 0, 0, 0.8)',
              formatter: '{value}%'
            },
            data: [{ value: 0 }]
          }
        ]
      }
      this.employmentChart.setOption(employmentOption)
      
      // 添加点击事件
      this.employmentChart.on('click', (params) => {
        this.$message.info(`当前就业率为${params.value}%`)
      })
      
      this.socialSecurityChart = echarts.init(this.$refs.socialSecurityChart)
      const socialSecurityOption = {
        backgroundColor: 'transparent',
        tooltip: {
          trigger: 'item',
          backgroundColor: 'rgba(255, 255, 255, 0.95)',
          borderColor: '#667eea',
          textStyle: { color: '#333' }
        },
        radar: {
          indicator: [
            { name: '养老保险', max: 1500 },
            { name: '医疗保险', max: 1500 },
            { name: '失业保险', max: 1500 },
            { name: '工伤保险', max: 1500 },
            { name: '生育保险', max: 1500 }
          ],
          radius: '60%',
          axisName: { 
            color: 'rgba(0, 0, 0, 0.8)',
            fontSize: 12
          },
          splitArea: { areaStyle: { color: ['rgba(0, 0, 0, 0.01)', 'rgba(0, 0, 0, 0.03)'] } },
          axisLine: { lineStyle: { color: 'rgba(0, 0, 0, 0.1)' } },
          splitLine: { lineStyle: { color: 'rgba(0, 0, 0, 0.06)' } }
        },
        series: [
          {
            name: '参保人数',
            type: 'radar',
            areaStyle: {
              opacity: 0.3,
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: 'rgba(102, 126, 234, 0.5)' },
                { offset: 1, color: 'rgba(118, 75, 162, 0.2)' }
              ])
            },
            itemStyle: { color: '#667eea' },
            lineStyle: { width: 2, color: '#667eea' },
            symbol: 'circle',
            symbolSize: 6,
            label: {
              show: true,
              color: 'rgba(0, 0, 0, 0.8)',
              fontSize: 11
            },
            data: [{ value: [], name: '参保人数' }]
          }
        ]
      }
      this.socialSecurityChart.setOption(socialSecurityOption)
      
      // 添加点击事件
      this.socialSecurityChart.on('click', (params) => {
        this.$message.info(`您点击了${params.name}，参保率分别为${params.value}%`)
      })
      
      this.propertyChart = echarts.init(this.$refs.propertyChart)
      const propertyOption = {
        backgroundColor: 'transparent',
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)',
          backgroundColor: 'rgba(255, 255, 255, 0.95)',
          borderColor: '#667eea',
          textStyle: { color: '#333' }
        },
        legend: {
          orient: 'vertical',
          right: 10,
          top: 'middle',
          textStyle: { color: 'rgba(0, 0, 0, 0.7)' }
        },
        series: [
          {
            name: '房产类型',
            type: 'pie',
            selectedMode: 'single',
            radius: [0, '30%'],
            label: {
              show: false
            },
            labelLine: { show: false },
            data: []
          },
          {
            name: '房产类型',
            type: 'pie',
            radius: ['40%', '55%'],
            label: {
              show: true,
              formatter: '{b}: {d}%',
              color: 'rgba(0, 0, 0, 0.8)'
            },
            labelLine: {
              show: true,
              lineStyle: { color: 'rgba(0, 0, 0, 0.4)' }
            },
            data: []
          }
        ]
      }
      this.propertyChart.setOption(propertyOption)
      
      // 添加点击事件
      this.propertyChart.on('click', (params) => {
        this.$message.info(`您点击了${params.name}，数量为${params.value}套，占比${params.percent.toFixed(1)}%`)
      })
      
      this.vehicleChart = echarts.init(this.$refs.vehicleChart)
      const vehicleOption = {
        backgroundColor: 'transparent',
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'shadow' },
          formatter: function (params) {
            return params[0].name + ': ' + params[0].value + ' 辆'
          },
          backgroundColor: 'rgba(255, 255, 255, 0.95)',
          borderColor: '#667eea',
          textStyle: { color: '#333' }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: [],
          axisLine: { lineStyle: chartAxisStyle },
          axisLabel: { color: 'rgba(0, 0, 0, 0.7)', rotate: 0 }
        },
        yAxis: {
          type: 'value',
          axisLine: { lineStyle: chartAxisStyle },
          splitLine: { lineStyle: chartSplitLineStyle },
          axisLabel: { color: 'rgba(0, 0, 0, 0.7)' }
        },
        series: [
          {
            name: '数量',
            type: 'bar',
            barWidth: '40%',
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: '#667eea' },
                { offset: 1, color: '#764ba2' }
              ]),
              borderRadius: [6, 6, 0, 0]
            },
            label: {
              show: true,
              position: 'top',
              formatter: '{c} 辆',
              color: 'rgba(0, 0, 0, 0.8)'
            },
            data: []
          }
        ]
      }
      this.vehicleChart.setOption(vehicleOption)
      
      // 添加点击事件
      this.vehicleChart.on('click', (params) => {
        this.$message.info(`您点击了${params.name}，数量为${params.value}辆`)
      })
    },
    
    loadChartData() {
      // 加载各地区人口统计数据
      getPopulationByRegion().then(response => {
        const data = response.data
        this.populationByRegionChart.setOption({
          yAxis: {
            data: data.regions
          },
          series: [
            {
              name: '人口数量',
              data: data.populations
            }
          ]
        })
      }).catch(error => {
        console.error('获取各地区人口分布数据失败:', error)
        this.$message.error('获取各地区人口分布数据失败')
      })
      
      // 加载年龄分布数据
      getAgeDistribution().then(response => {
        const data = response.data
        this.ageDistributionChart.setOption({
          series: [
            {
              name: '年龄分布',
              data: data
            }
          ]
        })
      }).catch(error => {
        console.error('获取年龄分布数据失败:', error)
        this.$message.error('获取年龄分布数据失败')
      })
      
      // 加载教育程度分布数据
      getEducationLevel().then(response => {
        const data = response.data
        this.educationLevelChart.setOption({
          yAxis: {
            data: data.levels
          },
          series: [
            {
              name: '男性',
              data: data.maleData
            },
            {
              name: '女性',
              data: data.femaleData
            }
          ]
        })
      }).catch(error => {
        console.error('获取教育程度分布数据失败:', error)
        this.$message.error('获取教育程度分布数据失败')
      })
      
      // 加载医疗保险类型分布数据
      getMedicalInsurance().then(response => {
        const data = response.data
        this.medicalInsuranceChart.setOption({
          series: [
            {
              name: '保险类型',
              data: data
            }
          ]
        })
      }).catch(error => {
        console.error('获取医疗保险类型分布数据失败:', error)
        this.$message.error('获取医疗保险类型分布数据失败')
      })
      
      // 加载人口增长趋势数据
      getPopulationTrend().then(response => {
        const data = response.data
        this.populationTrendChart.setOption({
          xAxis: {
            data: data.years
          },
          series: [
            {
              name: '总人口',
              data: data.totalData
            },
            {
              name: '男性',
              data: data.maleData
            },
            {
              name: '女性',
              data: data.femaleData
            }
          ]
        })
      }).catch(error => {
        console.error('获取人口增长趋势数据失败:', error)
        this.$message.error('获取人口增长趋势数据失败')
        // 清空图表数据，确保只显示来自数据库的数据
        this.populationTrendChart.setOption({
          xAxis: {
            data: []
          },
          series: [
            {
              name: '总人口',
              data: []
            },
            {
              name: '男性',
              data: []
            },
            {
              name: '女性',
              data: []
            }
          ]
        })
      })
      
      // 加载就业数据图表
      getEmploymentStatusDistribution().then(response => {
        const data = response.data
        // 计算就业率（在职人数/总人数）
        const employedItem = data.find(item => item.name === '在职')
        const employed = employedItem ? employedItem.value : 0
        const total = data.reduce((sum, item) => sum + item.value, 0)
        const employmentRate = total > 0 ? Math.round((employed / total) * 100) : 0
        
        this.employmentChart.setOption({
          series: [
            {
              data: [{ value: employmentRate }]
            }
          ]
        })
      }).catch(error => {
        console.error('获取就业状态分布数据失败:', error)
        this.$message.error('获取就业状态分布数据失败')
        // 清空图表数据，确保只显示来自数据库的数据
        this.employmentChart.setOption({
          series: [
            {
              data: [{ value: 0 }]
            }
          ]
        })
      })
      
      // 加载社保数据图表
      getSocialSecurityCoverage().then(response => {
        const data = response.data
        // 优化雷达图配置，让数据更加饱满，对比更加鲜明
        this.socialSecurityChart.setOption({
          radar: {
            indicator: [
              { name: '养老保险', max: Math.max(...data.coverageRates) + 10 },
              { name: '医疗保险', max: Math.max(...data.coverageRates) + 10 },
              { name: '失业保险', max: Math.max(...data.coverageRates) + 10 },
              { name: '工伤保险', max: Math.max(...data.coverageRates) + 10 },
              { name: '生育保险', max: Math.max(...data.coverageRates) + 10 }
            ],
            radius: '60%'
          },
          series: [
            {
              name: '参保率',
              type: 'radar',
              areaStyle: {
                opacity: 0.3
              },
              itemStyle: {
                color: '#67C23A'
              },
              lineStyle: {
                width: 2
              },
              symbol: 'circle',
              symbolSize: 6,
              data: [
                {
                  value: data.coverageRates,
                  name: '参保率(%)'
                }
              ]
            }
          ]
        })
      }).catch(error => {
        console.error('获取社保参保率数据失败:', error)
        this.$message.error('获取社保参保率数据失败')
        // 清空图表数据，确保只显示来自数据库的数据
        this.socialSecurityChart.setOption({
          radar: {
            indicator: [
              { name: '养老保险', max: 100 },
              { name: '医疗保险', max: 100 },
              { name: '失业保险', max: 100 },
              { name: '工伤保险', max: 100 },
              { name: '生育保险', max: 100 }
            ],
            radius: '60%'
          },
          series: [
            {
              name: '参保率',
              type: 'radar',
              areaStyle: {
                opacity: 0.3
              },
              itemStyle: {
                color: '#67C23A'
              },
              lineStyle: {
                width: 2
              },
              symbol: 'circle',
              symbolSize: 6,
              data: [
                {
                  value: [0, 0, 0, 0, 0],
                  name: '参保率(%)'
                }
              ]
            }
          ]
        })
      })
      
      // 加载房产数据图表
      getPropertyTypeDistribution().then(response => {
        const data = response.data
        this.propertyChart.setOption({
          series: [
            {
              name: '房产类型',
              data: data.map(item => ({...item, selected: item.value > 200}))
            },
            {
              name: '房产类型',
              data: data
            }
          ]
        })
      }).catch(error => {
        console.error('获取房产类型分布数据失败:', error)
        this.$message.error('获取房产类型分布数据失败')
        // 清空图表数据，确保只显示来自数据库的数据
        this.propertyChart.setOption({
          series: [
            {
              name: '房产类型',
              data: []
            },
            {
              name: '房产类型',
              data: []
            }
          ]
        })
      })
      
      // 加载车辆数据图表
      getVehicleBrandDistribution().then(response => {
        const data = response.data
        const brands = data.map(item => item.name)
        const counts = data.map(item => item.value)
        this.vehicleChart.setOption({
          xAxis: {
            data: brands
          },
          series: [
            {
              name: '数量',
              data: counts
            }
          ]
        })
      }).catch(error => {
        console.error('获取车辆品牌分布数据失败:', error)
        this.$message.error('获取车辆品牌分布数据失败')
        // 清空图表数据，确保只显示来自数据库的数据
        this.vehicleChart.setOption({
          xAxis: {
            data: []
          },
          series: [
            {
              name: '数量',
              data: []
            }
          ]
        })
      })
      
      // 加载最新数据
      getLatestData().then(response => {
        const data = response.data
        this.tableData = data
      }).catch(error => {
        console.error('获取最新数据失败:', error)
        this.$message.error('获取最新数据失败')
      })
    },
    
    // 获取用户信息
    getUserInfo() {
      try {
        // 从localStorage获取用户信息
        const userInfoStr = localStorage.getItem('userInfo')
        if (userInfoStr && userInfoStr !== 'null') { // 确保不是null或'null'字符串
          this.userInfo = JSON.parse(userInfoStr)
        }
        // 如果userInfoStr为空或'null'，则使用默认的USER角色
      } catch (error) {
        console.error('解析用户信息失败:', error)
        this.userInfo = { username: 'user', role: 'USER' } // 默认使用普通用户角色
      }
    },
    
    // 导航方法
    navigateTo(path) {
      this.$router.push(path)
    },
    
    // 合并加载所有数据，减少重复请求
    loadAllData() {
      // 使用Promise.all并行加载数据，提高加载速度
      Promise.all([
        getSummary(),
        getPopulationByRegion(),
        getAgeDistribution(),
        getEducationLevel(),
        getMedicalInsurance(),
        getPopulationTrend(),
        getEmploymentStatusDistribution(),
        getSocialSecurityCoverage(),
        getPropertyTypeDistribution(),
        getVehicleBrandDistribution(),
        getLatestData()
      ]).then(results => {
        // 处理返回结果
        const [
          summaryResult,
          populationByRegionResult,
          ageDistributionResult,
          educationLevelResult,
          medicalInsuranceResult,
          populationTrendResult,
          employmentStatusDistributionResult,
          socialSecurityCoverageResult,
          propertyTypeDistributionResult,
          vehicleBrandDistributionResult,
          latestDataResult
        ] = results
        
        // 更新系统统计摘要
        this.summaryData = summaryResult.data
        
        // 更新各地区人口统计图表
        const populationByRegionData = populationByRegionResult.data
        this.populationByRegionChart.setOption({
          yAxis: {
            data: populationByRegionData.regions
          },
          series: [
            {
              name: '人口数量',
              data: populationByRegionData.populations
            }
          ]
        })
        
        // 更新年龄分布图表
        const ageDistributionData = ageDistributionResult.data
        this.ageDistributionChart.setOption({
          series: [
            {
              name: '年龄分布',
              data: ageDistributionData
            }
          ]
        })
        
        // 更新教育程度分布图表
        const educationLevelData = educationLevelResult.data
        this.educationLevelChart.setOption({
          yAxis: {
            data: educationLevelData.levels
          },
          series: [
            {
              name: '男性',
              data: educationLevelData.maleData
            },
            {
              name: '女性',
              data: educationLevelData.femaleData
            }
          ]
        })
        
        // 更新医疗保险类型分布图表
        const medicalInsuranceData = medicalInsuranceResult.data
        this.medicalInsuranceChart.setOption({
          series: [
            {
              name: '保险类型',
              data: medicalInsuranceData
            }
          ]
        })
        
        // 更新人口增长趋势图表
        const populationTrendData = populationTrendResult.data
        this.populationTrendChart.setOption({
          xAxis: {
            data: populationTrendData.years
          },
          series: [
            {
              name: '总人口',
              data: populationTrendData.totalData
            },
            {
              name: '男性',
              data: populationTrendData.maleData
            },
            {
              name: '女性',
              data: populationTrendData.femaleData
            }
          ]
        })
        
        // 更新就业数据图表
        const employmentStatusDistributionData = employmentStatusDistributionResult.data
        const employedItem = employmentStatusDistributionData.find(item => item.name === '在职')
        const employed = employedItem ? employedItem.value : 0
        const total = employmentStatusDistributionData.reduce((sum, item) => sum + item.value, 0)
        const employmentRate = total > 0 ? Math.round((employed / total) * 100) : 0
        this.employmentChart.setOption({
          series: [
            {
              data: [{ value: employmentRate }]
            }
          ]
        })
        
        // 更新社保数据图表
        const socialSecurityCoverageData = socialSecurityCoverageResult.data
        this.socialSecurityChart.setOption({
          radar: {
            indicator: [
              { name: '养老保险', max: Math.max(...socialSecurityCoverageData.coverageRates) + 10 },
              { name: '医疗保险', max: Math.max(...socialSecurityCoverageData.coverageRates) + 10 },
              { name: '失业保险', max: Math.max(...socialSecurityCoverageData.coverageRates) + 10 },
              { name: '工伤保险', max: Math.max(...socialSecurityCoverageData.coverageRates) + 10 },
              { name: '生育保险', max: Math.max(...socialSecurityCoverageData.coverageRates) + 10 }
            ],
            radius: '60%'
          },
          series: [
            {
              name: '参保率',
              type: 'radar',
              areaStyle: {
                opacity: 0.3
              },
              itemStyle: {
                color: '#67C23A'
              },
              lineStyle: {
                width: 2
              },
              symbol: 'circle',
              symbolSize: 6,
              data: [
                {
                  value: socialSecurityCoverageData.coverageRates,
                  name: '参保率(%)'
                }
              ]
            }
          ]
        })
        
        // 更新房产数据图表
        const propertyTypeDistributionData = propertyTypeDistributionResult.data
        this.propertyChart.setOption({
          series: [
            {
              name: '房产类型',
              data: propertyTypeDistributionData.map(item => ({...item, selected: item.value > 200}))
            },
            {
              name: '房产类型',
              data: propertyTypeDistributionData
            }
          ]
        })
        
        // 更新车辆数据图表
        const vehicleBrandDistributionData = vehicleBrandDistributionResult.data
        const brands = vehicleBrandDistributionData.map(item => item.name)
        const counts = vehicleBrandDistributionData.map(item => item.value)
        this.vehicleChart.setOption({
          xAxis: {
            data: brands
          },
          series: [
            {
              name: '数量',
              data: counts
            }
          ]
        })
        
        // 更新最新数据
        this.tableData = latestDataResult.data
      }).catch(error => {
        console.error('获取数据失败:', error)
        this.$message.error('获取数据失败')
      })
    },
    
    // 销毁所有图表实例
    destroyAllCharts() {
      // 图表实例列表 - 只包含实际创建的图表实例
      const chartInstances = [
        'populationByRegionChart',
        'ageDistributionChart',
        'educationLevelChart',
        'medicalInsuranceChart',
        'populationTrendChart',
        'employmentChart',
        'socialSecurityChart',
        'propertyChart',
        'vehicleChart'
      ]
      
      // 遍历销毁所有图表实例
      chartInstances.forEach(chartName => {
        if (this[chartName]) {
          this[chartName].dispose()
          this[chartName] = null // 清空引用
        }
      })
    }
  }
}
</script>

<style scoped>
.dashboard {
  min-height: 100vh;
  position: relative;
  overflow-x: hidden;
  background: #FFFFFF;
}

.dashboard-content-wrapper {
  position: relative;
  z-index: 1;
  padding: 24px;
  max-width: 1600px;
  margin: 0 auto;
}

.glass-card {
  background: #FFFFFF;
  border: 1px solid rgba(0, 0, 0, 0.08);
  border-radius: 16px;
  box-shadow: 
    0 4px 20px rgba(0, 0, 0, 0.08);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.glass-surface {
  position: relative;
  overflow: hidden;
}

.glass-surface::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(255, 255, 255, 0.3),
    transparent
  );
  transition: left 0.5s ease;
}

.glass-surface:hover::before {
  left: 100%;
}

.glass-card:hover {
  transform: translateY(-8px);
  box-shadow: 
    0 12px 40px rgba(56, 189, 248, 0.25),
    0 4px 15px rgba(0, 0, 0, 0.15),
    inset 0 1px 0 rgba(255, 255, 255, 0.9);
  border-color: rgba(56, 189, 248, 0.5);
}

.card-glow {
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(
    circle,
    rgba(102, 126, 234, 0.1) 0%,
    transparent 70%
  );
  opacity: 0;
  transition: opacity 0.5s ease;
  pointer-events: none;
}

.glass-card:hover .card-glow {
  opacity: 1;
}

.gradient-text {
  background: linear-gradient(135deg, #ffffff 0%, #0c4a6e 50%, #ffffff 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  background-size: 200% 200%;
  animation: gradientFlow 3s ease infinite;
}

@keyframes gradientFlow {
  0%, 100% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
}

.shiny-text {
  position: relative;
  display: inline-block;
}

.shiny-text::after {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(255, 255, 255, 0.8),
    transparent
  );
  animation: shiny 3s ease-in-out infinite;
}

@keyframes shiny {
  0%, 100% {
    left: -100%;
  }
  50% {
    left: 100%;
  }
}

.gradient-text-title {
  color: #1a1a2e;
  font-weight: 600;
}

.gradient-text-subtitle {
  color: #000000;
  font-weight: 500;
}

.animated-fade-in {
  animation: fadeInUp 0.8s ease-out forwards;
  opacity: 0;
}

.animated-fade-in-delay {
  animation: fadeInUp 0.8s ease-out 0.3s forwards;
  opacity: 0;
}

.animated-icon {
  animation: iconFloat 3s ease-in-out infinite, pulse 2s ease-in-out infinite;
}

.animated-icon-small {
  animation: iconSpin 0.6s ease-out;
}

@keyframes iconFloat {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-5px);
  }
}

@keyframes iconSpin {
  from {
    transform: rotate(-180deg) scale(0.5);
    opacity: 0;
  }
  to {
    transform: rotate(0) scale(1);
    opacity: 1;
  }
}

.animated-bounce {
  animation: bounce 2s ease-in-out infinite;
}

@keyframes bounce {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-5px);
  }
}

.animated-count {
  animation: countUp 0.8s ease-out forwards;
}

@keyframes countUp {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.animated-btn {
  position: relative;
  overflow: hidden;
}

.animated-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(255, 255, 255, 0.3),
    transparent
  );
  transition: left 0.5s ease;
}

.animated-btn:hover::before {
  left: 100%;
}

.animated-btn:hover {
  transform: translateY(-3px) scale(1.02);
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.3);
}

.welcome-section {
  margin-bottom: 32px;
}

.welcome-card {
  padding: 48px 32px;
  text-align: center;
  animation: fadeInUp 0.8s ease-out;
  background: linear-gradient(135deg, #7dd3fc 0%, #38bdf8 50%, #7dd3fc 100%);
}

.welcome-card::before {
  display: none;
}

.welcome-icon {
  width: 80px;
  height: 80px;
  margin: 0 auto 24px;
  background: rgba(255, 255, 255, 0.25);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
}

.welcome-icon svg {
  width: 40px;
  height: 40px;
  color: white;
}

.welcome-title {
  font-size: 32px;
  font-weight: 700;
  margin: 0 0 12px;
  letter-spacing: -0.5px;
  color: #ffffff;
  text-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.welcome-subtitle {
  font-size: 18px;
  color: rgba(255, 255, 255, 0.95);
  margin: 0;
}

.welcome-meta {
  margin-top: 24px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.current-time {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  color: rgba(255, 255, 255, 0.95);
  font-weight: 500;
}

.time-icon {
  width: 18px;
  height: 18px;
}

.greeting {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.85);
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

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
    box-shadow: 0 0 0 0 rgba(102, 126, 234, 0.4);
  }
  50% {
    transform: scale(1.05);
    box-shadow: 0 0 0 20px rgba(102, 126, 234, 0);
  }
}

.section-title {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 24px;
  font-weight: 600;
  color: #1a1a2e;
  margin-bottom: 24px;
}

.title-icon {
  width: 36px;
  height: 36px;
  background: linear-gradient(135deg, #7dd3fc 0%, #38bdf8 100%);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.title-icon svg {
  width: 20px;
  height: 20px;
  color: white;
}

.stats-section {
  margin-bottom: 32px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.stat-card {
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 16px;
  animation: fadeInUp 0.8s ease-out;
  animation-fill-mode: both;
  position: relative;
  overflow: hidden;
}

.stat-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: linear-gradient(90deg, #667eea, #764ba2);
  opacity: 0;
  transition: opacity 0.3s;
}

.stat-card:hover::before {
  opacity: 1;
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.stat-info {
  flex: 1;
}

.stat-title {
  font-size: 14px;
  color: #666666;
  margin-bottom: 4px;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #1a1a2e;
}

.stat-decoration {
  position: absolute;
  right: -20px;
  bottom: -20px;
  width: 100px;
  height: 100px;
  background: linear-gradient(135deg, rgba(0,0,0,0.03) 0%, transparent 100%);
  border-radius: 50%;
}

.bg-blue {
  background: linear-gradient(135deg, #7dd3fc 0%, #38bdf8 100%);
  color: white;
}

.bg-green {
  background: linear-gradient(135deg, #6ee7b7 0%, #34d399 100%);
  color: white;
}

.bg-yellow {
  background: linear-gradient(135deg, #fcd34d 0%, #fbbf24 100%);
  color: white;
}

.bg-red {
  background: linear-gradient(135deg, #fca5a5 0%, #f87171 100%);
  color: white;
}

.bg-purple {
  background: linear-gradient(135deg, #7F00FF 0%, #E100FF 100%);
  color: white;
}

.bg-cyan {
  background: linear-gradient(135deg, #00c6fb 0%, #005bea 100%);
  color: white;
}

.bg-orange {
  background: linear-gradient(135deg, #f7971e 0%, #ffd200 100%);
  color: white;
}

.bg-indigo {
  background: linear-gradient(135deg, #8E2DE2 0%, #4A00E0 100%);
  color: white;
}

.modules-section {
  margin-bottom: 32px;
}

.modules-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 24px;
}

.module-card {
  padding: 28px;
  animation: fadeInUp 0.8s ease-out;
  animation-fill-mode: both;
}

.module-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 16px;
}

.module-icon-wrapper {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.module-icon-wrapper .module-icon {
  font-size: 24px;
  color: white;
}

.icon-blue {
  background: linear-gradient(135deg, #7dd3fc 0%, #38bdf8 100%);
}

.icon-green {
  background: linear-gradient(135deg, #6ee7b7 0%, #34d399 100%);
}

.icon-yellow {
  background: linear-gradient(135deg, #fcd34d 0%, #fbbf24 100%);
}

.icon-purple {
  background: linear-gradient(135deg, #c4b5fd 0%, #a78bfa 100%);
}

.icon-orange {
  background: linear-gradient(135deg, #fdba74 0%, #fb923c 100%);
}

.icon-cyan {
  background: linear-gradient(135deg, #67e8f9 0%, #22d3ee 100%);
}

.module-title {
  font-size: 20px;
  font-weight: 600;
  margin: 0;
}

.module-description {
  font-size: 14px;
  color: #666666;
  line-height: 1.6;
  margin-bottom: 20px;
}

.module-actions {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.action-btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 12px 20px;
  border: none;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  color: white;
}

.action-btn.primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.action-btn.success {
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
}

.action-btn.warning {
  background: linear-gradient(135deg, #f7971e 0%, #ffd200 100%);
}

.action-btn.danger {
  background: linear-gradient(135deg, #ff416c 0%, #ff4b2b 100%);
}

.action-btn.info {
  background: linear-gradient(135deg, #00c6fb 0%, #005bea 100%);
}

.charts-section {
  margin-bottom: 32px;
}

.charts-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 24px;
  margin-bottom: 24px;
}

.chart-card {
  padding: 24px;
  animation: fadeInUp 0.8s ease-out;
  animation-fill-mode: both;
}

.chart-card.full-width {
  grid-column: 1 / -1;
}

.chart-header {
  margin-bottom: 20px;
}

.chart-header h3 {
  font-size: 18px;
  font-weight: 600;
  margin: 0;
  color: #1a1a2e;
}

.chart-container {
  width: 100%;
  height: 300px;
}

.chart-container.large {
  height: 350px;
}

.latest-data-section {
  margin-bottom: 32px;
}

.data-card {
  padding: 24px;
  animation: fadeInUp 0.8s ease-out;
}

.data-card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.data-card-header h3 {
  font-size: 18px;
  font-weight: 600;
  margin: 0;
  color: #1a1a2e;
}

.update-time {
  font-size: 12px;
  color: #999999;
}

.table-wrapper {
  background: rgba(0, 0, 0, 0.02);
  border-radius: 12px;
  overflow: hidden;
  border: 1px solid rgba(0, 0, 0, 0.06);
}

.modern-table {
  --el-table-bg-color: transparent;
  --el-table-tr-bg-color: transparent;
  --el-table-header-bg-color: rgba(0, 0, 0, 0.03);
  --el-table-row-hover-bg-color: rgba(0, 0, 0, 0.04);
  --el-table-text-color: #333333;
  --el-table-header-text-color: #1a1a2e;
  --el-table-border-color: rgba(0, 0, 0, 0.08);
}

.modern-table ::v-deep(.el-table__header th) {
  background: rgba(0, 0, 0, 0.03);
  font-weight: 600;
  color: #1a1a2e;
}

.modern-table ::v-deep(.el-table__body tr) {
  transition: all 0.3s;
}

.modern-table ::v-deep(.el-table__body tr:hover td) {
  background: rgba(0, 0, 0, 0.04);
}

.modern-table ::v-deep(.el-table__body td) {
  color: #333333;
  border-bottom-color: rgba(0, 0, 0, 0.06);
}

@media (max-width: 1200px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .modules-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .dashboard-content-wrapper {
    padding: 16px;
  }
  
  .welcome-title {
    font-size: 24px;
  }
  
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .charts-grid {
    grid-template-columns: 1fr;
  }
  
  .section-title {
    font-size: 20px;
  }
  
  .stat-card {
    padding: 20px;
  }
  
  .stat-value {
    font-size: 24px;
  }
  
  .module-card {
    padding: 20px;
  }
  
  .module-actions {
    flex-direction: column;
  }
  
  .action-btn {
    width: 100%;
    justify-content: center;
  }
}
</style>