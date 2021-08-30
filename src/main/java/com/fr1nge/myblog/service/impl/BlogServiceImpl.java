package com.fr1nge.myblog.service.impl;

import com.fr1nge.myblog.entity.Blog;
import com.fr1nge.myblog.mapper.BlogMapper;
import com.fr1nge.myblog.service.BlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2021-08-30
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

}
