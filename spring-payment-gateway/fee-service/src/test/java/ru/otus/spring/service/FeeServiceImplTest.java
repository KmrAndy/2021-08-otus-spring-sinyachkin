package ru.otus.spring.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import ru.otus.spring.model.Fee;
import ru.otus.spring.repository.FeeRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
@DisplayName("Сервис для работы с комиссиями")
class FeeServiceImplTest {

    @MockBean
    private FeeRepository feeRepository;

    @Autowired
    private FeeServiceImpl feeService;

    private final Fee expectedFee = new Fee("1", "555555", BigDecimal.valueOf(15.0), 1.5);

    @DisplayName("Добавляем комиссию")
    @Test
    void shouldAddNewFee() {
        Fee fee = new Fee(expectedFee.getBin(), expectedFee.getMinAmount(), expectedFee.getPercentValue());
        when(feeRepository.save(fee)).thenReturn(expectedFee);

        Fee actualFee = feeService.addFee(expectedFee.getBin(), expectedFee.getMinAmount(), expectedFee.getPercentValue());
        assertThat(actualFee).isEqualTo(expectedFee);
    }

    @DisplayName("Получить комиссию по id")
    @Test
    void shouldReturnFeeById() {
        when(feeRepository.findById(expectedFee.getId())).thenReturn(Optional.of(expectedFee));

        Fee actualFee = feeService.getFeeById(expectedFee.getId());
        assertThat(actualFee).isEqualTo(expectedFee);
    }

    @DisplayName("Получить комиссию по бин банка")
    @Test
    void shouldReturnFeeByBin() {
        when(feeRepository.findFeeByBin(expectedFee.getBin())).thenReturn(Optional.of(expectedFee));

        Fee actualFee = feeService.getFeeByBin(expectedFee.getBin());
        assertThat(actualFee).isEqualTo(expectedFee);
    }

    @DisplayName("Получить величину комиссии по бин банка и сумме оплаты")
    @Test
    void shouldReturnFeeByBinForAmount() {
        when(feeRepository.findFeeByBin(expectedFee.getBin())).thenReturn(Optional.of(expectedFee));

        BigDecimal actualFeeAmount = feeService.getFeeByBinForAmount(expectedFee.getBin(), BigDecimal.valueOf(300.0));
        assertThat(actualFeeAmount).isEqualTo(expectedFee.getMinAmount().setScale(2, RoundingMode.HALF_UP));
    }
}