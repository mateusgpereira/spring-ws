package com.springws.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SpringWsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringWsApplication.class, args);
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordencoder() {
		return new BCryptPasswordEncoder();
	}

}
