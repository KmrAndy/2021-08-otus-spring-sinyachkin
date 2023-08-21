package ru.otus.spring.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import ru.otus.spring.model.User;
import ru.otus.spring.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
@DisplayName("Сервис для работы с пользователями")
class UserServiceImplTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private final User expectedUser = new User("1", "Test", "User", "+79992223311", "qwerty");

    @DisplayName("Добавляем пользователя")
    @Test
    void shouldAddNewUser() {
        User user = new User(
                expectedUser.getFirstName(), expectedUser.getLastName(),
                expectedUser.getPhone(), expectedUser.getPassword()
        );

        when(userRepository.save(user)).thenReturn(expectedUser);
        when(bCryptPasswordEncoder.encode(user.getPassword())).thenReturn(user.getPassword());

        User actualUser = userService.addUser(user.getFirstName(), user.getLastName(), user.getPhone(), user.getPassword());

        assertThat(actualUser).isEqualTo(expectedUser);
    }

    @DisplayName("Получаем пользователя по id")
    @Test
    void shouldReturnUserById() {
        when(userRepository.findById(expectedUser.getId())).thenReturn(Optional.of(expectedUser));
        assertEquals(userService.getUserById(expectedUser.getId()), expectedUser);
    }

    @DisplayName("Получаем пользователя по его телефону")
    @Test
    void shouldReturnUserByPhone() {
        when(userRepository.findUserByPhone(expectedUser.getPhone())).thenReturn(Optional.of(expectedUser));
        assertEquals(userService.getUserByPhone(expectedUser.getPhone()), expectedUser);
    }
}