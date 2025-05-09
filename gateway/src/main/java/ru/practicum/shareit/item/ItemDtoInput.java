package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validation.CreateObject;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDtoInput {

    @NotNull(groups = {CreateObject.class}, message = "Название вещи должно быть указано.")
    @NotBlank(groups = {CreateObject.class}, message = "Название вещи не может быть пустым.")
    private String name;

    @NotNull(groups = {CreateObject.class}, message = "Описание вещи должно быть указано.")
    @NotBlank(groups = {CreateObject.class}, message = "Описание вещи не может быть пустым.")
    private String description;

    @NotNull(groups = {CreateObject.class}, message = "Доступность вещи должна быть указана.")
    private Boolean available;

    private Long requestId;

}
