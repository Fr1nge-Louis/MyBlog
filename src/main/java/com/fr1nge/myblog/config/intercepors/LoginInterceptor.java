package com.fr1nge.myblog.config.intercepors;

import com.fr1nge.myblog.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Value("${jwt.token.name}")
    private String tokenName;

    @Value("${jwt.signing.key}")
    private String signingKey;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        //获取session中的token
        String token = JwtUtil.getSubject(request, tokenName, signingKey);

        //token失效后，需要重新登陆
        if (token == null) {
            //把session设置为失效
            session.invalidate();
            log.info("token is invalid" + ",请求路径：" + request.getRequestURI());
            response.sendRedirect("/admin/login");
            return false;
        } else {
            log.info("token is valid" + ",请求路径：" + request.getRequestURI());
            log.info("loginUser=" + session.getAttribute("loginUser"));
            log.info("create newToken begin");
            //更新token
            String newToken = JwtUtil.generateToken(signingKey, (String) session.getAttribute("loginUser"));
            session.setAttribute(tokenName, newToken);
            log.info("create newToken end");
            return true;
        }

    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, @Nullable ModelAndView modelAndView) throws Exception {

        //log.info ("==========postHandle===========");
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, @Nullable Exception ex) throws Exception {

        //log.info ("========afterCompletion========");
    }

}
