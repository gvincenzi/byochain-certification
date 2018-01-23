package org.byochain.model;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot Starter Class
 * @author Giuseppe Vincenzi
 *
 */
@SpringBootApplication(scanBasePackages = { "org.byochain.model" })
public class AppModel {
	public static void main(String[] args) {
		SpringApplication.run(AppModel.class, args);
	}
}
