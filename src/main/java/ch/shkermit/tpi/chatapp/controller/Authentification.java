package ch.shkermit.tpi.chatapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("api/auth")
public class Authentification {
    @PostMapping("login")
    public String login() {
        return "login";
    }

    @PostMapping("register")
    public String register() {
        return "register";
    }

    @GetMapping("logout")
    public String logout() {
        return "logout";
    }
}
