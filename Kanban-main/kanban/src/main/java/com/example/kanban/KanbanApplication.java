package com.example.kanban;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class KanbanApplication {

	public static void main(String[] args) {
		SpringApplication.run(KanbanApplication.class, args);
	}

	@Bean
	public CommandLineRunner inicializacao() {
		return args -> {
			System.out.println("Funcionou!");
		};
	}
}
