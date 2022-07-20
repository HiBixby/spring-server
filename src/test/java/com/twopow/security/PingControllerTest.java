package com.twopow.security;


import com.twopow.security.config.SecurityConfig;
import com.twopow.security.controller.PingController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = PingController.class, excludeFilters = { //!Added!
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
public class PingControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser
    public void pong이_리턴된다() throws Exception {
        String pong = "pong";
        mvc.perform(get("/ping"))
                .andExpect(status().isOk())
                .andExpect(content().string(pong));

    }

    @Test
    @WithMockUser
    public void admin_pong이_리턴된다() throws Exception{
        String admin_pong = "admin pong";
        mvc.perform(get("/admin/ping"))
                .andExpect(status().isOk())
                .andExpect(content().string(admin_pong));
    }
}