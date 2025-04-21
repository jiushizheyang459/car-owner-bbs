package com.ori.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ori.domain.dto.AddInformationDto;
import com.ori.domain.dto.UpdateInformationDto;
import com.ori.domain.entity.Information;
import com.ori.domain.vo.InformationDetailVo;
import com.ori.domain.vo.informationListVo;
import com.ori.domain.vo.newInformationListVo;

import java.util.List;


/**
 * 资讯表(Information)表服务接口
 *
 * @author leeway
 * @since 2025-04-15 00:02:18
 */
public interface InformationService extends IService<Information> {

    /**
     * 查询所有资讯
     *
     * @return 所有资讯
     */
    List<informationListVo> informationList();

    /**
     * 查询最新资讯
     *
     * @return 最新的4篇资讯
     */
    List<newInformationListVo> newinformationList();

    /**
     * 根据资讯ID查询资讯详情
     *
     * @param id 资讯ID
     * @return 资讯明细
     */
    InformationDetailVo informationDetail(Long id);

    /**
     * 新增资讯
     *
     * @param addInformationDto 资讯数据
     */
    void addInformation(AddInformationDto addInformationDto);

    /**
     * 修改资讯
     *
     * @param updateInformationDto 要修改的资讯数据
     */
    void updateInformation(UpdateInformationDto updateInformationDto);

    /**
     * 删除资讯
     *
     * @param ids 资讯ID集合
     */
    void deleteInformation(List<Long> ids);
}
