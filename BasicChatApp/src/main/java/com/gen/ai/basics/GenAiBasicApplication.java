package com.gen.ai.basics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:/config/application.properties")
public class GenAiBasicApplication {

    public static void main(String[] args) {
        SpringApplication.run(GenAiBasicApplication.class, args);
    }

}
