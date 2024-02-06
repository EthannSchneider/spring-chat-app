package ch.shkermit.tpi.chatapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class ChatappApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ChatappApplication.class, args);
	}

}
