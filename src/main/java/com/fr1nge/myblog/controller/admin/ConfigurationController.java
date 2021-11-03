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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class ConfigurationController {

    @Resource
    private BlogConfigService configService;

    @GetMapping("/configurations")
    public String list(HttpServletRequest request) {
        List<BlogConfig> blogConfigList = configService.list();
        Map<String, String> configMap = blogConfigList.stream()
                .collect(Collectors.toMap(BlogConfig::getConfigName, BlogConfig::getConfigValue));
        request.setAttribute("path", "configurations");
        request.setAttribute("configurations", configMap);
        return "admin/configuration";
    }

    @PostMapping("/configurations/website")
    @ResponseBody
    public Result website(@RequestParam(value = "websiteName", required = false) String websiteName,
                          @RequestParam(value = "websiteDescription", required = false) String websiteDescription,
                          @RequestParam(value = "websiteLogo", required = false) String websiteLogo,
                          @RequestParam(value = "websiteIcon", required = false) String websiteIcon,
                          @RequestParam(value = "backgroudImg", required = false) String backgroudImg) {
        List<BlogConfig> blogConfigList = new ArrayList<>();
        if (StringUtils.isNotBlank(websiteName)) {
            BlogConfig blogConfig = getByName("websiteName", websiteName);
            if (blogConfig != null) {
                blogConfigList.add(blogConfig);
            }
        }
        if (StringUtils.isNotBlank(websiteDescription)) {
            BlogConfig blogConfig = getByName("websiteDescription", websiteDescription);
            if (blogConfig != null) {
                blogConfigList.add(blogConfig);
            }
        }
        if (StringUtils.isNotBlank(websiteLogo)) {
            BlogConfig blogConfig = getByName("websiteLogo", websiteLogo);
            if (blogConfig != null) {
                blogConfigList.add(blogConfig);
            }
        }
        if (StringUtils.isNotBlank(websiteIcon)) {
            BlogConfig blogConfig = getByName("websiteIcon", websiteIcon);
            if (blogConfig != null) {
                blogConfigList.add(blogConfig);
            }
        }
        if (StringUtils.isNotBlank(backgroudImg)) {
            BlogConfig blogConfig = getByName("backgroudImg", backgroudImg);
            if (blogConfig != null) {
                blogConfigList.add(blogConfig);
            }
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
                           @RequestParam(value = "yourCareer", required = false) String yourCareer,
                           @RequestParam(value = "yourEmail", required = false) String yourEmail) {
        List<BlogConfig> blogConfigList = new ArrayList<>();
        if (StringUtils.isNotBlank(yourAvatar)) {
            BlogConfig blogConfig = getByName("yourAvatar", yourAvatar);
            if (blogConfig != null) {
                blogConfigList.add(blogConfig);
            }
        }
        if (StringUtils.isNotBlank(yourName)) {
            BlogConfig blogConfig = getByName("yourName", yourName);
            if (blogConfig != null) {
                blogConfigList.add(blogConfig);
            }
        }
        if (StringUtils.isNotBlank(yourCareer)) {
            BlogConfig blogConfig = getByName("yourCareer", yourCareer);
            if (blogConfig != null) {
                blogConfigList.add(blogConfig);
            }
        }
        if (StringUtils.isNotBlank(yourEmail)) {
            BlogConfig blogConfig = getByName("yourEmail", yourEmail);
            if (blogConfig != null) {
                blogConfigList.add(blogConfig);
            }
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
            BlogConfig blogConfig = getByName("footerAbout", footerAbout);
            if (blogConfig != null) {
                blogConfigList.add(blogConfig);
            }
        }
        if (StringUtils.isNotBlank(footerICP)) {
            BlogConfig blogConfig = getByName("footerICP", footerICP);
            if (blogConfig != null) {
                blogConfigList.add(blogConfig);
            }
        }
        if (StringUtils.isNotBlank(footerCopyRight)) {
            BlogConfig blogConfig = getByName("footerCopyRight", footerCopyRight);
            if (blogConfig != null) {
                blogConfigList.add(blogConfig);
            }
        }
        if (StringUtils.isNotBlank(footerPoweredBy)) {
            BlogConfig blogConfig = getByName("footerPoweredBy", footerPoweredBy);
            if (blogConfig != null) {
                blogConfigList.add(blogConfig);
            }
        }
        if (StringUtils.isNotBlank(footerPoweredByURL)) {
            BlogConfig blogConfig = getByName("footerPoweredByURL", footerPoweredByURL);
            if (blogConfig != null) {
                blogConfigList.add(blogConfig);
            }
        }
        boolean flag = true;
        if (!blogConfigList.isEmpty()) {
            flag = configService.updateBatchById(blogConfigList);
        }
        return ResultGenerator.genSuccessResult(flag);
    }

    /**
     * 通过配置名称获取其实体类
     *
     * @param name 配置名称
     * @return BlogConfig
     */
    private BlogConfig getByName(String name, String value) {
        LambdaQueryWrapper<BlogConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlogConfig::getConfigName, name);
        BlogConfig blogConfig = configService.getOne(queryWrapper);
        if (StringUtils.equals(blogConfig.getConfigValue(), value)) {
            return null;
        }
        blogConfig.setConfigValue(value).setUpdateTime(new Date());
        return blogConfig;
    }

}
