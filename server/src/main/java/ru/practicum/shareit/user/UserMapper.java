package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.CommentMapper;

import java.util.Collections;
import java.util.Optional;

@Component
public class UserMapper {

    public static UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }
        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setItems(user.getItems());
        userDto.setBookings(user.getBookings());
        userDto.setComments(user.getComments());

        return userDto;
    }

    public static UserDtoOutput toUserDtoOutput(User user) {
        if (user == null) {
            return null;
        }
        UserDtoOutput userDto = new UserDtoOutput();

        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setItems(Optional.ofNullable(user.getItems())
                .map(items -> items.stream().map(ItemMapper::toItemDtoShort).toList())
                .orElse(Collections.emptyList()));
        userDto.setBookings(Optional.ofNullable(user.getBookings())
                .map(bookings -> bookings.stream().map(BookingMapper::toBookingDtoShort).toList())
                .orElse(Collections.emptyList()));
        userDto.setComments(Optional.ofNullable(user.getComments())
                .map(comments -> comments.stream().map(CommentMapper::toCommentDtoShort).toList())
                .orElse(Collections.emptyList()));

        return userDto;
    }

    public static UserDtoShort toUserDtoShort(User user) {
        if (user == null) {
            return null;
        }
        UserDtoShort userDto = new UserDtoShort();

        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());

        return userDto;
    }

    public static User toUser(UserDto userDto) {
        User user = new User();

        if (userDto.getId() != null) {
            if (userDto.getId() > 0) {
                user.setId(userDto.getId());
            }
        }
        if (userDto.getName() != null) {
            user.setName(userDto.getName().trim());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail().trim());
        }
        if (userDto.getItems() != null) {
            if (!userDto.getItems().isEmpty()) {
                user.setItems(userDto.getItems());
            }
        }
        if (userDto.getBookings() != null) {
            if (!userDto.getBookings().isEmpty()) {
                user.setBookings(userDto.getBookings());
            }
        }
        if (userDto.getComments() != null) {
            if (!userDto.getComments().isEmpty()) {
                user.setComments(userDto.getComments());
            }
        }

        return user;
    }

}
