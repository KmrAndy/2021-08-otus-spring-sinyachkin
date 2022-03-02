package ru.otus.spring.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

@FeignClient(name = "fee-service")
public interface FeeServiceProxy {

    @GetMapping(value = "/api/fees/bin/{bin}/amount/{amount}")
    BigDecimal getFeeByBinForAmount(@PathVariable String bin, @PathVariable BigDecimal amount);
}
