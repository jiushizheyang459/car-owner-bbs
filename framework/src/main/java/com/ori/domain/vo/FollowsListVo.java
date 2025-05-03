package com.ori.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowsListVo {
    private Long id;

    /**
     * 关注者ID
     */
    private Long followerId;

    /**
     * 关注者
     */
    private String followerUser;

    /**
     * 被关注者ID
     */
    private Long followedId;

    /**
     * 被关注者
     */
    private String followedUser;

    /**
     * 被关注者头像
     */
    private String followedAvatar;

    /**
     * 关注时间
     */
    private LocalDateTime createTime;
}
