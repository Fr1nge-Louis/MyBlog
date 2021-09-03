package com.fr1nge.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fr1nge.myblog.entity.Blog;
import com.fr1nge.myblog.dao.BlogMapper;
import com.fr1nge.myblog.service.BlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2021-08-31
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

    @Override
    public IPage<Blog> selectPage(IPage<Blog> page, Wrapper<Blog> wrapper) {
        return baseMapper.selectPage(page,wrapper);
    }
}
