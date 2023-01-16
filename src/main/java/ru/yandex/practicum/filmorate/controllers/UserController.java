package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private int generatorId = 0;
    @PostMapping(value = "/users")
    public User create(@RequestBody User user) throws ValidationException {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now().plusDays(1))) {
            throw new ValidationException("дата рождения не может быть в будущем");
        }
        if (user.getName() == null) {
            log.info("Имя не задано, логин будет использован как имя");
            user.setName(user.getEmail());
        } else if (user.getName().isEmpty() || user.getName().isBlank()) {
            log.info("Задано пустое имя, логин будет использован как имя");
            user.setName(user.getEmail());
        }
        if (user.getId() == 0) {
            user.setId(++generatorId);
        }
        users.put(user.getId(), user);
        log.debug("");
        log.info("пользователь добавлен: " + user);
        return user;
    }

    @PutMapping(value = "/users")
    public User update(@RequestBody User user) throws ValidationException {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now().plusDays(1))) {
            throw new ValidationException("дата рождения не может быть в будущем");
        }
        if (user.getName() == null) {
            log.info("Имя не задано, логин будет использован как имя");
            user.setName(user.getEmail());
        } else if (user.getName().isEmpty() || user.getName().isBlank()) {
            log.info("Задано пустое имя, логин будет использован как имя");
            user.setName(user.getEmail());
        }
        if (user.getId() == 0) {
            user.setId(++generatorId);
        }
        users.put(user.getId(), user);
        log.debug("");
        log.info("пользователь добавлен: " + user);
        return user;
    }

    @GetMapping("/users")
    public Map<Integer, User> findAll() {
        return users;
    }
}
