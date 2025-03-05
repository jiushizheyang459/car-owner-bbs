package com.ori.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ori.constants.SystemConstants;
import com.ori.domain.entity.Link;
import com.ori.domain.vo.LinkVo;
import com.ori.mapper.LinkMapper;
import com.ori.service.LinkService;
import com.ori.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    /**
     * 查询所有友情链接
     *
     * @return 所有友情链接
     */
    @Override
    public List<LinkVo> linkList() {
        List<Link> list = lambdaQuery()
                .eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL)
                .list();

        List<LinkVo> vos = BeanCopyUtils.copyBeanList(list, LinkVo.class);
        return vos;
    }
}
