package cn.com.szht.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


public class CommonInterceptor extends HandlerInterceptorAdapter{  

    @Override    
    public boolean preHandle(HttpServletRequest request,    
            HttpServletResponse response, Object handler) throws Exception {    
        if ("GET".equalsIgnoreCase(request.getMethod())) {  
        }  
        //System.out.println("拦截器==============执行顺序: 1、preHandle================");    
        String requestUri = request.getRequestURI();  
        String contextPath = request.getContextPath();  
        String url = requestUri.substring(contextPath.length());  
        //判断是否为ajax请求
        String type = request.getHeader("X-Requested-With");  
        System.out.println("requestUri:"+requestUri);    
        System.out.println("contextPath:"+contextPath);    
        System.out.println("url:"+url);      
        System.out.println("type:"+type);    
          
        String username =  (String)request.getSession().getAttribute("user");   
        if(username == null){  
            //System.out.println("Interceptor：跳转到login页面！");  
            //request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);  
            //request.getRequestDispatcher("hello").forward(request, response);  
            //return false;  
            
            
            return true;
        }else{  
            return true;
        }
        
    }    
    

    @Override    
    public void postHandle(HttpServletRequest request,    
            HttpServletResponse response, Object handler,    
            ModelAndView modelAndView) throws Exception {     

        String type = request.getHeader("X-Requested-With");  
        if (!"XMLHttpRequest".equals(type)){
        }
    }    
    
  
    @Override    
    public void afterCompletion(HttpServletRequest request,    
            HttpServletResponse response, Object handler, Exception ex)    
            throws Exception {    
        //System.out.println("拦截器==============执行顺序: 3、afterCompletion================"); 
    	
    }    
  
}
