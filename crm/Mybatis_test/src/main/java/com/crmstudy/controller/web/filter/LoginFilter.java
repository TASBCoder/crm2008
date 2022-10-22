package com.crmstudy.controller.web.filter;

import com.crmstudy.commons.constans.Constant;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    private String excludedPage;
    private String[] excludedPages;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //读取初始化配置属性
        excludedPage = filterConfig.getInitParameter("excludedPages");
        if (excludedPage != null && excludedPage.length() > 0){
            excludedPages = excludedPage.split(",");
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        HttpSession session = request.getSession(false);
        String servletPath = request.getServletPath();

        //不拦截 /settings/qx/user/toLogin 和 /settings/qx/user/toIndex
        for (String page : excludedPages) {
            //如果请求的路径是[/settings/qx/user/toLogin,/settings/qx/user/toIndex]或者是正常登录的话就放行
            if((page.equals(servletPath)) || (session != null & session.getAttribute(Constant.SESSION_USER) != null)){
                filterChain.doFilter(request,response);
            }else{
                response.sendRedirect(request.getContextPath());
            }
        }
    }

    @Override
    public void destroy() {

    }
}
