package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private int generatorId = 0;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        validation(user);
        return userStorage.addUser(user);
    }

    public User update(final User user) {
        validation(user);
        return userStorage.updateUser(user);
    }

    public Collection<User> findAll() {
        return userStorage.getAllUsers();
    }

    private void generateId(User user) {
        if (user.getId() == 0) {
            user.setId(++generatorId);
        }
    }

    public void addFriend(int userId, int friendId) {
        validateId(userId, friendId);
        userStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(int userId, int friendId) {
        validateId(userId, friendId);
        userStorage.deleteFriend(userId, friendId);
    }

    public Collection<User> getFriends(int userId) {
        User user = getUser(userId);
        Collection<User> friends = new ArrayList<>();
        for (Integer id : user.getFriends()) {
            friends.add(getUser(id));
        }
        return friends;
    }

    public User getUser(int userId) {
        return Optional.ofNullable(userStorage.getUser(userId))
                .orElseThrow(() -> new NotFoundException("Пользователь с идентификатором " + userId + " не зарегистрирован!"));
    }

    public Collection<User> getCommonFriends(int userID, int otherId) {
        User user = getUser(userID);
        User otherUser = getUser(otherId);
        Collection<User> commonFriends = new ArrayList<>();
        if (user.getFriends() == null || otherUser.getFriends() == null) {
            return Collections.emptyList();
        }
        for (int id : user.getFriends()) {
            if (otherUser.getFriends().contains(id)) {
                commonFriends.add(getUser(id));
            }
        }
        return commonFriends;
    }

    private void validation(User user) {
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
        generateId(user);
    }

    private void validateId(int userId, int friendId) {
       /* if (!userStorage.userMap().containsKey(userId)) {
            throw new NotFoundException("Пользователь с идентификатором " +
                    userId + " не найден!");
        }
        if (!userStorage.userMap().containsKey(friendId)) {
            throw new NotFoundException("Пользователь с идентификатором " +
                    friendId + " не найден!");
        }*/

        Optional.ofNullable(userStorage.getUser(userId))
                .orElseThrow(() -> new NotFoundException("Пользователь с идентификатором " + userId + " не зарегистрирован!"));
        Optional.ofNullable(userStorage.getUser(friendId))
                .orElseThrow(() -> new NotFoundException("Пользователь с идентификатором " + friendId + " не зарегистрирован!"));
    }
}
