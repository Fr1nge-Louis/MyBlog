package com.fr1nge.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fr1nge.myblog.dao.BlogCommentMapper;
import com.fr1nge.myblog.entity.BlogComment;
import com.fr1nge.myblog.service.BlogCommentService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author author
 * @since 2021-08-31
 */
@Service
public class BlogCommentServiceImpl extends ServiceImpl<BlogCommentMapper, BlogComment> implements BlogCommentService {

    @Override
    public IPage<BlogComment> selectPage(IPage<BlogComment> page, Wrapper<BlogComment> wrapper) {
        return baseMapper.selectPage(page, wrapper);
    }
}
