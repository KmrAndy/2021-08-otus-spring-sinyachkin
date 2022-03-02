package ru.otus.spring.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.enums.PaymentType;
import ru.otus.spring.model.Incoming;
import ru.otus.spring.model.Payment;
import ru.otus.spring.service.PaymentService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
@DisplayName("Тестирование рест контроллера платежей")
class PaymentControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private PaymentService paymentService;

    private final Incoming expectedIncoming =
            new Incoming("1234123456785678", BigDecimal.valueOf(300.0), BigDecimal.valueOf(15.0), "desc");

    private final Payment expectedGeneralPayment =
            new Payment("1", expectedIncoming.getCardNumber(), LocalDateTime.now(),
                    PaymentType.GENERAL, expectedIncoming.getOperationAmount(), expectedIncoming.getDescription());
    private final Payment expectedFeePayment =
            new Payment("2", expectedIncoming.getCardNumber(), LocalDateTime.now(),
                    PaymentType.FEE, expectedIncoming.getFeeAmount(), expectedIncoming.getDescription());

    @DisplayName("Добавляем платеж")
    @Test
    void shouldAddPayment() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestBody = ow.writeValueAsString(expectedIncoming);

        ArgumentCaptor<Incoming> captureIncoming = ArgumentCaptor.forClass(Incoming.class);

        when(paymentService.addPayment(captureIncoming.capture())).thenReturn(List.of(expectedGeneralPayment, expectedFeePayment));

        mvc.perform(post("/api/payments/add").contentType(APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk());

        assertThat(captureIncoming.getValue().getCardNumber()).isEqualTo(expectedIncoming.getCardNumber());
        assertThat(captureIncoming.getValue().getDescription()).isEqualTo(expectedIncoming.getDescription());
        assertThat(captureIncoming.getValue().getOperationAmount()).isEqualTo(expectedIncoming.getOperationAmount());
        assertThat(captureIncoming.getValue().getFeeAmount()).isEqualTo(expectedIncoming.getFeeAmount());
    }
}