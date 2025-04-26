package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {

    private final RequestClient requestClient;

    /**
     * Добавляет новый запрос.
     *
     * @param requestorId Идентификатор пользователя, добавляющего запрос.
     * @param requestDto Данные запроса.
     * @return ResponseEntity с объектом, представляющим результат добавления запроса.
     */
    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestHeader("X-Sharer-User-Id") Long requestorId,
                                             @Valid @RequestBody RequestDtoInput requestDto) {
        log.info("Получен запрос на добавление запроса с параметрами: {}", requestDto);
        return requestClient.addRequest(requestorId, requestDto);
    }

    /**
     * Возвращает расширенный список запросов конкретного пользователя.
     *
     * @param requestorId Идентификатор пользователя, запросы которого нужно вернуть.
     * @return ResponseEntity с объектом, представляющим список запросов пользователя.
     */
    @GetMapping
    public ResponseEntity<Object> getRequests(@RequestHeader("X-Sharer-User-Id") Long requestorId) {
        log.info("Получен запрос на получение расширенного списка запросов пользователя с id: {}", requestorId);
        return requestClient.getRequests(requestorId);
    }

    /**
     * Возвращает все запросы (за исключением запросов самого пользователя).
     *
     * @param requestorId Идентификатор пользователя.
     * @return ResponseEntity с объектом, представляющим все запросы.
     */
    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long requestorId) {
        log.info("Получен запрос на получение всех запросов, кроме запросов самого пользователя с id: {}", requestorId);
        return requestClient.getAllRequests(requestorId);
    }

    /**
     * Возвращает запрос по его идентификатору.
     *
     * @param requestorId Идентификатор пользователя, который делает запрос.
     * @param requestId Идентификатор запроса, который нужно вернуть.
     * @return ResponseEntity с объектом, представляющим запрос по его идентификатору.
     */
    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestsById(@RequestHeader("X-Sharer-User-Id") Long requestorId,
                                                  @PathVariable Long requestId) {
        log.info("Получен запрос на получение запроса с id: {} по идентификатору пользователя с id: {}",
                requestId, requestorId);
        return requestClient.getRequestById(requestId);
    }

}