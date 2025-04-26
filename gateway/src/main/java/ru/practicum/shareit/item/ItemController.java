package ru.practicum.shareit.item;

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
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    /**
     * Добавляет новую вещь.
     *
     * @param itemDto Данные вещи.
     * @param idUser Идентификатор пользователя, добавляющего вещь.
     * @return ResponseEntity с объектом, представляющим результат добавления вещи.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addItem(@Validated(CreateObject.class) @RequestBody ItemDtoInput itemDto,
                                          @RequestHeader(value = "X-Sharer-User-Id") Long idUser) {
        log.info("Получен запрос пользователем {} на добавление вещи {}", idUser, itemDto);
        return itemClient.addItem(idUser, itemDto);
    }

    /**
     * Обновляет вещь.
     *
     * @param idItem Идентификатор вещи, которую нужно обновить.
     * @param itemDto Данные вещи.
     * @param idUser Идентификатор пользователя, обновляющего вещь.
     * @return ResponseEntity с объектом, представляющим результат обновления вещи.
     */
    @PatchMapping("/{idItem}")
    public ResponseEntity<Object> updateItem(@PathVariable Long idItem,
                                             @Validated(UpdateObject.class) @RequestBody ItemDtoInput itemDto,
                                             @RequestHeader(value = "X-Sharer-User-Id") Long idUser) {
        log.info("Получен запрос пользователем {} на обновление вещи {}", idUser, itemDto);
        return itemClient.updateItem(idUser, idItem, itemDto);
    }

    /**
     * Возвращает вещь по ее идентификатору.
     *
     * @param idItem Идентификатор вещи, которую нужно вернуть.
     * @param idUser Идентификатор пользователя, запрашивающего вещь.
     * @return ResponseEntity с объектом, представляющим вещь по ее идентификатору.
     */
    @GetMapping("/{idItem}")
    public ResponseEntity<Object> getItemById(@PathVariable Long idItem,
                                              @RequestHeader(value = "X-Sharer-User-Id") Long idUser) {
        log.info("Получен запрос пользователем {} на получение вещи с id={}", idUser, idItem);
        return itemClient.getItemById(idItem, idUser);
    }

    /**
     * Возвращает все вещи пользователя.
     *
     * @param idUser Идентификатор пользователя, вещи которого нужно вернуть.
     * @return ResponseEntity с объектом, представляющим все вещи пользователя.
     */
    @GetMapping
    public ResponseEntity<Object> getAllItemsByUser(@RequestHeader(value = "X-Sharer-User-Id") Long idUser) {
        log.info("Получен запрос пользователем {} на получение всех своих вещей", idUser);
        return itemClient.getAllItems(idUser);
    }

    /**
     * Удаляет вещь по ее идентификатору.
     *
     * @param idItem Идентификатор вещи, которую нужно удалить.
     * @param idUser Идентификатор пользователя, удаляющего вещь.
     * @return ResponseEntity с объектом, представляющим результат удаления вещи.
     */
    @DeleteMapping("/{idItem}")
    public ResponseEntity<Object> removeItem(@PathVariable Long idItem,
                                             @RequestHeader(value = "X-Sharer-User-Id") Long idUser) {
        log.info("Получен запрос пользователем {} на удаление вещи с id={}", idUser, idItem);
        return itemClient.removeItem(idUser, idItem);
    }

    /**
     * Ищет вещи по тексту.
     *
     * @param text Текст, по которому нужно искать вещи.
     * @param idUser Идентификатор пользователя, инициирующего поиск.
     * @return ResponseEntity с объектом, представляющим результат поиска вещей по тексту.
     */
    @GetMapping("/search")
    public ResponseEntity<Object> searchItemsByText(@RequestParam String text,
                                                    @RequestHeader(value = "X-Sharer-User-Id") Long idUser) {
        log.info("Получен запрос пользователем {} на поиск вещей по тексту: {}", idUser, text);
        return itemClient.searchItems(text);
    }

    /**
     * Добавляет комментарий к вещи.
     *
     * @param userId Идентификатор пользователя, добавляющего комментарий.
     * @param itemId Идентификатор вещи, к которой добавляется комментарий.
     * @param commentDto Данные комментария.
     * @return ResponseEntity с объектом, представляющим результат добавления комментария.
     */
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addCommentToItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @PathVariable Long itemId,
                                                   @RequestBody CommentDtoInput commentDto) {
        log.info("Получен запрос пользователем {} на добавление отзыва на вещь с id={} с текстом: {}",
                  userId, itemId, commentDto.getText());
        return itemClient.saveComment(userId, itemId, commentDto);
    }
}
