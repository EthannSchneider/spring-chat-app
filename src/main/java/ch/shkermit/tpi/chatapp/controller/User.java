package ch.shkermit.tpi.chatapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("api/user")
public class User {
    @GetMapping
    public String getUser(Principal principal) {
        return principal.getName();
    }
}
