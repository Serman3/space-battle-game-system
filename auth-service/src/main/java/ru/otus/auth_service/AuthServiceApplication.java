package ru.otus.auth_service;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableFeignClients
@ComponentScan(basePackages = {
		"ru.otus.auth_service.*",
		"ru.otus.shared.*"
})
@EntityScan(basePackages = {
		"ru.otus.auth_service.datasource.entity",
		"ru.otus.shared.datasource.entity"
})
@EnableJpaRepositories(basePackages = {
		"ru.otus.auth_service.datasource.repository",
		"ru.otus.shared.datasource.repository"
})
@SpringBootApplication
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
