package ru.yandex.practicum.filmorate;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
