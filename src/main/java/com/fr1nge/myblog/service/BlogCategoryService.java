package com.fr1nge.myblog.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fr1nge.myblog.entity.BlogCategory;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author author
 * @since 2021-08-31
 */
public interface BlogCategoryService extends IService<BlogCategory> {
    IPage<BlogCategory> selectPage(IPage<BlogCategory> page, Wrapper<BlogCategory> wrapper);
}
