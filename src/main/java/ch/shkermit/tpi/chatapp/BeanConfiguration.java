package ch.shkermit.tpi.chatapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

import ch.shkermit.tpi.chatapp.service.TokenService;
import ch.shkermit.tpi.chatapp.service.UserService;
import ch.shkermit.tpi.chatapp.service.UserSessionService;
import ch.shkermit.tpi.chatapp.service.impl.DefaultTokenService;
import ch.shkermit.tpi.chatapp.service.impl.DefaultUserService;
import ch.shkermit.tpi.chatapp.service.impl.DefaultUserSessionService;

@Configuration
public class BeanConfiguration {
    @Bean
	UserService userService() {
		return new DefaultUserService();
	}

	@Bean
	UserSessionService userSessionService() {
		return new DefaultUserSessionService();
	}

	@Bean
	TokenService tokenService() {
		return new DefaultTokenService();
	}

	@Bean
	ProjectionFactory projectionFactory() {
		return new SpelAwareProxyProjectionFactory();
	}
}