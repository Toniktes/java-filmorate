package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

@Slf4j
@Service
public class UserService {
    UserStorage userStorage;
    private int generatorId = 0;

    @Autowired
    public UserService(@Autowired(required = false) UserStorage userStorage) {
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
        User user = getUser(userId);
        User friend = getUser(friendId);
        userStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(int userId, int friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);
        userStorage.deleteFriend(user.getId(), friend.getId());
    }

    public Collection<User> getFriends(int userId) {
        User user = getUser(userId);
        Collection<User> friends = new HashSet<>();
        for (Integer id : user.getFriends()) {
            friends.add(getUser(id));
        }
        return friends;
    }

    public User getUser(int userId) {
        User user = userStorage.getUser(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с идентификатором " +
                    userId + " не зарегистрирован!");
        }
        return user;
    }

    public Collection<User> getCommonFriends(int userID, int otherId) {
        User user = getUser(userID);
        User otherUser = getUser(otherId);
        Collection<User> commonFriends = new HashSet<>();
        for (Integer id : user.getFriends()) {
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
}