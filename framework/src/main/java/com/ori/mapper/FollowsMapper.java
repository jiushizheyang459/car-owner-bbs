package com.ori.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ori.domain.entity.Follows;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


/**
 * (Follows)表数据库访问层
 *
 * @author leeway
 * @since 2025-04-03 00:01:11
 */
public interface FollowsMapper extends BaseMapper<Follows> {

    /**
     * 查询是否曾经关注过用户
     *
     * @param followerId 关注者ID
     * @param followedId 被关注者ID
     * @return 取消收藏的文章的ID和delFlag
     */
    @Select("select id,del_flag FROM tb_follow WHERE follower_id = #{followerId} AND followed_id = #{followedId}")
    Follows selectExisting(@Param("followerId") Long followerId,@Param("followedId") Long followedId);

    /**
     * 将取消关注的用户恢复关注
     *
     * @param existingSaveId 取消关注的关注表数据ID
     * @return
     */
    @Update("UPDATE tb_follow SET del_flag = 0, update_time = NOW() WHERE id = #{existingSaveId}")
    boolean reFollows(@Param("existingSaveId") Long existingSaveId);
}

