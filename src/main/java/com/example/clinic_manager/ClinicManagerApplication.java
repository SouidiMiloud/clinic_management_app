package com.example.clinic_manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class ClinicManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClinicManagerApplication.class, args);
	}
}
