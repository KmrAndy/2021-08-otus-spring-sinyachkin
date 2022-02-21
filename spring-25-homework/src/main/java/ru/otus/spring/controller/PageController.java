package ru.otus.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PageController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/accessdenied")
    public String getAccessDenied() {
        return "accessdenied";
    }

    @PostMapping("/accessdenied")
    public String postAccessDenied() {
        return "accessdenied";
    }
}
