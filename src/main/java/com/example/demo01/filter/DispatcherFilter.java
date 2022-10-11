//package com.example.demo01.filter;
//
//import com.example.demo01.entity.xmlToBean.Messages;
//import com.example.demo01.utils.AsyncUtils;
//import com.example.demo01.utils.RedisUtils;
//import com.example.demo01.utils.UUIDUtil;
//import com.fasterxml.jackson.dataformat.xml.XmlMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.util.StringUtils;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.time.Duration;
//@Slf4j
//public class DispatcherFilter implements Filter {
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        Filter.super.init(filterConfig);
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest req = (HttpServletRequest)request;
//        HttpServletResponse res=(HttpServletResponse)response;
//        //若使用 req.getRequestURI() 会报错 404
//        String servletPath = req.getServletPath();
//        if (StringUtils.hasText(servletPath)) {
//            //替换接口
//            log.info("sourceUrl : {}", servletPath);
//            String[] split = servletPath.split("/");
//            req.setAttribute("ChatbotURI",split[split.length-1]);
//            request.getRequestDispatcher("http://localhost:8888/xiuzhi/bj_mobile/test").forward(request, response);
//            // 重定向之后不能再将请求转发给过滤器链下一个filter，否则会抛异常
//            return;
//        }
//        chain.doFilter(request, response);
//
//    }
//
//    @Override
//    public void destroy() {
//        Filter.super.destroy();
//    }
//}
