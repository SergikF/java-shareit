package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.RestrictedAccessException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    /**
     * Создание брони в БД.
     *
     * @param bookerId        пользователь, пытающийся забронировать вещь.
     * @param inputBookingDto создаваемая бронь.
     * @return бронь из БД.
     */
    @Override
    public Booking addBooking(Long bookerId, BookingDtoInput inputBookingDto) {
        Item itemFromDB = itemRepository.findById(inputBookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("При создании бронирования не найдена " +
                        "вещь с ID = " + inputBookingDto.getItemId() + " в БД."));
        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(inputBookingDto.getStart());
        bookingDto.setEnd(inputBookingDto.getEnd());
        bookingDto.setItem(itemFromDB);
        User bookerFromDb = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException("При " +
                        "создании бронирования не найден пользователь с ID = " + bookerId + " в БД."));
        validateBooking(bookingDto, itemFromDB, bookerFromDb);
        if (!itemFromDB.getAvailable()) {
            throw new ValidationException("Вещь нельзя забронировать, поскольку available = false.");
        }
        bookingDto.setStatus(BookingStatus.WAITING);
        bookingDto.setItem(itemFromDB);
        bookingDto.setBooker(bookerFromDb);
        Booking result = bookingRepository.save(BookingMapper.toBooking(bookingDto));
        log.info("Создано бронирование с ID = [ {} ].", result.getId());
        return result;
    }

    /**
     * Обновить бронь в БД.
     *
     * @param ownerId   хозяин вещи.
     * @param bookingId ID брони.
     * @param approved  True - подтверждение со стороны хозяина вещи,
     *                  False - отклонено хозяином вещи.
     * @return обновлённая бронь.
     */
    @Override
    public Booking updateBooking(Long ownerId, Long bookingId, Boolean approved) {
        Booking bookingFromBd = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException(
                "При обновлении бронирования не найдено бронирование с ID = '" + bookingId + "' в БД."));
        if (bookingFromBd.getStatus().equals(BookingStatus.APPROVED) && approved) {
            String message = "Данное бронирование уже было обработано и имеет статус '"
                    + bookingFromBd.getStatus() + "'.";
            log.info(message);
            throw new ValidationException(message);
        }
        User ownerFromDb = userRepository.findById(ownerId).orElseThrow(() -> new RestrictedAccessException("При " +
                "обновлении бронирования не найден пользователь с ID = '" + ownerId + "' в БД."));
        List<Item> items = ownerFromDb.getItems();
        for (Item i : ownerFromDb.getItems()) {
            if (i.getId().equals(bookingFromBd.getItem().getId())) {
                bookingFromBd.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
                Booking result = bookingRepository.save(bookingFromBd);
                log.info("Бронирование с ID = [ {} ] обновлено.", bookingId);
                return result;
            }
        }
        String message = "При обновлении брони у хозяина вещи эта вещь не найдена. Ошибка в запросе.";
        log.info(message);
        throw new NotFoundException(message);
    }

    /**
     * • Получение данных о конкретном бронировании (включая его статус).
     * Может быть выполнено либо автором бронирования, либо владельцем вещи,
     * к которой относится бронирование.
     *
     * @param userId    ID пользователя, делающего запрос.
     * @param bookingId ID брони.
     */
    @Override
    public Booking getWithStatusById(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с ID = '" + bookingId
                        + "не найдено в БД при его получении."));
        if (userId.equals(booking.getBooker().getId()) || userId.equals(booking.getItem().getOwner().getId())) {
            return booking;
        }
        throw new NotFoundException("Ошибка при получении брони с ID = '" + bookingId
                + "'. Пользователь с ID = '" + userId
                + "' не является ни хозяином, ни пользователем, забронировавшим вещь.");
    }

    /**
     * • Получение списка всех бронирований текущего пользователя.
     * Параметр state необязательный и по умолчанию равен ALL (англ. «все»).
     * Также он может принимать значения CURRENT (англ. «текущие»), PAST (англ. «завершённые»),
     * FUTURE (англ. «будущие»), WAITING (англ. «ожидающие подтверждения»), REJECTED (англ. «отклонённые»).
     * Бронирования должны возвращаться отсортированными по дате от более новых к более старым.
     *
     * @param userId ID пользователя.
     * @param state  статус бронирования.
     */
    @Override
    public List<Booking> getByUserId(Long userId, String state) {
        final LocalDateTime nowDateTime = LocalDateTime.now();
        BookingState bookingState;

        if (state.isBlank()) {
            bookingState = BookingState.ALL;
        } else {
            try {
                bookingState = BookingState.valueOf(state);
            } catch (IllegalArgumentException ex) {
                throw new ValidationException("Неизвестное состояние бронирования.");
            }
        }
        User bookerFromDb = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("При " +
                "получении списка бронирований не найден пользователь (арендующий) с ID = " + userId + " в БД."));
        List<Booking> result = new ArrayList<>();

        switch (bookingState) {
            case ALL: {
                result = bookingRepository.findAllByBookerOrderByStartDesc(bookerFromDb);
                break;
            }
            case CURRENT: {
                result = bookingRepository.findAllByBookerAndStartBeforeAndEndAfterOrderByStartDesc(
                        bookerFromDb, nowDateTime, nowDateTime);
                break;
            }
            case PAST: {
                result = bookingRepository.findAllByBookerAndEndIsBeforeOrderByStartDesc(
                        bookerFromDb, nowDateTime);
                break;
            }
            case FUTURE: {
                result = bookingRepository.findAllByBookerAndStartIsAfterOrderByStartDesc(
                        bookerFromDb, nowDateTime);
                break;
            }
            case WAITING: {
                result = bookingRepository.findAllByBookerAndStatusEqualsOrderByStartDesc(
                        bookerFromDb, BookingStatus.WAITING);
                break;
            }
            case REJECTED: {
                result = bookingRepository.findAllByBookerAndStatusEqualsOrderByStartDesc(
                        bookerFromDb, BookingStatus.REJECTED);
                break;
            }
            default: {
                throw new ValidationException("Неизвестное состояние бронирования.");
            }
        }

        return result;
    }

    /**
     * • Получение списка бронирований для всех вещей текущего пользователя, то есть хозяина вещей.
     *
     * @param userId ID хозяина вещей.
     * @param state  Параметр state необязательный и по умолчанию равен ALL (англ. «все»).
     *               Также он может принимать значения CURRENT (англ. «текущие»), PAST (англ. «завершённые»),
     *               FUTURE (англ. «будущие»), WAITING (англ. «ожидающие подтверждения»),
     *               REJECTED (англ. «отклонённые»).
     * @return Бронирования должны возвращаться отсортированными по дате от более новых к более старым.
     */
    @Override
    public List<Booking> getByOwnerId(Long userId, String state) {
        final LocalDateTime nowDateTime = LocalDateTime.now();
        BookingState bookingState;

        try {
            bookingState = BookingState.valueOf(state);
        } catch (IllegalArgumentException ex) {
            throw new ValidationException("Неизвестное состояние бронирования.");
        }
        User bookerFromDb = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("При " +
                "получении списка бронирований не найден хозяин с ID = " + userId + " в БД."));
        List<Booking> result = new ArrayList<>();

        switch (bookingState) {
            case ALL: {
                result = bookingRepository.findAllByItem_OwnerOrderByStartDesc(bookerFromDb);
                System.out.println(result);
                break;
            }
            case CURRENT: {
                result = bookingRepository.findAllByItem_OwnerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
                        bookerFromDb, nowDateTime, nowDateTime);
                break;
            }
            case PAST: {
                result = bookingRepository.findAllByItem_OwnerAndEndIsBeforeOrderByStartDesc(
                        bookerFromDb, nowDateTime);
                break;
            }
            case FUTURE: {
                result = bookingRepository.findAllByItem_OwnerAndStartIsAfterOrderByStartDesc(
                        bookerFromDb, nowDateTime);
                break;
            }
            case WAITING: {
                result = bookingRepository.findAllByItem_OwnerAndStatusEqualsOrderByStartDesc(
                        bookerFromDb, BookingStatus.WAITING);
                break;
            }
            case REJECTED: {
                result = bookingRepository.findAllByItem_OwnerAndStatusEqualsOrderByStartDesc(
                        bookerFromDb, BookingStatus.REJECTED);
                break;
            }
            default: {
                throw new ValidationException("Неизвестное состояние бронирования.");
            }
        }

        return result;
    }

    /**
     * Проверка при создании бронирования вещи.
     *
     * @param bookingDto бронь.
     * @param item       вещь.
     * @param booker     пользователь.
     */
    private void validateBooking(BookingDto bookingDto, Item item, User booker) {
        if (item.getOwner().equals(booker)) {
            String message = "Создать бронь на свою вещь нельзя.";
            log.info(message);
            throw new ValidationException(message);
        }

        if (bookingDto.getStart().equals(bookingDto.getEnd())) {
            String message = "Начало и окончание бронирования не может быть одним и тем же временем.";
            log.info(message);
            throw new ValidationException(message);
        }

        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            String message = "Окончание бронирования не может быть раньше его начала.";
            log.info(message);
            throw new ValidationException(message);
        }

        List<Booking> bookings = item.getBookings();
        if (!bookings.isEmpty()) {
            for (Booking b : bookings) {
                if (!(b.getEnd().isBefore(bookingDto.getStart()) ||
                        b.getStart().isAfter(bookingDto.getStart()))) {
                    String message = "Найдено пересечение дат бронирования на вещь с name = " + item.getName() + ".";
                    log.debug(message);
                    throw new ValidationException(message);
                }
            }
        }
    }
}
