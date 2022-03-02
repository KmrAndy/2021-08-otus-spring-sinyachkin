package ru.otus.spring.repositories;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.TestPropertySource;
import ru.otus.spring.model.Waiter;
import ru.otus.spring.repository.WaiterRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataMongoTest
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
@DisplayName("Репозиторий для работы с официантами")
class WaiterRepositoryTest {

    @Autowired
    private WaiterRepository waiterRepository;

    @DisplayName("Получаем официанта по его телефону")
    @Test
    void shouldReturnWaiterByPhone() {
        Waiter waiter = waiterRepository.save(new Waiter("Test", "Waiter", "+79992223311", "testRest"));
        Optional<Waiter> actualWaiter = waiterRepository.findWaiterByPhone("+79992223311");

        assertThat(actualWaiter.isPresent()).isEqualTo(true);
        assertThat(actualWaiter.get()).isEqualTo(waiter);
    }
}