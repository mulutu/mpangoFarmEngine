package com.mpangoEngine.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

import org.springframework.format.FormatterRegistry;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.mpangoEngine.core.util.formatters.AmountFormatter;

@Configuration
@EnableTransactionManagement
class MyConfig implements WebMvcConfigurer {
	

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addFormatter(new AmountFormatter());
	}

	@Bean
	public JavaMailSenderImpl mailSender() {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

		javaMailSender.setProtocol("SMTP");
		javaMailSender.setHost("127.0.0.1");
		javaMailSender.setPort(25);

		return javaMailSender;
	}	
	
	/*
	@Bean
	public JavaMailSender getJavaMailSender() {
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost("smtp.gmail.com");
	    mailSender.setPort(587);
	     
	    mailSender.setUsername("my.gmail@gmail.com");
	    mailSender.setPassword("password");
	     
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.debug", "true");
	     
	    return mailSender;
	}*/
}