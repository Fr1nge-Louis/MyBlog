package com.fr1nge.myblog.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fr1nge.myblog.entity.BlogCategory;
import com.fr1nge.myblog.service.BlogCategoryService;
import com.fr1nge.myblog.util.PageResult;
import com.fr1nge.myblog.util.Result;
import com.fr1nge.myblog.util.ResultGenerator;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class CategoryController {

    @Resource
    private BlogCategoryService categoryService;

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
    public Result list( @RequestParam(required = false) Integer page,
                        @RequestParam(required = false) Integer limit) {

        LambdaQueryWrapper<BlogCategory> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BlogCategory::getIsDeleted,0);
        if(page == null){
            page = 1;
        }
        if(limit == null){
            limit = 10;
        }
        Page<BlogCategory> pageQuery = new Page<>((page-1)*page,limit);

        IPage<BlogCategory> categoryIPage = categoryService.selectPage(pageQuery,lambdaQueryWrapper);
        PageResult pageResult =  new PageResult(categoryIPage.getRecords(),
                (int)categoryIPage.getTotal(),(int)categoryIPage.getSize(),(int)categoryIPage.getCurrent());
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
        queryWrapper.eq(BlogCategory::getCategoryName,categoryName);
        if(categoryService.getOne(queryWrapper) != null){
            return ResultGenerator.genFailResult("分类名称重复");
        }
        BlogCategory blogCategory = new BlogCategory();
        blogCategory.setCategoryName(categoryName).setCategoryIcon(categoryIcon);
        categoryService.save(blogCategory);
        return ResultGenerator.genSuccessResult();

    }


    /**
     * 分类修改
     */
    @RequestMapping(value = "/categories/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(@RequestParam("categoryId") Integer categoryId,
                         @RequestParam("categoryName") String categoryName,
                         @RequestParam("categoryIcon") String categoryIcon) {
        BlogCategory blogCategory = categoryService.getById(categoryId);
        blogCategory.setCategoryName(categoryName).setCategoryIcon(categoryIcon);
        if (categoryService.updateById(blogCategory)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("分类名称重复");
        }
    }


    /**
     * 分类删除
     */
    @PostMapping(value = "/categories/delete")
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (categoryService.removeByIds(Arrays.asList(ids))) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }

}
