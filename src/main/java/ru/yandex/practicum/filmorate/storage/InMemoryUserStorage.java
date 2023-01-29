package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int generatorId = 0;

    public User create(User user) {
        validation(user);
        generateId(user);
        users.put(user.getId(), user);
        log.debug("");
        log.info("пользователь добавлен: " + user);
        return user;
    }

    public User update(User user) {
        validation(user);
        if (users.get(user.getId()) == null) {
            throw new ValidationException("Попытка обновить данные не существующего пользователя");
        }
        generateId(user);
        users.put(user.getId(), user);
        log.debug("");
        log.info("пользователь добавлен: " + user);
        return user;
    }

    public Collection<User> findAll() {
        return users.values();
    }

    private void generateId(User user) {
        if (user.getId() == 0) {
            user.setId(++generatorId);
        }
    }

    private void validation(User user) throws ValidationException {
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
            user.setName(user.getLogin());
        } else if (user.getName().isEmpty() || user.getName().isBlank()) {
            log.info("Задано пустое имя, логин будет использован как имя");
            user.setName(user.getLogin());
        }
    }
}

