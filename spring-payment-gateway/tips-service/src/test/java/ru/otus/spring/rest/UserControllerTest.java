package ru.otus.spring.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.model.User;
import ru.otus.spring.rest.dto.UserDto;
import ru.otus.spring.service.UserService;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserController.class)
@DisplayName("Тестирование рест контроллера пользователей")
class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private final User expectedUser = new User("1", "Test", "User", "+79992223311", "qwerty");
    private final UserDto expectedUserDto = UserDto.convertToDto(expectedUser);

    @WithMockUser(value = "user", authorities = {"ROLE_USER"})
    @DisplayName("Получаем пользователя по id")
    @Test
    void shouldReturnExpectedUserById() throws Exception {
        when(userService.getUserById(expectedUser.getId())).thenReturn(expectedUser);

        mvc.perform(get("/api/users/id/" + expectedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedUserDto.getId()))
                .andExpect(jsonPath("$.firstName").value(expectedUserDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(expectedUserDto.getLastName()))
                .andExpect(jsonPath("$.phone").value(expectedUserDto.getPhone()));
    }

    @WithMockUser(value = "user", authorities = {"ROLE_USER"})
    @DisplayName("Получаем пользователя по телефону")
    @Test
    void shouldReturnExpectedUserByPhone() throws Exception {
        when(userService.getUserByPhone(expectedUser.getPhone())).thenReturn(expectedUser);

        mvc.perform(get("/api/users/phone/" + expectedUser.getPhone()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedUserDto.getId()))
                .andExpect(jsonPath("$.firstName").value(expectedUserDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(expectedUserDto.getLastName()))
                .andExpect(jsonPath("$.phone").value(expectedUserDto.getPhone()));
    }

    @WithMockUser(value = "user", authorities = {"ROLE_USER"})
    @DisplayName("Добавляем пользователя")
    @Test
    void shouldAddBook() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestBody = ow.writeValueAsString(expectedUser);

        when(userService
                .addUser(
                        expectedUser.getFirstName(),expectedUser.getLastName(),
                        expectedUser.getPhone(), expectedUser.getPassword()))
                .thenReturn(expectedUser);

        mvc.perform(post("/api/users/add").contentType(APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedUserDto.getId()))
                .andExpect(jsonPath("$.firstName").value(expectedUserDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(expectedUserDto.getLastName()))
                .andExpect(jsonPath("$.phone").value(expectedUserDto.getPhone()));
    }
}