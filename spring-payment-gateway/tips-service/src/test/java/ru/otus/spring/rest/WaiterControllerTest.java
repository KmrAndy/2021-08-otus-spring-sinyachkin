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
import ru.otus.spring.model.Waiter;
import ru.otus.spring.rest.dto.WaiterDto;
import ru.otus.spring.service.UserService;
import ru.otus.spring.service.WaiterService;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WaiterController.class)
@DisplayName("Тестирование рест контроллера официантов")
class WaiterControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private WaiterService waiterService;

    @MockBean
    private UserService userService;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private final Waiter expectedWaiter = new Waiter("1", "Test", "User", "+79992223311", "rest");
    private final WaiterDto expectedWaiterDto = WaiterDto.convertToDto(expectedWaiter);

    @WithMockUser(value = "user", authorities = {"ROLE_USER"})
    @DisplayName("Получаем официанта по id")
    @Test
    void shouldReturnWaiterById() throws Exception {
        when(waiterService.getWaiterById(expectedWaiter.getId())).thenReturn(expectedWaiter);

        mvc.perform(get("/api/waiters/id/" + expectedWaiter.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedWaiterDto.getId()))
                .andExpect(jsonPath("$.firstName").value(expectedWaiterDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(expectedWaiterDto.getLastName()))
                .andExpect(jsonPath("$.phone").value(expectedWaiterDto.getPhone()))
                .andExpect(jsonPath("$.restaurant").value(expectedWaiterDto.getRestaurant()));
    }

    @WithMockUser(value = "user", authorities = {"ROLE_USER"})
    @DisplayName("Получаем пользователя по телефону")
    @Test
    void shouldReturnWaiterByPhone() throws Exception {
        when(waiterService.getWaiterByPhone(expectedWaiter.getPhone())).thenReturn(expectedWaiter);

        mvc.perform(get("/api/waiters/phone/" + expectedWaiter.getPhone()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedWaiterDto.getId()))
                .andExpect(jsonPath("$.firstName").value(expectedWaiterDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(expectedWaiterDto.getLastName()))
                .andExpect(jsonPath("$.phone").value(expectedWaiterDto.getPhone()))
                .andExpect(jsonPath("$.restaurant").value(expectedWaiterDto.getRestaurant()));
    }

    @WithMockUser(value = "user", authorities = {"ROLE_USER"})
    @DisplayName("Добавляем официанта")
    @Test
    void shouldAddBook() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestBody = ow.writeValueAsString(expectedWaiter);

        when(waiterService
                .addWaiter(
                        expectedWaiter.getFirstName(),expectedWaiter.getLastName(),
                        expectedWaiter.getPhone(), expectedWaiter.getRestaurant()))
                .thenReturn(expectedWaiter);

        mvc.perform(post("/api/waiters/add").contentType(APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedWaiterDto.getId()))
                .andExpect(jsonPath("$.firstName").value(expectedWaiterDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(expectedWaiterDto.getLastName()))
                .andExpect(jsonPath("$.phone").value(expectedWaiterDto.getPhone()))
                .andExpect(jsonPath("$.restaurant").value(expectedWaiterDto.getRestaurant()));
    }
}