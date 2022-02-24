package ru.otus.spring;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@EnableMongock
@EnableCircuitBreaker
@EnableHystrixDashboard
@SpringBootApplication
public class Application {

	public static void main(String[] args) throws Exception{
		AnsiOutput.setEnabled(AnsiOutput.Enabled.ALWAYS);
		SpringApplication.run(Application.class, args);
	}

}
