package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDtoInput {

    @NotNull(message = "Комментарий должен быть указан.")
    @NotBlank(message = "Комментарий не может быть пустым.")
    private String text;

}
