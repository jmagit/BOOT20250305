package com.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoMvcApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DemoMvcApplication.class, args);
	}

	
	@Override
	public void run(String... args) {
		System.err.println("Aplicación arrancada...");
	}

}
