package com.ori.constants;

public class MqConstants {
    /**
     * 文章浏览量队列
     */
    public static final String VIEW_COUNT_QUEUE = "article.view.count";

    /**
     * 文章浏览量交换机
     */
    public static final String VIEW_COUNT_EXCHANGE = "article.view.exchange";

    /**
     * 文章浏览量路由key
     */
    public static final String VIEW_COUNT_ROUTING_KEY = "article.view.routing.key";

    /**
     * 文章点赞队列
     */
    public static final String LIKE_QUEUE = "article.like.count";

    /**
     * 文章点赞交换机
     */
    public static final String LIKE_EXCHANGE = "article.like.exchange";

    /**
     * 文章点赞路由key
     */
    public static final String LIKE_ROUTING_KEY = "article.like.routing.key";
}
