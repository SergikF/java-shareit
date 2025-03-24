package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {

    User addUser(UserDto userDto);

    User updateUser(Long idUser,UserDto userDto);

    User getUserById(Long idUser);

    void removeUser(Long idUser);

    List<User> getAllUsers();

}
