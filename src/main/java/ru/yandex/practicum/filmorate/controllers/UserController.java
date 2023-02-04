package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(@Autowired(required = false) UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Получен запрос на добавление пользователя");
        User addUser = userService.create(user);
        log.info(String.format("Пользователь с идентификатором %d добавлен!", addUser.getId()));
        return addUser;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Получен запрос на обновление данных пользователя");
        User updateUser = userService.update(user);
        log.info("Пользователь с идентификатором {} обновлен", updateUser.getId());
        return updateUser;
    }

    @GetMapping
    public Collection<User> findAll() {
        log.info("Запрос на получение списка пользователей");
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {
        log.info("Запрос на на получение пользователя id: {}", id);
        return userService.getUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Получен запрос на добавление пользователей друг к другу в друзья с индентификаторами {} и {}", id, friendId);
        userService.addFriend(id, friendId);
        log.info("Пользователь добавлен в друзья");
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Получен запрос от пользователя с id: {} на удаление из друзей пользователя c id: {}", id, friendId);
        userService.deleteFriend(id, friendId);
        log.info("Пользователь удален из друзей");
    }

    @GetMapping("/{id}/friends")
    public Collection<User> findFriends(@PathVariable int id) {
        log.info("Получен запрос на получение списка друзей пользователя с id: {}", id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> findCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info("Получен запрос на получение списка общих друзей от пользователя с id: {} c пользователем id: {}",
                id, otherId);
        return userService.getCommonFriends(id, otherId);
    }
}
