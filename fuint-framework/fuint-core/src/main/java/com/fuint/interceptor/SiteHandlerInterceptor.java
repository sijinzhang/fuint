package com.fuint.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器
 *
 * Created by FSQ
 * Contact wx fsq_better
 * Site https://www.fuint.cn
 */
public class SiteHandlerInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private Environment env;

    public SiteHandlerInterceptor(Environment env) {
        this.env = env;
    }

    /**
     * 业务请求之前调用
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return true;
    }

    /**
     * 业务请求执行完成后,生成试图之前调用
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        if (modelAndView != null) {
            try {
                String systemName = new String((env.getProperty("system.name")).getBytes("iso-8859-1"), "utf-8");
                Long randNow = System.currentTimeMillis();
                modelAndView.addObject("systemName", systemName);//系统名称
                modelAndView.addObject("randNow", randNow);//当前时间毫秒数的long型数字
            } catch (Exception e) {
                throw new RuntimeException("参数读取错误,{}", e);
            }
        }
    }

    /**
     * 在DispatcherServlet完全处理完请求后被调用,可用于清理资源等
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
       return;
    }
}
