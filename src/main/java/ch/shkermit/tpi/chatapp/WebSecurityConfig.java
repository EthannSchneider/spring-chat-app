package ch.shkermit.tpi.chatapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import ch.shkermit.tpi.chatapp.security.CustomAuthenticationManager;
import ch.shkermit.tpi.chatapp.security.CustomBearerTokenResolver;
import ch.shkermit.tpi.chatapp.security.CustomJwtDecoder;

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
        AuthenticationManager authenticationManager() {
                return new CustomAuthenticationManager();
        }

        @Configuration
        public class apiWebSecurityConfig {
                @Bean
                @Order(0)
                SecurityFilterChain apiWebSecurity(HttpSecurity http) throws Exception {
                        http
                        .csrf(csrf -> csrf.disable())
                        .authenticationManager(authenticationManager())
                        .authorizeHttpRequests(authorizeRequest ->
                                authorizeRequest
                                .requestMatchers("/api/auth/login", "/api/auth/register", "/error").permitAll()
                                .anyRequest().hasAuthority("USER")
                        )
                        .oauth2ResourceServer(
                                oauth2 -> oauth2.bearerTokenResolver(bearerTokenResolver()).jwt(jwt -> jwt.decoder(jwtDecoder()))
                        );
                        return http.build();
                }
        }

        @Bean
        WebMvcConfigurer corsConfigurer() {
                return new WebMvcConfigurer() {
                        @Override
                        public void addCorsMappings(@SuppressWarnings("null") CorsRegistry registry) {
                                registry.addMapping("/**")
                                        .allowedOriginPatterns("http://localhost:*")
                                        .allowedMethods("*")
                                        .maxAge(3600)
                                        .allowedHeaders("*")
                                        .exposedHeaders("*")
                                        .allowCredentials(true);
                        }
                };
        }
}
