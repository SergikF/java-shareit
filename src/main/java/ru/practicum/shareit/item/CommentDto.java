package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.validation.CreateObject;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private Long id;

    @NotNull(groups = {CreateObject.class}, message = "комментарий должен быть указан.")
    @NotBlank(groups = {CreateObject.class}, message = "Комментарий не может быть пустым.")
    private String text;

    private Item item;

    private User user;

    private LocalDateTime created;

}
