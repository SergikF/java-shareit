package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.exception.ValidationException;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> addBooking(Long userId, BookingDtoInput requestDto) {
        if (requestDto.getStart().equals(requestDto.getEnd())) {
            throw new ValidationException("Начало и окончание бронирования не может быть одним и тем же моментом.");
        }
        if (requestDto.getEnd().isBefore(requestDto.getStart())) {
            throw new ValidationException("Окончание бронирования не может быть раньше его начала.");
        }
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> updateByOwner(Long userId, Long bookingId, Boolean approved) {
        Map<String, Object> parameters = Map.of(
                "approved", approved
        );
        return patch("/" + bookingId + "?approved={approved}", userId, parameters, null);
    }

    public ResponseEntity<Object> getWithStatusById(Long bookingId, Long userId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getByUserId(Long userId, BookingState state) {
        return get("?state=" + state, userId);
    }

    public ResponseEntity<Object> getByOwnerId(Long userId, BookingState state) {
        return get("/owner?state=" + state, userId);
    }
}
