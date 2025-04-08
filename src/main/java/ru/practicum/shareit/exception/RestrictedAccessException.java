package ru.practicum.shareit.exception;

public class RestrictedAccessException extends RuntimeException {
    public RestrictedAccessException(String message) {
        super(message);
    }
}