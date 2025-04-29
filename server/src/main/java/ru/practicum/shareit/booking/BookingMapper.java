package ru.practicum.shareit.booking;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.UserMapper;

@Component
public class BookingMapper {

    public static BookingDtoOutput toBookingDtoOutput(Booking booking) {
        if (booking == null) {
            return null;
        }
        BookingDtoOutput bookingDto = new BookingDtoOutput();

        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setItem(ItemMapper.toItemDtoShort(booking.getItem()));
        bookingDto.setBooker(UserMapper.toUserDtoShort(booking.getBooker()));
        bookingDto.setStatus(booking.getStatus());

        return bookingDto;
    }

    public static BookingDtoShort toBookingDtoShort(Booking booking) {
        if (booking == null) {
            return null;
        }
        BookingDtoShort bookingDto = new BookingDtoShort();

        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setStatus(booking.getStatus());

        return bookingDto;
    }

    public static Booking toBooking(BookingDto bookingDto) {
        Booking booking = new Booking();

        if (bookingDto.getId() != null) {
            if (bookingDto.getId() > 0) {
                booking.setId(bookingDto.getId());
            }
        }
        if (bookingDto.getStart() != null) {
            booking.setStart(bookingDto.getStart());
        }
        if (bookingDto.getEnd() != null) {
            booking.setEnd(bookingDto.getEnd());
        }
        if (bookingDto.getItem() != null) {
            booking.setItem(bookingDto.getItem());
        }
        if (bookingDto.getBooker() != null) {
            booking.setBooker(bookingDto.getBooker());
        }
        if (bookingDto.getStatus() != null) {
            booking.setStatus(bookingDto.getStatus());
        }

        return booking;
    }

}
