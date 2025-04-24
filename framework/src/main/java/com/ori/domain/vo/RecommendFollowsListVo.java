package com.ori.domain.vo;

import com.ori.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendFollowsListVo {
    /**
     * ID
     */
    private Long id;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 是否已关注
     */
    private Boolean followsFlag;

    public RecommendFollowsListVo(User key) {
        this.id = key.getId();
        this.nickName = key.getNickName();
        this.avatar = key.getAvatar();
    }
}
