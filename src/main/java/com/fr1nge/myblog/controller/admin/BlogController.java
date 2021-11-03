package com.fr1nge.myblog.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fr1nge.myblog.entity.Blog;
import com.fr1nge.myblog.entity.BlogCategory;
import com.fr1nge.myblog.entity.BlogTag;
import com.fr1nge.myblog.entity.BlogTagRelation;
import com.fr1nge.myblog.service.BlogCategoryService;
import com.fr1nge.myblog.service.BlogService;
import com.fr1nge.myblog.service.BlogTagRelationService;
import com.fr1nge.myblog.service.BlogTagService;
import com.fr1nge.myblog.util.PageResult;
import com.fr1nge.myblog.util.Result;
import com.fr1nge.myblog.util.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/admin")
public class BlogController {

    @Resource
    private BlogService blogService;
    @Resource
    private BlogCategoryService categoryService;
    @Resource
    private BlogTagService tagService;
    @Resource
    private BlogTagRelationService tagRelationService;

    @GetMapping("/blogs/list")
    @ResponseBody
    public Result list(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) String blogStatus,
                       @RequestParam(required = false) String blogCategoryId,
                       @RequestParam(required = false) Integer page,
                       @RequestParam(required = false) Integer limit) {

        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Blog::getIsDeleted, 0);
        if (StringUtils.isNotBlank(keyword)) {
            queryWrapper.like(Blog::getBlogTitle, keyword).or()
                    .like(Blog::getBlogCategoryName, keyword);
        }
        if (StringUtils.isNotBlank(blogStatus)) {
            queryWrapper.eq(Blog::getBlogStatus, blogStatus);
        }
        if (StringUtils.isNotBlank(blogCategoryId)) {
            queryWrapper.eq(Blog::getBlogCategoryId, blogCategoryId);
        }
        if (page == null) {
            page = 1;
        }
        if (limit == null) {
            limit = 10;
        }
        Page<Blog> pageQuery = new Page<>(page, limit);
        IPage<Blog> blogIPage = blogService.selectPage(pageQuery, queryWrapper);
        PageResult pageResult = new PageResult(blogIPage.getRecords(),
                (int) blogIPage.getTotal(), (int) blogIPage.getSize(), page);
        return ResultGenerator.genSuccessResult(pageResult);

    }


    @GetMapping("/blogs")
    public String list(HttpServletRequest request) {
        request.setAttribute("path", "blogs");
        return "admin/blog";
    }

    @GetMapping("/blogs/edit")
    public String edit(HttpServletRequest request) {
        LambdaQueryWrapper<BlogCategory> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BlogCategory::getIsDeleted, 0);
        List<BlogCategory> blogCategoryList = categoryService.list(lambdaQueryWrapper);
        request.setAttribute("path", "edit");
        request.setAttribute("categories", blogCategoryList);
        return "admin/edit";
    }

    @GetMapping("/blogs/edit/{blogId}")
    public String edit(HttpServletRequest request, @PathVariable("blogId") Long blogId) {
        request.setAttribute("path", "edit");
        Blog blog = blogService.getById(blogId);
        if (blog == null) {
            return "error/error_400";
        }
        LambdaQueryWrapper<BlogCategory> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BlogCategory::getIsDeleted, 0);
        List<BlogCategory> blogCategoryList = categoryService.list(lambdaQueryWrapper);
        request.setAttribute("blog", blog);
        request.setAttribute("categories", blogCategoryList);
        return "admin/edit";
    }

    @Transactional
    @PostMapping("/blogs/save")
    @ResponseBody
    public Result save(@RequestParam("blogTitle") String blogTitle,
                       @RequestParam(name = "blogSubUrl", required = false) String blogSubUrl,
                       @RequestParam("blogCategoryId") Integer blogCategoryId,
                       @RequestParam("blogTags") String blogTags,
                       @RequestParam("blogContent") String blogContent,
                       @RequestParam("blogCoverImage") String blogCoverImage,
                       @RequestParam("blogStatus") Integer blogStatus,
                       @RequestParam("enableComment") Integer enableComment) {

        String[] tags = blogTags.split(",");
        if (tags.length > 6) {
            return ResultGenerator.genFailResult("标签数量限制为6");
        }
        try {
            Blog blog = new Blog()
                    .setBlogTitle(blogTitle)
                    .setBlogSubUrl(blogSubUrl)
                    .setBlogCategoryId(blogCategoryId)
                    .setBlogTags(blogTags)
                    .setBlogContent(blogContent)
                    .setBlogCoverImage(blogCoverImage.trim())
                    .setBlogStatus(blogStatus)
                    .setEnableComment(enableComment);
            if (!saveOrUpdateBlog(blog,null, tags)) {
                throw new RuntimeException();
            }
            return ResultGenerator.genSuccessResult();
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            log.error("保存文章失败！");
            return ResultGenerator.genFailResult("保存失败");
        }

    }

    @Transactional
    @PostMapping("/blogs/update")
    @ResponseBody
    public Result update(@RequestParam("blogId") Long blogId,
                         @RequestParam("blogTitle") String blogTitle,
                         @RequestParam(name = "blogSubUrl", required = false) String blogSubUrl,
                         @RequestParam("blogCategoryId") Integer blogCategoryId,
                         @RequestParam("blogTags") String blogTags,
                         @RequestParam("blogContent") String blogContent,
                         @RequestParam("blogCoverImage") String blogCoverImage,
                         @RequestParam("blogStatus") Integer blogStatus,
                         @RequestParam("enableComment") Integer enableComment) {
        String[] tags = blogTags.split(",");
        if (tags.length > 6) {
            return ResultGenerator.genFailResult("标签数量限制为6");
        }
        try {
            Blog oldBlog = blogService.getById(blogId);
            if (StringUtils.equals(blogTitle.trim(), oldBlog.getBlogTitle())
                    && StringUtils.equals(blogSubUrl.trim(), oldBlog.getBlogSubUrl())
                    && Objects.equals(blogCategoryId, oldBlog.getBlogCategoryId())
                    && StringUtils.equals(blogTags.trim(), oldBlog.getBlogTags())
                    && StringUtils.equals(blogContent, oldBlog.getBlogContent())
                    && StringUtils.equals(blogCoverImage.trim(), oldBlog.getBlogCoverImage())
                    && Objects.equals(blogStatus, oldBlog.getBlogStatus())
                    && Objects.equals(enableComment, oldBlog.getEnableComment())) {
                return ResultGenerator.genSuccessResult();
            }

            Blog newBlog = new Blog();
            newBlog.setBlogId(blogId)
                    .setBlogTitle(blogTitle.trim())
                    .setBlogSubUrl(blogSubUrl.trim())
                    .setBlogCategoryId(blogCategoryId)
                    .setBlogCoverImage(blogCoverImage.trim())
                    .setBlogContent(blogContent)
                    .setBlogCategoryId(blogCategoryId)
                    .setBlogCategoryName(oldBlog.getBlogCategoryName())
                    .setBlogTags(blogTags.trim())
                    .setBlogStatus(blogStatus)
                    .setBlogViews(oldBlog.getBlogViews())
                    .setEnableComment(enableComment)
                    .setIsDeleted(oldBlog.getIsDeleted())
                    .setCreateTime(oldBlog.getCreateTime())
                    .setUpdateTime(new Date());
            if (!saveOrUpdateBlog(newBlog,oldBlog, tags)) {
                throw new RuntimeException();
            }
            return ResultGenerator.genSuccessResult();
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            log.error("修改文章失败！");
            return ResultGenerator.genFailResult("修改失败");
        }

    }

    @Transactional
    @PostMapping("/blogs/delete")
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids) {
        try {
            LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Blog::getBlogId, Arrays.asList(ids));
            List<Blog> blogList = blogService.list(queryWrapper);
            for (int i = 0; i < blogList.size(); i++) {
                blogList.get(i).setIsDeleted(1);
            }
            if (!blogService.updateBatchById(blogList)) {

            }
            if (!tagRelationService.removeByIds(Arrays.asList(ids))) {
                throw new RuntimeException();
            }
            return ResultGenerator.genSuccessResult();
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            log.error("删除文章失败！");
            return ResultGenerator.genFailResult("删除失败");
        }
    }

    private boolean saveOrUpdateBlog(Blog newBlog, Blog oldBlog, String[] tags) {
        //插入或者更新blog
        BlogCategory blogCategory = categoryService.getById(newBlog.getBlogCategoryId());
        newBlog.setBlogCategoryName(blogCategory.getCategoryName());
        if (!blogService.saveOrUpdate(newBlog)) {
            return false;
        }

        if(oldBlog != null && StringUtils.equals(newBlog.getBlogTags(),oldBlog.getBlogTags())){
            return true;
        }

        //插入新的BlogTag
        LambdaQueryWrapper<BlogTag> tagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        tagLambdaQueryWrapper.in(BlogTag::getTagName, tags);
        List<BlogTag> blogTagList = tagService.list(tagLambdaQueryWrapper);
        List<BlogTag> saveTags = new ArrayList<>();
        if (blogTagList.size() < tags.length) {
            List<String> tagNameList = blogTagList.stream().map(e -> e.getTagName()).collect(Collectors.toList());
            for (int i = 0; i < tags.length; i++) {
                if (!tagNameList.contains(tags[i])) {
                    BlogTag blogTag = new BlogTag();
                    blogTag.setTagName(tags[i]);
                    saveTags.add(blogTag);
                }
            }
            if (!tagService.saveBatch(saveTags)) {
                log.error("保存标签失败！");
                return false;
            }
        }

        //tagRelation
        List<BlogTagRelation> blogTagRelations = new ArrayList<>();
        //如果是修改blog，则需要删除或者插入tagRelation
        if (oldBlog != null) {
            if(!StringUtils.equals(newBlog.getBlogTags(),oldBlog.getBlogTags())) {
                List<String> oldTagNameList = Arrays.asList(oldBlog.getBlogTags().split(","));
                List<String> newTagNameList = Arrays.asList(tags);
                List<String> saveTagNameList = new ArrayList<>();
                List<String> delTagNameList = new ArrayList<>();
                //newTagList中有，oldTagList没有，则是需要新增的
                for (String tagName : newTagNameList) {
                    if (!oldTagNameList.contains(tagName)) {
                        saveTagNameList.add(tagName);
                    }
                }
                //oldTagList中有，newTagList没有，则是需要删除的
                for (String tagName : oldTagNameList) {
                    if (!newTagNameList.contains(tagName)) {
                        delTagNameList.add(tagName);
                    }
                }

                //新增
                if (saveTagNameList.size() > 0) {
                    //查询已经存在的blogtag
                    tagLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    tagLambdaQueryWrapper.in(BlogTag::getTagName, saveTagNameList);
                    List<BlogTag> tagList = tagService.list(tagLambdaQueryWrapper);

                    //刚刚新增的blogtag中获取blogtagid
                    for (BlogTag blogTag:saveTags){
                        if(saveTagNameList.contains(blogTag.getTagName())){
                            tagList.add(blogTag);
                        }
                    }

                    for (BlogTag blogTag : tagList) {
                        BlogTagRelation blogTagRelation = new BlogTagRelation();
                        blogTagRelation.setBlogId(newBlog.getBlogId()).setTagId(blogTag.getTagId());
                        blogTagRelations.add(blogTagRelation);
                    }
                }

                //删除
                if (delTagNameList.size() > 0) {
                    tagLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    tagLambdaQueryWrapper.in(BlogTag::getTagName, delTagNameList);
                    List<BlogTag> tagList = tagService.list(tagLambdaQueryWrapper);
                    List<Integer> ids = tagList.stream().map(e -> e.getTagId()).collect(Collectors.toList());

                    LambdaUpdateWrapper<BlogTagRelation> tagRelationLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    tagRelationLambdaUpdateWrapper.in(BlogTagRelation::getTagId, ids);
                    tagRelationLambdaUpdateWrapper.eq(BlogTagRelation::getBlogId, newBlog.getBlogId());
                    tagRelationService.remove(tagRelationLambdaUpdateWrapper);
                }
            }
        }
        //如果是新增blog
        else {
            saveTags.addAll(blogTagList);
            for (BlogTag blogTag : saveTags) {
                BlogTagRelation blogTagRelation = new BlogTagRelation();
                blogTagRelation.setBlogId(newBlog.getBlogId()).setTagId(blogTag.getTagId());
                blogTagRelations.add(blogTagRelation);
            }
        }

        //TagRelation插入
        if (blogTagRelations.size() > 0) {
            if (!tagRelationService.saveBatch(blogTagRelations)) {
                return false;
            }
        }

        return true;
    }

}
