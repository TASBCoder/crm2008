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
            System.out.println(excludedPages[1]);
            System.out.println(excludedPages[0]);
            System.out.println("执行到这里了"+ servletPath);
            System.out.println("此时的page是："+ page);
            if(page.equals(servletPath)){
                System.out.println("这个登录路径 = "+ servletPath + "过去了");
                filterChain.doFilter(request,response);
                break;
            }else{
                //登录验证，判断是否属于正常登录
                if(session != null & session.getAttribute(Constant.SESSION_USER) != null){
                    System.out.println("我在获取session");
                    //正常登录
                    filterChain.doFilter(request,response);
                }else{
                    //重定向到登录页面
                    System.out.println("我没有获取到session");
                    response.sendRedirect(request.getContextPath());
                }
            }

        }


    }

    @Override
    public void destroy() {

    }
}
