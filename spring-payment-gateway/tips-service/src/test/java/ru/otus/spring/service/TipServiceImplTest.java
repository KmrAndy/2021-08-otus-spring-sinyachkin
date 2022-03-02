package ru.otus.spring.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import ru.otus.spring.feign.FeeServiceProxy;
import ru.otus.spring.feign.PaymentServiceProxy;
import ru.otus.spring.model.Incoming;
import ru.otus.spring.model.Tip;
import ru.otus.spring.model.User;
import ru.otus.spring.model.Waiter;
import ru.otus.spring.repository.TipRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
@DisplayName("Сервис для работы с чаевыми")
class TipServiceImplTest {

    @MockBean
    private TipRepository tipRepository;

    @MockBean
    private FeeServiceProxy feignFeeProxy;

    @MockBean
    private PaymentServiceProxy feignPaymentProxy;

    @Autowired
    private TipServiceImpl tipService;

    private final User expectedUser = new User("1", "Test", "User", "+79992223311", "qwerty");
    private final Waiter expectedWaiter = new Waiter("1", "Test", "Waiter", "+79992223322", "testRest");

    private final Tip expectedFirstTip =
            new Tip("1", expectedUser, "1234123456785678", expectedWaiter,
                    BigDecimal.valueOf(200.0), BigDecimal.valueOf(10.0), BigDecimal.valueOf(210.0));
    private final Tip expectedSecondTip =
            new Tip("2", expectedUser, "1234123456785679", expectedWaiter,
                    BigDecimal.valueOf(300.0), BigDecimal.valueOf(15.0), BigDecimal.valueOf(315.0));

    @DisplayName("Добавляем чаевые")
    @Test
    void shouldAddNewTip() {
        Tip tip = new Tip(
                expectedFirstTip.getFromUser(), expectedFirstTip.getFromCard(),
                expectedFirstTip.getToWaiter(), expectedFirstTip.getTipAmount(), expectedFirstTip.getFeeAmount()
        );

        when(tipRepository.save(tip)).thenReturn(expectedFirstTip);
        when(feignFeeProxy
                .getFeeByBinForAmount(expectedFirstTip.getFromCard().substring(0,6), expectedFirstTip.getTipAmount()))
                .thenReturn(expectedFirstTip.getFeeAmount());
        doNothing().when(feignPaymentProxy)
                .addPayment(
                        any(Incoming.class));

        Tip actualTip = tipService.addTip(
                expectedFirstTip.getFromUser(),
                expectedFirstTip.getFromCard(),
                expectedFirstTip.getToWaiter(),
                expectedFirstTip.getTipAmount());

        assertThat(actualTip).isEqualTo(expectedFirstTip);
    }

    @DisplayName("Получаем все оставленные чаевые по id пользователя")
    @Test
    void shouldReturnTipsByFromUserId() {
        when(tipRepository.findTipsByFromUserId(expectedUser.getId())).thenReturn(List.of(expectedFirstTip, expectedSecondTip));
        assertEquals(tipService.getTipsByFromUserId(expectedUser.getId()), List.of(expectedFirstTip, expectedSecondTip));
    }
}