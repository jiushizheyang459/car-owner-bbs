package com.ori.domain.vo;

import com.ori.domain.entity.User;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserInfoVo {
    private Long id;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String avatar;

    private Integer sex;

    private String email;

    public UserInfoVo(User key) {
        this.id = key.getId();
        this.nickName = key.getNickName();
        this.avatar = key.getAvatar();
        this.sex = key.getSex();
        this.email = key.getEmail();
    }
}
