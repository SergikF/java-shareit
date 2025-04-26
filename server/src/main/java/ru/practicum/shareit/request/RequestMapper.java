package ru.practicum.shareit.request;

import ru.practicum.shareit.item.ItemMapper;

public class RequestMapper {

    public static RequestDto toRequestDto(Request request) {
        if (request == null) {
            return null;
        }
        RequestDto requestDto = new RequestDto();

        requestDto.setId(request.getId());
        requestDto.setDescription(request.getDescription());
        requestDto.setRequestor(request.getRequestor());
        requestDto.setCreated(request.getCreated());

        return requestDto;
    }

    public static RequestDtoItems toRequestDtoItems(Request request) {
        if (request == null) {
            return null;
        }
        RequestDtoItems requestDto = new RequestDtoItems();

        requestDto.setId(request.getId());
        requestDto.setDescription(request.getDescription());
        requestDto.setRequestor(request.getRequestor());
        requestDto.setCreated(request.getCreated());
        requestDto.setItems(request.getItems().stream().map(ItemMapper::toItemDtoRequest).toList());

        return requestDto;
    }


    public static Request toRequest(RequestDto requestDto) {
        Request request = new Request();

        if (requestDto.getId() != null) {
            if (requestDto.getId() > 0) {
                request.setId(requestDto.getId());
            }
        }
        if (requestDto.getDescription() != null) {
            if (!requestDto.getDescription().trim().isEmpty()) {
                request.setDescription(requestDto.getDescription().trim());
            }
        }
        if (requestDto.getRequestor().getId() != null) {
            if (requestDto.getRequestor().getId() > 0) {
                request.setRequestor(requestDto.getRequestor());
            }
        }
        if (requestDto.getCreated() != null) {
            request.setCreated(requestDto.getCreated());
        }

        return request;
    }

}
