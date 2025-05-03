package com.ori.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ori.domain.dto.AddUserdto;
import com.ori.domain.dto.UpdatePasswordDto;
import com.ori.domain.dto.UpdateUserdto;
import com.ori.domain.entity.Article;
import com.ori.domain.entity.LoginUser;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private RoleServiceImpl roleService;

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
        String roleKey = roleService.selectRoleKeyByUserId(userId);
        vo.setRoleKey(roleKey);
        String roleName = roleService.selectRoleNameByUserId(userId);
        vo.setRoleName(roleName);
        return vo;
    }

    @Override
    public void updateUserInfo(UpdateUserdto updateUserdto) {
        Long currentUserId = SecurityUtils.getUserId();
        if (nickNameExist(updateUserdto.getNickName(), currentUserId)) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        User user = BeanCopyUtils.copyBean(updateUserdto, User.class);
        updateById(user);
    }

    @Override
    public void register(AddUserdto addUserdto) {
        Long currentUserId = SecurityUtils.getUserId();

        //校验数据
        if (!StringUtils.hasText(addUserdto.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(addUserdto.getPassword())) {
            throw new SystemException(AppHttpCodeEnum.PSSWORD_NOT_NULL);
        }
        if (!StringUtils.hasText(addUserdto.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(addUserdto.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }

        if (userNameExist(addUserdto.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }

        if (nickNameExist(addUserdto.getNickName(), currentUserId)) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }

        //加密密码
        String encodePassword = passwordEncoder.encode(addUserdto.getPassword());
        addUserdto.setPassword(encodePassword);

        User user = BeanCopyUtils.copyBean(addUserdto, User.class);

        //存入数据库
        save(user);
    }

    private boolean nickNameExist(String nickName, Long userId) {
        Integer count = lambdaQuery()
                .eq(User::getNickName, nickName)
                .ne(User::getId, userId) // 排除自己
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
    @Transactional
    public void updatePassword(UpdatePasswordDto updatePasswordDto) {
        // 获取当前登录用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        User currentUser = loginUser.getUser();

        // 验证旧密码是否正确
        if (!passwordEncoder.matches(updatePasswordDto.getOldPassword(), currentUser.getPassword())) {
            throw new RuntimeException("旧密码错误");
        }

        // 验证新密码和确认密码是否一致
        if (!updatePasswordDto.getNewPassword().equals(updatePasswordDto.getConfirmPassword())) {
            throw new RuntimeException("新密码和确认密码不一致");
        }

        // 验证新密码不能和旧密码相同
        if (updatePasswordDto.getNewPassword().equals(updatePasswordDto.getOldPassword())) {
            throw new RuntimeException("新密码不能和旧密码相同");
        }

        // 更新密码
        currentUser.setPassword(passwordEncoder.encode(updatePasswordDto.getNewPassword()));
        userMapper.updateById(currentUser);
    }
}
