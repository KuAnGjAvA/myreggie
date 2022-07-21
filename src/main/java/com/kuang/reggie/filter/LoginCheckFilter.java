package com.kuang.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.kuang.reggie.common.R;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(value = "LoginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        filterChain.doFilter(request,response);

//        //获取本次请求
//        String requestURI = request.getRequestURI();
//
//        //记录需要不需要拦截的路径
//        String[] urls = {
//                "/employee/login/",
//                "/employee/logout/",
//                "/backend/**",
//                "/front/**"
//        };
//
//        //判断是否放行
//        boolean b = checkFilter(requestURI, urls);
//
//        //如果不需要处理
//        if (b) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        //判断是否登录
//        if (request.getSession().getAttribute("loginEmployee") != null) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        //没有登录
//        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
//        return;

    }

//    /**
//     * 判断是否放行
//     *
//     * @param requestURL
//     * @param urls
//     * @return
//     */
//    public static boolean checkFilter(String requestURL, String[] urls) {
//        for (String url : urls) {
//            if (PATH_MATCHER.match(url, requestURL)) {
//                return true;
//            }
//        }
//        return false;
//    }
}
