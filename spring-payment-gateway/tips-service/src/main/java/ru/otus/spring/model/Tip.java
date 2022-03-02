package ru.otus.spring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Tip {
    @Id
    private String id;

    private User fromUser;

    private String fromCard;

    private Waiter toWaiter;

    private BigDecimal tipAmount;

    private BigDecimal feeAmount;

    private BigDecimal fullAmount;

    public Tip(User fromUser, String fromCard, Waiter toWaiter, BigDecimal tipAmount, BigDecimal feeAmount) {
        this.fromUser = fromUser;
        this.fromCard = fromCard;
        this.toWaiter = toWaiter;
        this.tipAmount = tipAmount;
        this.feeAmount = feeAmount;
        this.fullAmount = tipAmount.add(feeAmount);
    }
}
