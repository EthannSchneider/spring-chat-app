package ch.shkermit.tpi.chatapp.security;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
        UserSession session; 
        try {
            session = userSessionService.getSession(token.split(" ")[1]);
        } catch (Exception e) {
            throw new BadCredentialsException("Authentication Exception - Invalid Token");
        }

        Authentication newAuthentication = new CustomAuthentication(session, session.getUserInSession().getAuthorities());

        return newAuthentication;
    }

    class CustomAuthentication implements Authentication {
        private UserSession principal;
        private boolean authenticated;
        private Collection<? extends GrantedAuthority> authorities;

        public CustomAuthentication(UserSession userSession,  Collection<? extends GrantedAuthority> authorities) {
            this.principal = userSession;
            this.authorities = authorities;
            this.authenticated = true;
        }

        @Override
        public String getName() {
            return principal.getSessionUUID();
        }

        @Override
        public boolean isAuthenticated() {
            return authenticated;
        }

        @Override
        public Object getPrincipal() {
            return principal;
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
