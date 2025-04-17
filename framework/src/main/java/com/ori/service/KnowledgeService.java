package com.ori.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ori.domain.dto.AddKnowledgeDto;
import com.ori.domain.dto.UpdateKnowledgeDto;
import com.ori.domain.entity.Knowledge;
import com.ori.domain.vo.KnowledgeDetailVo;
import com.ori.domain.vo.PageVo;

import java.util.List;


/**
 * 知识表(Knowledge)表服务接口
 *
 * @author leeway
 * @since 2025-04-16 22:50:02
 */
public interface KnowledgeService extends IService<Knowledge> {

    /**
     * 分页查询全部知识
     *
     * @param pageNum 多少页
     * @param pageSize 每页多少条
     * @return 全部知识结果
     */
    PageVo knowledgeList(Integer pageNum, Integer pageSize);

    /**
     * 根据知识ID查询知识详情
     *
     * @param id 知识ID
     * @return 知识明细
     */
    KnowledgeDetailVo knowledgeDetail(Long id);

    /**
     * 新增知识
     *
     * @param addKnowledgeDto 要新增知识数据
     */
    void addKnowledge(AddKnowledgeDto addKnowledgeDto);

    /**
     * 修改知识
     *
     * @param updateKnowledgeDto 要修改的知识数据
     */
    void updateKnowledge(UpdateKnowledgeDto updateKnowledgeDto);

    /**
     * 删除知识
     *
     * @param ids 知识ID集合
     */
    void deleteKnowledge(List<Long> ids);
}
