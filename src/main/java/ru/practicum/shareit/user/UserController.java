package ru.practicum.shareit.user;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.validation.CreateObject;
import ru.practicum.shareit.validation.UpdateObject;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@Validated(CreateObject.class) @RequestBody UserDto userDto) {
        return UserMapper.toUserDto(userService.addUser(userDto));
    }

    @PatchMapping("/{idUser}")
    public UserDto updateUser(@PathVariable Long idUser, @Validated(UpdateObject.class) @RequestBody UserDto userDto) {
        return UserMapper.toUserDto(userService.updateUser(idUser, userDto));
    }

    @GetMapping("/{idUser}")
    public UserDto getUserById(@PathVariable Long idUser) {
        return UserMapper.toUserDto(userService.getUserById(idUser));
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers().stream().map(UserMapper::toUserDto).toList();
    }

    @DeleteMapping("/{idUser}")
    public void removeUser(@PathVariable Long idUser) {
        userService.removeUser(idUser);
    }
}