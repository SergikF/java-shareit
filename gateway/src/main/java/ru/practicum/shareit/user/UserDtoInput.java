package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validation.CreateObject;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoInput {

    @NotNull(groups = {CreateObject.class}, message = "Имя должно быть указано.")
    @NotBlank(groups = {CreateObject.class}, message = "Имя не может быть пустым.")
    private String name;

    @NotNull(groups = {CreateObject.class}, message = "Email должен быть указан.")
    @Email(groups = {CreateObject.class}, message = "Email должен быть указан корректно.")
    private String email;

}
