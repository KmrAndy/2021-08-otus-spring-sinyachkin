package ru.otus.spring.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.TestPropertySource;
import ru.otus.spring.model.User;
import ru.otus.spring.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
@DisplayName("Репозиторий для работы с пользователями")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("Получаем пользователя по его телефону")
    @Test
    void shouldReturnUserByPhone() {
        User user = userRepository.save(new User("Test", "User", "+79992223311", "qwerty"));
        Optional<User> actualUser = userRepository.findUserByPhone("+79992223311");

        assertThat(actualUser.isPresent()).isEqualTo(true);
        assertThat(actualUser.get()).isEqualTo(user);
    }
}