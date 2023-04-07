package com.fr1nge.myblog.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fr1nge.myblog.entity.Blog;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author author
 * @since 2021-08-31
 */
public interface BlogService extends IService<Blog> {
    IPage<Blog> selectPage(IPage<Blog> page, Wrapper<Blog> wrapper);
    List<Blog> selectBlogPage(Map<String,Object> map);
    int selectBlogPageCount(Map<String,Object> map);
}
