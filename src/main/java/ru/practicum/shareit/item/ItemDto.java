package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.validation.CreateObject;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    private Long id;

    @NotNull(groups = {CreateObject.class}, message = "Название вещи должно быть указано.")
    @NotBlank(groups = {CreateObject.class}, message = "Название вещи не может быть пустым.")
    private String name;

    @NotNull(groups = {CreateObject.class}, message = "Описание вещи должно быть указано.")
    @NotBlank(groups = {CreateObject.class}, message = "Описание вещи не может быть пустым.")
    private String description;

    @NotNull(groups = {CreateObject.class}, message = "Доступность вещи должна быть указана.")
    private Boolean available;

    private User owner;

    private Request request;

    private List<Comment> comments;

    private List<Booking> bookings;

    private Booking lastBooking;

    private Booking nextBooking;

}
