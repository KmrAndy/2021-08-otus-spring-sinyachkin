package ru.otus.spring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.otus.spring.enums.PaymentType;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Payment {
    @Id
    private String id;

    private String cardNumber;

    private LocalDateTime paymentDateTime;

    private PaymentType type;

    private BigDecimal amount;

    private String description;

    public Payment(PaymentType type, String cardNumber, LocalDateTime paymentDateTime, BigDecimal amount, String description) {
        this.type = type;
        this.cardNumber = cardNumber;
        this.paymentDateTime = paymentDateTime;
        this.amount = amount;
        this.description = description;
    }

}