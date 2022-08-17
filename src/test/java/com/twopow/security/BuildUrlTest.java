package com.twopow.security;


import com.twopow.security.config.SecurityConfig;
import com.twopow.security.controller.PingController;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = PingController.class, excludeFilters = { //!Added!
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
public class BuildUrlTest {

    @Test
    public void build_url_test() throws Exception {
        String originUrl = UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host("localhost")
                .port(null)
                .path("")
                .encode()
                .build()
                .toUriString();
        log.trace(originUrl);
    }
}