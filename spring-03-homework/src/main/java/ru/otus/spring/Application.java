package ru.otus.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.otus.spring.service.QuizService;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		AnsiOutput.setEnabled(AnsiOutput.Enabled.ALWAYS);
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

		QuizService quizService = context.getBean(QuizService.class);
		quizService.run();

		context.close();
	}

}
