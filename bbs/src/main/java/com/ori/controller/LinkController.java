package com.ori.controller;


import com.ori.domain.ResponseResult;
import com.ori.domain.entity.Link;
import com.ori.domain.vo.LinkVo;
import com.ori.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

