package ru.otus.spring.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.model.Incoming;

@FeignClient(name = "payment-service")
public interface PaymentServiceProxy {
    @PostMapping(value = "/api/payments/add")
    void addPayment(@RequestBody Incoming incoming);
}
