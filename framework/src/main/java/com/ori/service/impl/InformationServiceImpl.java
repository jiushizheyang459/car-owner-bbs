package com.ori.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ori.constants.SystemConstants;
import com.ori.domain.dto.AddInformationDto;
import com.ori.domain.dto.UpdateInformationDto;
import com.ori.domain.entity.Information;
import com.ori.domain.entity.User;
import com.ori.domain.vo.InformationDetailVo;
import com.ori.domain.vo.informationListVo;
import com.ori.domain.vo.newInformationListVo;
import com.ori.enums.AppHttpCodeEnum;
import com.ori.exception.SystemException;
import com.ori.mapper.InformationMapper;
import com.ori.mapper.UserMapper;
import com.ori.service.InformationService;
import com.ori.utils.BeanCopyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 资讯表(Information)表服务实现类
 *
 * @author leeway
 * @since 2025-04-15 00:02:18
 */
@Service("informationService")
public class InformationServiceImpl extends ServiceImpl<InformationMapper, Information> implements InformationService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<informationListVo> informationList() {
        List<Information> informations = lambdaQuery()
                .eq(Information::getStatus, SystemConstants.INFORMATION_STATUS_NORMAL)
                .orderByDesc(Information::getCreateTime)
                .list();

        if (CollectionUtils.isEmpty(informations)) {
            return Collections.emptyList();
        }

        List<Long> userIds = informations.stream()
                .map(Information::getCreateById)
                .distinct()
                .collect(Collectors.toList());

        List<User> users = userMapper.selectBatchIds(userIds);

        Map<Long, User> userMap = users.stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        List<informationListVo> vos = informations.stream()
                .map(information -> {
                    User user = userMap.getOrDefault(information.getCreateById(), new User());
                    LocalDate createDate = information.getCreateTime().toLocalDate();
                    LocalTime createTime = information.getCreateTime().toLocalTime();

                    return new informationListVo(
                            information.getId(),
                            user.getNickName(),
                            user.getAvatar(),
                            information.getTitle(),
                            information.getContent(),
                            information.getThumbnail(),
                            createDate,
                            createTime
                    );
                })
                .collect(Collectors.toList());

        return vos;
    }

    @Override
    public List<newInformationListVo> newinformationList() {
        List<Information> informations = lambdaQuery()
                .eq(Information::getStatus, SystemConstants.INFORMATION_STATUS_NORMAL)
                .orderByDesc(Information::getCreateTime)
                .last("LIMIT 4")
                .list();

        if (CollectionUtils.isEmpty(informations)) {
            return Collections.emptyList();
        }
        List<newInformationListVo> vos = informations.stream()
                .map(information -> new newInformationListVo(
                        information.getTitle(),
                        information.getContent()
                ))
                .collect(Collectors.toList());

        return vos;
    }

    @Override
    public InformationDetailVo informationDetail(Long id) {
        Information information = getById(id);
        if (information == null) {
            return null;
        }

        InformationDetailVo vo = BeanCopyUtils.copyBean(information, InformationDetailVo.class);

        return vo;
    }

    @Transactional
    @Override
    public void addInformation(AddInformationDto addInformationDto) {
        Information information = BeanCopyUtils.copyBean(addInformationDto, Information.class);

        if (!StringUtils.hasText(information.getContent())) {
            throw new SystemException(AppHttpCodeEnum.INFORMATION_NOT_NULL);
        }
        save(information);
    }

    @Transactional
    @Override
    public void updateInformation(UpdateInformationDto updateInformationDto) {
        Information information = getById(updateInformationDto.getId());
        if (Objects.isNull(information)) {
            throw new SystemException(AppHttpCodeEnum.INFORMATION_NOT_FOUND);
        }
        information = BeanCopyUtils.copyBean(updateInformationDto, Information.class);
        updateById(information);
    }

    @Transactional
    @Override
    public void deleteInformation(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new SystemException(AppHttpCodeEnum.INFORMATION_IDS_NOT_NULL);
        }
        lambdaUpdate()
                .set(Information::getDelFlag, 1)
                .in(Information::getId, ids);
    }
}
