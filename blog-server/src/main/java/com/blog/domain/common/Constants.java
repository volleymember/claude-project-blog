package com.blog.domain.common;

/**
 * 常量定义
 */
public class Constants {

    // 文章状态
    public static final int ARTICLE_STATUS_DRAFT = 0;      // 草稿
    public static final int ARTICLE_STATUS_PUBLISHED = 1;  // 已发布
    public static final int ARTICLE_STATUS_RECYCLE = 2;    // 回收站

    // 用户状态
    public static final int USER_STATUS_DISABLED = 0;      // 禁用
    public static final int USER_STATUS_NORMAL = 1;        // 正常

    // 评论状态
    public static final int COMMENT_STATUS_PENDING = 0;    // 待审核
    public static final int COMMENT_STATUS_APPROVED = 1;   // 已通过
    public static final int COMMENT_STATUS_REJECTED = 2;   // 已拒绝

    // Redis Key前缀
    public static final String REDIS_ARTICLE_VIEW = "blog:article:view:";
    public static final String REDIS_ARTICLE_LIKE = "blog:article:like:";
    public static final String REDIS_SITE_CONFIG = "blog:site:config";
    public static final String REDIS_VISIT_COUNT = "blog:visit:count:";
    public static final String REDIS_HOT_ARTICLES = "blog:article:hot";

    // 角色标识
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_EDITOR = "EDITOR";
}
