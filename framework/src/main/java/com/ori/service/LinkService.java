package com.ori.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ori.domain.dto.AddLinkDto;
import com.ori.domain.dto.UpdateLinkDto;
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

    /**
     * 新增友情链接
     *
     * @param addLinkDto 要新增友情链接数据
     */
    void addLink(AddLinkDto addLinkDto);

    /**
     * 修改友情链接
     *
     * @param updateLinkDto 要修改的友情链接数据
     */
    void updateLink(UpdateLinkDto updateLinkDto);

    /**
     * 删除友情链接
     *
     * @param ids 友情链接ID集合
     */
    void deleteLink(List<Long> ids);
}
