package com.ori.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ori.domain.entity.Article;
import com.ori.domain.entity.User;
import com.ori.domain.vo.UserInfoVo;
import com.ori.enums.AppHttpCodeEnum;
import com.ori.exception.SystemException;
import com.ori.mapper.UserMapper;
import com.ori.service.ArticleService;
import com.ori.service.UserService;
import com.ori.utils.BeanCopyUtils;
import com.ori.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private ArticleService articleService;

    /**
     * 查询个人信息
     *
     * @return 用户个人信息
     */
    @Override
    public UserInfoVo userInfo() {
        Long userId = SecurityUtils.getUserId();
        User user = getById(userId);
        UserInfoVo vo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return vo;
    }

    @Override
    public void updateUserInfo(User user) {
        if (nickNameExist(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        updateById(user);
    }

    @Override
    public void register(User user) {
        //校验数据
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getPassword())) {
            throw new SystemException(AppHttpCodeEnum.PSSWORD_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }

        if (userNameExist(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }

        if (nickNameExist(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }

        //加密密码
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);

        //存入数据库
        save(user);
    }

    private boolean nickNameExist(String nickName) {
        Integer count = lambdaQuery()
                .eq(User::getNickName, nickName)
                .count();
        return count > 0;
    }

    private boolean userNameExist(String userName) {
        Integer count = lambdaQuery()
                .eq(User::getUserName, userName)
                .count();
        return count > 0;
    }

    @Override
    public List<UserInfoVo> getRecommendedAttention() {
        Long userId = SecurityUtils.getUserId();

        // 计算每个用户的总浏览量并存入 Map
        Map<User, Long> userViewCountMap = userService.lambdaQuery().list()
                .stream()
                .collect(Collectors.toMap(
                        user -> user,
                        user -> articleService.lambdaQuery()
                                .eq(Article::getCreateById, user.getId())
                                .eq(Article::getStatus, 0)
                                .list()
                                .stream()
                                .mapToLong(Article::getViewCount)
                                .sum()
                ));

        // 按总浏览量降序排序
        List<Map.Entry<User, Long>> sortedUsers = userViewCountMap.entrySet().stream()
                .sorted(Map.Entry.<User, Long>comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());

        // 取前 5 名
        List<UserInfoVo> vos = new ArrayList<>();
        int count = 0;
        for (Map.Entry<User, Long> entry : sortedUsers) {
            if (!entry.getKey().getId().equals(userId)) { // 排除当前用户
                vos.add(new UserInfoVo(entry.getKey()));
                count++;
            }
            if (count == 5) break;
        }

        return vos;
    }
}
