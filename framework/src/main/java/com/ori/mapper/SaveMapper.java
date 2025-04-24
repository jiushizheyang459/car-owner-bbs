package com.ori.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ori.domain.entity.Save;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


/**
 * 收藏表(Save)表数据库访问层
 *
 * @author leeway
 * @since 2025-04-18 00:45:35
 */
public interface SaveMapper extends BaseMapper<Save> {

    /**
     * 查询是否曾经收藏过文章
     *
     * @param userId 用户ID
     * @param articleId 文章ID
     * @return 取消收藏的文章的ID和delFlag
     */
    @Select("select id,del_flag FROM tb_save WHERE user_id = #{userId} AND article_id = #{articleId}")
    Save selectExisting(@Param("userId") Long userId,@Param("articleId") Long articleId);

    /**
     * 将取消收藏的文章恢复收藏
     *
     * @param existingSaveId 取消收藏的收藏表数据ID
     * @return 是否更新成功
     */
    @Update("UPDATE tb_save SET del_flag = 0, update_time = NOW() WHERE id = #{existingSaveId}")
    Boolean reSave(@Param("existingSaveId") Long existingSaveId);
}

