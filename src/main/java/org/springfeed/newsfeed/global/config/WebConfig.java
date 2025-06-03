package org.springfeed.newsfeed.global.config;

import org.springfeed.newsfeed.global.filter.JwtFilter;
import org.springfeed.newsfeed.global.jwt.blacklist.BlacklistManager;
import org.springfeed.newsfeed.global.jwt.JwtUtil;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilter(JwtUtil jwtUtil, BlacklistManager blacklistManager) {
        FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new JwtFilter(jwtUtil, blacklistManager));
        registrationBean.setOrder(1);
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }
}
