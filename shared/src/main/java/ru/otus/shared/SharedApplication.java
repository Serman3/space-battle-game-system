package ru.otus.shared;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "ru.otus.shared.*"
})
public class SharedApplication {

    public static void main(String[] args) {
        SpringApplication.run(SharedApplication.class, args);
    }
}