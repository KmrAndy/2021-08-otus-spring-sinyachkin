package ru.otus.spring.rest;

import org.springframework.web.bind.annotation.*;
import ru.otus.spring.model.User;
import ru.otus.spring.rest.dto.UserDto;
import ru.otus.spring.service.UserService;

@RestController
@RequestMapping(value = "/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/id/{id}")
    public UserDto getUserById(@PathVariable String id) {
        return UserDto.convertToDto(userService.getUserById(id));
    }

    @GetMapping(value = "/phone/{phone}")
    public UserDto getUserByPhone(@PathVariable String phone) {
        return UserDto.convertToDto(userService.getUserByPhone(phone));
    }

    @PostMapping(value = "/add")
    public UserDto addUser(@RequestBody User user) {
        return UserDto.convertToDto(
                userService.addUser(
                        user.getFirstName(),
                        user.getLastName(),
                        user.getPhone(),
                        user.getPassword()));
    }
}
