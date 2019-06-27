package ca.netopia.projects.shortbin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ShortbinApplication {
	public static void main(String[] args) {
		SpringApplication.run(ShortbinApplication.class, args);
	}
}
