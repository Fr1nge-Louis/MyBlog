package com.fr1nge.myblog.service.impl;

import com.fr1nge.myblog.entity.BlogComment;
import com.fr1nge.myblog.dao.BlogCommentMapper;
import com.fr1nge.myblog.service.BlogCommentService;
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
public class BlogCommentServiceImpl extends ServiceImpl<BlogCommentMapper, BlogComment> implements BlogCommentService {

}
