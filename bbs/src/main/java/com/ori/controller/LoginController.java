package com.ori.controller;

import com.ori.domain.ResponseResult;
import com.ori.domain.entity.User;
import com.ori.domain.vo.UserLoginVo;
import com.ori.enums.AppHttpCodeEnum;
import com.ori.exception.SystemException;
import com.ori.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user) {
        if (!StringUtils.hasText(user.getUserName())) {
            //提示 必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        UserLoginVo vo = loginService.login(user);
        return ResponseResult.okResult(vo);
    }

    @PostMapping("/logout")
    public ResponseResult logout() {
        loginService.logout();
        return ResponseResult.okResult();
    }
}
