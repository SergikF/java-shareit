package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDtoOutput addUser(@RequestBody UserDto userDto) {
        return UserMapper.toUserDtoOutput(userService.addUser(userDto));
    }

    @PatchMapping("/{idUser}")
    public UserDtoOutput updateUser(@PathVariable Long idUser,
                                    @RequestBody UserDto userDto) {
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