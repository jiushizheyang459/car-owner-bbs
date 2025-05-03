package com.ori.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ori.domain.dto.AddUserdto;
import com.ori.domain.dto.UpdatePasswordDto;
import com.ori.domain.dto.UpdateUserdto;
import com.ori.domain.entity.User;
import com.ori.domain.vo.UserInfoVo;

import java.util.List;

public interface UserService extends IService<User> {

    /**
     * 查询个人信息
     *
     * @return 用户个人信息
     */
    UserInfoVo userInfo();

    /**
     * 更新用户信息
     *
     * @param updateUserdto 新用户信息
     */
    void updateUserInfo(UpdateUserdto updateUserdto);

    /**
     * 注册
     *
     * @param addUserdto 注册用户信息
     */
    void register(AddUserdto addUserdto);

    /**
     * 修改用户密码
     * @param updatePasswordDto 密码修改信息
     */
    void updatePassword(UpdatePasswordDto updatePasswordDto);
}
