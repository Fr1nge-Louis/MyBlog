package com.fr1nge.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fr1nge.myblog.dao.BlogCategoryMapper;
import com.fr1nge.myblog.entity.BlogCategory;
import com.fr1nge.myblog.service.BlogCategoryService;
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
public class BlogCategoryServiceImpl extends ServiceImpl<BlogCategoryMapper, BlogCategory> implements BlogCategoryService {

    @Override
    public IPage<BlogCategory> selectPage(IPage<BlogCategory> page, Wrapper<BlogCategory> wrapper) {
        return baseMapper.selectPage(page, wrapper);
    }
}
