package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.validation.CreateObject;
import ru.practicum.shareit.validation.UpdateObject;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    /**
     * Добавляет нового пользователя.
     *
     * @param userDto данные пользователя
     * @return ResponseEntity с объектом, представляющим результат добавления пользователя
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addUser(@Validated(CreateObject.class) @RequestBody UserDtoInput userDto) {
        log.info("Добавление нового пользователя {}", userDto);
        return userClient.addUser(userDto);
    }

    /**
     * Обновляет данные пользователя.
     *
     * @param idUser идентификатор пользователя, данные которого нужно обновить
     * @param userDto новые данные пользователя
     * @return ResponseEntity с объектом, представляющим результат обновления данных пользователя
     */
    @PatchMapping("/{idUser}")
    public ResponseEntity<Object> updateUser(@PathVariable Long idUser,
                                             @Validated(UpdateObject.class) @RequestBody UserDtoInput userDto) {
        log.info("Обновление данных пользователя с id {} на основе данных {}", idUser, userDto);
        return userClient.updateUser(idUser, userDto);
    }

    /**
     * Получает данные пользователя по его идентификатору.
     *
     * @param idUser идентификатор пользователя, данные которого нужно получить
     * @return ResponseEntity с объектом, представляющим результат получения данных пользователя
     */
    @GetMapping("/{idUser}")
    public ResponseEntity<Object> getUserById(@PathVariable Long idUser) {
        log.info("Получение данных пользователя с id {}", idUser);
        return userClient.getUserById(idUser);
    }

    /**
     * Получает всех пользователей.
     *
     * @return ResponseEntity с объектом, представляющим результат получения всех пользователей
     */
    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Получение всех пользователей");
        return userClient.getAllUsers();
    }

    /**
     * Удаляет пользователя по его идентификатору.
     *
     * @param idUser идентификатор пользователя, которого нужно удалить
     */
    @DeleteMapping("/{idUser}")
    public ResponseEntity<Object> removeUser(@PathVariable Long idUser) {
        log.info("Удаление пользователя с id {}", idUser);
        return userClient.removeUser(idUser);
    }
}