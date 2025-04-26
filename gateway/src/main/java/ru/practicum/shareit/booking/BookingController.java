package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

    private final BookingClient bookingClient;

    /**
     * Создает бронирование.
     *
     * @param bookerId идентификатор пользователя, создающего бронирование
     * @param bookingDto данные бронирования
     * @return ResponseEntity с объектом, представляющим результат создания бронирования
     */
    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                             @Valid @RequestBody BookingDtoInput bookingDto) {
        log.info("Создано бронирование {}, пользователем {}", bookingDto, bookerId);
        return bookingClient.addBooking(bookerId, bookingDto);
    }

    /**
     * Обновляет бронирование владельцем.
     *
     * @param ownerId идентификатор владельца бронирования
     * @param bookingId идентификатор бронирования
     * @param approved флаг, указывающий, одобрено ли бронирование
     * @return ResponseEntity с объектом, представляющим результат обновления бронирования
     */
    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateByOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                @PathVariable Long bookingId,
                                                @RequestParam Boolean approved) {
        log.info("Обновление бронирования {}, владельцем {}, в состояние {}", bookingId, ownerId, approved);
        return bookingClient.updateByOwner(ownerId, bookingId, approved);
    }

    /**
     * Получает данные о бронировании по его идентификатору.
     *
     * @param userId идентификатор пользователя, запрашивающего данные о бронировании
     * @param bookingId идентификатор бронирования
     * @return ResponseEntity с объектом, представляющим результат получения данных о бронировании
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getWithStatusById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                    @PathVariable Long bookingId) {
        log.info("Получение данных о бронировании {}, пользователем {}", bookingId, userId);
        return bookingClient.getWithStatusById( bookingId, userId);
    }

    /**
     * Получает все бронирования пользователя по его идентификатору и состоянию.
     *
     * @param userId идентификатор пользователя, запрашивающего данные о бронировании
     * @param stateParam состояние бронирования
     * @return ResponseEntity с объектом, представляющим результат получения данных о бронировании
     */
    @GetMapping
    public ResponseEntity<Object> getByUserId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam(value = "state",
                                                      defaultValue = "ALL", required = false) String stateParam) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() ->
                        new ValidationException("Неизвестное состояние бронирования: " + stateParam));
        log.info("Получение всех бронирований пользователя{}, в состоянии {}", userId, state);
        return bookingClient.getByUserId(userId, state);
    }

    /**
     * Получает все забронированные вещи пользователя по его идентификатору и состоянию.
     *
     * @param userId идентификатор пользователя, запрашивающего данные о бронировании
     * @param stateParam состояние бронирования
     * @return ResponseEntity с объектом, представляющим результат получения данных о бронировании
     */
    @GetMapping("/owner")
    public ResponseEntity<Object> getByOwnerId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @RequestParam(value = "state", defaultValue = "ALL",
                                                       required = false) String stateParam) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() ->
                        new ValidationException("Неизвестное состояние бронирования: " + stateParam));
        log.info("Получение всех забронированных вещей пользователя{}, в состоянии {}", userId, state);
        return bookingClient.getByOwnerId(userId, state);
    }
}