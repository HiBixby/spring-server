/*package com.twopow.security.config;

import com.twopow.security.filter.MyFilter1;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<MyFilter1>filter1(){
        FilterRegistrationBean<MyFilter1>bean=new FilterRegistrationBean<>(new MyFilter1());
        bean.addUrlPatterns("/*");
        bean.setOrder(0);//낮은 순서가 먼저 처리됨.
        return bean;
    }
}*/
