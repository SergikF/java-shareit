package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @PostMapping
    public RequestDto addRequest(@RequestHeader("X-Sharer-User-Id") Long requestorId,
                                 @RequestBody RequestDtoInput requestDto) {
        return RequestMapper.toRequestDto(requestService.addRequest(requestorId, requestDto));
    }

    @GetMapping
    public List<RequestDtoItems> getRequests(@RequestHeader("X-Sharer-User-Id") Long requestorId) {
        return requestService.getRequests(requestorId).stream().map(RequestMapper::toRequestDtoItems).toList();
    }

    @GetMapping("/all")
    public List<RequestDto> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long requestorId) {
        return requestService.getAllRequests(requestorId).stream().map(RequestMapper::toRequestDto).toList();
    }

    @GetMapping("/{requestId}")
    public RequestDtoItems getRequestsById(@PathVariable Long requestId) {
        return RequestMapper.toRequestDtoItems(requestService.getRequestById(requestId));
    }

}