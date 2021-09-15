package com.fr1nge.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fr1nge.myblog.dao.BlogFileMapper;
import com.fr1nge.myblog.entity.BlogFile;
import com.fr1nge.myblog.service.BlogFileService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author author
 * @since 2021-09-15
 */
@Service
public class BlogFileServiceImpl extends ServiceImpl<BlogFileMapper, BlogFile> implements BlogFileService {

    @Override
    public IPage<BlogFile> selectPage(IPage<BlogFile> page, Wrapper<BlogFile> wrapper) {
        return baseMapper.selectPage(page, wrapper);
    }
}
