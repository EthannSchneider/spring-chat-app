package ch.shkermit.tpi.chatapp.security;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import ch.shkermit.tpi.chatapp.model.UserSession;
import ch.shkermit.tpi.chatapp.service.UserSessionService;

public class CustomAuthenticationManager implements AuthenticationManager {
    @Autowired
    private UserSessionService userSessionService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = authentication.getName();
        UserSession session = userSessionService.getSession(token.split(" ")[1]);

        Authentication newAuthentication = new CustomAuthentication(session.getUserInSession().getUsername(), session.getUserInSession().getAuthorities());

        return newAuthentication;
    }

    class CustomAuthentication implements Authentication {
        private String name;
        private boolean authenticated;
        private Collection<? extends GrantedAuthority> authorities;

        public CustomAuthentication(String name, Collection<? extends GrantedAuthority> authorities) {
            this.name = name;
            this.authorities = authorities;
            this.authenticated = true;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public boolean isAuthenticated() {
            return authenticated;
        }

        @Override
        public Object getPrincipal() {
            return name;
        }

        @Override
        public Object getCredentials() {
            return null;
        }

        @Override
        public Object getDetails() {
            return null;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorities;
        }

        @Override
        public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
            this.authenticated = isAuthenticated;
        }
    }
    
}
