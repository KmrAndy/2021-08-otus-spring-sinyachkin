package ru.otus.spring.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import ru.otus.spring.model.Waiter;
import ru.otus.spring.repository.WaiterRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
@DisplayName("Сервис для работы с официантами")
class WaiterServiceImplTest {

    @MockBean
    private WaiterRepository waiterRepository;

    @Autowired
    private WaiterServiceImpl waiterService;

    private final Waiter expectedWaiter = new Waiter("1", "Test", "Waiter", "+79992223311", "testRest");

    @DisplayName("Добавляем официанта")
    @Test
    void shouldAddNewWaiter() {
        Waiter waiter = new Waiter(
                expectedWaiter.getFirstName(), expectedWaiter.getLastName(),
                expectedWaiter.getPhone(), expectedWaiter.getRestaurant()
        );

        when(waiterRepository.save(waiter)).thenReturn(expectedWaiter);

        Waiter actualWaiter = waiterService.addWaiter(waiter.getFirstName(), waiter.getLastName(), waiter.getPhone(), waiter.getRestaurant());

        assertThat(actualWaiter).isEqualTo(expectedWaiter);
    }

    @DisplayName("Получаем официанта по id")
    @Test
    void shouldReturnWaiterById() {
        when(waiterRepository.findById(expectedWaiter.getId())).thenReturn(Optional.of(expectedWaiter));
        assertEquals(waiterService.getWaiterById(expectedWaiter.getId()), expectedWaiter);
    }

    @DisplayName("Получаем официанта по его телефону")
    @Test
    void shouldReturnUserByPhone() {
        when(waiterRepository.findWaiterByPhone(expectedWaiter.getPhone())).thenReturn(Optional.of(expectedWaiter));
        assertEquals(waiterService.getWaiterByPhone(expectedWaiter.getPhone()), expectedWaiter);
    }
}