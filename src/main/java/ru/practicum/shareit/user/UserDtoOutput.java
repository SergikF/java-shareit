package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.BookingDtoShort;
import ru.practicum.shareit.item.CommentDtoShort;
import ru.practicum.shareit.item.ItemDtoShort;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoOutput {

    private Long id;

    private String name;

    private String email;

    private List<ItemDtoShort> items;

    private List<BookingDtoShort> bookings;

    private List<CommentDtoShort> comments;

}
