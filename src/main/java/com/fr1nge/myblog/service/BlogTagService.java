package com.fr1nge.myblog.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fr1nge.myblog.entity.BlogTag;
import com.fr1nge.myblog.entity.BlogTagCount;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author author
 * @since 2021-08-31
 */
public interface BlogTagService extends IService<BlogTag> {
    IPage<BlogTag> selectPage(Page<BlogTag> page, Wrapper<BlogTag> wrapper);

    List<BlogTagCount> getTagCount();
}
