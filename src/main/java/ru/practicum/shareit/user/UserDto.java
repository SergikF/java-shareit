package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.validation.CreateObject;
import ru.practicum.shareit.validation.UpdateObject;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;

    @NotNull(groups = {CreateObject.class}, message = "Имя должно быть указано.")
    @NotBlank(groups = {CreateObject.class}, message = "Имя не может быть пустым.")
    private String name;

    @NotNull(groups = {CreateObject.class}, message = "Email должен быть указан.")
    @Email(groups = {CreateObject.class, UpdateObject.class}, message = "Email должен быть указан корректно.")
    private String email;

    private List<Item> items;

    private List<Booking> bookings;

    private List<Comment> comments;

}
