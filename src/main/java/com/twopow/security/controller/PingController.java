package com.twopow.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {
    @GetMapping("/ping")
    public String Hello() {
        return "pong";
    }

    @GetMapping("/admin/ping")
    public String AdminHello() {
        return "admin pong";
    }
}