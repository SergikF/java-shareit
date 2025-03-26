package ru.practicum.shareit.item;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.validation.CreateObject;

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

    private ItemRequest request;




}
