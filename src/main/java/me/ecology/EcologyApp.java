package me.ecology;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"me.ecology.*"})
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class, JpaRepositoriesAutoConfiguration.class})
@SpringBootApplication
public class EcologyApp {
	public static void main(String[] args) {
		SpringApplication.run(EcologyApp.class, args);
	}
}
