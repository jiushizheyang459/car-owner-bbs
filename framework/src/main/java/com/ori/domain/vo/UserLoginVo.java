package com.ori.domain.vo;

import com.ori.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginVo {
    private String token;
    private UserInfoVo userInfo;
    private List<MenuVo> menus;
}
