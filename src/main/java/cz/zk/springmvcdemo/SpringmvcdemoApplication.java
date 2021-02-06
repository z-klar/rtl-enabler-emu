package cz.zk.springmvcdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableIntegration
public class SpringmvcdemoApplication {


	public static void main(String[] args) {
		SpringApplication.run(SpringmvcdemoApplication.class, args);
	}

}
