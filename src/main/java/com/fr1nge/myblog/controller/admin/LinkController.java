package com.fr1nge.myblog.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fr1nge.myblog.entity.BlogLink;
import com.fr1nge.myblog.service.BlogLinkService;
import com.fr1nge.myblog.util.PageResult;
import com.fr1nge.myblog.util.Result;
import com.fr1nge.myblog.util.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin")
public class LinkController {

    @Resource
    private BlogLinkService linkService;

    @GetMapping("/links")
    public String linkPage(HttpServletRequest request) {
        request.setAttribute("path", "links");
        return "admin/link";
    }

    @GetMapping("/links/list")
    @ResponseBody
    public Result list(@RequestParam(required = false) Integer page,
                       @RequestParam(required = false) Integer limit) {
        if (page == null) {
            page = 1;
        }
        if (limit == null) {
            limit = 10;
        }
        LambdaQueryWrapper<BlogLink> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                //.eq(BlogLink::getIsDeleted,0)
                .orderByAsc(BlogLink::getLinkRank);
        Page<BlogLink> pageQuery = new Page<>(page, limit);
        IPage<BlogLink> linkIPage = linkService.selectPage(pageQuery, queryWrapper);
        PageResult pageResult = new PageResult(linkIPage.getRecords(),
                (int) linkIPage.getTotal(), (int) linkIPage.getSize(), page);
        return ResultGenerator.genSuccessResult(pageResult);
    }

    /**
     * 友链添加
     */
    @RequestMapping(value = "/links/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(@RequestParam("linkType") Integer linkType,
                       @RequestParam("linkName") String linkName,
                       @RequestParam("linkUrl") String linkUrl,
                       @RequestParam("linkRank") Integer linkRank,
                       @RequestParam("linkDescription") String linkDescription) {

        BlogLink link = new BlogLink();
        link.setLinkType(linkType)
                .setLinkRank(linkRank)
                .setLinkName(linkName)
                .setLinkUrl(linkUrl)
                .setLinkDescription(linkDescription);
        return ResultGenerator.genSuccessResult(linkService.save(link));
    }

    /**
     * 详情
     */
    @GetMapping("/links/info/{id}")
    @ResponseBody
    public Result info(@PathVariable("id") Integer id) {
        BlogLink link = linkService.getById(id);
        return ResultGenerator.genSuccessResult(link);
    }

    /**
     * 友链修改
     */
    @RequestMapping(value = "/links/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(@RequestParam("linkId") Integer linkId,
                         @RequestParam("linkType") Integer linkType,
                         @RequestParam("linkName") String linkName,
                         @RequestParam("linkUrl") String linkUrl,
                         @RequestParam("linkRank") Integer linkRank,
                         @RequestParam("linkDescription") String linkDescription) {
        BlogLink tempLink = linkService.getById(linkId);
        if (tempLink == null) {
            return ResultGenerator.genFailResult("无数据！");
        }
        tempLink.setLinkType(linkType)
                .setLinkRank(linkRank)
                .setLinkName(linkName)
                .setLinkUrl(linkUrl)
                .setLinkDescription(linkDescription);
        return ResultGenerator.genSuccessResult(linkService.updateById(tempLink));
    }

    /**
     * 友链删除
     */
    @RequestMapping(value = "/links/delete", method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids) {
        LambdaQueryWrapper<BlogLink> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(BlogLink::getLinkId, Arrays.asList(ids));
        List<BlogLink> blogLinkList = linkService.list(queryWrapper);
        for (int i = 0; i < blogLinkList.size(); i++) {
            blogLinkList.get(i).setIsDeleted(1);
        }
        if (linkService.updateBatchById(blogLinkList)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }

}
