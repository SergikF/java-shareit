package ru.practicum.shareit.booking;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDtoInput {

    @NotNull(message = "При создании брони должна быть информация о вещи.")
    private Long itemId;

    @FutureOrPresent(message = "Дата начала бронирования не должна быть в прошлом")
    @NotNull(message = "Дата бронирования не должна быть пустой")
    private LocalDateTime start;

    @Future(message = "Дата завершения бронирования должна быть в будущем")
    @NotNull(message = "Дата бронирования не должна быть пустой")
    private LocalDateTime end;

}
