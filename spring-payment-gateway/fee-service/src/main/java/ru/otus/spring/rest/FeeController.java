package ru.otus.spring.rest;

import org.springframework.web.bind.annotation.*;
import ru.otus.spring.model.Fee;
import ru.otus.spring.rest.dto.FeeDto;
import ru.otus.spring.service.FeeService;

import java.math.BigDecimal;

@RestController
@RequestMapping(value = "/api/fees")
public class FeeController {
    private final FeeService feeService;

    public FeeController(FeeService feeService) {
        this.feeService = feeService;
    }

    @GetMapping(value = "/id/{id}")
    public FeeDto getFeeById(@PathVariable String id) {
        return FeeDto.convertToDto(feeService.getFeeById(id));
    }

    @GetMapping(value = "/bin/{bin}")
    public FeeDto getFeeByBin(@PathVariable String bin) {
        return FeeDto.convertToDto(feeService.getFeeByBin(bin));
    }

    @GetMapping(value = "/bin/{bin}/amount/{amount}")
    public BigDecimal getFeeByBinForAmount(@PathVariable String bin, @PathVariable BigDecimal amount) {
        return feeService.getFeeByBinForAmount(bin, amount);
    }

    @PostMapping(value = "/add")
    public FeeDto addFee(@RequestBody Fee fee) {
        return FeeDto.convertToDto(
                feeService.addFee(
                        fee.getBin(),
                        fee.getMinAmount(),
                        fee.getPercentValue()));
    }
}
