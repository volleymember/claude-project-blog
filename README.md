# 个人博客系统

一个采用前后端分离架构的现代化个人博客系统，支持文章管理、评论互动、访问统计等功能。

## 项目结构

```
blog/
├── blog-server/    # 后端服务 (Spring Boot)
├── blog-admin/     # 管理后台 (Vue 3)
└── blog-web/       # 前台展示 (Vue 3)
```

## 技术栈

### 后端 (blog-server)

- **框架**: Spring Boot 3.2.0 + Java 17
- **数据库**: MySQL 8.0+ + MyBatis 3.0.3
- **缓存**: Redis
- **安全**: Spring Security + JWT认证
- **工具库**: Hutool 5.8.24、Lombok

**架构特点**: 采用DDD（领域驱动设计）四层架构
- `interfaces` - 接口层（Controller、DTO）
- `application` - 应用服务层
- `domain` - 领域层（Entity、Repository接口）
- `infrastructure` - 基础设施层（Mapper、Repository实现）

### 管理后台 (blog-admin)

- **框架**: Vue 3.4 + Vite 5
- **UI组件**: Element Plus 2.4
- **状态管理**: Pinia
- **路由**: Vue Router 4
- **富文本编辑器**: WangEditor
- **图表**: ECharts

### 前台展示 (blog-web)

- **框架**: Vue 3.4 + Vite 5
- **样式**: Tailwind CSS
- **状态管理**: Pinia
- **路由**: Vue Router 4
- **Markdown解析**: Marked + Highlight.js
- **HTML净化**: DOMPurify

## 核心功能

### 文章管理
- Markdown编辑与渲染
- 分类与标签（多对多）
- 文章置顶、草稿、发布状态
- SEO优化（关键词、描述）
- 浏览量、点赞统计

### 评论系统
- 嵌套回复支持
- 评论审核机制
- 游客评论（无需登录）

### 用户权限
- RBAC角色权限模型
- JWT令牌认证
- 管理员/编辑角色分离

### 其他功能
- 访问日志与操作日志
- 网站配置管理
- Dashboard数据统计
- 文章归档

## 快速开始

### 后端服务

1. 创建数据库并导入schema
```bash
mysql -u root -p < blog-server/src/main/resources/db/schema.sql
```

2. 配置application.yml中的数据库和Redis连接信息

3. 启动后端服务
```bash
cd blog-server
mvn spring-boot:run
```

默认管理员账号: `admin` / `admin123`

### 管理后台

```bash
cd blog-admin
npm install
npm run dev
```

### 前台展示

```bash
cd blog-web
npm install
npm run dev
```

## API设计

后端API采用前后台分离设计:
- `/api/web/**` - 前台公开API
- `/api/admin/**` - 后台管理API（需认证）

## 数据库设计

- 用户与角色（RBAC）
- 文章、分类、标签
- 评论（支持嵌套）
- 网站配置
- 访问日志、操作日志

## License

MIT