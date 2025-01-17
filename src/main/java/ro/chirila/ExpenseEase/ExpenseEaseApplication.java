package ro.chirila.ExpenseEase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ExpenseEaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpenseEaseApplication.class, args);
	}

}
