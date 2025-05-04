package com.ori.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ori.domain.dto.AddKnowledgeDto;
import com.ori.domain.dto.UpdateKnowledgeDto;
import com.ori.domain.entity.Knowledge;
import com.ori.domain.vo.KnowledgeDetailVo;
import com.ori.domain.vo.KnowledgeListVo;
import com.ori.domain.vo.PageVo;
import com.ori.enums.AppHttpCodeEnum;
import com.ori.exception.SystemException;
import com.ori.mapper.KnowledgeMapper;
import com.ori.service.KnowledgeService;
import com.ori.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 知识表(Knowledge)表服务实现类
 *
 * @author leeway
 * @since 2025-04-16 22:50:02
 */
@Service("knowledgeService")
public class KnowledgeServiceImpl extends ServiceImpl<KnowledgeMapper, Knowledge> implements KnowledgeService {
    @Override
    public PageVo knowledgeList(Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Knowledge> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Knowledge::getCreateTime);

        // 分页查询
        Page<Knowledge> page = new Page<>(pageNum, pageSize);
        page(page, wrapper);

        // region 获取知识列表并转换为 EventListVo 列表
        List<KnowledgeListVo> knowledgeListVos = page.getRecords().stream()
                .map(knowledge -> {
                    return new KnowledgeListVo(
                            knowledge.getId(),
                            knowledge.getTitle(),
                            knowledge.getContent(),
                            knowledge.getThumbnail()
                    );
                })
                .collect(Collectors.toList());
        // endregion

        return new PageVo(knowledgeListVos, page.getTotal());
    }

    @Override
    public KnowledgeDetailVo knowledgeDetail(Long id) {
        Knowledge knowledge = getById(id);
        if (knowledge == null) {
            return null;
        }

        KnowledgeDetailVo vo = BeanCopyUtils.copyBean(knowledge, KnowledgeDetailVo.class);

        return vo;
    }

    @Transactional
    @Override
    public void addKnowledge(AddKnowledgeDto addKnowledgeDto) {
        Knowledge knowledge = BeanCopyUtils.copyBean(addKnowledgeDto, Knowledge.class);

        if (!StringUtils.hasText(knowledge.getContent())) {
            throw new SystemException(AppHttpCodeEnum.KNOWLEDGE_NOT_NULL);
        }
        save(knowledge);
    }

    @Transactional
    @Override
    public void updateKnowledge(UpdateKnowledgeDto updateKnowledgeDto) {
        Knowledge knowledge = getById(updateKnowledgeDto.getId());
        if (Objects.isNull(knowledge)) {
            throw new SystemException(AppHttpCodeEnum.KNOWLEDGE_NOT_FOUND);
        }
        knowledge = BeanCopyUtils.copyBean(updateKnowledgeDto, Knowledge.class);
        updateById(knowledge);
    }

    @Transactional
    @Override
    public void deleteKnowledge(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new SystemException(AppHttpCodeEnum.KNOWLEDGE_IDS_NOT_NULL);
        }
        lambdaUpdate()
                .set(Knowledge::getDelFlag, 1)
                .in(Knowledge::getId, ids);
    }
}
