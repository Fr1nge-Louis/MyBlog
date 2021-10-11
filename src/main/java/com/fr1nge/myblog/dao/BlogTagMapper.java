package com.fr1nge.myblog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fr1nge.myblog.entity.BlogTag;
import com.fr1nge.myblog.entity.BlogTagCount;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author author
 * @since 2021-08-31
 */
public interface BlogTagMapper extends BaseMapper<BlogTag> {
    List<BlogTagCount> getTagCount();
}
