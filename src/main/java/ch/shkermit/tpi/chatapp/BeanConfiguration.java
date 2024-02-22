package ch.shkermit.tpi.chatapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ch.shkermit.tpi.chatapp.service.TokenService;
import ch.shkermit.tpi.chatapp.service.UserService;
import ch.shkermit.tpi.chatapp.service.impl.DefaultTokenService;
import ch.shkermit.tpi.chatapp.service.impl.DefaultUserService;

@Configuration
public class BeanConfiguration {
    @Bean
	UserService userService() {
		return new DefaultUserService();
	}
}
	@Bean
	TokenService tokenService() {
		return new DefaultTokenService();
	}