package ru.otus.spring.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.exception.OtherAccessException;
import ru.otus.spring.feign.FeeServiceProxy;
import ru.otus.spring.feign.PaymentServiceProxy;
import ru.otus.spring.model.Incoming;
import ru.otus.spring.model.Tip;
import ru.otus.spring.model.User;
import ru.otus.spring.model.Waiter;
import ru.otus.spring.repository.TipRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TipServiceImpl implements TipService{
    private final TipRepository tipRepository;
    private final FeeServiceProxy feignFeeProxy;
    private final PaymentServiceProxy feignPaymentProxy;

    @Value("${tips.description: Оплата чаевых}")
    private String tipDescription;

    public TipServiceImpl(TipRepository tipRepository,
                          FeeServiceProxy feignFeeProxy,
                          PaymentServiceProxy feignPaymentProxy){
        this.tipRepository = tipRepository;
        this.feignFeeProxy = feignFeeProxy;
        this.feignPaymentProxy = feignPaymentProxy;
    }

    @Transactional
    public Tip addTip(User fromUser, String fromCard, Waiter toWaiter, BigDecimal tipAmount){
        try{
            BigDecimal feeAmount = feignFeeProxy.getFeeByBinForAmount(getCardBin(fromCard), tipAmount);
            feignPaymentProxy.addPayment(new Incoming(fromCard, tipAmount, feeAmount, this.tipDescription));
            return tipRepository.save(
                    new Tip(fromUser,
                            fromCard,
                            toWaiter,
                            tipAmount,
                            feeAmount));
        } catch (Exception e) {
            throw new OtherAccessException(e);
        }
    }

    public List<Tip> getTipsByFromUserId(String fromUserId){
        try {
            return tipRepository.findTipsByFromUserId(fromUserId);
        } catch (DataAccessException e) {
            throw new OtherAccessException(e);
        }
    }

    private String getCardBin(String number){
        return number.substring(0,6);
    }
}
