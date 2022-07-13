package com.twopow.security.controller;

import com.twopow.security.config.auth.PrincipalDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

@RestController
public class RestLoginController {
    @GetMapping("/hello")
    public String Hello() {
        return "hello";
    }

    @GetMapping("/afterLogin")
    public void afterLogin(HttpServletResponse response, Authentication authentication) throws IOException {

        if (true) {
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            String role = principalDetails.getUser().getRole();

            if (Objects.equals(role, "ROLE_USER")) {
                int status=308;
                response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
                response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
                response.setHeader("Location", "http://localhost:3000/Register");
                response.setHeader("Connection", "close");
            } else if (Objects.equals(role, "ROLE_VERIFIED")) {
                response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setHeader("Location", "http://localhost:3000/Interview");
                response.setHeader("Connection", "close");
            } else {
                response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setHeader("Location", "http://localhost:3000");
                response.setHeader("Connection", "close");
            }
        }else{
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
