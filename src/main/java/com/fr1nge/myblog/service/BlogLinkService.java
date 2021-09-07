package com.fr1nge.myblog.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fr1nge.myblog.entity.BlogLink;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author author
 * @since 2021-08-31
 */
public interface BlogLinkService extends IService<BlogLink> {
    IPage<BlogLink> selectPage(Page<BlogLink> page, Wrapper<BlogLink> wrapper);
}
