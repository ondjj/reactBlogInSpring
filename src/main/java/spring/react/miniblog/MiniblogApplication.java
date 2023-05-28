package spring.react.miniblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import spring.react.miniblog.domain.User;
import spring.react.miniblog.repository.UserRepository;

@SpringBootApplication
public class MiniblogApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiniblogApplication.class, args);
	}
}
