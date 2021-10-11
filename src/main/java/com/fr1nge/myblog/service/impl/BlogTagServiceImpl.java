package com.fr1nge.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fr1nge.myblog.dao.BlogTagMapper;
import com.fr1nge.myblog.entity.BlogTag;
import com.fr1nge.myblog.entity.BlogTagCount;
import com.fr1nge.myblog.service.BlogTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author author
 * @since 2021-08-31
 */
@Service
public class BlogTagServiceImpl extends ServiceImpl<BlogTagMapper, BlogTag> implements BlogTagService {

    @Autowired
    private BlogTagMapper blogTagMapper;

    @Override
    public IPage<BlogTag> selectPage(Page<BlogTag> page, Wrapper<BlogTag> wrapper) {
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public List<BlogTagCount> getTagCount() {
        return blogTagMapper.getTagCount();
    }
}
