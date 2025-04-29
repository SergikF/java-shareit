package ru.practicum.shareit.request;

import java.util.List;

public interface RequestService {

    Request addRequest(Long requestorId, RequestDtoInput requestDto);

    List<Request> getRequests(Long requestorId);

    List<Request> getAllRequests(Long requestorId);

    Request getRequestById(Long requestId);

}
