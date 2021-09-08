package com.fr1nge.myblog.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fr1nge.myblog.entity.AdminUser;
import com.fr1nge.myblog.entity.BlogConfig;
import com.fr1nge.myblog.service.*;
import com.fr1nge.myblog.util.GetMD5;
import com.fr1nge.myblog.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Value("${jwt.token.name}")
    private String tokenName;

    @Value("${jwt.signing.key}")
    private String signingKey;

    @Resource
    private AdminUserService adminUserService;
    @Resource
    private BlogService blogService;
    @Resource
    private BlogCategoryService categoryService;
    @Resource
    private BlogLinkService linkService;
    @Resource
    private BlogTagService tagService;
    @Resource
    private BlogCommentService commentService;
    @Resource
    private BlogConfigService configService;

    @GetMapping({"/index"})
    public String index(HttpServletRequest request) {
        request.setAttribute("path", "index");
        request.setAttribute("categoryCount", categoryService.count());
        request.setAttribute("blogCount", blogService.count());
        request.setAttribute("linkCount", linkService.count());
        request.setAttribute("tagCount", tagService.count());
        request.setAttribute("commentCount", commentService.count());
        return "admin/index";
    }

    @GetMapping({"/login"})
    public String login() {
        return "admin/login";
    }

    @PostMapping(value = "/user/login")
    public String login(@RequestParam("userName") String userName,
                        @RequestParam("password") String password,
                        @RequestParam("verifyCode") String verifyCode,
                        HttpSession session) {
        String kaptchaCode = (String) session.getAttribute("verifyCode");
//        if (StringUtils.isEmpty(kaptchaCode) || !verifyCode.equals(kaptchaCode)) {
//            session.setAttribute("errorMsg", "验证码错误");
//            return "admin/login";
//        }
        QueryWrapper<AdminUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AdminUser::getLoginUserName, userName).eq(AdminUser::getLoginPassword, GetMD5.encryptString(password));
        AdminUser adminUser = adminUserService.getOne(wrapper);
        if (adminUser != null) {
            //登陆成功，将信息储存到session
            //储存用户信息
            session.setAttribute("loginUser", adminUser.getNickName());
            session.setAttribute("loginUserId", adminUser.getAdminUserId());
            //储存token
            String token = JwtUtil.generateToken(signingKey, adminUser.getNickName());
            session.setAttribute(tokenName, token);
            //储存配置信息
            List<BlogConfig> blogConfigList = configService.list();
            Map<String, String> configMap = blogConfigList.stream()
                    .collect(Collectors.toMap(BlogConfig::getConfigName, BlogConfig::getConfigValue));
            session.setAttribute("config", configMap);
            return "redirect:/admin/index";
        } else {
            session.setAttribute("errorMsg", "登陆失败");
            return "admin/login";
        }
    }

    @GetMapping("/user/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "admin/login";
    }


    @GetMapping("/profile")
    public String profile(HttpServletRequest request) {
        Integer loginUserId = (int) request.getSession().getAttribute("loginUserId");
        AdminUser adminUser = adminUserService.getById(loginUserId);
        if (adminUser == null) {
            return "admin/login";
        }
        request.setAttribute("path", "profile");
        request.setAttribute("loginUserName", adminUser.getLoginUserName());
        request.setAttribute("nickName", adminUser.getNickName());
        return "admin/profile";
    }

    @PostMapping("/profile/password")
    @ResponseBody
    public String passwordUpdate(HttpServletRequest request,
                                 @RequestParam("originalPassword") String originalPassword,
                                 @RequestParam("newPassword") String newPassword) {
        Integer loginUserId = (int) request.getSession().getAttribute("loginUserId");
        AdminUser adminUser = adminUserService.getById(loginUserId);

        //判断原来的密码是否正确
        if (StringUtils.equals(GetMD5.encryptString(originalPassword),adminUser.getLoginPassword())) {
            return "修改失败";
        }
        //修改密码
        adminUser.setLoginPassword(GetMD5.encryptString(newPassword));
        if (adminUserService.updateById(adminUser)) {
            //修改成功后清空session中的数据，前端控制跳转至登录页
            request.getSession().invalidate();
            return "success";
        } else {
            return "修改失败";
        }

    }

    @PostMapping("/profile/name")
    @ResponseBody
    public String nameUpdate(HttpServletRequest request,
                             @RequestParam("loginUserName") String loginUserName,
                             @RequestParam("nickName") String nickName) {
        Integer loginUserId = (int) request.getSession().getAttribute("loginUserId");
        AdminUser adminUser = adminUserService.getById(loginUserId);
        adminUser.setNickName(nickName).setLoginUserName(loginUserName);
        if (adminUserService.updateById(adminUser)) {
            request.getSession().setAttribute("loginUser", adminUser.getNickName());
            request.getSession().setAttribute("loginUserId", adminUser.getAdminUserId());
            return "success";
        } else {
            return "修改失败";
        }
    }

}
