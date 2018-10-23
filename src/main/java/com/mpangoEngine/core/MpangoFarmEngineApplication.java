package com.mpangoEngine.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

//@SpringBootApplication // same as 
@Configuration 
@EnableAutoConfiguration 
@ComponentScan({"com.mpangoEngine"})
@SpringBootApplication
public class MpangoFarmEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(MpangoFarmEngineApplication.class, args);
	}
}
