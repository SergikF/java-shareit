package ru.practicum.shareit.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDtoInput {

    @NotNull(message = "Запрос не может быть пустым.")
    @NotBlank(message = "Запрос не может быть пустым.")
    private String description;

}
