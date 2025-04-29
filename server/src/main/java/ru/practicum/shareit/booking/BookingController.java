package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    /**
     * • Добавление нового запроса на бронирование. Запрос может быть создан любым пользователем,
     * а затем подтверждён владельцем вещи. Эндпоинт — POST /bookings.
     * После создания запрос находится в статусе WAITING — «ожидает подтверждения».
     */
    @PostMapping
    public BookingDtoOutput addBooking(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                       @RequestBody BookingDtoInput bookingDto) {
        return BookingMapper.toBookingDtoOutput(bookingService.addBooking(bookerId, bookingDto));
    }

    /**
     * • Подтверждение или отклонение запроса на бронирование. Может быть выполнено только владельцем вещи.
     * Затем статус бронирования становится либо APPROVED, либо REJECTED.
     * Эндпоинт — PATCH /bookings/{bookingId}?approved={approved}, параметр approved может принимать
     * значения true или false.
     *
     * @param ownerId   ID владельца вещи.
     * @param bookingId ID брони.
     * @param approved  True - подтверждено, False - отклонено.
     * @return Обновленная бронь.
     */
    @PatchMapping("/{bookingId}")
    public BookingDtoOutput updateByOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                          @PathVariable Long bookingId,
                                          @RequestParam Boolean approved) {
        return BookingMapper.toBookingDtoOutput(bookingService.updateBooking(ownerId, bookingId, approved));
    }

    /**
     * • Получение данных о конкретном бронировании (включая его статус).
     * Может быть выполнено либо автором бронирования, либо владельцем вещи,
     * к которой относится бронирование.
     * Эндпоинт — GET /bookings/{bookingId}.
     */
    @GetMapping("/{bookingId}")
    public BookingDtoOutput getWithStatusById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @PathVariable Long bookingId) {
        return BookingMapper.toBookingDtoOutput(bookingService.getWithStatusById(userId, bookingId));
    }

    /**
     * • Получение списка всех бронирований текущего пользователя.
     * Эндпоинт — GET /bookings?state={state}.
     * Параметр state необязательный и по умолчанию равен ALL (англ. «все»).
     * Также он может принимать значения CURRENT (англ. «текущие»), PAST (англ. «завершённые»),
     * FUTURE (англ. «будущие»), WAITING (англ. «ожидающие подтверждения»), REJECTED (англ. «отклонённые»).
     * Бронирования должны возвращаться отсортированными по дате от более новых к более старым.
     */
    @GetMapping
    public List<BookingDtoOutput> getByUserId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam(value = "state",
                                                      defaultValue = "ALL", required = false) String state) {
        return bookingService.getByUserId(userId, state)
                .stream().map(BookingMapper::toBookingDtoOutput).toList();
    }

    /**
     * • Получение списка бронирований для всех вещей текущего пользователя.
     * Эндпоинт — GET /bookings/owner?state={state}.
     * Этот запрос имеет смысл для владельца хотя бы одной вещи.
     * Работа параметра state аналогична его работе в предыдущем сценарии.
     */
    @GetMapping("/owner")
    public List<BookingDtoOutput> getByOwnerId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @RequestParam(value = "state", defaultValue = "ALL",
                                                       required = false) String state) {
        return bookingService.getByOwnerId(userId, state)
                .stream().map(BookingMapper::toBookingDtoOutput).toList();
    }
}