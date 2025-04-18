package ru.practicum.shareit.booking;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.ItemDtoShort;
import ru.practicum.shareit.user.UserDtoShort;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDtoOutput {

    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private ItemDtoShort item;

    private UserDtoShort booker;

    @JsonProperty("status")
    private BookingStatus status;

}
