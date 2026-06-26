package com.preesa.razorpay;

import org.apache.logging.log4j.spi.MutableThreadContextStack;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RazorpayApplication {

	public static void main(String[] args) {

		SpringApplication.run(RazorpayApplication.class, args);
	}

}