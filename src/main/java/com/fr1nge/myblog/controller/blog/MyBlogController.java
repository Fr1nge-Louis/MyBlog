package com.fr1nge.myblog.controller.blog;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fr1nge.myblog.controller.vo.BlogDetailVO;
import com.fr1nge.myblog.entity.*;
import com.fr1nge.myblog.service.*;
import com.fr1nge.myblog.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class MyBlogController {
    public static String theme = "yummy-jekyll";
    @Resource
    private BlogService blogService;
    @Resource
    private BlogTagService tagService;
    @Resource
    private BlogLinkService linkService;
    @Resource
    private BlogCommentService commentService;
    @Resource
    private BlogConfigService configService;
    @Resource
    private BlogCategoryService categoryService;
    @Resource
    private BlogTagRelationService tagRelationService;

    /**
     * 首页
     *
     * @return
     */
    @GetMapping({"/index"})
    public String index(HttpServletRequest request) {
        return this.page(request, 1);
    }

    /**
     * 首页 分页数据
     *
     * @return
     */
    @GetMapping({"/page/{pageNum}"})
    public String page(HttpServletRequest request, @PathVariable("pageNum") int pageNum) {


        //分页详细博客
        Map<String,Object> param = new HashMap<>();
        param.put("blogStatus",1);
        param.put("isDeleted",0);
        param.put("rows",(pageNum-1)*9);
        param.put("limit",9);

        PageResult blogPageResult = getBlogByMap(param, pageNum, 9);
        if (blogPageResult == null) {
            return "error/error_404";
        }

        //最热博客--点击最多
        if(pageNum == 1) {
            LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Blog::getBlogStatus, 1).eq(Blog::getIsDeleted, 0).orderByDesc(Blog::getBlogViews);
            queryWrapper.select(Blog::getBlogId, Blog::getCreateTime, Blog::getBlogTitle, Blog::getBlogCategoryName);
            PageResult hotBlogPageResult = getBlogByWrapper(queryWrapper, 0, 3);
            request.getSession().setAttribute("hotBlogs", hotBlogPageResult.getList());
        }
        //返回值设定
        request.setAttribute("blogPageResult", blogPageResult);
        request.setAttribute("pageName", "首页");
        request.setAttribute("configurations", getConfig());
        return "blog/" + theme + "/index";
    }

    /**
     * Categories页面(包括分类数据和标签数据)
     *
     * @return
     */
    @GetMapping({"/categories"})
    public String categories(HttpServletRequest request) {
        LambdaQueryWrapper<BlogCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlogCategory::getIsDeleted, 0).orderByAsc(BlogCategory::getCreateTime);
        request.setAttribute("categories", categoryService.list(queryWrapper));
        request.setAttribute("pageName", "分类页面");
        request.setAttribute("configurations", getConfig());
        return "blog/" + theme + "/category";
    }

    /**
     * 详情页
     *
     * @return
     */
    @GetMapping({"/blog/{blogId}", "/article/{blogId}"})
    public String detail(HttpServletRequest request,
                         @PathVariable("blogId") Long blogId,
                         @RequestParam(value = "commentPage", required = false, defaultValue = "1") Integer commentPage) {
        Blog blog = blogService.getById(blogId);
        if (blog != null) {
            /***
            //增加浏览量
            long views = blog.getBlogViews() + 1;
            blog.setBlogViews(views);
            blogService.updateById(blog);

            //BlogDetailVO
            BlogDetailVO blogDetailVO = new BlogDetailVO();
            BeanUtils.copyProperties(blog, blogDetailVO);
            //主体内容
            blogDetailVO.setBlogContent(MarkDownUtil.markdownToHtmlExtensions(blogDetailVO.getBlogContent()));
            //分类
            BlogCategory blogCategory = categoryService.getById(blog.getBlogCategoryId());
            if (blogCategory == null) {
                blogCategory = new BlogCategory();
                blogCategory.setCategoryId(0);
                blogCategory.setCategoryName("默认分类");
                blogCategory.setCategoryIcon("/admin/dist/img/category/00.png");
            }
            blogDetailVO.setBlogCategoryIcon(blogCategory.getCategoryIcon());
            //标签
            if (!StringUtils.isEmpty(blog.getBlogTags())) {
                List<String> tags = Arrays.asList(blog.getBlogTags().split(","));
                blogDetailVO.setBlogTags(tags);
            }
            //设置评论数
            LambdaQueryWrapper<BlogComment> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BlogComment::getBlogId, blog.getBlogId())
                    .eq(BlogComment::getIsDeleted, 0)
                    .eq(BlogComment::getCommentStatus, 1);//过滤审核通过的数据
            int commentCount = commentService.count(queryWrapper);
            blogDetailVO.setCommentCount(commentCount);

            //评论
            queryWrapper.orderByAsc(BlogComment::getCommentCreateTime);
            Page<BlogComment> pageQuery = new Page<>(commentPage, 8);
            IPage<BlogComment> commentIPage = commentService.selectPage(pageQuery, queryWrapper);
            PageResult commentPageResult = null;
            if (!commentIPage.getRecords().isEmpty()) {
                commentPageResult = new PageResult(commentIPage.getRecords(),
                        (int) commentIPage.getTotal(), (int) commentIPage.getSize(), commentPage);
            }
             */
            getBlogDetailAndComment(blog,request,commentPage);
        }

        request.setAttribute("pageName", "详情");
        request.setAttribute("configurations", getConfig());
        return "blog/" + theme + "/detail";
    }

    /**
     * 标签列表页
     *
     * @return
     */
    @GetMapping({"/tag/{tagName}"})
    public String tag(HttpServletRequest request, @PathVariable("tagName") String tagName) {
        return tag(request, tagName, 1);
    }

    /**
     * 标签列表页
     *
     * @return
     */
    @GetMapping({"/tag/{tagName}/{page}"})
    public String tag(HttpServletRequest request,
                      @PathVariable("tagName") String tagName,
                      @PathVariable("page") Integer page) {
        //分页博客
        LambdaQueryWrapper<BlogTag> tagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        tagLambdaQueryWrapper.eq(BlogTag::getTagName, tagName);
        BlogTag blogTag = tagService.getOne(tagLambdaQueryWrapper);

        LambdaQueryWrapper<BlogTagRelation> tagRelationLambdaQueryWrapper = new LambdaQueryWrapper<>();
        tagRelationLambdaQueryWrapper.eq(BlogTagRelation::getTagId, blogTag.getTagId());
        List<BlogTagRelation> relationList = tagRelationService.list(tagRelationLambdaQueryWrapper);
        List<Long> blogIds = relationList.stream().map(e -> e.getBlogId()).collect(Collectors.toList());

        LambdaQueryWrapper<Blog> blogLambdaQueryWrapper = new LambdaQueryWrapper<>();
        blogLambdaQueryWrapper.eq(Blog::getBlogStatus, 1)
                .eq(Blog::getIsDeleted, 0)
                .in(Blog::getBlogId, blogIds)
                .orderByDesc(Blog::getCreateTime);
        PageResult blogPageResult = getBlogByWrapper(blogLambdaQueryWrapper, page, 9);
        if (blogPageResult.getList().size() == 0) {
            blogPageResult = null;
        }
        request.setAttribute("blogPageResult", blogPageResult);
        request.setAttribute("pageName", "标签");
        request.setAttribute("pageUrl", "tag");
        request.setAttribute("keyword", tagName);
        request.setAttribute("configurations", getConfig());
        return "blog/" + theme + "/list";
    }

    /**
     * 分类列表页
     *
     * @return
     */
    @GetMapping({"/category/{categoryName}"})
    public String category(HttpServletRequest request, @PathVariable("categoryName") String categoryName) {
        return category(request, categoryName, 1);
    }

    /**
     * 分类列表页
     *
     * @return
     */
    @GetMapping({"/category/{categoryName}/{page}"})
    public String category(HttpServletRequest request,
                           @PathVariable("categoryName") String categoryName,
                           @PathVariable("page") Integer page) {

        //分页博客
        LambdaQueryWrapper<BlogCategory> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.eq(BlogCategory::getCategoryName, categoryName);
        BlogCategory blogCategory = categoryService.getOne(categoryLambdaQueryWrapper);

        LambdaQueryWrapper<Blog> blogLambdaQueryWrapper = new LambdaQueryWrapper<>();
        blogLambdaQueryWrapper.eq(Blog::getBlogStatus, 1)
                .eq(Blog::getIsDeleted, 0)
                .eq(Blog::getBlogCategoryId, blogCategory.getCategoryId())
                .orderByDesc(Blog::getCreateTime);
        PageResult blogPageResult = getBlogByWrapper(blogLambdaQueryWrapper, page, 9);
        if (blogPageResult.getList().size() == 0) {
            blogPageResult = null;
        }
        request.setAttribute("blogPageResult", blogPageResult);
        request.setAttribute("pageName", "分类");
        request.setAttribute("pageUrl", "category");
        request.setAttribute("keyword", categoryName);
        request.setAttribute("configurations", getConfig());
        return "blog/" + theme + "/list";
    }

    /**
     * 友情链接页
     *
     * @return
     */
    @GetMapping({"/link"})
    public String link(HttpServletRequest request) {
        LambdaQueryWrapper<BlogLink> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlogLink::getIsDeleted, 0);
        List<BlogLink> linkList = linkService.list(queryWrapper);
        Map<Integer, List<BlogLink>> linkMap = linkList.stream().collect(Collectors.groupingBy(BlogLink::getLinkType));
        if (linkMap != null) {
            //判断友链类别并封装数据 0-友链 1-推荐 2-个人网站
            if (linkMap.containsKey(0)) {
                request.setAttribute("favoriteLinks", linkMap.get((0)));
            }
            if (linkMap.containsKey(1)) {
                request.setAttribute("recommendLinks", linkMap.get(1));
            }
            if (linkMap.containsKey(2)) {
                request.setAttribute("personalLinks", linkMap.get(2));
            }
        }
        request.setAttribute("pageName", "友情链接");
        request.setAttribute("configurations", getConfig());
        return "blog/" + theme + "/link";
    }

    /**
     * 评论操作
     */
    @PostMapping(value = "/blog/comment")
    @ResponseBody
    public Result comment(HttpServletRequest request, HttpSession session,
                          @RequestParam Long blogId, @RequestParam String verifyCode,
                          @RequestParam String commentator, @RequestParam String email,
                          @RequestParam String websiteUrl, @RequestParam String commentBody) {
        if (StringUtils.isEmpty(verifyCode)) {
            return ResultGenerator.genFailResult("验证码不能为空");
        }
        String kaptchaCode = session.getAttribute("verifyCode").toString();
        if (StringUtils.isEmpty(kaptchaCode)) {
            return ResultGenerator.genFailResult("非法请求");
        }
        if (!verifyCode.equals(kaptchaCode)) {
            return ResultGenerator.genFailResult("验证码错误");
        }
        String ref = request.getHeader("Referer");
        if (StringUtils.isEmpty(ref)) {
            return ResultGenerator.genFailResult("非法请求");
        }
        if (null == blogId || blogId < 0) {
            return ResultGenerator.genFailResult("非法请求");
        }
        if (StringUtils.isEmpty(commentator)) {
            return ResultGenerator.genFailResult("请输入称呼");
        }
        if (StringUtils.isEmpty(email)) {
            return ResultGenerator.genFailResult("请输入邮箱地址");
        }
        if (!PatternUtil.isEmail(email)) {
            return ResultGenerator.genFailResult("请输入正确的邮箱地址");
        }
        if (StringUtils.isEmpty(commentBody)) {
            return ResultGenerator.genFailResult("请输入评论内容");
        }
        if (commentBody.trim().length() > 200) {
            return ResultGenerator.genFailResult("评论内容过长");
        }
        BlogComment comment = new BlogComment();
        comment.setBlogId(blogId);
        comment.setCommentator(MyBlogUtils.cleanString(commentator));
        comment.setEmail(email);
        if (PatternUtil.isURL(websiteUrl)) {
            comment.setWebsiteUrl(websiteUrl);
        }
        comment.setCommentBody(MyBlogUtils.cleanString(commentBody));
        commentService.save(comment);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 关于页面 以及其他配置了subUrl的文章页
     */
    @GetMapping({"/{subUrl}"})
    public String detail(HttpServletRequest request,
                         @PathVariable("subUrl") String subUrl,
                         @RequestParam(value = "commentPage", required = false, defaultValue = "1") Integer commentPage) {
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Blog::getBlogSubUrl, subUrl)
                .eq(Blog::getIsDeleted, 0)//未删除
                .eq(Blog::getBlogStatus, 1);//已发布
        Blog blog = blogService.getOne(queryWrapper);
        if (blog != null) {
            getBlogDetailAndComment(blog, request, commentPage);
            request.setAttribute("pageName", subUrl);
            request.setAttribute("configurations", getConfig());
            return "blog/" + theme + "/detail";
        } else {
            return "error/error_400";
        }
    }

    @GetMapping("/my/resume")
    public String getResume(HttpServletRequest request){
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Blog::getBlogSubUrl, "resume");
        Blog blog = blogService.getOne(queryWrapper);

        if (blog != null) {
            getBlogDetailForPreview(blog, request, 1);
            request.setAttribute("pageName", "resume");
            request.setAttribute("configurations", getConfig());
            return "blog/resume/detail";
        } else {
            return "error/error_400";
        }
    }

    /**
     * 预览文章
     */
    @GetMapping({"/preview/{subUrl}"})
    public String previewDetail(HttpServletRequest request,
                                @PathVariable("subUrl") String subUrl,
                                @RequestParam(value = "commentPage", required = false, defaultValue = "1") Integer commentPage) {
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Blog::getBlogSubUrl, subUrl).orderByAsc(Blog::getBlogViews);
        Blog blog = blogService.getOne(queryWrapper);
        if (blog != null) {
            getBlogDetailForPreview(blog, request, commentPage);
            request.setAttribute("pageName", subUrl);
            request.setAttribute("configurations", getConfig());
            return "blog/" + theme + "/detail";
        } else {
            return "error/error_400";
        }
    }

    @GetMapping({"/search/{keyword}"})
    public String search(HttpServletRequest request, @PathVariable("keyword") String keyword) {
        return search(request, keyword, 1);
    }

    @GetMapping({"/search/{keyword}/{page}"})
    public String search(HttpServletRequest request, @PathVariable("keyword") String keyword, @PathVariable("page") Integer page) {
        Map<String,Object> param = new HashMap<>();
        if(keyword.isEmpty()){
            keyword = null;
        }
        param.put("keyword",keyword);
        param.put("rows",(page-1)*9);
        param.put("limit",9);

        PageResult blogPageResult = getBlogByMap(param,page,9);

        request.setAttribute("blogPageResult", blogPageResult);
        request.setAttribute("pageName", "搜索");
        request.setAttribute("pageUrl", "search");
        request.setAttribute("keyword", keyword);
        request.setAttribute("configurations", getConfig());
        return "blog/" + theme + "/list";
    }

    //不同条件分页查询blog
    private PageResult getBlogByWrapper(LambdaQueryWrapper<Blog> wrapper, int pageNum, int pageSize) {
        wrapper.select(
                Blog::getBlogId,
                Blog::getBlogTitle,
                Blog::getBlogSubUrl,
                Blog::getBlogCoverImage,
                Blog::getBlogCategoryId,
                Blog::getBlogCategoryName,
                Blog::getBlogTags,
                Blog::getBlogStatus,
                Blog::getBlogViews,
                Blog::getEnableComment,
                Blog::getIsDeleted,
                Blog::getCreateTime,
                Blog::getUpdateTime);
        Page<Blog> pageQuery = new Page<>(pageNum, pageSize);
        IPage<Blog> blogIPage = blogService.selectPage(pageQuery, wrapper);
        return new PageResult(blogIPage.getRecords(), (int) blogIPage.getTotal(),
                (int) blogIPage.getSize(), pageNum);
    }


    private PageResult getBlogByMap(Map<String,Object> param, int pageNum, int pageSize) {
        List<Blog> blogList = blogService.selectBlogPage(param);
        int total = blogService.selectBlogPageCount(param);
        return new PageResult(blogList,total,pageSize,pageNum);
    }

    //获取所有的配置Map
    private Map<String, String> getConfig() {
        List<BlogConfig> blogConfigList = configService.list();
        Map<String, String> configMap = blogConfigList.stream()
                .collect(Collectors.toMap(BlogConfig::getConfigName, BlogConfig::getConfigValue));
        return configMap;
    }

    private void getBlogDetailAndComment(Blog blog, HttpServletRequest request, int commentPage) {
        //增加浏览量
        long views = blog.getBlogViews() + 1;
        LambdaUpdateWrapper<Blog> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Blog::getBlogViews,views);
        updateWrapper.eq(Blog::getBlogId,blog.getBlogId());
        blogService.update(updateWrapper);

        //BlogDetailVO
        BlogDetailVO blogDetailVO = new BlogDetailVO();
        BeanUtils.copyProperties(blog, blogDetailVO);
        //主体内容
        blogDetailVO.setBlogContent(MarkDownUtil.markdownToHtmlExtensions(blogDetailVO.getBlogContent()));
        //分类
        BlogCategory blogCategory = categoryService.getById(blog.getBlogCategoryId());
        if (blogCategory == null) {
            blogCategory = new BlogCategory();
            blogCategory.setCategoryId(0);
            blogCategory.setCategoryName("默认分类");
            blogCategory.setCategoryIcon("/admin/dist/img/category/00.png");
        }
        blogDetailVO.setBlogCategoryIcon(blogCategory.getCategoryIcon());
        //标签
//        if (!StringUtils.isEmpty(blog.getBlogTags())) {
//            List<String> tags = Arrays.asList(blog.getBlogTags().split(","));
//            blogDetailVO.setBlogTags(tags);
//        }

        //如果开启了评论，就获取评论
        if (blog.getEnableComment() == 1) {
            //设置评论数
            LambdaQueryWrapper<BlogComment> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BlogComment::getBlogId, blog.getBlogId())
                    .eq(BlogComment::getIsDeleted, 0)//过滤未删除的评论
                    .eq(BlogComment::getCommentStatus, 1);//过滤审核通过的数据
            int commentCount = commentService.count(queryWrapper);
            blogDetailVO.setCommentCount(commentCount);

            //评论
            Page<BlogComment> pageQuery = new Page<>(commentPage, 8);
            IPage<BlogComment> commentIPage = commentService.selectPage(pageQuery, queryWrapper);
            PageResult commentPageResult = new PageResult(commentIPage.getRecords(), (int) commentIPage.getTotal(),
                    (int) commentIPage.getSize(), commentPage);
            request.setAttribute("commentPageResult", commentPageResult);
        } else {
            blogDetailVO.setCommentCount(0);
        }

        request.setAttribute("blogDetailVO", blogDetailVO);

    }

    private void getBlogDetailForPreview(Blog blog, HttpServletRequest request, int commentPage) {
        //BlogDetailVO
        BlogDetailVO blogDetailVO = new BlogDetailVO();
        //不显示评论
        blogDetailVO.setCommentCount(0);

        BeanUtils.copyProperties(blog, blogDetailVO);
        //主体内容
        blogDetailVO.setBlogContent(MarkDownUtil.markdownToHtmlExtensions(blogDetailVO.getBlogContent()));
        //分类
        BlogCategory blogCategory = categoryService.getById(blog.getBlogCategoryId());
        if (blogCategory == null) {
            blogCategory = new BlogCategory();
            blogCategory.setCategoryId(0);
            blogCategory.setCategoryName("默认分类");
            blogCategory.setCategoryIcon("/admin/dist/img/category/00.png");
        }
        blogDetailVO.setBlogCategoryIcon(blogCategory.getCategoryIcon());
        //标签
        if (!StringUtils.isEmpty(blog.getBlogTags())) {
            List<String> tags = Arrays.asList(blog.getBlogTags().split(","));
            blogDetailVO.setBlogTags(tags);
        }
        request.setAttribute("blogDetailVO", blogDetailVO);
    }

}
