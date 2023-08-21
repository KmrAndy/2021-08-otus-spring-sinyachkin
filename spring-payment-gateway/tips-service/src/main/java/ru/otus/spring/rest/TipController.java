package ru.otus.spring.rest;

import org.springframework.web.bind.annotation.*;
import ru.otus.spring.model.Tip;
import ru.otus.spring.rest.dto.TipDto;
import ru.otus.spring.service.TipService;
import ru.otus.spring.service.UserService;
import ru.otus.spring.service.WaiterService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/tips")
public class TipController {
    private final TipService tipService;
    private final UserService userService;
    private final WaiterService waiterService;

    public TipController(TipService tipService, UserService userService, WaiterService waiterService){
        this.tipService = tipService;
        this.userService = userService;
        this.waiterService = waiterService;
    }

    @PostMapping(value = "/add/from/{fromId}/to/{toId}")
    public TipDto addTip(@PathVariable(name = "fromId") String fromUserId,
                         @PathVariable(name = "toId") String toWaiterId,
                         @RequestBody Tip tip){
        return TipDto.convertToDto(
                tipService.addTip(
                        userService.getUserById(fromUserId),
                        tip.getFromCard(),
                        waiterService.getWaiterById(toWaiterId),
                        tip.getTipAmount()));
    }

    @GetMapping(value = "/from/{id}")
    public List<TipDto> getTipsByFromUserId(@PathVariable(name = "id") String fromUserId){
        return tipService.getTipsByFromUserId(fromUserId)
                .stream()
                .map(TipDto::convertToDto)
                .collect(Collectors.toList());
    }
}
