package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.request.RequestDto;
import ru.practicum.shareit.user.User;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private User owner;

    private RequestDto request;

    private List<Comment> comments;

    private List<Booking> bookings;

    private Booking lastBooking;

    private Booking nextBooking;

}
