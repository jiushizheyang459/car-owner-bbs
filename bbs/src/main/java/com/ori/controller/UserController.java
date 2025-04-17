package com.ori.controller;

import com.ori.annotation.SystemLog;
import com.ori.domain.ResponseResult;
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

    /**
     * 查询推荐关注
     *
     * @return 根据发布文章浏览量从高到低排序的前5个用户
     */
    @GetMapping("/getRecommendedAttention")
    public ResponseResult getRecommendedAttention() {
        List<UserInfoVo> vos = userService.getRecommendedAttention();
        return ResponseResult.okResult(vos);
    }
}
