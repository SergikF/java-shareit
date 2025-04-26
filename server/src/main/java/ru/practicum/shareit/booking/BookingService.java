package ru.practicum.shareit.booking;

import java.util.List;

public interface BookingService {

    Booking addBooking(Long bookerId, BookingDtoInput bookingDto);

    Booking updateBooking(Long ownerId, Long bookingId, Boolean approved);

    Booking getWithStatusById(Long userId, Long bookingId);

    List<Booking> getByUserId(Long userId, String state);

    List<Booking> getByOwnerId(Long userId, String state);
}
