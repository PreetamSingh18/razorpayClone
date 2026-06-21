package com.preesa.razorpay;

import org.apache.logging.log4j.spi.MutableThreadContextStack;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RazorpayApplication {

	public static void main(String[] args) {

		SpringApplication.run(RazorpayApplication.class, args);
	}

}