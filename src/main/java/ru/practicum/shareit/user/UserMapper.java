package ru.practicum.shareit.user;

public class UserMapper {

    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getName(),
                user.getEmail()
        );
    }

    public static User toUser(UserDto userDto) {
        if (userDto.getEmail() != null) {
            userDto.setEmail(userDto.getEmail().trim());
        }
        if (userDto.getName() != null) {
            userDto.setName(userDto.getName().trim());
        }
        return new User(
                null,
                userDto.getName(),
                userDto.getEmail()
        );
    }

}
