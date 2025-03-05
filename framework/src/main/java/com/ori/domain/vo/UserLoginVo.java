package com.ori.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserLoginVo {

    private String token;

    private UserInfoVo userInfo;
}
