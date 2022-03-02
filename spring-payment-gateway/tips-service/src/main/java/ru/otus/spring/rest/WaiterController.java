package ru.otus.spring.rest;

import org.springframework.web.bind.annotation.*;
import ru.otus.spring.model.User;
import ru.otus.spring.model.Waiter;
import ru.otus.spring.rest.dto.UserDto;
import ru.otus.spring.rest.dto.WaiterDto;
import ru.otus.spring.service.WaiterService;

@RestController
@RequestMapping(value = "/api/waiters")
public class WaiterController {
    private final WaiterService waiterService;

    public WaiterController(WaiterService waiterService) {
        this.waiterService = waiterService;
    }

    @GetMapping(value = "/id/{id}")
    public WaiterDto getWaiterById(@PathVariable String id) {
        return WaiterDto.convertToDto(waiterService.getWaiterById(id));
    }

    @GetMapping(value = "/phone/{phone}")
    public WaiterDto getWaiterByPhone(@PathVariable String phone) {
        return WaiterDto.convertToDto(waiterService.getWaiterByPhone(phone));
    }

    @PostMapping(value = "/add")
    public WaiterDto addWaiter(@RequestBody Waiter waiter) {
        return WaiterDto.convertToDto(
                waiterService.addWaiter(
                        waiter.getFirstName(),
                        waiter.getLastName(),
                        waiter.getPhone(),
                        waiter.getRestaurant()));
    }
}
