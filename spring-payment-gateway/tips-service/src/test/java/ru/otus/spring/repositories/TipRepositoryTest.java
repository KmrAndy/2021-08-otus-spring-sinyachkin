package ru.otus.spring.repositories;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.TestPropertySource;
import ru.otus.spring.model.Tip;
import ru.otus.spring.model.User;
import ru.otus.spring.model.Waiter;
import ru.otus.spring.repository.TipRepository;
import ru.otus.spring.repository.UserRepository;
import ru.otus.spring.repository.WaiterRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataMongoTest
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
@DisplayName("Репозиторий для работы с чаевыми")
class TipRepositoryTest {
    @Autowired
    private TipRepository tipRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WaiterRepository waiterRepository;

    @DisplayName("Получаем чаевые, оставленные пользователем")
    @Test
    void shouldReturnTipsByFromUserId() {
        User user = userRepository.save(new User("Test", "User", "+79992223333", "qwerty"));
        Waiter waiter = waiterRepository.save(new Waiter("Test", "Waiter", "+79992223344", "testRest"));

        Tip firstTip = tipRepository.save(new Tip(user, "1234123456785678", waiter, BigDecimal.valueOf(200.0), BigDecimal.valueOf(10.0)));
        Tip secondTip = tipRepository.save(new Tip(user, "1234123456785679", waiter, BigDecimal.valueOf(300.0), BigDecimal.valueOf(15.0)));

        List<Tip> expectedTips = List.of(firstTip, secondTip);
        List<Tip> actualTips = tipRepository.findTipsByFromUserId(user.getId());

        assertThat(actualTips).usingRecursiveComparison().isEqualTo(expectedTips);
    }
}