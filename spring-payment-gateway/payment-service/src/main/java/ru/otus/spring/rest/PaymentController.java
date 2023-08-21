package ru.otus.spring.rest;

import org.springframework.web.bind.annotation.*;
import ru.otus.spring.model.Incoming;
import ru.otus.spring.service.PaymentService;

@RestController
@RequestMapping(value = "/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping(value = "/add")
    public void addPayment(@RequestBody Incoming incoming) {
        paymentService.addPayment(incoming);
    }
}
