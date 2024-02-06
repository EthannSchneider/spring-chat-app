package ch.shkermit.tpi.chatapp.security;

import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;

import jakarta.servlet.http.HttpServletRequest;

public class CustomBearerTokenResolver implements BearerTokenResolver {
    @Override
    public String resolve(HttpServletRequest request) {
        System.out.println("CustomBearerTokenResolver.resolve() called");
        System.out.println(request.getHeader("Authorization"));
        return request.getHeader("Authorization");
    }
}
