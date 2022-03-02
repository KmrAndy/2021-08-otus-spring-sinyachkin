package ru.otus.spring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Fee {
    @Id
    private String id;

    @Indexed(unique = true)
    private String bin;

    private BigDecimal minAmount;

    private double percentValue;

    public Fee(String bin, BigDecimal minAmount, double percentValue) {
        this.bin = bin;
        this.minAmount = minAmount;
        this.percentValue = percentValue;
    }

}