package com.fr1nge.myblog.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fr1nge.myblog.entity.BlogComment;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author author
 * @since 2021-08-31
 */
public interface BlogCommentService extends IService<BlogComment> {
    IPage<BlogComment> selectPage(IPage<BlogComment> page, Wrapper<BlogComment> wrapper);
}
