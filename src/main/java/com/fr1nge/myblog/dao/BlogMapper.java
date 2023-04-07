package com.fr1nge.myblog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fr1nge.myblog.entity.Blog;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author author
 * @since 2021-08-31
 */
public interface BlogMapper extends BaseMapper<Blog> {
    List<Blog> selectBlogPage(Map<String, Object> map);
    int selectBlogPageCount(Map<String, Object> map);
}
