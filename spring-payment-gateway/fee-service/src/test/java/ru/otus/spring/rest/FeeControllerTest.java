package ru.otus.spring.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.model.Fee;
import ru.otus.spring.rest.dto.FeeDto;
import ru.otus.spring.service.FeeService;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FeeController.class)
@DisplayName("Тестирование рест контроллера комиссий")
class FeeControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private FeeService feeService;

    private final Fee expectedFee = new Fee("1", "555555", BigDecimal.valueOf(15.0), 1.5);
    private final FeeDto expectedFeeDto = FeeDto.convertToDto(expectedFee);

    @DisplayName("Добавляем комиссию")
    @Test
    void shouldAddFee() throws Exception {
        Fee fee = new Fee(expectedFee.getBin(), expectedFee.getMinAmount(), expectedFee.getPercentValue());

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestBody = ow.writeValueAsString(fee);

        when(feeService.addFee(fee.getBin(), fee.getMinAmount(), fee.getPercentValue()))
                .thenReturn(expectedFee);

        mvc.perform(post("/api/fees/add").contentType(APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedFeeDto.getId()))
                .andExpect(jsonPath("$.minAmount").value(expectedFeeDto.getMinAmount()))
                .andExpect(jsonPath("$.percentValue").value(expectedFeeDto.getPercentValue()));
    }

    @DisplayName("Получаем комиссию по id")
    @Test
    void shouldReturnFeeById() throws Exception {
        when(feeService.getFeeById(expectedFee.getId())).thenReturn(expectedFee);

        mvc.perform(get("/api/fees/id/" + expectedFee.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedFeeDto.getId()))
                .andExpect(jsonPath("$.minAmount").value(expectedFeeDto.getMinAmount()))
                .andExpect(jsonPath("$.percentValue").value(expectedFeeDto.getPercentValue()));
    }

    @DisplayName("Получаем комиссию по бин банка")
    @Test
    void shouldReturnFeeByBin() throws Exception {
        when(feeService.getFeeByBin(expectedFee.getBin())).thenReturn(expectedFee);

        mvc.perform(get("/api/fees/bin/" + expectedFee.getBin()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedFeeDto.getId()))
                .andExpect(jsonPath("$.minAmount").value(expectedFeeDto.getMinAmount()))
                .andExpect(jsonPath("$.percentValue").value(expectedFeeDto.getPercentValue()));
    }

    @DisplayName("Получаем величину комиссии по бин банка для размера оплаты")
    @Test
    void shouldReturnFeeByBinForAmount() throws Exception {
        when(feeService.getFeeByBinForAmount(expectedFee.getBin(), BigDecimal.valueOf(300.0))).thenReturn(expectedFee.getMinAmount());

        mvc.perform(get("/api/fees/bin/" + expectedFee.getBin() + "/amount/" + 300.0))
                .andExpect(status().isOk())
                .andExpect(content().string("15.0"));
    }
}