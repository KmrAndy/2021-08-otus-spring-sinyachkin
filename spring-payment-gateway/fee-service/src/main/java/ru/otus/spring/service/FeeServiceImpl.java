package ru.otus.spring.service;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.exception.OtherAccessException;
import ru.otus.spring.model.Fee;
import ru.otus.spring.repository.FeeRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class FeeServiceImpl implements FeeService {
    private final FeeRepository feeRepository;

    public FeeServiceImpl(FeeRepository feeRepository){
        this.feeRepository = feeRepository;
    }

    @Transactional
    public Fee addFee(String bin, BigDecimal minAmount, double percentValue){
        try {
            return feeRepository.save(new Fee(bin, minAmount, percentValue));
        } catch (DataAccessException e) {
            throw new OtherAccessException(e);
        }
    }

    public Fee getFeeById(String id){
        return feeRepository.findById(id).orElse(getDefaultFee());
    }

    public Fee getFeeByBin(String bin){
        return feeRepository.findFeeByBin(bin).orElse(getDefaultFee());
    }

    public BigDecimal getFeeByBinForAmount(String bin, BigDecimal amount){
        amount = amount.setScale(2, RoundingMode.HALF_UP);
        Fee bankFee = getFeeByBin(bin);
        return BigDecimal.valueOf(
                Math.max(
                        bankFee.getMinAmount().doubleValue(),
                        amount.doubleValue() * bankFee.getPercentValue() / 100))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public static Fee getDefaultFee(){
        return new Fee("000000", BigDecimal.valueOf(100.0), 1.5);
    }
}
