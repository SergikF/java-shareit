package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.request.RequestMapper;
import ru.practicum.shareit.user.UserMapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        if (item == null) {
            return null;
        }
        ItemDto itemDto = new ItemDto();

        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setOwner(item.getOwner());
        itemDto.setRequest(RequestMapper.toRequestDto(item.getRequest()));
        itemDto.setComments(item.getComments());
        itemDto.setBookings(item.getBookings());

        return itemDto;
    }

    public static ItemDtoOutput toItemDtoOutput(Item item, Long userId) {
        if (item == null) {
            return null;
        }
        ItemDtoOutput itemDto = new ItemDtoOutput();

        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setOwner(UserMapper.toUserDtoShort(item.getOwner()));
        itemDto.setRequest(RequestMapper.toRequestDto(item.getRequest()));
        List<Comment> comments = Optional.ofNullable(item.getComments()).orElse(Collections.emptyList());
        if (!comments.isEmpty()) {
            itemDto.setComments(comments.stream().map(CommentMapper::toCommentDtoShort).toList());
        } else {
            itemDto.setComments(Collections.emptyList());
        }
        List<Booking> bookings = Optional.ofNullable(item.getBookings()).orElse(Collections.emptyList());
        if (!bookings.isEmpty()) {
            if (itemDto.getOwner().getId().equals(userId)) {
                itemDto.setBookings(bookings.stream().map(BookingMapper::toBookingDtoShort).toList());
                itemDto.setLastBooking(BookingMapper.toBookingDtoShort(findLastBooking(bookings)));
                itemDto.setNextBooking(BookingMapper.toBookingDtoShort(findNextBooking(bookings)));
            } else {
                itemDto.setBookings(Collections.emptyList());
                itemDto.setLastBooking(null);
                itemDto.setNextBooking(null);
            }
        } else {
            itemDto.setBookings(Collections.emptyList());
            itemDto.setLastBooking(null);
            itemDto.setNextBooking(null);
        }

        return itemDto;
    }

    public static ItemDtoShort toItemDtoShort(Item item) {
        if (item == null) {
            return null;
        }
        ItemDtoShort itemDto = new ItemDtoShort();

        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());

        return itemDto;
    }

    public static ItemDtoRequest toItemDtoRequest(Item item) {
        if (item == null) {
            return null;
        }
        ItemDtoRequest itemDto = new ItemDtoRequest();

        itemDto.setIdItem(item.getId());
        itemDto.setName(item.getName());
        itemDto.setIdOwner(item.getOwner().getId());
        itemDto.setOwner(item.getOwner().getName());

        return itemDto;
    }

    public static Item toItem(ItemDto itemDto) {
        Item item = new Item();

        if (itemDto.getId() != null) {
            if (itemDto.getId() > 0) {
                item.setId(itemDto.getId());
            }
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName().trim());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription().trim());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        if (itemDto.getOwner().getId() != null) {
            if (itemDto.getOwner().getId() > 0) {
                item.setOwner(itemDto.getOwner());
            }
        }
        if (itemDto.getRequest() != null) {
            if (itemDto.getRequest().getId() > 0) {
                item.setRequest(RequestMapper.toRequest(itemDto.getRequest()));
            }
        }
        if (itemDto.getComments() != null) {
            if (!itemDto.getComments().isEmpty()) {
                item.setComments(itemDto.getComments());
            }
        }
        if (itemDto.getBookings() != null) {
            if (!itemDto.getBookings().isEmpty()) {
                item.setBookings(itemDto.getBookings());
            }
        }

        return item;
    }

    /**
     * Метод поиска первой аренды после текущего момента времени.
     *
     * @param bookings список бронирований.
     * @return следующее бронирование после текущего момента времени.
     */
    private static Booking findNextBooking(List<Booking> bookings) {
        return Optional.ofNullable(bookings)
                .filter(list -> !list.isEmpty())
                .flatMap(list -> list.stream()
                        .filter(b -> b.getStart().isAfter(LocalDateTime.now()))
                        .filter(b -> b.getStatus().equals(BookingStatus.APPROVED) ||
                                b.getStatus().equals(BookingStatus.WAITING))
                        .min(Comparator.comparing(Booking::getStart)))
                .orElse(null);
    }

    /**
     * Метод поиска последней аренды до текущего момента времени.
     *
     * @param bookings список бронирований.
     * @return последнее бронирование до текущего момента времени.
     */
    private static Booking findLastBooking(List<Booking> bookings) {
        return Optional.ofNullable(bookings)
                .filter(list -> !list.isEmpty())
                .flatMap(list -> list.stream()
                        .filter(b -> b.getEnd().isBefore(LocalDateTime.now()))
                        .filter(b -> b.getStatus().equals(BookingStatus.APPROVED))
                        .max(Comparator.comparing(Booking::getEnd)))
                .orElse(null);
    }
}
