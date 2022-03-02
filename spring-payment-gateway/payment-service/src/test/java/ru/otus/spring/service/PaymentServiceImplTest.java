package ru.otus.spring.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import ru.otus.spring.enums.PaymentType;
import ru.otus.spring.model.Incoming;
import ru.otus.spring.model.Payment;
import ru.otus.spring.repository.PaymentRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
@DisplayName("Сервис для работы с платежами")
class PaymentServiceImplTest {

    @MockBean
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentServiceImpl paymentService;

    private final Incoming expectedIncoming =
            new Incoming("1234123456785678", BigDecimal.valueOf(300.0), BigDecimal.valueOf(15.0), "desc");

    private final Payment expectedGeneralPayment =
            new Payment("1", expectedIncoming.getCardNumber(), LocalDateTime.now(),
                    PaymentType.GENERAL, expectedIncoming.getOperationAmount(), expectedIncoming.getDescription());

    @DisplayName("Добавляем платеж")
    @Test
    void shouldAddNewPayment() {
        when(paymentRepository.save(any(Payment.class))).thenReturn(expectedGeneralPayment);

        List<Payment> actualPayments = paymentService.addPayment(expectedIncoming);

        assertThat(actualPayments).isEqualTo(List.of(expectedGeneralPayment, expectedGeneralPayment));
    }
}