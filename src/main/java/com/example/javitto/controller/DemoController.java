package com.example.javitto.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DemoController {

    @GetMapping("/hello")
    @PreAuthorize("hasRole('client_user')")
    public String hello() {
        return "hello from SB & KC";
    }

    @GetMapping("/helloAdmin")
    @PreAuthorize("hasRole('client_admin')")
    public String hello2() {
        return "hello from SB & KC + ADMIN!";
    }
}
