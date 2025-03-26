package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.RestrictedAccessException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    /**
     * Метод для добавления новой вещи.
     *
     * @param idUser  идентификатор пользователя
     * @param itemDto объект ItemDto, содержащий данные новой вещи
     * @return объект Item, содержащий данные добавленной вещи
     * @throws ValidationException если идентификатор пользователя не указан
     * @throws NotFoundException   если пользователь с указанным id не найден в БД
     */
    @Override
    public Item addItem(Long idUser, ItemDto itemDto) {
        if (userRepository.findById(idUser).isEmpty()) {
            String error = "Пользователь с id [ " + idUser + " ] не найден в БД при добавлении вещи.";
            log.info(error);
            throw new NotFoundException(error);
        }
        Item result = ItemMapper.toItem(itemDto);
        result.setOwner(userRepository.findById(idUser).get());
        itemRepository.save(result);
        log.info("Добавлена вещь [ {} ] пользователем [ {} ]", result, idUser);
        return result;
    }

    /**
     * Метод для обновления данных вещи.
     *
     * @param idUser  идентификатор пользователя
     * @param idItem  идентификатор вещи
     * @param itemDto объект ItemDto, содержащий новые данные вещи
     * @return объект Item, содержащий обновленные данные вещи
     * @throws ValidationException       если идентификатор пользователя не указан
     * @throws NotFoundException         если пользователь или вещь с указанным id не найдены в БД
     * @throws RestrictedAccessException если пользователь не является владельцем вещи
     */
    @Override
    public Item updateItem(Long idUser, Long idItem, ItemDto itemDto) {
        if (userRepository.findById(idUser).isEmpty()) {
            String error = "Пользователь с id [ " + idUser + " ] не найден в БД при изменение данных вещи.";
            log.info(error);
            throw new NotFoundException(error);
        }
        Optional<Item> oldItem = itemRepository.findById(idItem);
        if (oldItem.isEmpty()) {
            String error = "Вещь с id [ " + idItem + " ] не найдена в БД при изменение данных вещи.";
            log.info(error);
            throw new NotFoundException(error);
        }
        if (!idUser.equals(oldItem.get().getOwner().getId())) {
            String error = "Пользователь с id [ " + idUser + " ] не является владельцем вещи с id [ " + idItem + " ].";
            log.info(error);
            throw new RestrictedAccessException(error);
        }
        Item newItem = ItemMapper.toItem(itemDto);
        newItem.setId(idItem);
        newItem.setOwner(oldItem.get().getOwner());
        newItem.setRequest(oldItem.get().getRequest());
        if (newItem.getName() == null) {
            newItem.setName(oldItem.get().getName());
        }
        if (newItem.getDescription() == null) {
            newItem.setDescription(oldItem.get().getDescription());
        }
        if (newItem.getAvailable() == null) {
            newItem.setAvailable(oldItem.get().getAvailable());
        }
        itemRepository.save(newItem);
        log.info("Обновлены данные вещи [ {} ]", newItem);
        return newItem;
    }

    /**
     * Метод для получения данных вещи по идентификатору.
     *
     * @param idItem идентификатор вещи
     * @return объект Item, содержащий данные вещи
     * @throws NotFoundException если вещь с указанным id не найдена в БД
     */
    @Override
    public Item getItemById(Long idItem) {
        Optional<Item> oldItem = itemRepository.findById(idItem);
        if (oldItem.isEmpty()) {
            String error = "Вещь с id [ " + idItem + " ] не найдена в БД при запросе данных вещи.";
            log.info(error);
            throw new NotFoundException(error);
        }
        log.info("По вещи с id [ {} ] успешно получены данные.", idItem);
        return oldItem.get();
    }

    /**
     * Метод для удаления вещи по идентификатору.
     *
     * @param idUser идентификатор пользователя
     * @param idItem идентификатор вещи
     * @throws ValidationException если идентификатор пользователя не указан
     * @throws NotFoundException   если пользователь или вещь с указанным id не найдены в БД
     */
    @Override
    public void removeItem(Long idUser, Long idItem) {
        if (userRepository.findById(idUser).isEmpty()) {
            String error = "Пользователь с id [ " + idUser + " ] не найден в БД при удалении данных о вещи.";
            log.info(error);
            throw new NotFoundException(error);
        }
        if (itemRepository.findById(idItem).isEmpty()) {
            String error = "Вещь с id [ " + idItem + " ] не найдена в БД при удалении данных о вещи.";
            log.info(error);
            throw new NotFoundException(error);
        }
        itemRepository.delete(itemRepository.findById(idItem).get());
        log.info("Вещь с id [ {} ] успешно удалена.", idItem);
    }

    /**
     * Метод для получения списка всех вещей пользователя.
     *
     * @param idUser идентификатор пользователя
     * @return список всех вещей пользователя, отсортированных по имени
     * @throws ValidationException если идентификатор пользователя не указан
     * @throws NotFoundException   если пользователь с указанным id не найден в БД
     */
    @Override
    public List<Item> getAllItems(Long idUser) {
        Optional<User> user = userRepository.findById(idUser);
        if (user.isEmpty()) {
            String error = "Пользователь с id [ " + idUser + " ] не найден в БД при изменение данных вещи.";
            log.info(error);
            throw new NotFoundException(error);
        }
        List<Item> result = itemRepository.findAllByOwner(user.get(), Sort.by("name"));
        log.info("Получен список всех вещей пользователя [ {} ] : [ {} ]", user.get(), result);
        return result;
    }

    /**
     * Метод для поиска вещей по поисковому запросу.
     *
     * @param text поисковый запрос
     * @return список вещей, в названии или описании которых присутствует поисковый запрос
     */
    @Override
    public List<Item> searchItems(String text) {
        if (text.isBlank()) {
            return List.of();
        }
        List<Item> result = itemRepository.findAllByText(text);
        log.info("Получен список вещей по поисковому запросу [ {} ] : [ {} ]", text, result);
        return result;
    }

}
