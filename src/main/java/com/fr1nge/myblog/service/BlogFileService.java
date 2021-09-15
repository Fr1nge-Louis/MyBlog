package com.fr1nge.myblog.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fr1nge.myblog.entity.BlogFile;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author author
 * @since 2021-09-15
 */
public interface BlogFileService extends IService<BlogFile> {
    IPage<BlogFile> selectPage(IPage<BlogFile> page, Wrapper<BlogFile> wrapper);
}
