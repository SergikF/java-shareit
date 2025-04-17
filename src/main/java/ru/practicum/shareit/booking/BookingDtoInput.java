package ru.practicum.shareit.booking;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validation.CreateObject;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDtoInput {

    @NotNull(groups = {CreateObject.class}, message = "При создании брони должна быть информация о вещи.")
    private Long itemId;

    @FutureOrPresent(groups = {CreateObject.class}, message = "Дата не должна быть в прошлом")
    @NotNull(groups = {CreateObject.class}, message = "Дата не должна быть пустой")
    private LocalDateTime start;

    @FutureOrPresent(groups = {CreateObject.class}, message = "Дата не должна быть в прошлом")
    @NotNull(groups = {CreateObject.class}, message = "Дата не должна быть пустой")
    private LocalDateTime end;

}
