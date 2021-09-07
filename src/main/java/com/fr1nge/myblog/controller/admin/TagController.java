package com.fr1nge.myblog.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fr1nge.myblog.entity.BlogTag;
import com.fr1nge.myblog.service.BlogTagService;
import com.fr1nge.myblog.util.PageResult;
import com.fr1nge.myblog.util.Result;
import com.fr1nge.myblog.util.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
@RequestMapping("/admin")
public class TagController {

    @Resource
    private BlogTagService tagService;

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
        Page<BlogTag> pageQuery = new Page<>((page - 1) * page, limit);
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
        LambdaUpdateWrapper<BlogTag> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(BlogTag::getTagId, ids)
                .set(BlogTag::getIsDeleted, 1);
        if (tagService.update(updateWrapper)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("有关联数据请勿强行删除");
        }
    }


}
