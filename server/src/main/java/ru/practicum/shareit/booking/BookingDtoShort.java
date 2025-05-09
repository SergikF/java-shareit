package ru.practicum.shareit.booking;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDtoShort {

    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    @JsonProperty("status")
    private BookingStatus status;

}
