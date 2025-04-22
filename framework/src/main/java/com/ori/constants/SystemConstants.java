package com.ori.constants;

public class SystemConstants {
    /**
     * 文章是草稿状态
     */
    public static final int ARTICLE_STATUS_DRAFT = 1;

    /**
     * 文章是发布状态
     */
    public static final int ARTICLE_STATUS_NORMAL = 0;

    /**
     * 资讯是草稿状态
     */
    public static final int INFORMATION_STATUS_DRAFT = 1;

    /**
     * 资讯是发布状态
     */
    public static final int INFORMATION_STATUS_NORMAL = 0;

    /**
     * 分类是可用状态
     */
    public static final int CATEGORY_STATUS_NORMAL = 0;

    /**
     * 友链是审核通过状态
     */
    public static final int LINK_STATUS_NORMAL = 0;

    /**
     * 评论是根评论
     */
    public static final Long COMMENT_ROOT = -1L;

    /**
     * 评论类型是文章评论
     */
    public static final String ARTICLE_COMMENT = "0";

    /**
     * 评论类型是友情链接评论
     */
    public static final String LINK_COMMENT = "1";

    /**
     * Redis里浏览量的Key
     */
    public static final String VIEW_COUNT_KEY = "article:view:count:";

    /**
     * 访问者唯一标识前缀
     */
    public static final String ARTICLE_VISITED_KEY = "article:view:record:";

    /**
     * 访问者唯一标识有效时长
     */
    public static final int ARTICLE_VISITED_TIME = 10;

    /**
     * 热门文章redis key有效时间
     */
    public static final long HOT_ARTICLES_EXPIRE_DAYS = 7;

    /**
     * 普通文章redis key有效时间
     */
    public static final long NORMAL_ARTICLES_EXPIRE_HOURS = 24;

    /**
     * 浏览量消息批处理阈值
     */
    public static final int VIEW_COUNT_BATCH_SIZE = 100;

    /**
     * 热门文章判定阈值
     */
    public static final int HOT_ARTICLE_VIEW_COUNT = 100;

    /**
     * 文章浏览量单次批量处理阈值
     * 避免一次处理过多数据
     */
    public static final int VIEW_COUNT_SINGLE_SYNC_COUNT = 20;

    /**
     * 点赞消息批处理阈值
     */
    public static final int LIKE_BATCH_SIZE = 100;

    /**
     * Redis里文章点赞量的Key
     */
    public static final String ARTICLE_LIKE_COUNT_KEY = "article:like:count:";

    /**
     * Redis里文章点赞用户的Key
     */
    public static final String ARTICLE_LIKE_USERS_KEY = "article:like:users:";

    /**
     * 文章点赞Redis key过期时间
     */
    public static final int LIKE_CACHE_EXPIRE_DAYS = 7;

    public static final String LIKE_CHANGED_ARTICLE_KEY = "article:like:changed";
}
