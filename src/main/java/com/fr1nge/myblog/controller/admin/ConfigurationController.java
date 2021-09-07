package com.fr1nge.myblog.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fr1nge.myblog.entity.BlogConfig;
import com.fr1nge.myblog.service.BlogConfigService;
import com.fr1nge.myblog.util.Result;
import com.fr1nge.myblog.util.ResultGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class ConfigurationController {

    @Resource
    private BlogConfigService configService;

    @GetMapping("/configurations")
    public String list(HttpServletRequest request) {
        request.setAttribute("path", "configurations");
        request.setAttribute("configurations", configService.list());
        return "admin/configuration";
    }

    @PostMapping("/configurations/website")
    @ResponseBody
    public Result website(@RequestParam(value = "websiteName", required = false) String websiteName,
                          @RequestParam(value = "websiteDescription", required = false) String websiteDescription,
                          @RequestParam(value = "websiteLogo", required = false) String websiteLogo,
                          @RequestParam(value = "websiteIcon", required = false) String websiteIcon) {
        List<BlogConfig> blogConfigList = new ArrayList<>();
        if (StringUtils.isNotBlank(websiteName)) {
            blogConfigList.add(getByName("websiteName"));
        }
        if (StringUtils.isNotBlank(websiteDescription)) {
            blogConfigList.add(getByName("websiteDescription"));
        }
        if (StringUtils.isNotBlank(websiteLogo)) {
            blogConfigList.add(getByName("websiteLogo"));
        }
        if (StringUtils.isNotBlank(websiteIcon)) {
            blogConfigList.add(getByName("websiteIcon"));
        }
        boolean flag = true;
        if (!blogConfigList.isEmpty()) {
            flag = configService.updateBatchById(blogConfigList);
        }
        return ResultGenerator.genSuccessResult(flag);
    }

    @PostMapping("/configurations/userInfo")
    @ResponseBody
    public Result userInfo(@RequestParam(value = "yourAvatar", required = false) String yourAvatar,
                           @RequestParam(value = "yourName", required = false) String yourName,
                           @RequestParam(value = "yourEmail", required = false) String yourEmail) {
        List<BlogConfig> blogConfigList = new ArrayList<>();
        if (StringUtils.isNotBlank(yourAvatar)) {
            blogConfigList.add(getByName("yourAvatar"));
        }
        if (StringUtils.isNotBlank(yourName)) {
            blogConfigList.add(getByName("yourName"));
        }
        if (StringUtils.isNotBlank(yourEmail)) {
            blogConfigList.add(getByName("yourEmail"));
        }
        boolean flag = true;
        if (!blogConfigList.isEmpty()) {
            flag = configService.updateBatchById(blogConfigList);
        }
        return ResultGenerator.genSuccessResult(flag);
    }

    @PostMapping("/configurations/footer")
    @ResponseBody
    public Result footer(@RequestParam(value = "footerAbout", required = false) String footerAbout,
                         @RequestParam(value = "footerICP", required = false) String footerICP,
                         @RequestParam(value = "footerCopyRight", required = false) String footerCopyRight,
                         @RequestParam(value = "footerPoweredBy", required = false) String footerPoweredBy,
                         @RequestParam(value = "footerPoweredByURL", required = false) String footerPoweredByURL) {
        List<BlogConfig> blogConfigList = new ArrayList<>();
        if (StringUtils.isNotBlank(footerAbout)) {
            blogConfigList.add(getByName("footerAbout"));
        }
        if (StringUtils.isNotBlank(footerICP)) {
            blogConfigList.add(getByName("footerICP"));
        }
        if (StringUtils.isNotBlank(footerCopyRight)) {
            blogConfigList.add(getByName("footerCopyRight"));
        }
        if (StringUtils.isNotBlank(footerPoweredBy)) {
            blogConfigList.add(getByName("footerPoweredBy"));
        }
        if (StringUtils.isNotBlank(footerPoweredByURL)) {
            blogConfigList.add(getByName("footerPoweredByURL"));
        }
        boolean flag = true;
        if (!blogConfigList.isEmpty()) {
            flag = configService.updateBatchById(blogConfigList);
        }
        return ResultGenerator.genSuccessResult(flag);
    }

    /**
     * 通过配置名称获取其实体类
     * @param name 配置名称
     * @return BlogConfig
     */
    private BlogConfig getByName(String name) {
        LambdaQueryWrapper<BlogConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlogConfig::getConfigName, name);
        return configService.getOne(queryWrapper);
    }

}
