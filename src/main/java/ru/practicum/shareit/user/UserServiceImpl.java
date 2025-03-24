package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DataConflictException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Метод для добавления нового пользователя.
     *
     * @param userDto объект UserDto, содержащий данные нового пользователя
     * @return объект User, содержащий данные добавленного пользователя
     * @throws DataConflictException если пользователь с указанным email уже существует в БД
     */
    @Override
    public User addUser(UserDto userDto) {
        Optional<User> oldUser = userRepository.getUserByEmail(userDto.getEmail());
        if (oldUser.isPresent()) {
            String error = "Пользователь с email [ "+userDto.getEmail()+" ] уже существует в БД. " +
                    "Добавление пользователя невозможно.";
            log.error(error);
            throw new DataConflictException(error);
        }
        User result = userRepository.save(UserMapper.toUser(userDto));
        log.info("Добавлен пользователь [ {} ]", result);
        return result;
    }

    /**
     * Метод для обновления данных пользователя.
     *
     * @param idUser идентификатор пользователя
     * @param userDto объект UserDto, содержащий новые данные пользователя
     * @return объект User, содержащий обновленные данные пользователя
     * @throws DataConflictException если пользователь с указанным email уже существует в БД
     * @throws NotFoundException если пользователь с указанным id не найден в БД
     */
    @Override
    public User updateUser(Long idUser, UserDto userDto) {
        Optional<User> oldUser = userRepository.getUserByEmail(userDto.getEmail());
        if (oldUser.isPresent()) {
            String error = "Пользователь с email [ "+userDto.getEmail()+" ] уже существует в БД. " +
                        "Обновление данных пользователя невозможно.";
            log.error(error);
            throw new DataConflictException(error);
        }
        User newUser = UserMapper.toUser(userDto);
        newUser.setId(idUser);
        oldUser = userRepository.findById(idUser);
        if (oldUser.isPresent()) {
            if (newUser.getName() == null) {
                newUser.setName(oldUser.get().getName());
            }
            if (newUser.getEmail() == null ) {
                newUser.setEmail(oldUser.get().getEmail());
            }
        } else {
            String error = "Пользователь с id [ "+idUser+" ] не найден в БД при обновлении данных пользователя.";
            log.info(error);
            throw new NotFoundException(error);
        }
        userRepository.save(newUser);
        log.info("Обновлен пользователь [ {} ]", newUser);
        return newUser;
    }


    /**
     * Метод для получения данных пользователя по идентификатору.
     *
     * @param idUser идентификатор пользователя
     * @return объект User, содержащий данные пользователя
     * @throws NotFoundException если пользователь с указанным id не найден в БД
     */
    @Override
    public User getUserById(Long idUser) {
        Optional<User> oldUser = userRepository.findById(idUser);
        if (oldUser.isEmpty()) {
            String error = "Пользователь с id [ "+idUser+" ] не найден в БД при запросе данных пользователя.";
            log.info(error);
            throw new NotFoundException(error);
        }
        log.info("По id [ {} ] успешно получены данные пользователя [ {} ].", idUser, oldUser.get());
        return oldUser.get();
    }

    /**
     * Метод для получения списка всех пользователей.
     *
     * @return список всех пользователей, отсортированных по имени
     */
    @Override
    public List<User> getAllUsers() {
        List<User> result = userRepository.findAll(Sort.by("name"));
        log.info("Получен список всех пользователей : [ {} ]", result);
        return result;
    }

    /**
     * Метод для удаления пользователя по идентификатору.
     *
     * @param idUser идентификатор пользователя
     * @throws NotFoundException если пользователь с указанным id не найден в БД
     */
    @Override
    public void removeUser(Long idUser) {
        Optional<User> oldUser = userRepository.findById(idUser);
        if (oldUser.isEmpty()) {
            String error = "Пользователь с id [ "+idUser+" ] не найден в БД при удалении пользователя.";
            log.info(error);
            throw new NotFoundException(error);
        }
        userRepository.delete(oldUser.get());
        log.info("По id [ {} ] успешно удален пользователь [ {} ].", idUser, oldUser.get());
    }

}