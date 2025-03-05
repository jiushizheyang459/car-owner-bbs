package com.ori.controller;

import com.ori.annotation.SystemLog;
import com.ori.domain.ResponseResult;
import com.ori.domain.entity.User;
import com.ori.domain.vo.UserInfoVo;
import com.ori.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 查询个人信息
     *
     * @return 用户个人信息
     */
    @GetMapping("/userInfo")
    public ResponseResult userInfo() {
        UserInfoVo vo = userService.userInfo();
        return ResponseResult.okResult(vo);
    }

    @PutMapping("/userInfo")
    @SystemLog(businessName = "更新用户信息")
    public ResponseResult updateUserInfo(@RequestBody User user) {
        userService.updateUserInfo(user);
        return ResponseResult.okResult();
    }

    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user) {
        userService.register(user);
        return ResponseResult.okResult();
    }
}
