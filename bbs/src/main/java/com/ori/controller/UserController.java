package com.ori.controller;

import com.ori.annotation.SystemLog;
import com.ori.domain.ResponseResult;
import com.ori.domain.dto.AddUserdto;
import com.ori.domain.dto.UpdateUserdto;
import com.ori.domain.dto.UpdatePasswordDto;
import com.ori.domain.entity.User;
import com.ori.domain.vo.UserInfoVo;
import com.ori.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * 更新用户信息
     *
     * @param updateUserdto 新用户信息
     * @return 请求成功
     */
    @PutMapping("/userInfo")
    @SystemLog(businessName = "更新用户信息")
    public ResponseResult updateUserInfo(@RequestBody UpdateUserdto updateUserdto) {
        userService.updateUserInfo(updateUserdto);
        return ResponseResult.okResult();
    }

    /**
     * 注册
     *
     * @param addUserdto 注册用户信息
     * @return 请求成功
     */
    @PostMapping("/register")
    public ResponseResult register(@RequestBody AddUserdto addUserdto) {
        userService.register(addUserdto);
        return ResponseResult.okResult();
    }

    /**
     * 修改用户密码
     *
     * @param updatePasswordDto 密码修改信息
     * @return 请求成功
     */
    @PutMapping("/updatePassword")
    @SystemLog(businessName = "修改密码")
    public ResponseResult updatePassword(@RequestBody UpdatePasswordDto updatePasswordDto) {
        userService.updatePassword(updatePasswordDto);
        return ResponseResult.okResult();
    }
}
