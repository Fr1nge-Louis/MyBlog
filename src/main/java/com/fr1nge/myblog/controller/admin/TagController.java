package com.fr1nge.myblog.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fr1nge.myblog.entity.Blog;
import com.fr1nge.myblog.entity.BlogLink;
import com.fr1nge.myblog.entity.BlogTag;
import com.fr1nge.myblog.service.BlogService;
import com.fr1nge.myblog.service.BlogTagService;
import com.fr1nge.myblog.util.PageResult;
import com.fr1nge.myblog.util.Result;
import com.fr1nge.myblog.util.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin")
public class TagController {

    @Resource
    private BlogTagService tagService;

    @Resource
    private BlogService blogService;

    @GetMapping("/tags")
    public String tagPage(HttpServletRequest request) {
        request.setAttribute("path", "tags");
        return "admin/tag";
    }

    @GetMapping("/tags/list")
    @ResponseBody
    public Result list(@RequestParam(required = false) Integer page,
                       @RequestParam(required = false) Integer limit) {
        if (page == null) {
            page = 1;
        }
        if (limit == null) {
            limit = 10;
        }
        LambdaQueryWrapper<BlogTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlogTag::getIsDeleted, 0)
                .orderByAsc(BlogTag::getTagId);
        Page<BlogTag> pageQuery = new Page<>((long) (page - 1) * page, limit);
        IPage<BlogTag> tagIPage = tagService.selectPage(pageQuery, queryWrapper);
        PageResult pageResult = new PageResult(tagIPage.getRecords(),
                (int) tagIPage.getTotal(), (int) tagIPage.getSize(), (int) tagIPage.getCurrent());
        return ResultGenerator.genSuccessResult(pageResult);
    }

    @PostMapping("/tags/save")
    @ResponseBody
    public Result save(@RequestParam("tagName") String tagName) {
        LambdaQueryWrapper<BlogTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlogTag::getTagName, tagName);
        BlogTag blogTag = tagService.getOne(queryWrapper);
        if (blogTag == null) {
            blogTag = new BlogTag();
            blogTag.setTagName(tagName);
            tagService.save(blogTag);
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("标签名称重复");
        }
    }

    @PostMapping("/tags/delete")
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids) {
        LambdaQueryWrapper<BlogTag> tagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        tagLambdaQueryWrapper.in(BlogTag::getTagId, Arrays.asList(ids));
        List<BlogTag> blogTagList = tagService.list(tagLambdaQueryWrapper);
        //检查是否有关联数据
        for (BlogTag tag : blogTagList) {
            LambdaQueryWrapper<Blog> blogLambdaQueryWrapper = new LambdaQueryWrapper<>();
            blogLambdaQueryWrapper.like(Blog::getBlogTags, tag.getTagName())
                    .eq(Blog::getIsDeleted,0);
            if(!blogService.list(blogLambdaQueryWrapper).isEmpty()){
                return ResultGenerator.genFailResult("{"+tag.getTagName()+"},关联未删除的博客");
            }
        }
        for (int i = 0; i < blogTagList.size(); i++) {
            blogTagList.get(i).setIsDeleted(1);
        }
        if (tagService.updateBatchById(blogTagList)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }


}
