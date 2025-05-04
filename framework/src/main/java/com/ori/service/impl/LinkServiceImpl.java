package com.ori.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ori.constants.SystemConstants;
import com.ori.domain.dto.AddLinkDto;
import com.ori.domain.dto.UpdateLinkDto;
import com.ori.domain.entity.Article;
import com.ori.domain.entity.Link;
import com.ori.domain.vo.LinkVo;
import com.ori.enums.AppHttpCodeEnum;
import com.ori.exception.SystemException;
import com.ori.mapper.LinkMapper;
import com.ori.service.LinkService;
import com.ori.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

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

    @Transactional
    @Override
    public void addLink(AddLinkDto addLinkDto) {
        Link link = BeanCopyUtils.copyBean(addLinkDto, Link.class);

        if (!StringUtils.hasText(link.getUrl())) {
            throw new SystemException(AppHttpCodeEnum.LINK_NOT_NULL);
        }
        save(link);
    }

    @Transactional
    @Override
    public void updateLink(UpdateLinkDto updateLinkDto) {
        Link link = getById(updateLinkDto.getId());
        if (Objects.isNull(link)) {
            throw new SystemException(AppHttpCodeEnum.LINK_NOT_FOUND);
        }
        link = BeanCopyUtils.copyBean(updateLinkDto, Link.class);
        updateById(link);
    }

    @Transactional
    @Override
    public void deleteLink(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new SystemException(AppHttpCodeEnum.LINK_IDS_NOT_NULL);
        }
        lambdaUpdate()
                .set(Link::getDelFlag, 1)
                .in(Link::getId, ids);
    }
}
