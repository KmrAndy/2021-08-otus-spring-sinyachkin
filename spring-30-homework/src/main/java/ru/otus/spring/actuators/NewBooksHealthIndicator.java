package ru.otus.spring.actuators;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.CommentaryService;
import ru.otus.spring.service.GenreService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
public class NewBooksHealthIndicator implements HealthIndicator {
    private final static long ALERT_BORDER = 20L;
    private double previousNewBookCounter;
    private LocalDateTime previousLocalDateTime;
    private final MeterRegistry meterRegistry;

    public NewBooksHealthIndicator(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.previousNewBookCounter = meterRegistry.counter("newbooks.counter").count();
        this.previousLocalDateTime = LocalDateTime.now();
    }

    @Override
    public Health health() {
        double newBookCounter = meterRegistry.counter("newbooks.counter").count();
        double newBooksDiff = newBookCounter - previousNewBookCounter;

        LocalDateTime now = LocalDateTime.now();
        long diffSeconds = ChronoUnit.SECONDS.between(previousLocalDateTime, now);

        if(newBooksDiff == 0){
            if (diffSeconds > ALERT_BORDER){
                return Health.down()
                        .status(Status.DOWN)
                        .withDetail("message", "No new books added in last " + ALERT_BORDER + " seconds")
                        .build();
            }

            return Health.up()
                    .withDetail("message", "No new books added in last " + diffSeconds + " seconds")
                    .build();
        } else {
            previousNewBookCounter = newBookCounter;
            previousLocalDateTime = now;
            return Health.up()
                    .withDetail("message", "Added new books: " + newBooksDiff + " in last " + ALERT_BORDER + " seconds")
                    .build();
        }
    }
}
