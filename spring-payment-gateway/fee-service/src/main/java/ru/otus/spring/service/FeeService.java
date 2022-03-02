package ru.otus.spring.service;

import ru.otus.spring.model.Fee;

import java.math.BigDecimal;

public interface FeeService {
    Fee addFee(String bin, BigDecimal minAmount, double percentValue);

    Fee getFeeById(String id);

    Fee getFeeByBin(String bin);

    BigDecimal getFeeByBinForAmount(String bin, BigDecimal amount);
}
