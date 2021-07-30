package com.novo.report.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.novo.report.beans.User;

/**
 * 登陆拦截器.
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    private static final String[] IGNORE_URI = {"/login", "/js/","/jquery/","/css/","/images/","/main"};
 
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean flag = false;
        String url = request.getRequestURL().toString();
        for (String s : IGNORE_URI) {
            if (url.contains(s)) {
                flag = true;
                break;
            }
        }
        if (!flag) {
        	User user = (User)request.getSession().getAttribute("user");
            if (user != null) {
            	flag = true;
            }else {
            	//重定向到登录页面
    			response.sendRedirect(request.getContextPath());
			}
        }
        return flag;
    }
 
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }
}
