package com.ori.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 菜单VO类，用于前端动态路由
 *
 * @author leeway
 * @since 2025-04-27 17:06:55
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuVo {
    /**
     * 菜单ID
     */
    private Long id;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 显示顺序
     */
    private Integer orderNum;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 是否为外链（0是 1否）
     */
    private Integer isFrame;

    /**
     * 菜单类型（M目录 C菜单 F按钮）
     */
    private String menuType;

    /**
     * 菜单状态（0显示 1隐藏）
     */
    private String visible;

    /**
     * 菜单状态（0正常 1停用）
     */
    private String status;

    /**
     * 权限标识
     */
    private String perms;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 布局类型（0默认布局，1全屏布局，2自定义布局）
     */
    private Integer layoutType;

    /**
     * 路由参数键名(例如id）
     */
    private String paramKey;

    /**
     * 子菜单
     */
    private List<MenuVo> children;
} 