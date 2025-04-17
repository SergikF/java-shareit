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
    public UserDtoOutput addUser(@Validated(CreateObject.class) @RequestBody UserDto userDto) {
        return UserMapper.toUserDtoOutput(userService.addUser(userDto));
    }

    @PatchMapping("/{idUser}")
    public UserDtoOutput updateUser(@PathVariable Long idUser, @Validated(UpdateObject.class) @RequestBody UserDto userDto) {
        return UserMapper.toUserDtoOutput(userService.updateUser(idUser, userDto));
    }

    @GetMapping("/{idUser}")
    public UserDtoOutput getUserById(@PathVariable Long idUser) {
        return UserMapper.toUserDtoOutput(userService.getUserById(idUser));
    }

    @GetMapping
    public List<UserDtoOutput> getAllUsers() {
        return userService.getAllUsers().stream().map(UserMapper::toUserDtoOutput).toList();
    }

    @DeleteMapping("/{idUser}")
    public void removeUser(@PathVariable Long idUser) {
        userService.removeUser(idUser);
    }
}