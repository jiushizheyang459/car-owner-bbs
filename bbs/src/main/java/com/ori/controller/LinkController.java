package com.ori.controller;


import com.ori.domain.ResponseResult;
import com.ori.domain.dto.AddLinkDto;
import com.ori.domain.dto.UpdateLinkDto;
import com.ori.domain.entity.Link;
import com.ori.domain.vo.LinkVo;
import com.ori.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 友情链接表(Link)表控制层
 *
 * @author leeway
 * @since 2025-02-08 11:06:00
 */
@RestController
@RequestMapping("/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    /**
     * 查询所有友情链接
     *
     * @return 所有友情链接
     */
    @GetMapping("/linkList")
    public ResponseResult linkList() {
        List<LinkVo> vos = linkService.linkList();
        return ResponseResult.okResult(vos);
    }

    /**
     * 新增友情链接
     *
     * @param addLinkDto 要新增友情链接数据
     * @return 新增结果
     */
    @PostMapping
    public ResponseResult addLink(@RequestBody AddLinkDto addLinkDto) {
        linkService.addLink(addLinkDto);
        return ResponseResult.okResult();
    }

    /**
     * 修改友情链接
     *
     * @param updateLinkDto 要修改的友情链接数据
     * @return 修改结果
     */
    @PutMapping
    public ResponseResult updateLink(@RequestBody UpdateLinkDto updateLinkDto) {
        linkService.updateLink(updateLinkDto);
        return ResponseResult.okResult();
    }

    /**
     * 删除友情链接
     *
     * @param ids 友情链接ID集合
     * @return 删除结果
     */
    @DeleteMapping
    public ResponseResult deleteLink(@RequestParam List<Long> ids) {
        linkService.deleteLink(ids);
        return ResponseResult.okResult();
    }
}

