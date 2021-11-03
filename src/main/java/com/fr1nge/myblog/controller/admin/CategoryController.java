package com.fr1nge.myblog.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fr1nge.myblog.entity.Blog;
import com.fr1nge.myblog.entity.BlogCategory;
import com.fr1nge.myblog.service.BlogCategoryService;
import com.fr1nge.myblog.service.BlogService;
import com.fr1nge.myblog.util.PageResult;
import com.fr1nge.myblog.util.Result;
import com.fr1nge.myblog.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class CategoryController {

    @Resource
    private BlogCategoryService categoryService;

    @Resource
    private BlogService blogService;

    @GetMapping("/categories")
    public String categoryPage(HttpServletRequest request) {
        request.setAttribute("path", "categories");
        return "admin/category";
    }

    /**
     * 分类列表
     */
    @RequestMapping(value = "/categories/list", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@RequestParam(required = false) Integer page,
                       @RequestParam(required = false) Integer limit) {

        LambdaQueryWrapper<BlogCategory> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BlogCategory::getIsDeleted, 0);
        if (page == null) {
            page = 1;
        }
        if (limit == null) {
            limit = 10;
        }
        Page<BlogCategory> pageQuery = new Page<>(page, limit);

        IPage<BlogCategory> categoryIPage = categoryService.selectPage(pageQuery, lambdaQueryWrapper);
        PageResult pageResult = new PageResult(categoryIPage.getRecords(),
                (int) categoryIPage.getTotal(), (int) categoryIPage.getSize(), page);
        return ResultGenerator.genSuccessResult(pageResult);
    }

    /**
     * 分类添加
     */
    @RequestMapping(value = "/categories/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(@RequestParam("categoryName") String categoryName,
                       @RequestParam("categoryIcon") String categoryIcon) {
        LambdaQueryWrapper<BlogCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlogCategory::getCategoryName, categoryName);
        if (categoryService.getOne(queryWrapper) != null) {
            return ResultGenerator.genFailResult("分类名称重复");
        }
        BlogCategory blogCategory = new BlogCategory();
        blogCategory.setCategoryName(categoryName).setCategoryIcon(categoryIcon);
        boolean flag = categoryService.save(blogCategory);
        if (flag) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("添加失败");
        }


    }


    /**
     * 分类修改
     */
    @Transactional
    @RequestMapping(value = "/categories/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(@RequestParam("categoryId") Integer categoryId,
                         @RequestParam("categoryName") String categoryName,
                         @RequestParam("categoryIcon") String categoryIcon) {
        try {
            BlogCategory blogCategory = categoryService.getById(categoryId);
            blogCategory.setCategoryName(categoryName).setCategoryIcon(categoryIcon);
            if (!categoryService.updateById(blogCategory)) {
                throw new RuntimeException();
            }

            //修改blog的冗余字段
            LambdaQueryWrapper<Blog> blogLambdaQueryWrapper = new LambdaQueryWrapper<>();
            blogLambdaQueryWrapper.eq(Blog::getBlogCategoryId, categoryId);
            List<Blog> blogList = blogService.list(blogLambdaQueryWrapper);
            for (Blog blog : blogList) {
                blog.setBlogCategoryName(categoryName);
            }
            if (!blogService.updateBatchById(blogList)) {
                throw new RuntimeException();
            }
            return ResultGenerator.genSuccessResult();
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return ResultGenerator.genFailResult("修改失败");
        }


    }


    /**
     * 分类删除
     */
    @PostMapping(value = "/categories/delete")
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids) {
        LambdaQueryWrapper<Blog> blogLambdaQueryWrapper = new LambdaQueryWrapper<>();
        blogLambdaQueryWrapper.in(Blog::getBlogCategoryId, Arrays.asList(ids))
                .eq(Blog::getIsDeleted, 0);
        List<Blog> blogList = blogService.list(blogLambdaQueryWrapper);
        if (blogList.size() > 0) {
            return ResultGenerator.genFailResult("删除失败,该分类下有未删除的博客");
        }
        LambdaQueryWrapper<BlogCategory> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.in(BlogCategory::getCategoryId, Arrays.asList(ids));
        List<BlogCategory> blogCategoryList = categoryService.list(categoryLambdaQueryWrapper);
        for (int i = 0; i < blogCategoryList.size(); i++) {
            blogCategoryList.get(i).setIsDeleted(1);
        }
        if (categoryService.updateBatchById(blogCategoryList)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }

}
