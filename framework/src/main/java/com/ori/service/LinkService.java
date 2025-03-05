package com.ori.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ori.domain.entity.Link;
import com.ori.domain.vo.LinkVo;

import java.util.List;


/**
 * 友情链接表(Link)表服务接口
 *
 * @author leeway
 * @since 2025-02-08 11:06:09
 */
public interface LinkService extends IService<Link> {

    /**
     * 查询所有友情链接
     *
     * @return 所有友情链接
     */
    List<LinkVo> linkList();
}
