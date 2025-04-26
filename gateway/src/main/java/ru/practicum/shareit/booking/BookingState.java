package ru.practicum.shareit.booking;

import java.util.Optional;

public enum BookingState {
    ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED;

    public static Optional<ru.practicum.shareit.booking.BookingState> from(String stringState) {
        for (ru.practicum.shareit.booking.BookingState state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
