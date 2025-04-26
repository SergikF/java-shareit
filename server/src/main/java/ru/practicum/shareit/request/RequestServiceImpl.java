package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    /**
     * Добавляет новый запрос в базу данных.
     *
     * @param requestorId идентификатор пользователя, который создает запрос
     * @param requestDto объект, содержащий данные для создания запроса
     * @return созданный запрос
     * @throws NotFoundException если пользователь с указанным идентификатором не найден в базе данных
     */
    @Override
    public Request addRequest(Long requestorId, RequestDtoInput requestDto) {
        User requestor = new User();
        Request request = new Request();
        requestor = userRepository.findById(requestorId).orElse(null);
        if (requestor == null) {
            String error = "Пользователь с id [ " + requestorId + " ] не найден в БД при добавлении запроса.";
            log.info(error);
            throw new NotFoundException(error);
        }
        request.setDescription(requestDto.getDescription());
        request.setRequestor(requestor);
        request.setCreated(LocalDateTime.now());
        request = requestRepository.save(request);
        log.info("Запрос [ {} ] добавлен в БД.",request);
        return request;
    }

    /**
     * Возвращает список запросов, созданных определенным пользователем, отсортированный по дате создания в обратном порядке.
     *
     * @param requestorId идентификатор пользователя, чьи запросы нужно получить
     * @return список запросов, созданных пользователем
     */
    @Override
    public List<Request> getRequests(Long requestorId) {
        User requestor = new User();
        requestor = userRepository.findById(requestorId).orElse(null);
        if (requestor == null) {
            log.info("Пользователь с id [ {} ] не найден в БД при получении своего списка запросов - вернулся пустой список.", requestorId);
            return List.of();
        }
        List<Request> requests = requestRepository.findAllByRequestorOrderByCreatedDesc(requestor);
        log.info("Список запросов пользователя [ {} ] получен из БД в количестве [ {} ].", requestorId, requests.size());
        return requests;
    }

    /**
     * Возвращает список всех запросов, отсортированных по дате создания в обратном порядке.
     * Если передан идентификатор пользователя, то возвращаются все запросы,
     * кроме тех, которые были созданы этим пользователем.
     *
     * @param requestorId идентификатор пользователя, чьи запросы нужно исключить из списка (может быть null)
     * @return список всех запросов, отсортированных по дате создания в обратном порядке
     */
    @Override
    public List<Request> getAllRequests(Long requestorId) {
        User requestor = new User();
        requestor = userRepository.findById(requestorId).orElse(null);
        if (requestor == null) {
            List<Request> requests = requestRepository.findAllByOrderByCreatedDesc();
            log.info("Список доступных запросов получен из БД в количестве [ {} ].", requests.size());
            return requests;
        }
        List<Request> requests = requestRepository.findAllByRequestorNotOrderByCreatedDesc(requestor);
        log.info("Список доступных запросов ( кроме запросов пользователя [ {} ] ) получен из БД в количестве [ {} ].",
                requestorId, requests.size());
        return requests;
    }

    /**
     * Возвращает запрос по его идентификатору.
     *
     * @param requestId идентификатор запроса
     * @return запрос с указанным идентификатором
     * @throws NotFoundException если запрос с указанным идентификатором не найден в базе данных
     */
    @Override
    public Request getRequestById(Long requestId) {
        Request request = requestRepository.findById(requestId).orElse(null);
        if (request == null) {
            String error = "Запрос с id [ " + requestId + " ] не найден в БД при получении.";
            log.info(error);
            throw new NotFoundException(error);
        }
        return request;
    }
}
