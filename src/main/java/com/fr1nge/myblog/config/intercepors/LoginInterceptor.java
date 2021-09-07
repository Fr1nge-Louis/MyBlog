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

    //这个方法是在访问接口之前执行的，我们只需要在这里写验证登陆状态的业务逻辑，就可以在用户调用指定接口之前验证登陆状态了
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //每一个项目对于登陆的实现逻辑都有所区别，我这里使用最简单的Session提取User来验证登陆。
        HttpSession session = request.getSession();

        //这里的token是登陆时放入session的
        String token = JwtUtil.getSubject(request, tokenName, signingKey);

        log.debug("请求路径：" + request.getRequestURI());

        //如果session中没有user，表示没登陆
        if (token == null) {
            //这个方法返回false表示忽略当前请求，如果一个用户调用了需要登陆才能使用的接口，如果他没有登陆这里会直接忽略掉
            //当然你可以利用response给用户返回一些提示信息，告诉他没登陆
            log.debug("no session" + ",请求路径：" + request.getRequestURI());
            response.sendRedirect("/admin/login");
            return false;
        } else {
            //更新token
            String newToken = JwtUtil.generateToken(signingKey, (String) session.getAttribute("loginUser"));
            session.setAttribute(tokenName, newToken);

            log.debug("has session" + ",请求路径：" + request.getRequestURI());
            log.debug("session=" + session.getAttribute("loginUser"));
            log.debug("newToken=" + newToken);

            return true;    //如果session里有user，表示该用户已经登陆，放行，用户即可继续调用自己需要的接口
        }

    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, @Nullable ModelAndView modelAndView) throws Exception {

        //log.debug ("==========postHandle===========");
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, @Nullable Exception ex) throws Exception {

        //log.debug ("========afterCompletion========");
    }

}
