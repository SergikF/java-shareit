package ru.practicum.shareit.item;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.RestrictedAccessException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.request.RequestMapper;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final RequestRepository requestRepository;

    /**
     * Метод для добавления новой вещи.
     *
     * @param idUser  идентификатор пользователя
     * @param inputItemDto объект ItemDtoInput, содержащий данные новой вещи
     * @return объект Item, содержащий данные добавленной вещи
     * @throws ValidationException если идентификатор пользователя не указан
     * @throws NotFoundException   если пользователь с указанным id не найден в БД
     */
    @Override
    public Item addItem(Long idUser, ItemDtoInput inputItemDto) {
        ItemDto itemDto = new ItemDto();
        User user = userRepository.findById(idUser).orElse(null);
        if (user == null) {
            String error = "Пользователь с id [ " + idUser + " ] не найден в БД при добавлении вещи.";
            log.info(error);
            throw new NotFoundException(error);
        }
        itemDto.setOwner(user);
        itemDto.setName(inputItemDto.getName());
        itemDto.setDescription(inputItemDto.getDescription());
        itemDto.setAvailable(inputItemDto.getAvailable());
        if (inputItemDto.getRequestId() == null) {
            itemDto.setRequest(null);
        } else {
            itemDto.setRequest(RequestMapper.toRequestDto(requestRepository.findById(inputItemDto.getRequestId()).orElse(null)));
        }
        Item result = ItemMapper.toItem(itemDto);
        itemRepository.save(result);
        log.info("Добавлена вещь [ {} ] пользователем [ {} ]", result.getId(), idUser);
        return result;
    }

    /**
     * Метод для обновления данных вещи.
     *
     * @param idUser  идентификатор пользователя
     * @param idItem  идентификатор вещи
     * @param itemDtoInput объект ItemDtoInput, содержащий новые данные вещи
     * @return объект Item, содержащий обновленные данные вещи
     * @throws ValidationException       если идентификатор пользователя не указан
     * @throws NotFoundException         если пользователь или вещь с указанным id не найдены в БД
     * @throws RestrictedAccessException если пользователь не является владельцем вещи
     */
    @Override
    public Item updateItem(Long idUser, Long idItem, ItemDtoInput itemDtoInput) {
        ItemDto itemDto = new ItemDto();
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
        itemDto.setId(idItem);
        itemDto.setOwner(oldItem.get().getOwner());
        itemDto.setRequest(RequestMapper.toRequestDto(oldItem.get().getRequest()));
        if (itemDtoInput.getName() == null) {
            itemDto.setName(oldItem.get().getName());
        } else {
            itemDto.setName(itemDtoInput.getName());
        }
        if (itemDtoInput.getDescription() == null) {
            itemDto.setDescription(oldItem.get().getDescription());
        } else {
            itemDto.setDescription(itemDtoInput.getDescription());
        }
        if (itemDtoInput.getAvailable() == null) {
            itemDto.setAvailable(oldItem.get().getAvailable());
        } else {
            itemDto.setAvailable(itemDtoInput.getAvailable());
        }
        Item newItem = ItemMapper.toItem(itemDto);
        itemRepository.save(newItem);
        log.info("Обновлены данные вещи [ {} ]", ItemMapper.toItemDtoShort(newItem));
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
        log.info("Получен список всех вещей пользователя [ {} ] : [ {} ]", UserMapper.toUserDtoShort(user.get()), result.size());
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
        log.info("Получен список вещей по поисковому запросу [ {} ] : [ {} ]", text, result.size());
        return result;
    }

    /**
     * Добавить комментарий к вещи пользователем, действительно бравшим вещь в аренду.
     *
     * @param bookerId ID пользователя, добавляющего комментарий.
     * @param itemId   ID вещи, которой оставляется комментарий.
     */
    @Override
    @Transactional
    public Comment saveComment(Long bookerId, Long itemId, CommentDto commentDto) {

        User userFromBd = userRepository.findById(bookerId).orElseThrow(() ->
                new NotFoundException("Ошибка при сохранении комментария к вещи с ID = " + itemId
                        + " пользователя с ID = " + bookerId + " в БД. В БД отсутствует запись о пользователе."));
        Item itemFromBd = itemRepository.findById(itemId).orElseThrow(() ->
                new NotFoundException("Ошибка при сохранении комментария к вещи с ID = " + itemId
                        + " пользователя с ID = " + bookerId + " в БД. В БД отсутствует запись о вещи."));
        List<Booking> bookings = itemFromBd.getBookings();
        boolean isBooker = false;
        for (Booking b : bookings) {
            if (b.getBooker().getId().equals(bookerId) && b.getEnd().isBefore(LocalDateTime.now())) {
                isBooker = true;
                break;
            }
        }
        if (!isBooker) {
            throw new ValidationException("Ошибка при сохранении комментария к вещи с ID = " + itemId
                    + " пользователя с ID = " + bookerId + " в БД. Пользователь не арендовал эту вещь.");
        }
        commentDto.setItem(itemFromBd);
        commentDto.setUser(userFromBd);
        commentDto.setCreated(LocalDateTime.now());
        Comment result = CommentMapper.toComment(commentDto);
        result = commentRepository.save(result);
        log.info("Комментарий к вещи успешно добавлен. Данные комментария: {}", CommentMapper.toCommentDtoShort(result));
        return result;
    }

}
