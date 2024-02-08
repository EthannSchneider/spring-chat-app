package ch.shkermit.tpi.chatapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;

import ch.shkermit.tpi.chatapp.security.CustomBearerTokenResolver;
import ch.shkermit.tpi.chatapp.security.CustomJwtDecoder;
import ch.shkermit.tpi.chatapp.security.UserIdentityService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
        @Bean
        PasswordEncoder passwordEncoder() {
                return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }

        @Bean
        BearerTokenResolver bearerTokenResolver() {
                CustomBearerTokenResolver bearerTokenResolver = new CustomBearerTokenResolver(); 
                return bearerTokenResolver;
        }

        @Bean
        JwtDecoder jwtDecoder() {
                return new CustomJwtDecoder();
        }

        @Bean
        UserIdentityService userIdentityService() {
                return new UserIdentityService();
        }

        @Configuration
        public class apiWebSecurityConfig {
                @Bean
                @Order(0)
                SecurityFilterChain apiWebSecurity(HttpSecurity http) throws Exception {
                        http
                        .csrf(csrf -> csrf.disable())
                        .authorizeHttpRequests(authorizeRequest ->
                        authorizeRequest
                                .requestMatchers("/api/auth/**").permitAll()
                                .anyRequest().hasAuthority("user"))
                        .oauth2ResourceServer(oauth2 -> oauth2.bearerTokenResolver(bearerTokenResolver()).jwt(jwt -> jwt.decoder(jwtDecoder())));
                        return http.build();
                }
        }
}
