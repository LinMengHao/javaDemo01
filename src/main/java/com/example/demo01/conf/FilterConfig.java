package com.example.demo01.conf;


import com.example.demo01.filter.VerifyFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
//    @Bean
//    public FilterRegistrationBean registerMyFilter(){
//        FilterRegistrationBean<VerifyFilter> bean = new FilterRegistrationBean<>();
//        bean.setOrder(1);
//        bean.setFilter(new VerifyFilter());
//        bean.addUrlPatterns("/xiuzhi/bj_mobile/MsgSync/oc/v1/status");
//        return bean;
//    }
//    @Bean
//    public FilterRegistrationBean registerMsgMOFilter(){
//        FilterRegistrationBean<DispatcherFilter> bean = new FilterRegistrationBean<>();
//        bean.setOrder(1);
//        bean.setFilter(new DispatcherFilter());
//        bean.addUrlPatterns("/xiuzhi/bj_mobile/Media/MsgMO/*");
//        return bean;
//    }
}
