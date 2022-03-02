package ru.otus.spring.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.TestPropertySource;
import ru.otus.spring.model.Fee;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
@DisplayName("Репозиторий для работы с комиссиями")
class FeeRepositoryTest {
    @Autowired
    private FeeRepository feeRepository;

    @DisplayName("Получаем комиссию по бину карты")
    @Test
    void shouldReturnFeeByBin() {
        Fee fee = feeRepository.save(new Fee("555555", BigDecimal.valueOf(15.0), 1.5));

        Optional<Fee> actualFee = feeRepository.findFeeByBin(fee.getBin());

        assertThat(actualFee.isPresent()).isEqualTo(true);
        assertThat(actualFee.get()).isEqualTo(fee);
    }
}