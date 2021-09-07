package com.fr1nge.myblog.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fr1nge.myblog.entity.AdminUser;
import com.fr1nge.myblog.service.*;
import com.fr1nge.myblog.util.GetMD5;
import com.fr1nge.myblog.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


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
        String kaptchaCode = session.getAttribute("verifyCode") + "";
        if (StringUtils.isEmpty(kaptchaCode) || !verifyCode.equals(kaptchaCode)) {
            session.setAttribute("errorMsg", "验证码错误");
            return "admin/login";
        }
        QueryWrapper<AdminUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AdminUser::getLoginUserName, userName).eq(AdminUser::getLoginPassword, GetMD5.encryptString(password));
        AdminUser adminUser = adminUserService.getOne(wrapper);
        if (adminUser != null) {
            session.setAttribute("loginUser", adminUser.getNickName());
            session.setAttribute("loginUserId", adminUser.getAdminUserId());
            String token = JwtUtil.generateToken(signingKey, adminUser.getNickName());
            log.info("token=" + token);
            session.setAttribute(tokenName, token);
            return "redirect:/admin/index";
        } else {
            session.setAttribute("errorMsg", "登陆失败");
            return "admin/login";
        }
    }

    @GetMapping("/user/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute("loginUserId");
        request.getSession().removeAttribute("loginUser");
        request.getSession().removeAttribute("errorMsg");
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
        if (!adminUser.getLoginPassword().equals(GetMD5.encryptString(originalPassword))) {
            return "修改失败";
        }
        //修改密码
        adminUser.setLoginPassword(GetMD5.encryptString(newPassword));
        if (adminUserService.updateById(adminUser)) {
            //修改成功后清空session中的数据，前端控制跳转至登录页
            request.getSession().removeAttribute("loginUserId");
            request.getSession().removeAttribute("loginUser");
            request.getSession().removeAttribute("errorMsg");
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
            return "success";
        } else {
            return "修改失败";
        }
    }

}
