package com.fr1nge.myblog.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fr1nge.myblog.entity.Blog;
import com.fr1nge.myblog.service.BlogCategoryService;
import com.fr1nge.myblog.service.BlogService;
import com.fr1nge.myblog.util.PageResult;
import com.fr1nge.myblog.util.Result;
import com.fr1nge.myblog.util.ResultGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


@Controller
@RequestMapping("/admin")
public class BlogController {

    @Resource
    private BlogService blogService;
    @Resource
    private BlogCategoryService categoryService;

    @GetMapping("/blogs/list")
    @ResponseBody
    public Result list(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) String blogStatus,
                       @RequestParam(required = false) String blogCategoryId,
                       @RequestParam(required = false) Integer page,
                       @RequestParam(required = false) Integer limit) {

        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(keyword)){
            queryWrapper.like(Blog::getBlogTitle,keyword).or()
                    .like(Blog::getBlogCategoryName,keyword);
        }
        if(StringUtils.isNotBlank(blogStatus)){
            queryWrapper.eq(Blog::getBlogStatus,blogStatus);
        }
        if(StringUtils.isNotBlank(blogCategoryId)){
            queryWrapper.eq(Blog::getBlogCategoryId,blogCategoryId);
        }
        if(page == null){
            page = 1;
        }
        if(limit == null){
            limit = 10;
        }
        Page<Blog> pageQuery = new Page<>((page-1)*page,limit);
        IPage<Blog> blogIPage = blogService.selectPage(pageQuery,queryWrapper);
        PageResult pageResult =  new PageResult(blogIPage.getRecords(),
                (int)blogIPage.getTotal(),(int)blogIPage.getSize(),(int)blogIPage.getCurrent());
        return ResultGenerator.genSuccessResult(pageResult);

    }


//    @GetMapping("/blogs")
//    public String list(HttpServletRequest request) {
//        request.setAttribute("path", "blogs");
//        return "admin/blog";
//    }
//
//    @GetMapping("/blogs/edit")
//    public String edit(HttpServletRequest request) {
//        request.setAttribute("path", "edit");
//        request.setAttribute("categories", categoryService.getAllCategories());
//        return "admin/edit";
//    }
//
//    @GetMapping("/blogs/edit/{blogId}")
//    public String edit(HttpServletRequest request, @PathVariable("blogId") Long blogId) {
//        request.setAttribute("path", "edit");
//        Blog blog = blogService.getBlogById(blogId);
//        if (blog == null) {
//            return "error/error_400";
//        }
//        request.setAttribute("blog", blog);
//        request.setAttribute("categories", categoryService.getAllCategories());
//        return "admin/edit";
//    }
//
//    @PostMapping("/blogs/save")
//    @ResponseBody
//    public Result save(@RequestParam("blogTitle") String blogTitle,
//                       @RequestParam(name = "blogSubUrl", required = false) String blogSubUrl,
//                       @RequestParam("blogCategoryId") Integer blogCategoryId,
//                       @RequestParam("blogTags") String blogTags,
//                       @RequestParam("blogContent") String blogContent,
//                       @RequestParam("blogCoverImage") String blogCoverImage,
//                       @RequestParam("blogStatus") Byte blogStatus,
//                       @RequestParam("enableComment") Byte enableComment) {
//        if (StringUtils.isEmpty(blogTitle)) {
//            return ResultGenerator.genFailResult("请输入文章标题");
//        }
//        if (blogTitle.trim().length() > 150) {
//            return ResultGenerator.genFailResult("标题过长");
//        }
//        if (StringUtils.isEmpty(blogTags)) {
//            return ResultGenerator.genFailResult("请输入文章标签");
//        }
//        if (blogTags.trim().length() > 150) {
//            return ResultGenerator.genFailResult("标签过长");
//        }
//        if (blogSubUrl.trim().length() > 150) {
//            return ResultGenerator.genFailResult("路径过长");
//        }
//        if (StringUtils.isEmpty(blogContent)) {
//            return ResultGenerator.genFailResult("请输入文章内容");
//        }
//        if (blogTags.trim().length() > 100000) {
//            return ResultGenerator.genFailResult("文章内容过长");
//        }
//        if (StringUtils.isEmpty(blogCoverImage)) {
//            return ResultGenerator.genFailResult("封面图不能为空");
//        }
//        Blog blog = new Blog();
//        blog.setBlogTitle(blogTitle);
//        blog.setBlogSubUrl(blogSubUrl);
//        blog.setBlogCategoryId(blogCategoryId);
//        blog.setBlogTags(blogTags);
//        blog.setBlogContent(blogContent);
//        blog.setBlogCoverImage(blogCoverImage);
//        blog.setBlogStatus(blogStatus);
//        blog.setEnableComment(enableComment);
//        String saveBlogResult = blogService.saveBlog(blog);
//        if ("success".equals(saveBlogResult)) {
//            return ResultGenerator.genSuccessResult("添加成功");
//        } else {
//            return ResultGenerator.genFailResult(saveBlogResult);
//        }
//    }
//
//    @PostMapping("/blogs/update")
//    @ResponseBody
//    public Result update(@RequestParam("blogId") Long blogId,
//                         @RequestParam("blogTitle") String blogTitle,
//                         @RequestParam(name = "blogSubUrl", required = false) String blogSubUrl,
//                         @RequestParam("blogCategoryId") Integer blogCategoryId,
//                         @RequestParam("blogTags") String blogTags,
//                         @RequestParam("blogContent") String blogContent,
//                         @RequestParam("blogCoverImage") String blogCoverImage,
//                         @RequestParam("blogStatus") Byte blogStatus,
//                         @RequestParam("enableComment") Byte enableComment) {
//        if (StringUtils.isEmpty(blogTitle)) {
//            return ResultGenerator.genFailResult("请输入文章标题");
//        }
//        if (blogTitle.trim().length() > 150) {
//            return ResultGenerator.genFailResult("标题过长");
//        }
//        if (StringUtils.isEmpty(blogTags)) {
//            return ResultGenerator.genFailResult("请输入文章标签");
//        }
//        if (blogTags.trim().length() > 150) {
//            return ResultGenerator.genFailResult("标签过长");
//        }
//        if (blogSubUrl.trim().length() > 150) {
//            return ResultGenerator.genFailResult("路径过长");
//        }
//        if (StringUtils.isEmpty(blogContent)) {
//            return ResultGenerator.genFailResult("请输入文章内容");
//        }
//        if (blogTags.trim().length() > 100000) {
//            return ResultGenerator.genFailResult("文章内容过长");
//        }
//        if (StringUtils.isEmpty(blogCoverImage)) {
//            return ResultGenerator.genFailResult("封面图不能为空");
//        }
//        Blog blog = new Blog();
//        blog.setBlogId(blogId);
//        blog.setBlogTitle(blogTitle);
//        blog.setBlogSubUrl(blogSubUrl);
//        blog.setBlogCategoryId(blogCategoryId);
//        blog.setBlogTags(blogTags);
//        blog.setBlogContent(blogContent);
//        blog.setBlogCoverImage(blogCoverImage);
//        blog.setBlogStatus(blogStatus);
//        blog.setEnableComment(enableComment);
//        String updateBlogResult = blogService.updateBlog(blog);
//        if ("success".equals(updateBlogResult)) {
//            return ResultGenerator.genSuccessResult("修改成功");
//        } else {
//            return ResultGenerator.genFailResult(updateBlogResult);
//        }
//    }
//
//    @PostMapping("/blogs/md/uploadfile")
//    public void uploadFileByEditormd(HttpServletRequest request,
//                                     HttpServletResponse response,
//                                     @RequestParam(name = "editormd-image-file", required = true)
//                                             MultipartFile file) throws IOException, URISyntaxException {
//        String fileName = file.getOriginalFilename();
//        String suffixName = fileName.substring(fileName.lastIndexOf("."));
//        //生成文件名称通用方法
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
//        Random r = new Random();
//        StringBuilder tempName = new StringBuilder();
//        tempName.append(sdf.format(new Date())).append(r.nextInt(100)).append(suffixName);
//        String newFileName = tempName.toString();
//        //创建文件
//        File destFile = new File(Constants.FILE_UPLOAD_DIC + newFileName);
//        String fileUrl = MyBlogUtils.getHost(new URI(request.getRequestURL() + "")) + "/upload/" + newFileName;
//        File fileDirectory = new File(Constants.FILE_UPLOAD_DIC);
//        try {
//            if (!fileDirectory.exists()) {
//                if (!fileDirectory.mkdir()) {
//                    throw new IOException("文件夹创建失败,路径为：" + fileDirectory);
//                }
//            }
//            file.transferTo(destFile);
//            request.setCharacterEncoding("utf-8");
//            response.setHeader("Content-Type", "text/html");
//            response.getWriter().write("{\"success\": 1, \"message\":\"success\",\"url\":\"" + fileUrl + "\"}");
//        } catch (UnsupportedEncodingException e) {
//            response.getWriter().write("{\"success\":0}");
//        } catch (IOException e) {
//            response.getWriter().write("{\"success\":0}");
//        }
//    }
//
//    @PostMapping("/blogs/delete")
//    @ResponseBody
//    public Result delete(@RequestBody Integer[] ids) {
//        if (ids.length < 1) {
//            return ResultGenerator.genFailResult("参数异常！");
//        }
//        if (blogService.deleteBatch(ids)) {
//            return ResultGenerator.genSuccessResult();
//        } else {
//            return ResultGenerator.genFailResult("删除失败");
//        }
//    }

}
