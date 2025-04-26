package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.BookingDtoShort;
import ru.practicum.shareit.request.RequestDto;
import ru.practicum.shareit.user.UserDtoShort;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDtoOutput {

    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private UserDtoShort owner;

    private RequestDto request;

    private List<CommentDtoShort> comments;

    private List<BookingDtoShort> bookings;

    private BookingDtoShort lastBooking;

    private BookingDtoShort nextBooking;

}
