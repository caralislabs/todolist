package nicolaecaralicea.todolist;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class TodolistApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodolistApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(JdbcTemplate jdbcTemplate) {
		return args -> {
			jdbcTemplate.execute("CREATE TABLE todos (\n" +
					"    id INTEGER PRIMARY KEY,\n" +
					"    description TEXT NOT NULL,\n" +
					"    completion_status INTEGER NOT NULL\n" +
					");");
		};
	}
}
