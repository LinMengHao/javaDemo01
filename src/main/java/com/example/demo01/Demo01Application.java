package com.example.demo01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class Demo01Application {

	public static void main(String[] args) {
		SpringApplication.run(Demo01Application.class, args);
	}

}
