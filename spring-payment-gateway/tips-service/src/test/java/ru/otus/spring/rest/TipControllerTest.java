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
import ru.otus.spring.model.Tip;
import ru.otus.spring.model.User;
import ru.otus.spring.model.Waiter;
import ru.otus.spring.rest.dto.TipDto;
import ru.otus.spring.service.TipService;
import ru.otus.spring.service.UserService;
import ru.otus.spring.service.WaiterService;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TipController.class)
@DisplayName("Тестирование рест контроллера чаевых")
class TipControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private TipService tipService;

    @MockBean
    private UserService userService;

    @MockBean
    private WaiterService waiterService;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private final User expectedUser = new User("1", "Test", "User", "+79992223311", "qwerty");
    private final Waiter expectedWaiter = new Waiter("1", "Test", "Waiter", "+79992223322", "testRest");

    private final Tip expectedFirstTip =
            new Tip("1", expectedUser, "1234123456785678", expectedWaiter,
                    BigDecimal.valueOf(200.0), BigDecimal.valueOf(10.0), BigDecimal.valueOf(210.0));
    private final Tip expectedSecondTip =
            new Tip("2", expectedUser, "1234123456785679", expectedWaiter,
                    BigDecimal.valueOf(300.0), BigDecimal.valueOf(15.0), BigDecimal.valueOf(315.0));

    private final TipDto expectedFirstTipDto = TipDto.convertToDto(expectedFirstTip);
    private final TipDto expectedSecondTipDto = TipDto.convertToDto(expectedSecondTip);

    @WithMockUser(value = "user", authorities = {"ROLE_USER"})
    @DisplayName("Добавляем чаевые")
    @Test
    void shouldAddBook() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestBody = ow.writeValueAsString(expectedFirstTip);

        when(userService.getUserById(expectedUser.getId())).thenReturn(expectedUser);
        when(waiterService.getWaiterById(expectedWaiter.getId())).thenReturn(expectedWaiter);
        when(tipService
                .addTip(
                        expectedFirstTip.getFromUser(),expectedFirstTip.getFromCard(),
                        expectedFirstTip.getToWaiter(), expectedFirstTip.getTipAmount()))
                .thenReturn(expectedFirstTip);

        mvc.perform(post("/api/tips/add/from/" + expectedUser.getId() + "/to/" + expectedWaiter.getId())
                        .contentType(APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedFirstTipDto.getId()))
                .andExpect(jsonPath("$.fromFullName").value(expectedFirstTipDto.getFromFullName()))
                .andExpect(jsonPath("$.fromCard").value(expectedFirstTipDto.getFromCard()))
                .andExpect(jsonPath("$.toFullName").value(expectedFirstTipDto.getToFullName()))
                .andExpect(jsonPath("$.tipAmount").value(expectedFirstTipDto.getTipAmount()))
                .andExpect(jsonPath("$.feeAmount").value(expectedFirstTipDto.getFeeAmount()))
                .andExpect(jsonPath("$.fullAmount").value(expectedFirstTipDto.getFullAmount()));
    }

    @WithMockUser(value = "user", authorities = {"ROLE_USER"})
    @DisplayName("Получаем все оставленные чаевые по id пользователя")
    @Test
    void shouldReturnCardsByCardHolderId() throws Exception {
        when(tipService.getTipsByFromUserId(expectedUser.getId())).thenReturn(List.of(expectedFirstTip, expectedSecondTip));

        mvc.perform(get("/api/tips/from/" + expectedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(expectedFirstTipDto.getId()))
                .andExpect(jsonPath("$[0].fromFullName").value(expectedFirstTipDto.getFromFullName()))
                .andExpect(jsonPath("$[0].fromCard").value(expectedFirstTipDto.getFromCard()))
                .andExpect(jsonPath("$[0].toFullName").value(expectedFirstTipDto.getToFullName()))
                .andExpect(jsonPath("$[0].tipAmount").value(expectedFirstTipDto.getTipAmount()))
                .andExpect(jsonPath("$[0].feeAmount").value(expectedFirstTipDto.getFeeAmount()))
                .andExpect(jsonPath("$[0].fullAmount").value(expectedFirstTipDto.getFullAmount()))
                .andExpect(jsonPath("$[1].id").value(expectedSecondTipDto.getId()))
                .andExpect(jsonPath("$[1].fromFullName").value(expectedSecondTipDto.getFromFullName()))
                .andExpect(jsonPath("$[1].fromCard").value(expectedSecondTipDto.getFromCard()))
                .andExpect(jsonPath("$[1].toFullName").value(expectedSecondTipDto.getToFullName()))
                .andExpect(jsonPath("$[1].tipAmount").value(expectedSecondTipDto.getTipAmount()))
                .andExpect(jsonPath("$[1].feeAmount").value(expectedSecondTipDto.getFeeAmount()))
                .andExpect(jsonPath("$[1].fullAmount").value(expectedSecondTipDto.getFullAmount()));
    }

}