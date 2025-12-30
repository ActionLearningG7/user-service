package com.medibridge.user_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
public class HomeController {

    @RequestMapping("/home")
    public String home() {
        return "User Service is up and running!";
    }
}
